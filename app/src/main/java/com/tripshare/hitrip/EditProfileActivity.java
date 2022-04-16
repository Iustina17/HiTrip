package com.tripshare.hitrip;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;



public class EditProfileActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1;
    private static final int PICK_IMAGE = 2;
    private ImageView imagineProfil;
    TextView nume, prenume, varsta;
    private EditText despre, preferinte,locuri_vizitate, limbi_vorbite;
    private Button save_modif;
    private ProgressBar progressBarProfile;

    private StorageReference mStorage;
    private String currentPhotoPath;
    private String imagineProfilS;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        imagineProfil = (ImageView)findViewById(R.id.editare_imagineProfil);
        nume = findViewById(R.id.nume);
        prenume = findViewById(R.id.prenume);
        varsta = findViewById(R.id.varsta);
        despre = findViewById(R.id.despre);
        preferinte = findViewById(R.id.preferinte);
        locuri_vizitate = findViewById(R.id.locuri_vizitate);
        limbi_vorbite = findViewById(R.id.limbi_vorbite);
        save_modif = findViewById(R.id.save_modif);
        progressBarProfile = (ProgressBar)findViewById(R.id.progressBarProfile);
    }

    public void EditProfilePhoto(View view) {
        schimbareImagineProfil();
    }

    private void schimbareImagineProfil() {
        final AlertDialog schimbaImagineProfil = new AlertDialog.Builder(getApplicationContext()).create();
        Window window = schimbaImagineProfil.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setPadding(20, 20, 50, 20);

        GradientDrawable shape = new GradientDrawable();
        shape.setColor(getResources().getColor(R.color.dell));
        shape.setCornerRadius(100);

        ImageButton stergePoza = new ImageButton(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            stergePoza.setImageDrawable(getResources().getDrawable(R.drawable.ic_stergere, getApplicationContext().getTheme()));
        } else {
            stergePoza.setImageDrawable(getResources().getDrawable(R.drawable.ic_stergere));
        }
        stergePoza.setBackground(shape);
        stergePoza.setPadding(10, 10, 10, 10);
        stergePoza.setMinimumWidth(200);
        stergePoza.setMinimumHeight(200);
        stergePoza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stergePoza();
                schimbaImagineProfil.dismiss();
            }
        });
        layout.addView(stergePoza);

        ImageButton galeriePoza = new ImageButton(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            galeriePoza.setImageDrawable(getResources().getDrawable(R.drawable.ic_galerie, getApplicationContext().getTheme()));
        } else {
            galeriePoza.setImageDrawable(getResources().getDrawable(R.drawable.ic_galerie));
        }
        galeriePoza.setBackground(shape);
        galeriePoza.setPadding(10, 10, 10, 10);
        galeriePoza.setMinimumWidth(200);
        galeriePoza.setMinimumHeight(200);
        galeriePoza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pozaGalerie();
                schimbaImagineProfil.dismiss();
            }
        });
        layout.addView(galeriePoza);

//        ImageButton cameraPoza = new ImageButton(getApplicationContext());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            cameraPoza.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera, getApplicationContext().getTheme()));
//        } else {
//            cameraPoza.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera));
//        }
//        cameraPoza.setBackground(shape);
//        cameraPoza.setPadding(10, 10, 10, 10);
//        cameraPoza.setMinimumWidth(200);
//        cameraPoza.setMinimumHeight(200);
//        cameraPoza.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                cameraPoza();
//                schimbaImagineProfil.dismiss();
//            }
//        });
//        layout.addView(cameraPoza);

        schimbaImagineProfil.setTitle("Schimbă poza profil");
        schimbaImagineProfil.setView(layout);
        schimbaImagineProfil.show();
    }

    private void cameraPoza() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ignored) {
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        "com.tripshare.hitrip.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }

        }
    }

    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @SuppressLint("IntentReset")
    private void pozaGalerie() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(intent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        someActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture")); //PICK_IMAGE
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
//            if (result.getResultCode() == Activity.RESULT_OK){
//                Intent data = result.getData();
//                Uri imagineProfilUri = data.getData();
//                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                final StorageReference imageReference = FirebaseStorage.getInstance().getReference().child("/imaginiProfil").child(user.getUid() + ".jpeg");
//                UploadTask uploadTask = imageReference.putFile(imagineProfilUri);
//                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                progressBarProfile.setVisibility(View.INVISIBLE);
//                                Task<Uri> downloadUrl = imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                    @Override
//                                    public void onSuccess(Uri uri) {
//                                        imagineProfilS = uri.toString();
//                                        Picasso.get()
//                                                .load(imagineProfilS)
//                                                .fit().centerCrop()
//                                                .transform(new CropCircleTransformation())
//                                                .into(imagineProfil);
//                                    }
//                                });
//                            }
//                        }, 2000);
//                        Toast.makeText(getApplicationContext(), "Salvează modificările", Toast.LENGTH_LONG).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressBarProfile.setVisibility(View.INVISIBLE);
//                        progressBarProfile.setProgress(0);
//                        Toast.makeText(getApplicationContext(), "Poza nu a putut fi încărcată", Toast.LENGTH_LONG).show();
//                    }
//                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//                        progressBarProfile.setVisibility(View.VISIBLE);
//                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                        progressBarProfile.setProgress((int) progress);
//                    }
//                });




            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();
                Uri imagineProfilUri = data.getData();
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final StorageReference imageReference = FirebaseStorage.getInstance().getReference().child("/imaginiProfil").child(user.getUid() + ".jpeg");
                UploadTask uploadTask = imageReference.putFile(imagineProfilUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBarProfile.setVisibility(View.INVISIBLE);
                                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imagineProfilS = uri.toString();
                                        Picasso.get()
                                                .load(imagineProfilS)
                                                .fit().centerCrop()
                                                .transform(new CropCircleTransformation())
                                                .into(imagineProfil);
                                    }
                                });
                            }
                        }, 2000);

                        Toast.makeText(getApplicationContext(), "Salvează modificările", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressBarProfile.setVisibility(View.INVISIBLE);
                        progressBarProfile.setProgress(0);
                        Toast.makeText(getApplicationContext(), "Poza nu a putut fi încărcată", Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        progressBarProfile.setVisibility(View.VISIBLE);
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressBarProfile.setProgress((int) progress);
                    }
                });
            }
            Toast.makeText(getApplicationContext(), "Salvează modificările", Toast.LENGTH_LONG).show();

        }
    });


    private void stergePoza() {
        imagineProfilS = "https://firebasestorage.googleapis.com/v0/b/aventura-in-natura-c313d.appspot.com/o/imaginiProfil%2FH1DA51VNlNXm28JvWs3T2wvrRvB3.jpeg?alt=media&token=682dd750-df3e-4fb8-972f-80781b18a96a";
        imagineProfil.setImageResource(R.drawable.user);
        Toast.makeText(getApplicationContext(), "Salvează modificările", Toast.LENGTH_LONG).show();
    }
}