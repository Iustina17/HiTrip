package com.tripshare.hitrip.Impresii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.MyTrips.MyTripsActivity;
import com.tripshare.hitrip.R;
import com.tripshare.hitrip.Trips.Trip;
import com.tripshare.hitrip.User;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdaugaImpresiePtOrganizatorActivity extends AppCompatActivity {

    RatingBar adauga_impresie_rating;
    EditText adauga_impresie_continut;
    Button adauga_impresie_buton_trimite;
    TextView titlu_excursie;
    TextView data;
    String data_start, data_fin, titlu_exc;
    ImageButton buton_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga_impresie_pt_organizator);

        titlu_exc = getIntent().getStringExtra("titlu");
        data_start = getIntent().getStringExtra("data_inceput");
        data_fin = getIntent().getStringExtra("data_final");
        adauga_impresie_rating = findViewById(R.id.adauga_impresie_rating_organizator);
        adauga_impresie_buton_trimite = findViewById(R.id.adauga_impresie_buton_trimite);
        adauga_impresie_continut = findViewById(R.id.adauga_impresie_continut);
//        adauga_impresie_buton_trimite = findViewById(R.id.adauga_impresie_buton_trimite);
        titlu_excursie = findViewById(R.id.titlu_exc);
        buton_cancel = findViewById(R.id.buton_cancel);

        data = findViewById(R.id.data);

        titlu_excursie.setText(titlu_exc);
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
                            if (trip.titlu_excursie.equals(titlu_exc) && trip.data_inceput.equals(data_start) && trip.data_final.equals(data_fin)) {
                                DatabaseReference referenceUsers;
                                referenceUsers = FirebaseDatabase.getInstance().getReference().child("Utilizatori");
                                Query query = referenceUsers.orderByChild("UID").equalTo(trip.UID_organiztor);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            User organizator = child.getValue(User.class);
                                            String key = child.getKey();
                                            User user;
                                            String uid_curent = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            for (Map.Entry<String, User> entry : trip.participanti.entrySet()) {
                                                user = entry.getValue();
                                                if (user.UID.equals(uid_curent)) {
                                                    Impresie impresie = new Impresie(user.nume, user.prenume, uid_curent, trip.titlu_excursie,
                                                            Calendar.getInstance().getTime().toString(), adauga_impresie_rating.getRating(), user.poza_profil, adauga_impresie_continut.getText().toString());
                                                    referenceUsers.child(key).child("impresie_organizare_user").push().setValue(impresie);

                                                    referenceUsers.child(key).child("nr_impresii_organizator").setValue(organizator.nr_impresii_organizator+1);
                                                    referenceUsers.child(key).child("rating_organizator").setValue(((organizator.rating_organizator * organizator.nr_impresii_organizator) +  adauga_impresie_rating.getRating() )/(organizator.nr_impresii_organizator + 1));

                                                    DatabaseReference referenceTrip = FirebaseDatabase.getInstance().getReference().child("Calatorii");
                                                    Query query = referenceTrip.orderByChild("UID_organiztor").equalTo(trip.UID_organiztor);
                                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                                String key = child.getKey();
                                                                Trip trip_nou = child.getValue(Trip.class);

                                                                if (trip_nou.titlu_excursie.equals(trip.titlu_excursie) && trip.data_inceput.equals(trip_nou.data_inceput) && trip.data_final.equals(trip_nou.data_final)) {
                                                                    DatabaseReference referenceFeedback = referenceTrip.child(key).child("impresii_date_de_participanti");
                                                                    Query query2 = referenceFeedback.orderByChild("uid_participant").equalTo(uid_curent);
                                                                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                                                Trip trip = child.getValue(Trip.class);
                                                                                referenceTrip.child(key).child("impresii_date_de_participanti").removeValue();
                                                                                HashMap<String, Feedback> hashMapFeedbacks;
                                                                                if (trip.impresii_date_de_participanti != null) {
                                                                                    hashMapFeedbacks = trip.impresii_date_de_participanti;
                                                                                    if (hashMapFeedbacks != null)
                                                                                        for (Map.Entry<String, Feedback> entry : hashMapFeedbacks.entrySet()) {
                                                                                            Feedback feedback1 = entry.getValue();
                                                                                            if (feedback1.uid_participant.equals(uid_curent)) {
                                                                                                hashMapFeedbacks.entrySet().remove(entry);
                                                                                                Feedback feedback2 = new Feedback(uid_curent, "da", feedback1.nume, feedback1.prenume, feedback1.poza);
                                                                                                hashMapFeedbacks.put(uid_curent, feedback2);
                                                                                                for (Map.Entry<String, Feedback> entry_push : hashMapFeedbacks.entrySet()) {
                                                                                                    referenceTrip.child(key).child("impresii_date_de_participanti").push().setValue(entry_push.getValue());
                                                                                                }
                                                                                            }
                                                                                        }

                                                                                }
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        }
                                                    });

                                                    Intent intent = new Intent(AdaugaImpresiePtOrganizatorActivity.this, MyTripsActivity.class);
                                                    startActivity(intent);
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
        buton_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference referenceTrips;
                referenceTrips = FirebaseDatabase.getInstance().getReference("Calatorii");
                referenceTrips.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                            Trip trip = keyNode.getValue(Trip.class);
                            if (trip.titlu_excursie.equals(titlu_exc) && trip.data_inceput.equals(data_start) && trip.data_final.equals(data_fin)) {
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
                                                if (user.UID.equals(uid_curent)) {
                                                    DatabaseReference referenceTrip = FirebaseDatabase.getInstance().getReference().child("Calatorii");
                                                    Query query = referenceTrip.orderByChild("UID_organiztor").equalTo(trip.UID_organiztor);
                                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                                String key = child.getKey();
                                                                Trip trip_nou = child.getValue(Trip.class);

                                                                if (trip_nou.titlu_excursie.equals(trip.titlu_excursie) && trip.data_inceput.equals(trip_nou.data_inceput) && trip.data_final.equals(trip_nou.data_final)) {
                                                                    DatabaseReference referenceFeedback = referenceTrip.child(key).child("impresii_date_de_participanti");
                                                                    Query query2 = referenceFeedback.orderByChild("uid_participant").equalTo(uid_curent);
                                                                    query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                                                Trip trip = child.getValue(Trip.class);
                                                                                referenceTrip.child(key).child("impresii_date_de_participanti").removeValue();
                                                                                HashMap<String, Feedback> hashMapFeedbacks;
                                                                                if (trip.impresii_date_de_participanti != null) {
                                                                                    hashMapFeedbacks = trip.impresii_date_de_participanti;
                                                                                    if (hashMapFeedbacks != null)
                                                                                        for (Map.Entry<String, Feedback> entry : hashMapFeedbacks.entrySet()) {
                                                                                            Feedback feedback1 = entry.getValue();
                                                                                            if (feedback1.uid_participant.equals(uid_curent)) {
                                                                                                hashMapFeedbacks.entrySet().remove(entry);
                                                                                                Feedback feedback2 = new Feedback(uid_curent, "pas", feedback1.nume, feedback1.prenume, feedback1.poza);
                                                                                                hashMapFeedbacks.put(uid_curent, feedback2);
                                                                                                for (Map.Entry<String, Feedback> entry_push : hashMapFeedbacks.entrySet()) {
                                                                                                    referenceTrip.child(key).child("impresii_date_de_participanti").push().setValue(entry_push.getValue());
                                                                                                }
                                                                                            }
                                                                                        }

                                                                                }
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        }
                                                    });

                                                    Intent intent = new Intent(AdaugaImpresiePtOrganizatorActivity.this, MyTripsActivity.class);
                                                    startActivity(intent);
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
