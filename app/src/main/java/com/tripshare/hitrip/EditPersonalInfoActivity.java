package com.tripshare.hitrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Trips.FirebaseDatabaseHelperTrips;
import com.tripshare.hitrip.Trips.Trip;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditPersonalInfoActivity extends AppCompatActivity {

    String uid_user_edpersinfo;
    static EditText birthday_pers_info;
    EditText nume_edit_pers_info, prenume_edit_persinfo, nationalitate_edit_pers_info;
    DatePickerDialog.OnDateSetListener setListener;
    Spinner spinner1; //asta e sex
    ArrayAdapter<CharSequence> adapter1;
    //TextView age_show;////////////////////////////////////
    TextView textCondition;
    Button confirmaNrTelefon, confirmaAcreditare, schimba_parola, save_modif_personal_info, sterge_cont;
    TextView nr_tel_verificat;
    Integer nr_mobil_verificatS; ///daca e sau nu verificat

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference referenceUsers = database.getReference("Utilizatori");
    private DatabaseReference referenceUtiliztaori;
    DatabaseReference referenceUtiliz;

    static int age;

    Button schimbaEmail;
    private int dateSchimbate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);

        nume_edit_pers_info = findViewById(R.id.nume_edit_pers_info);
        prenume_edit_persinfo = findViewById(R.id.prenume_edit_persinfo);
        nationalitate_edit_pers_info = findViewById(R.id.nationalitate_edit_pers_info);
        confirmaNrTelefon = findViewById(R.id.confirmaNrTelefon);
        confirmaAcreditare = findViewById(R.id.confirmaAcreditare);
        schimbaEmail = findViewById(R.id.schimbaEmail);
        schimba_parola = findViewById(R.id.schimba_parola);
        save_modif_personal_info = findViewById(R.id.save_modif_personal_info);
        sterge_cont = findViewById(R.id.sterge_cont);
        birthday_pers_info = findViewById(R.id.birthday_pers_info);
        nr_tel_verificat = findViewById(R.id.nr_tel_verificat);

        // age_show = findViewById(R.id.age_show);
        textCondition = findViewById(R.id.textCondition);

        referenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    User user = keyNode.getValue(User.class);

                    uid_user_edpersinfo = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    if (user.UID.equals(uid_user_edpersinfo)) {
                        //imagine_excursie.setAdjustViewBounds(trip.imagine_excursie);
                        nume_edit_pers_info.setText(user.nume);
                        prenume_edit_persinfo.setText(user.prenume);
                        nationalitate_edit_pers_info.setText(user.nationalitate.toString());
                        birthday_pers_info.setText(user.data_nasterii.toString());
                        //confirmaNrTelefon.setText(user.descriere.toString());
                        if (user.sex.toString().equals("Bărbat")) {
                            spinner1.setSelection(0);
                        } else if (user.sex.toString().equals("Femeie")) {
                            spinner1.setSelection(1);
                        } else if (user.sex.toString().equals("Altele")) {
                            spinner1.setSelection(2);
                        }
                        //nr_mobil_verificatS = user.nr_mobil_verificat; // am incercat sa fac asta mai jos
//                        nr_tel_verificat_my.setText(user.nume);
//                        nu_exista_info_verificate_my.setText(user.nume);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        birthday_pers_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditPersonalInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        birthday_pers_info.setText(date);
                        Calendar birthdate = Calendar.getInstance();
                        birthdate.set(year, month, day);
                        //age_show.setText(Integer.toString(calculateAge(birthdate)));
                        age = calculateAge(birthdate);
                        if (age < 18) {
                            textCondition.setError("Trebuie să ai cel puţin 18 ani pentru a utiliza HiTrip");
                            birthday_pers_info.requestFocus();
                            return;
                        } else {
                            textCondition.setError(null);
                        }
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();

            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = day + "/" + month + "/" + year + "/";
                birthday_pers_info.setText(date);
            }
        };

        spinner1 = (Spinner) findViewById(R.id.spinner_sex);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.sex, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ShowVerificareNr();


        schimbaEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schimbareEmail();
            }
        });
    }

    int calculateAge(Calendar date) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - date.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_MONTH) < date.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }
        return age;
    }

    public void ClickConfirmMobile(View view) {
        redirectActivity(this, SendOTPActivity.class);
    }

    private static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity, aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity((intent));
    }

    void ShowVerificareNr() {

        String uid = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //cautam copilul dupa UID

        referenceUtiliztaori = database.getInstance().getReference().child("Utilizatori");
        Query query = referenceUtiliztaori.orderByChild("UID").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User utilizatorCurent = data.getValue(User.class);
                    //Log.d(TAG, "Value is: " + utilizatorCurent);
                    nr_mobil_verificatS = utilizatorCurent.nr_mobil_verificat;
                    if (nr_mobil_verificatS == 1) {
                        confirmaNrTelefon.setVisibility(View.GONE);
                        nr_tel_verificat.setVisibility(View.VISIBLE);
                    } else if (nr_mobil_verificatS == 0) {
                        confirmaNrTelefon.setVisibility(View.VISIBLE);
                        nr_tel_verificat.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    private void uploadInformatiiFirebase() {
        final String numeS = nume_edit_pers_info.getText().toString().trim();
        final String prenumeS = prenume_edit_persinfo.getText().toString().trim();
        final String sexS = spinner1.getSelectedItem().toString().trim();
        final String birthday_pers_infoS = birthday_pers_info.getText().toString().trim();
        final String nationalitateS = nationalitate_edit_pers_info.getText().toString().trim();

        String uid = null;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }
        Log.d("EDIT PERSONAL INFO", uid);
        //cautam copilul dupa UID
        referenceUtiliztaori = FirebaseDatabase.getInstance().getReference().child("Utilizatori");
        Query query = referenceUtiliztaori.orderByChild("UID").equalTo(uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey();
                    referenceUtiliztaori = referenceUtiliztaori.child(key);
                    referenceUtiliztaori.child("nume").setValue(numeS);
                    referenceUtiliztaori.child("prenume").setValue(prenumeS);
                    referenceUtiliztaori.child("sex").setValue(sexS);
                    referenceUtiliztaori.child("data_nasterii").setValue(birthday_pers_infoS);
                    String[] from = birthday_pers_infoS.split("/");
                    Calendar cal = Calendar.getInstance();
                    cal.set(Integer.parseInt(from[2]), Integer.parseInt(from[1]), Integer.parseInt(from[0]));
                    Calendar today = Calendar.getInstance();
                    Integer age_nou = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
                    if (today.get(Calendar.DAY_OF_MONTH) < cal.get(Calendar.DAY_OF_MONTH)) {
                        age_nou--;
                    }
                    referenceUtiliztaori.child("varsta").setValue(age_nou);
                    referenceUtiliztaori.child("nationalitate").setValue(nationalitateS);
                    //referenceUtiliztaori.child("nr_mobil_verificat").setValue(nrTelVerificatS);
                    //referenceUtiliztaori.child("imagine").setValue(imagineProfilS);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void Save_Personal_Info(View view) {
        uploadInformatiiFirebase();
        if (age < 18) {
            textCondition.setError("Trebuie să ai cel puţin 18 ani pentru a utiliza HiTrip");
            birthday_pers_info.requestFocus();
            return;
        } else {
            textCondition.setError(null);
        }
        redirectActivity(this, MyProfileActivity.class);

        if (dateSchimbate == 1) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void schimbareEmail() {
        final AlertDialog alertEmail = new AlertDialog.Builder(this)
                .setPositiveButton(R.string.alert_dialog_buton_salvare, null)
                .setNegativeButton(R.string.alert_dialog_buton_anulare, null)
                .setTitle("Schimbă adresa de e-mail")
                .create();

        //liniar layout pentru AlertDialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(10, 10, 10, 10);

        //edit text email
        final EditText email = new EditText(this);
        email.setHint(getString(R.string.alert_dialog_schimba_email));
        layout.addView(email);

        //edit text email verificare
        final EditText emailVerificare = new EditText(this);
        emailVerificare.setHint(getString(R.string.alert_dialog_schimba_email_confirmare));
        layout.addView(emailVerificare);

        alertEmail.setView(layout);
        alertEmail.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button butonAnulare = alertEmail.getButton(Dialog.BUTTON_NEGATIVE);
                butonAnulare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertEmail.dismiss();
                    }
                });

                Button butonSalvare = alertEmail.getButton(Dialog.BUTTON_POSITIVE);
                butonSalvare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String emailS = email.getText().toString().trim();
                        String emailVerificareS = emailVerificare.getText().toString().trim();
                        validareEmail(emailS, emailVerificareS, email, emailVerificare, alertEmail);
                    }
                });
            }
        });

        alertEmail.show();
    }

    private void validareEmail(final String emailS, String emailVerificareS, EditText email, EditText emailVerificare, final AlertDialog alertEmail) {
        //final String emailTest = emailS;
        int verificareEmail = 10;

        if (emailVerificareS.length() != 0 || emailS.length() != 0) {
            if (emailS.length() == 0) {
                verificareEmail = 0;
            } else if (emailVerificareS.length() == 0) {
                verificareEmail = 1;
            } else if (!emailS.equals(emailVerificareS)) {
                verificareEmail = 2;
            } else if (!isEmailValid(emailS)) {
                verificareEmail = 3;
            } else if (emailS.equals(emailVerificareS))
                verificareEmail = 4;
        }

        if (verificareEmail != 10) {
            switch (verificareEmail) {
                case 0:
                    email.setError("Introdu adresa de e-mail");
                    email.requestFocus();
                    break;
                case 1:
                    emailVerificare.setError("Introdu din nou adresa de e-mail");
                    emailVerificare.requestFocus();
                    break;
                case 2:
                    emailVerificare.setError("Cele doua adrese de e-mail nu corespund");
                    emailVerificare.requestFocus();
                    break;
                case 3:
                    email.setError("Email invalid");
                    email.requestFocus();
                    break;
                case 4:
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    //user.updateEmail(emailS);
                    Task task = user.updateEmail(emailS);
                    if (user.getEmail().equals(emailS)) {
                        referenceUtiliz = FirebaseDatabase.getInstance().getReference().child("Utilizatori");
                        FirebaseUser userCurent = FirebaseAuth.getInstance().getCurrentUser();
                        Query query = referenceUtiliz.orderByChild("UID").equalTo(userCurent.getUid());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    String key = child.getKey();
                                    referenceUtiliz = referenceUtiliz.child(key);
                                    referenceUtiliz.child("email").setValue(emailS);
                                    Toast.makeText(EditPersonalInfoActivity.this, "Adresă schimbată cu succes", Toast.LENGTH_LONG).show();
                                    dateSchimbate = 1;
                                    alertEmail.dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }


                    break;
            }
        }

    }

    private static boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

}
