package com.tripshare.hitrip.Forum.Sectiuni;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.tripshare.hitrip.R;

import java.util.List;

public class FormularActivity extends AppCompatActivity {

    RecyclerView recycler_sectiuni_forum;
    String uid_organizator, data_start, data_fin, titlu;
    FloatingActionButton buton_adauga_sectiune;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formular);

        uid_organizator = getIntent().getStringExtra("Uid_organizator");
        data_start = getIntent().getStringExtra("data_inceput");
        data_fin = getIntent().getStringExtra("data_final");
        titlu = getIntent().getStringExtra("titlu");

        recycler_sectiuni_forum = findViewById(R.id.Recycler_sectiuni_forum);
        buton_adauga_sectiune = findViewById(R.id.buton_adauga_sectiune);

        new FirebaseDatabaseHelperForumSectiuni(uid_organizator, data_start, data_fin, titlu).showSectiuni(new FirebaseDatabaseHelperForumSectiuni.DataStatus() {
            @Override
            public void DataIsLoaded(List<SectiuneForum> sectiuniList, List<String> keys) {
                new RecyclerViewConfigForumSectiuni().setconfig(recycler_sectiuni_forum, FormularActivity.this, sectiuniList, keys);
            }
        });

        String uid_user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("UID_user", "" + uid_user);
        Log.d("UID_organizator", "" + uid_organizator);
        if (!uid_user.equals(uid_organizator)) {
            buton_adauga_sectiune.setVisibility(View.GONE);
        }

        buton_adauga_sectiune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertSectiuneAdd = new AlertDialog.Builder(FormularActivity.this)
                        .setPositiveButton(R.string.alert_dialog_buton_salvare, null)
                        .setNegativeButton(R.string.alert_dialog_buton_anulare, null)
                        .setTitle("Adaugă o nouă secţiune")
                        .create();

                //liniar layout pentru AlertDialog
                LinearLayout layout = new LinearLayout(FormularActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(10, 10, 10, 10);

                final EditText denumire_sectiune_add = new EditText(FormularActivity.this);
                denumire_sectiune_add.setHint("Denumire secţiune");
                layout.addView(denumire_sectiune_add);

                alertSectiuneAdd.setView(layout);

                alertSectiuneAdd.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button butonAnulare = alertSectiuneAdd.getButton(Dialog.BUTTON_NEGATIVE);
                        butonAnulare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertSectiuneAdd.dismiss();
                            }
                        });

                        Button butonSalvare = alertSectiuneAdd.getButton(Dialog.BUTTON_POSITIVE);
                        butonSalvare.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String sectiuneS = denumire_sectiune_add.getText().toString().trim();
                                DatabaseReference referenceSectiuni = FirebaseDatabase.getInstance().getReference("Sectiuni");
                                SectiuneForum sectiuneForumAdd = new SectiuneForum(uid_organizator, titlu, data_start, data_fin, sectiuneS, null);
                                referenceSectiuni.push().setValue(sectiuneForumAdd);
                                alertSectiuneAdd.dismiss();
                            }
                        });
                    }
                });

                alertSectiuneAdd.show();
            }
        });

    }

}