package com.tripshare.hitrip.Trips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.R;
import com.tripshare.hitrip.Trips.Oprire;
import com.tripshare.hitrip.Trips.Trip;
import com.tripshare.hitrip.User;

import java.util.ArrayList;
import java.util.List;

public class InsideTripActivity1 extends AppCompatActivity {
    private String uid_organizator, data_start, data_fin;
    ImageView imagine_excursie;
    TextView prenume, nume;
    TextView titlu_excursie, tematica, tip, data_inceput, data_final, data_inceput1, data_final1;
    TextView nr_zile, nr_zile2, tara, oras, tara1, oras1;
    TextView descriere_plecare, nr_opriri;
    ArrayList<Oprire> vect_opriri;
    TextView descriere_excursie, regulament, echipament_necesar, documente_necesare;
    TextView nr_min_particip, nr_max_particip;
    TextView pret, tip_moneda;
    TextView locuri_ramase;
    TextView dificultate;
    LinearLayout grad_dif_layout;
    LinearLayout layout_echipament_necesar, layout_documente_necesare, layout_descreiere_plecare;
    RecyclerView inside_trip_profil_recycler;
    Integer loc_ramase = 0;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference referenceTrips = database.getReference("Calatorii");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_trip1);
        uid_organizator = getIntent().getStringExtra("uid_organizator");
        data_start = getIntent().getStringExtra("data_start");
        data_fin = getIntent().getStringExtra("data_final");

        imagine_excursie = findViewById(R.id.img_cover_trip);
        prenume = findViewById(R.id.prenume_organizator);
        nume = findViewById(R.id.nume_organizator);
        titlu_excursie = findViewById(R.id.titlu);
        tematica = findViewById(R.id.tematici);
        tip = findViewById(R.id.tip);
        data_inceput = findViewById(R.id.data_inceput2);
        data_final = findViewById(R.id.data_final2);
        data_inceput1 = findViewById(R.id.date_start1);
        data_final1 = findViewById(R.id.date_end1);
        nr_zile = findViewById(R.id.nr_zile);
        nr_zile2 = findViewById(R.id.nr_zile2);
        tara = findViewById(R.id.tara);
        oras = findViewById(R.id.oras);
        tara1 = findViewById(R.id.tara1);
        oras1 = findViewById(R.id.oras1);
        descriere_plecare = findViewById(R.id.descriere_plecare);
        nr_opriri = findViewById(R.id.nr_opriri);
        descriere_excursie = findViewById(R.id.descriere);
        regulament = findViewById(R.id.regulament);
        echipament_necesar = findViewById(R.id.echipament_necesar);
        documente_necesare = findViewById(R.id.documente_necesare);
        nr_min_particip = findViewById(R.id.min);
        nr_max_particip = findViewById(R.id.max);
        pret = findViewById(R.id.pret);
        tip_moneda = findViewById(R.id.moneda);
        locuri_ramase = findViewById(R.id.locuri_ramase);
        dificultate = findViewById(R.id.dificultate);
        grad_dif_layout = findViewById(R.id.grad_dif_layout);
        layout_echipament_necesar = findViewById(R.id.layout_echipament_necesar);
        layout_documente_necesare = findViewById(R.id.layout_documente_necesare);
        layout_descreiere_plecare = findViewById(R.id.layout_descreiere_plecare);
        inside_trip_profil_recycler = findViewById(R.id.inside_trip_profil_recycler);


        referenceTrips.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Trip trip = keyNode.getValue(Trip.class);
                    if (trip.UID_organiztor.equals(uid_organizator) && trip.data_inceput.equals(data_start) && trip.data_final.equals(data_fin)) {
                        //imagine_excursie.setAdjustViewBounds(trip.imagine_excursie);
                        nume.setText(trip.nume);
                        prenume.setText(trip.prenume);
                        titlu_excursie.setText(trip.titlu_excursie);
                        tematica.setText(trip.tematica);
                        tip.setText(trip.tip);
                        data_inceput.setText(trip.data_inceput);
                        data_final.setText(trip.data_final);
                        data_inceput1.setText(trip.data_inceput);
                        data_final1.setText(trip.data_final);
                        nr_zile.setText(trip.nr_zile.toString());
                        nr_zile2.setText(trip.nr_zile.toString());
                        tara.setText(trip.tara);
                        oras.setText(trip.oras);
                        tara1.setText(trip.tara);
                        oras1.setText(trip.oras);
                        descriere_plecare.setText(trip.descriere_plecare);
                        //nr_opriri.setText(trip.nr_opriri);
                        descriere_excursie.setText(trip.descriere_excursie);
                        regulament.setText(trip.regulament);
                        echipament_necesar.setText(trip.echipament_necesar);
                        documente_necesare.setText(trip.documente_necesare);
                        nr_min_particip.setText(trip.nr_min_particip);
                        nr_max_particip.setText(trip.nr_max_particip);
                        pret.setText(trip.pret);
                        tip_moneda.setText(trip.tip_moneda);
                        dificultate.setText(trip.dificultate);
                        //locuri_ramase.setText(); TODO

                        if (trip.tip.equals("Drumeţie")) {
                            grad_dif_layout.setVisibility(View.VISIBLE);
                            layout_echipament_necesar.setVisibility(View.VISIBLE);
                        } else {
                            grad_dif_layout.setVisibility(View.GONE);
                        }

                        if (!trip.tip.equals("Drumeţie") && trip.echipament_necesar.isEmpty()) {
                            layout_echipament_necesar.setVisibility(View.GONE);
                        }

                        if (trip.documente_necesare.isEmpty()) {
                            layout_documente_necesare.setVisibility(View.GONE);
                        }

                        if (trip.descriere_plecare.isEmpty()) {
                            layout_descreiere_plecare.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        new FirebaseDatabaseHelperParticipanti().showParticipanti(new FirebaseDatabaseHelperParticipanti.DataStatus() {
            @Override
            public void DataIsLoaded(List<User> participanti, List<Integer> keys) {
                new RecyclerViewConfigParticipant().setconfig(inside_trip_profil_recycler, InsideTripActivity1.this, participanti, keys);
            }
        }, uid_organizator, data_start, data_fin);


    }
}