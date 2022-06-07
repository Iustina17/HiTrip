package com.tripshare.hitrip.Trips;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Forum.Sectiuni.FormularActivity;
import com.tripshare.hitrip.ProfileRelated.ProfileActivity;
import com.tripshare.hitrip.R;
import com.tripshare.hitrip.ProfileRelated.User;
import com.tripshare.hitrip.Trips.ParticipantiAsteptare.ParticipantiAsteptareActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class InsideTripActivity extends AppCompatActivity {
    String uid_organizator, data_start, data_fin;
    String titluHelp = null, data_inceputHelp = null, data_finalHelp = null, Uid_organizatorHelp = null;
    ImageView imagine_excursie;
    TextView prenume, nume;
    TextView titlu_excursie, tematica, tip, data_inceput, data_final, data_inceput1, data_final1;
    TextView nr_zile, nr_zile2, tara, oras, tara1, oras1;
    TextView descriere_plecare, nr_opriri;
    TextView descriere_excursie, regulament, echipament_necesar, documente_necesare;
    TextView nr_min_particip, nr_max_particip;
    TextView pret, tip_moneda;
    TextView locuri_ramase;
    TextView dificultate;
    LinearLayout grad_dif_layout;
    LinearLayout layout_echipament_necesar, layout_documente_necesare, layout_descreiere_plecare;
    RecyclerView inside_trip_profil_recycler;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;
    LinearLayoutManager HorizontalLayout;
    Button button_alaturare_la_excursie;
    Button button_forum_excursie;
    TextView text_dupa_cerere;
    TextView pret_min1, pret_max1, moneda_var;
    LinearLayout ll_pret_fix, ll_pret_var;
    TextView detalii_pret1;

    BottomNavigationView inscriere_excursie_layout;

    Integer loc_ramase = 0;

    LinearLayout profil_organizator_layout;

    TextView inside_nr_oprire; //afisarea nr opririi - 1,2,3, .... n
    int nr_op = 0;
    LinearLayout inside_layout_list;
    TextView inside_descriere_transport, inside_locatie_item, inside_descriere_item;

    Button button_lista_asteptare;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    private DatabaseReference referenceTrips = database.getReference("Calatorii");
    DatabaseReference referenceTrip = FirebaseDatabase.getInstance().getReference().child("Calatorii");
    DatabaseReference referenceUsers = database.getReference("Utilizatori");
    DatabaseReference referenceTripss = database.getReference("Calatorii");
    ImageButton imageButton_stergere_trip, imageButton_anulare_trip, imageButton_finalizare_trip;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


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
        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());
        inside_trip_profil_recycler.setLayoutManager(RecyclerViewLayoutManager);
        text_dupa_cerere = findViewById(R.id.text_dupa_cerere);
        profil_organizator_layout = findViewById(R.id.profil_organizator_layout);
        button_alaturare_la_excursie = findViewById(R.id.button_alaturare_la_excursie);
        button_forum_excursie = findViewById(R.id.button_forum_excursie);

        imageButton_stergere_trip = findViewById(R.id.imageButton_stergere_trip1);
        imageButton_anulare_trip = findViewById(R.id.imageButton_anulare_trip1);
        imageButton_finalizare_trip = findViewById(R.id.imageButton_finalizeaza_trip1);


        pret_min1 = findViewById(R.id.pret_min1);
        pret_max1 = findViewById(R.id.pret_max1);
        moneda_var = findViewById(R.id.moneda_var);
        ll_pret_fix = findViewById(R.id.ll_pret_fix);
        ll_pret_var = findViewById(R.id.ll_pret_var);
        detalii_pret1 = findViewById(R.id.detalii_pret1);

        inscriere_excursie_layout = findViewById(R.id.inscriere_excursie_layout);

        inside_layout_list = findViewById(R.id.inside_layout_list);

        button_lista_asteptare = findViewById(R.id.button_lista_asteptare);

        referenceTrips.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Trip trip = keyNode.getValue(Trip.class);

                    if (trip.UID_organiztor.equals(uid_organizator) && trip.data_inceput.equals(data_start) && trip.data_final.equals(data_fin)) {

                        List<String> uidsParticipanti = new ArrayList<>();
                        if (trip.participanti != null) {
                            trip.participanti.forEach(new BiConsumer<String, User>() {
                                @Override
                                public void accept(String s, User user) {
                                    uidsParticipanti.add(user.UID);
                                }
                            });
                        }

                        List<String> uidsAsteptare = new ArrayList<>();
                        if (trip.participantiAsteptare != null) {
                            trip.participantiAsteptare.forEach(new BiConsumer<String, User>() {
                                @Override
                                public void accept(String s, User user) {
                                    uidsAsteptare.add(user.UID);
                                }
                            });


                        } else {
                            button_lista_asteptare.setVisibility(View.GONE);

                        }


                        titluHelp = trip.titlu_excursie;
                        data_inceputHelp = trip.data_inceput;
                        data_finalHelp = trip.data_final;
                        Uid_organizatorHelp = trip.UID_organiztor;
//
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
                        nr_opriri.setText(String.valueOf(trip.nr_opriri));
                        descriere_excursie.setText(trip.descriere_excursie);
                        regulament.setText(trip.regulament);
                        echipament_necesar.setText(trip.echipament_necesar);
                        documente_necesare.setText(trip.documente_necesare);
                        nr_min_particip.setText(trip.nr_min_particip);
                        nr_max_particip.setText(trip.nr_max_particip);
                        tip_moneda.setText(trip.tip_moneda);
                        dificultate.setText(trip.dificultate);
                        moneda_var.setText(trip.tip_moneda);
                        pret.setText(trip.pret);
                        pret_min1.setText(trip.pret_min);
                        pret_max1.setText(trip.pret_max);
                        detalii_pret1.setText(trip.detalii_pret);

                        imageButton_stergere_trip.setVisibility(View.GONE);
                        imageButton_anulare_trip.setVisibility(View.GONE);
                        imageButton_finalizare_trip.setVisibility(View.GONE);

                        if (user.getUid().equals(trip.UID_organiztor) && trip.participanti == null && !trip.status.equals("incheiata")) {
                            imageButton_stergere_trip.setVisibility(View.VISIBLE);
                            imageButton_anulare_trip.setVisibility(View.GONE);
                            imageButton_finalizare_trip.setVisibility(View.GONE);
                        } else if (user.getUid().equals(trip.UID_organiztor) && trip.participanti != null && trip.status.equals("viitoare")) {
                            imageButton_stergere_trip.setVisibility(View.GONE);
                            imageButton_anulare_trip.setVisibility(View.VISIBLE);
                            imageButton_finalizare_trip.setVisibility(View.GONE);
                        } else if (user.getUid().equals(trip.UID_organiztor) && trip.participanti != null && trip.status.equals("desfasurare")) {
                            imageButton_stergere_trip.setVisibility(View.GONE);
                            imageButton_anulare_trip.setVisibility(View.GONE);
                            imageButton_finalizare_trip.setVisibility(View.VISIBLE);
                        }

                        functionareButoane(trip, imageButton_stergere_trip, imageButton_anulare_trip, imageButton_finalizare_trip);


                        if (trip.vect_opriri != null) {
                            for (Map.Entry<String, Oprire> entry : trip.vect_opriri.entrySet()) {
                                View stopView = getLayoutInflater().inflate(R.layout.inside_trip_row_add_stop, null, false);
                                inside_layout_list.addView(stopView);
                                View v = inside_layout_list.getChildAt(nr_op);
                                nr_op++;

                                inside_nr_oprire = v.findViewById(R.id.inside_nr_oprire);
                                inside_descriere_transport = v.findViewById(R.id.inside_descriere_transport);
                                inside_locatie_item = v.findViewById(R.id.inside_locatie_item);
                                inside_descriere_item = v.findViewById(R.id.inside_descriere_item);

                                Oprire oprire = entry.getValue();

                                inside_nr_oprire.setText(String.valueOf(nr_op));
                                inside_descriere_item.setText(oprire.descriere_oprire);
                                inside_descriere_transport.setText(oprire.descriere_transport);
                                inside_locatie_item.setText(oprire.locatie_oprire);
                            }
                        } else {
                            Log.d("Nu exista opriri", "yes: ");
                        }

                        if (trip.participanti != null) {
                            loc_ramase = Integer.parseInt(trip.nr_max_particip) - trip.participanti.size();
                            locuri_ramase.setText(String.valueOf(loc_ramase)); //TODO
                        } else {
                            locuri_ramase.setText(trip.nr_max_particip);
                        }


                        HorizontalLayout = new LinearLayoutManager(InsideTripActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        inside_trip_profil_recycler.setLayoutManager(HorizontalLayout);

                        if (trip.UID_organiztor.equals(user.getUid()) || uidsParticipanti.contains(user.getUid())) {
                            button_alaturare_la_excursie.setVisibility(View.GONE);
                            button_forum_excursie.setVisibility(View.VISIBLE);
                            text_dupa_cerere.setVisibility(View.GONE);
                        } else if (uidsAsteptare.contains(user.getUid())) {
                            button_alaturare_la_excursie.setVisibility(View.GONE);
                            button_forum_excursie.setVisibility(View.GONE);
                            text_dupa_cerere.setVisibility(View.VISIBLE);
                        } else {
                            button_alaturare_la_excursie.setVisibility(View.VISIBLE);
                            button_forum_excursie.setVisibility(View.GONE);
                            text_dupa_cerere.setVisibility(View.GONE);
                        }


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

                        if ((trip.pret_min.isEmpty() && trip.pret_max.isEmpty() && (!trip.pret.isEmpty()))) {
                            ll_pret_var.setVisibility(View.GONE);
                            ll_pret_fix.setVisibility(View.VISIBLE);
                        }

                        if ((!trip.pret_min.isEmpty()) && (!trip.pret_max.isEmpty()) && trip.pret.isEmpty()) {
                            ll_pret_var.setVisibility(View.VISIBLE);
                            ll_pret_fix.setVisibility(View.GONE);
                        }

                        String sDate1 = trip.data_final;
                        Date date_fin = null;
                        try {
                            date_fin = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String sDate2 = trip.data_inceput;
                        Date date_incep = null;
                        try {
                            date_incep = new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Date date_now = new Date();

                        if (!(trip.status.equals("anulata") || trip.status.equals("finalizata")))
                            if (date_fin.before(date_now)) {
                                trip.status = "incheiata";
                            } else if ((date_incep.before(date_now) || date_incep.equals(date_now)) && (date_fin.after(date_now) || date_fin.equals(date_now))) {
                                trip.status = "desfasurare";
                            } else if (date_incep.after(date_now)) {
                                trip.status = "viitoare";
                            }

                        final String statusF = trip.status;
                        Query query = referenceTripss.orderByChild("UID_organiztor").equalTo(trip.UID_organiztor);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    Trip tripuita = child.getValue(Trip.class);
                                    String key = child.getKey();
                                    if (tripuita.data_inceput.equals(data_start) && tripuita.data_final.equals(data_fin)) {
                                        referenceTripss = referenceTripss.child(key);
                                        referenceTripss.child("status").setValue(statusF);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                        if (user.getUid().equals(uid_organizator)) {

                            String sDate4 = trip.data_final;
                            Date date_fin2 = null;
                            try {
                                date_fin2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate4);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            String sDate3 = trip.data_inceput;
                            Date date_incep2 = null;
                            try {
                                date_incep2 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate3);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Date date_now2 = new Date();

//                            if (date_fin2.before(date_now2)) {
//                                //trip.status = "incheiata";
//                                imageButton_finalizare_trip.setVisibility(View.GONE);
//                            } else if ((date_incep2.before(date_now2) || date_incep2.equals(date_now2)) && (date_fin2.after(date_now2) || date_fin2.equals(date_now2))) {
//                                //trip.status = "desfasurare";
//                                imageButton_finalizare_trip.setVisibility(View.VISIBLE);
//                            } else if (date_incep2.after(date_now2)) {
//                                //trip.status = "viitoare";
//                                imageButton_finalizare_trip.setVisibility(View.GONE);
//                            }
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
                new RecyclerViewConfigParticipant().setconfig(inside_trip_profil_recycler, InsideTripActivity.this, participanti, keys);
            }
        }, uid_organizator, data_start, data_fin);

        profil_organizator_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InsideTripActivity.this, ProfileActivity.class);
                intent.putExtra("uid", uid_organizator);
                //Start activity
                startActivity(intent);
            }
        });

        button_lista_asteptare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InsideTripActivity.this, ParticipantiAsteptareActivity.class);
                intent.putExtra("uid_organizator", uid_organizator);
                intent.putExtra("data_start", data_start);
                intent.putExtra("data_final", data_fin);
                startActivity(intent);
            }
        });

        button_alaturare_la_excursie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (loc_ramase == 0) {
//                    Toast.makeText(getApplicationContext(), "This is a message displayed in a Toast", Toast.LENGTH_LONG).show();
//                } else {
                referenceTrips.child("participanti");
                Query query = referenceUsers.orderByChild("UID").equalTo(user.getUid());
                //Log.d("uid_organiztaor",uid_organizator);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            User user = child.getValue(User.class);
                            Query query = referenceTrips.orderByChild("UID_organiztor").equalTo(uid_organizator);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                        String key = child.getKey();
                                        Trip trip = child.getValue(Trip.class);
                                        if (trip.data_final.equals(data_fin) && trip.data_inceput.equals(data_start)) {
                                            DatabaseReference referenceTripParticipanti = referenceTrips.child(key);
                                            referenceTripParticipanti.child("participantiAsteptare").push().setValue(new User(user.UID, user.prenume, user.nume, user.poza_profil));
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                DatabaseReference referenceUsers = FirebaseDatabase.getInstance().getReference("Utilizatori");

                Query query_uid = referenceUsers.orderByChild("UID").equalTo(user.getUid());
                query_uid.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            User user = child.getValue(User.class);
                            String key = child.getKey();
                            FirebaseDatabase.getInstance().getReference("Utilizatori").child(key).child("nr_excursii_participare").setValue(user.nr_excursii_participare + 1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }
            //          }
        });

        button_forum_excursie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InsideTripActivity.this, FormularActivity.class);
                intent.putExtra("titlu", titluHelp);
                intent.putExtra("data_inceput", data_inceputHelp);
                intent.putExtra("data_final", data_finalHelp);
                intent.putExtra("Uid_organizator", Uid_organizatorHelp);
                startActivity((intent));
            }
        });

    }

    private void functionareButoane(Trip trip, ImageButton msterge, ImageButton manuleaza, ImageButton mfinalizeaza) {
        final Trip tripFinal = trip;

        msterge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertStergere = new AlertDialog.Builder(view.getContext())
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                referenceTrip = FirebaseDatabase.getInstance().getReference().child("Calatorii");
                                Query query = referenceTrip.orderByChild("UID_organiztor").equalTo(tripFinal.UID_organiztor);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            String key = child.getKey();
                                            if (tripFinal.data_inceput.equals(child.child("data_inceput").getValue())
                                                    && tripFinal.data_final.equals(child.child("data_final").getValue())) {
                                                referenceTrip = referenceTrip.child(key);
                                                if (tripFinal.participanti == null) {
                                                    referenceTrip.removeValue();
                                                    DatabaseReference referenceUtilizatori = FirebaseDatabase.getInstance().getReference().child("Utilizatori");
                                                    Query gqery1 = referenceUtilizatori.orderByChild("UID").equalTo(tripFinal.UID_organiztor);

                                                    gqery1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            Log.d("Inainte", "");

                                                            for (DataSnapshot child_user : snapshot.getChildren()) {

                                                                String keyUser = child_user.getKey();
                                                                User user = child_user.getValue(User.class);

                                                                Log.d("Inainte", "" + user.nr_excursii_organizate + " " + keyUser);
                                                                FirebaseDatabase.getInstance().getReference("Utilizatori").child(keyUser).child("nr_excursii_organizate").setValue(user.nr_excursii_organizate - 1);
                                                                Log.d("Dupa", "" + user.nr_excursii_organizate);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Nu", null)
                        .setTitle("Sunteţi sigur că doriţi să stergeţi excursia?")
                        .create();
                alertStergere.show();
            }
        });

        manuleaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertAnulare = new AlertDialog.Builder(view.getContext())
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                referenceTrip = FirebaseDatabase.getInstance().getReference().child("Calatorii");
                                Query query = referenceTrip.orderByChild("UID_organiztor").equalTo(tripFinal.UID_organiztor);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            String key = child.getKey();
                                            if (tripFinal.data_inceput.equals(child.child("data_inceput").getValue())
                                                    && tripFinal.data_final.equals(child.child("data_final").getValue())) {
                                                if (tripFinal.participanti != null) {
                                                    referenceTrip.child(key).child("status").setValue("anulata");
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Nu", null)
                        .setTitle("Sunteţi sigur că doriţi să anulaţi excursia?")
                        .create();
                alertAnulare.show();
            }
        });

        mfinalizeaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertAnulare = new AlertDialog.Builder(view.getContext())
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                referenceTrip = FirebaseDatabase.getInstance().getReference().child("Calatorii");
                                Query query = referenceTrip.orderByChild("UID_organiztor").equalTo(tripFinal.UID_organiztor);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            String key = child.getKey();
                                            if (tripFinal.data_inceput.equals(child.child("data_inceput").getValue())
                                                    && tripFinal.data_final.equals(child.child("data_final").getValue())) {
                                                if (tripFinal.participanti != null) {
                                                    referenceTrip.child(key).child("status").setValue("finalizata");
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Nu", null)
                        .setTitle("Sunteţi sigur că doriţi să anulaţi excursia?")
                        .create();
                alertAnulare.show();
            }
        });

    }
}