package com.tripshare.hitrip.Forum.Comentarii;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Forum.Sectiuni.SectiuneForum;
import com.tripshare.hitrip.ProfileRelated.User;
import com.tripshare.hitrip.R;

import java.util.Calendar;
import java.util.List;

public class InsideSectionForumActivity extends AppCompatActivity {

    Button button_adauga_comentariu;
    LinearLayout layout_adauga_comentariu_text;
    Button button_enter_posteaza_comentariu;
    String uid_organizator, data_start, data_fin, titlu, denumire_sectiune;
    TextView denumire_sectiune_TextView;
    RecyclerView recycler_sectiuni_forum;
    EditText adauga_comentariu_continut;
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_section_forum);

        uid_organizator = getIntent().getStringExtra("uid_organizator");
        data_start = getIntent().getStringExtra("data_start");
        data_fin = getIntent().getStringExtra("data_final");
        titlu = getIntent().getStringExtra("titlu_excursie");
        denumire_sectiune = getIntent().getStringExtra("denumire_sectiune");

        button_adauga_comentariu = findViewById(R.id.button_adauga_comentariu);
        layout_adauga_comentariu_text = findViewById(R.id.layout_adauga_comentariu_text);
        button_enter_posteaza_comentariu = findViewById(R.id.button_enter_posteaza_comentariu);
        adauga_comentariu_continut = findViewById(R.id.adauga_comentariu_continut);
        denumire_sectiune_TextView = findViewById(R.id.denumire_sectiune);

        denumire_sectiune_TextView.setText(denumire_sectiune);

        button_adauga_comentariu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_adauga_comentariu.setVisibility(View.GONE);
                layout_adauga_comentariu_text.setVisibility(View.VISIBLE);
            }
        });

        DatabaseReference referenceSectiuni = FirebaseDatabase.getInstance().getReference("Sectiuni");
        DatabaseReference referenceUsers = FirebaseDatabase.getInstance().getReference("Utilizatori");
        String uid_curent = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = referenceUsers.orderByChild("UID").equalTo(uid_curent);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    user = keyNode.getValue(User.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button_enter_posteaza_comentariu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_adauga_comentariu.setVisibility(View.VISIBLE);
                layout_adauga_comentariu_text.setVisibility(View.GONE);

                if (user.UID.equals(uid_curent)) {
                    Query querySectiuni = referenceSectiuni.orderByChild("titlu_excursie").equalTo(titlu);
                    querySectiuni.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot keyNodeSectiuni : dataSnapshot.getChildren()) {
                                String key = keyNodeSectiuni.getKey();
                                SectiuneForum sectiuneForum = keyNodeSectiuni.getValue(SectiuneForum.class);
                                //Log.d("Ciclu infinit - 0", "onDataChange:" + sectiuneForum.titlu_excursie + " " + sectiuneForum.denumire_sectiune);
                                if (sectiuneForum.uid_organizator.equals(uid_organizator)
                                        && sectiuneForum.data_final.equals(data_fin)
                                        && sectiuneForum.data_inceput.equals(data_start)
                                        && sectiuneForum.denumire_sectiune.equals(denumire_sectiune)) {
                                    //Log.d("Ciclu infinit - 1", "onDataChange:" + sectiuneForum.titlu_excursie + " " + sectiuneForum.denumire_sectiune);

                                    Comentariu comentariu = new Comentariu(user.nume, user.prenume,
                                            user.UID, adauga_comentariu_continut.getText().toString(),
                                            user.poza_profil, Calendar.getInstance().toString());
                                    referenceSectiuni.child(key).child("comentarii").push().setValue(comentariu);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


            }
        });


        recycler_sectiuni_forum =

                findViewById(R.id.recyclerview_comentarii);

        new

                FirebaseDatabaseHelperSectiuneComentarii(uid_organizator, data_start, data_fin, titlu, denumire_sectiune).

                showSectiuni(new FirebaseDatabaseHelperSectiuneComentarii.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Comentariu> comentarii, List<String> keys) {
                        new RecyclerViewConfigSectiuneComentarii().setconfig(recycler_sectiuni_forum, InsideSectionForumActivity.this, comentarii, keys);
                    }
                });

    }
}