package com.tripshare.hitrip.Impresii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.R;
import com.tripshare.hitrip.Trips.Trip;
import com.tripshare.hitrip.User;

import java.util.Calendar;
import java.util.Map;

public class AdaugaImpresiePtOrganizatorActivity extends AppCompatActivity {

    RatingBar adauga_impresie_rating;
    EditText adauga_impresie_continut;
    Button adauga_impresie_buton_trimite;
    TextView titlu_excursie;
    TextView data;
    String data_start, data_fin, titlu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga_impresie_pt_organizator);

        titlu = getIntent().getStringExtra("titlu");
        data_start = getIntent().getStringExtra("data_inceput");
        data_fin = getIntent().getStringExtra("data_final");
        adauga_impresie_rating = findViewById(R.id.adauga_impresie_rating_organizator);
        adauga_impresie_buton_trimite = findViewById(R.id.adauga_impresie_buton_trimite);
        adauga_impresie_continut = findViewById(R.id.adauga_impresie_continut);
//        adauga_impresie_buton_trimite = findViewById(R.id.adauga_impresie_buton_trimite);
        titlu_excursie = findViewById(R.id.titlu);
        data = findViewById(R.id.data);

        titlu_excursie.setText(titlu);
        data.setText(data_start + " - " + data_fin);

        adauga_impresie_buton_trimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference referenceTrips;
                referenceTrips = FirebaseDatabase.getInstance().getReference("Calatorii");
                referenceTrips.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                            Trip trip = keyNode.getValue(Trip.class);
                            if (trip.titlu_excursie.equals(titlu) && trip.data_inceput.equals(data_start) && trip.data_final.equals(data_fin)) {
                                DatabaseReference referenceUsers;
                                referenceUsers = FirebaseDatabase.getInstance().getReference().child("Utilizatori");
                                Query query = referenceUsers.orderByChild("UID").equalTo(trip.UID_organiztor);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            String key = child.getKey();
                                            User user;
                                            String uid_curent = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                            for (Map.Entry<String, User> entry : trip.participanti.entrySet()) {
                                                user = entry.getValue();
                                                if(user.UID.equals(uid_curent)) {
                                                    Impresie impresie = new Impresie(user.nume, user.prenume, uid_curent, trip.titlu_excursie,
                                                            Calendar.getInstance().getTime().toString(), adauga_impresie_rating.getRating(), user.poza_profil, adauga_impresie_continut.getText().toString());
                                                    referenceUsers.child(key).child("impresie_organizare_user").push().setValue(impresie);

                                                }
                                            }


                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }
}