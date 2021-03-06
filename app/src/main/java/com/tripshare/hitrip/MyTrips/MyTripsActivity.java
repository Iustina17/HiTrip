package com.tripshare.hitrip.MyTrips;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Forum.Comentarii.Comentariu;
import com.tripshare.hitrip.Impresii.OferireImpresii.AdaugaImpresiePtOrganizatorActivity;
import com.tripshare.hitrip.Impresii.OferireImpresii.AdaugaImpresiePtParticipantiActivity;
import com.tripshare.hitrip.HelpActivity;
import com.tripshare.hitrip.Impresii.Feedback;
import com.tripshare.hitrip.LoginSignUp.LoginActivity;
import com.tripshare.hitrip.ProfileRelated.MyProfileActivity;
import com.tripshare.hitrip.R;
import com.tripshare.hitrip.Sugestii.SugestiiActivity;
import com.tripshare.hitrip.Trips.CreateTrip;
import com.tripshare.hitrip.Trips.MainActivity;
import com.tripshare.hitrip.Trips.Trip;
import com.tripshare.hitrip.ProfileRelated.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class MyTripsActivity extends AppCompatActivity {
    Button organizare, participare;
    Button trecute, prezente, viitoare;
    RecyclerView mRecyclerView;
    TextView text_nuExista_excursii;

    //pentru nav_drawer
    TextView nume, prenume;
    ImageView poza;
    DrawerLayout drawerLayout;
    String uid_user_nav;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference referenceUsers = database.getReference("Utilizatori");

    DatabaseReference referenceTrip = FirebaseDatabase.getInstance().getReference().child("Calatorii");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);

        mRecyclerView = findViewById(R.id.my_trips1);
        organizare = findViewById(R.id.organizare);
        participare = findViewById(R.id.participare);
        trecute = findViewById(R.id.trecute);
        prezente = findViewById(R.id.prezente);
        viitoare = findViewById(R.id.viitoare);

        text_nuExista_excursii = findViewById(R.id.text_nuExista_excursii);

        //nav_drawer
        drawerLayout = findViewById((R.id.drawer_layout));
        nume = findViewById(R.id.nume_nav);
        prenume = findViewById(R.id.prenume_nav);
        poza = findViewById(R.id.menu_profile);

        referenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    User user = keyNode.getValue(User.class);

                    uid_user_nav = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    if (user.UID.equals(uid_user_nav)) {
                        //imagine_excursie.setAdjustViewBounds(trip.imagine_excursie); todo
                        nume.setText(user.nume);
                        prenume.setText(user.prenume);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        String buton1 = "", buton2 = "";
        if (getIntent().hasExtra("buton1"))
            buton1 = getIntent().getStringExtra("buton1");
        if (getIntent().hasExtra("buton2"))
            buton2 = getIntent().getStringExtra("buton2");

        verificare_feedback(buton1);


        new com.tripshare.hitrip.MyTrips.FirebaseDatabaseHelperMyTrips(buton1, buton2, text_nuExista_excursii).showTrips(new FirebaseDatabaseHelperMyTrips.DataStatus() {

            @Override
            public void DataIsLoaded(List<Trip> trips, List<String> keys) {
                new com.tripshare.hitrip.MyTrips.RecyclerViewConfigMyTrip().setconfig(mRecyclerView, MyTripsActivity.this, trips, keys);
            }
        });

        organizare.setBackgroundColor(getResources().getColor(R.color.dell));
        participare.setBackgroundColor(getResources().getColor(R.color.dell));

        if (buton1.equals("organizare")) {
            organizare.setBackgroundColor(getResources().getColor(R.color.highlight));
            participare.setBackgroundColor(getResources().getColor(R.color.dell));
        } else if (buton1.equals("participare")) {
            participare.setBackgroundColor(getResources().getColor(R.color.highlight));
            organizare.setBackgroundColor(getResources().getColor(R.color.dell));
        }

        if (buton2.equals("trecute")) {
            prezente.setBackgroundColor(getResources().getColor(R.color.green6));
            viitoare.setBackgroundColor(getResources().getColor(R.color.green6));
            trecute.setBackgroundColor(getResources().getColor(R.color.highlight2));
            text_nuExista_excursii.setText("Nu exist?? excursii ??ncheiate");
        } else if (buton2.equals("prezente")) {
            trecute.setBackgroundColor(getResources().getColor(R.color.green6));
            viitoare.setBackgroundColor(getResources().getColor(R.color.green6));
            prezente.setBackgroundColor(getResources().getColor(R.color.highlight2));
            text_nuExista_excursii.setText("Nu exist?? excursii ??n desf????urare");
        } else if (buton2.equals("viitoare")) {
            trecute.setBackgroundColor(getResources().getColor(R.color.green6));
            prezente.setBackgroundColor(getResources().getColor(R.color.green6));
            viitoare.setBackgroundColor(getResources().getColor(R.color.highlight2));
            text_nuExista_excursii.setText("Nu exist?? excursii viitoare");
        }

        organizare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apasare_butoane("organizare", "prezente");
            }
        });

        participare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apasare_butoane("participare", "prezente");

            }
        });

        String finalButon = buton1;
        trecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apasare_butoane(finalButon, "trecute");
            }
        });
        prezente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apasare_butoane(finalButon, "prezente");
            }
        });
        viitoare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apasare_butoane(finalButon, "viitoare");
            }
        });


    }

    private void verificare_feedback(String buton1) {
        if (buton1.equals("organizare")) {
            String uid_utilizator_curent = FirebaseAuth.getInstance().getCurrentUser().getUid();
            referenceTrip.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        Trip trip = keyNode.getValue(Trip.class);
                        if (trip.UID_organiztor.equals(uid_utilizator_curent)) {
                            if ((trip.status.equals("incheiata") || (trip.status.equals("finalizata")) && trip.impresii_date_de_organizator != null)) {
                                for (Map.Entry<String, Feedback> entry : trip.impresii_date_de_organizator.entrySet()) {
                                    Feedback feedback = entry.getValue();
                                    if (feedback.stare_feedback.equals("nu")) {
                                        Intent intent = new Intent(MyTripsActivity.this, AdaugaImpresiePtParticipantiActivity.class);
                                        intent.putExtra("titlu", trip.titlu_excursie);
                                        intent.putExtra("data_inceput", trip.data_inceput);
                                        intent.putExtra("data_final", trip.data_final);
                                        startActivity((intent));
                                        break;
                                    }
                                }
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        if (buton1.equals("participare")) {
            referenceTrip.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                        Trip trip = keyNode.getValue(Trip.class);
                        if ((trip.status.equals("incheiata") || (trip.status.equals("finalizata")))) {
                            List<String> uids = new ArrayList<>();
                            if (trip.participanti != null) {
                                trip.participanti.forEach(new BiConsumer<String, User>() {
                                    @Override
                                    public void accept(String s, User user) {
                                        uids.add(user.UID);
                                    }
                                });
                            }

                            String uid_utilizator_curent = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            if (uids.contains(uid_utilizator_curent)) {
                                for (Map.Entry<String, Feedback> entry : trip.impresii_date_de_participanti.entrySet()) {
                                    Feedback feedback = entry.getValue();
                                    if (feedback.uid_participant.equals(uid_utilizator_curent) && feedback.stare_feedback.equals("nu")) {
                                        Intent intent = new Intent(MyTripsActivity.this, AdaugaImpresiePtOrganizatorActivity.class);
                                        intent.putExtra("titlu", trip.titlu_excursie);
                                        intent.putExtra("data_inceput", trip.data_inceput);
                                        intent.putExtra("data_final", trip.data_final);
                                        startActivity((intent));
                                    }
                                }
                            }
                        }
                        else if (trip.status.equals("anulata")) {
                            List<String> uids_participanti = new ArrayList<>();
                            if (trip.participanti != null) {
                                trip.participanti.forEach(new BiConsumer<String, User>() {
                                    @Override
                                    public void accept(String s, User user) {
                                        uids_participanti.add(user.UID);
                                    }
                                });
                            }

                            String uid_utilizator_curent = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            if (uids_participanti.contains(uid_utilizator_curent)) {
                                final AlertDialog alertAcreditare = new AlertDialog.Builder(MyTripsActivity.this)
                                        .setPositiveButton("", null)
                                        .setNegativeButton("ok", null)
                                        .setTitle("Excursie anulat??")
                                        .create();

                                //liniar layout pentru AlertDialog
                                LinearLayout layout = new LinearLayout(MyTripsActivity.this);
                                layout.setOrientation(LinearLayout.VERTICAL);
                                layout.setPadding(40, 20, 40, 10);

                                //edit text parola verificare
                                final TextView excursie_anulata = new TextView(MyTripsActivity.this);
                                excursie_anulata.setText("Excursia " + trip.titlu_excursie + " din perioada "+trip.data_inceput+
                                        " - "+trip.data_final+" a fost anulat??. Pentru mai multe detalii contacta??i organizatorul "  +
                                        trip.prenume+" "+trip.nume + " la num??rul de telefon " + trip.telefon_organizator);
                                layout.addView(excursie_anulata);

                                alertAcreditare.setView(layout);
                                alertAcreditare.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface dialog) {
                                        Button butonAnulare = alertAcreditare.getButton(Dialog.BUTTON_NEGATIVE);
                                        butonAnulare.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String key_trip = keyNode.getKey();
                                                for (Map.Entry<String, User> entry : trip.participanti.entrySet()) {
                                                    User user1 = entry.getValue();
                                                    String uidCurent = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    if(user1.UID.equals(uidCurent)){
                                                        String keyUser = entry.getKey();
                                                        referenceTrip.child(key_trip).child("participanti").child(keyUser).removeValue();
                                                        alertAcreditare.dismiss();
                                                    }
                                                }



                                            }
                                        });
                                    }
                                });

                                alertAcreditare.show();
                            }

                            List<String> uids_participantiAsteptare = new ArrayList<>();
                            if (trip.participantiAsteptare != null) {
                                trip.participantiAsteptare.forEach(new BiConsumer<String, User>() {
                                    @Override
                                    public void accept(String s, User user) {
                                        uids_participantiAsteptare.add(user.UID);
                                    }
                                });
                            }

                            if (uids_participantiAsteptare.contains(uid_utilizator_curent)) {
                                final AlertDialog alertAcreditare = new AlertDialog.Builder(MyTripsActivity.this)
                                        .setPositiveButton("", null)
                                        .setNegativeButton("ok", null)
                                        .setTitle("Excursie anulat??")
                                        .create();

                                //liniar layout pentru AlertDialog
                                LinearLayout layout = new LinearLayout(MyTripsActivity.this);
                                layout.setOrientation(LinearLayout.VERTICAL);
                                layout.setPadding(40, 20, 40, 10);

                                //edit text parola verificare
                                final TextView excursie_anulata = new TextView(MyTripsActivity.this);
                                excursie_anulata.setText("Excursia " + trip.titlu_excursie + " din perioada "+trip.data_inceput+
                                        " - "+trip.data_final+" a fost anulat?? iar dumneavoastr?? v?? afla??i ??n lista de a??teptare.");
                                layout.addView(excursie_anulata);

                                alertAcreditare.setView(layout);
                                alertAcreditare.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface dialog) {
                                        Button butonAnulare = alertAcreditare.getButton(Dialog.BUTTON_NEGATIVE);
                                        butonAnulare.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                String key_trip = keyNode.getKey();
                                                for (Map.Entry<String, User> entry : trip.participantiAsteptare.entrySet()) {
                                                    User user1 = entry.getValue();
                                                    String uidCurent = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    if(user1.UID.equals(uidCurent)){
                                                        String keyUser = entry.getKey();
                                                        referenceTrip.child(key_trip).child("participantiAsteptare").child(keyUser).removeValue();
                                                        alertAcreditare.dismiss();
                                                    }
                                                }



                                            }
                                        });
                                    }
                                });

                                alertAcreditare.show();
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

    public void apasare_butoane(String buton1, String buton2) {
        if (buton1.equals("organizare")) {
            if (buton2.equals("trecute")) {
                Intent intent = new Intent(MyTripsActivity.this, MyTripsActivity.class);
                intent.putExtra("buton1", "organizare");
                intent.putExtra("buton2", "trecute");
                startActivity(intent);
                overridePendingTransition(0, 0);

            } else if (buton2.equals("prezente")) {
                Intent intent = new Intent(MyTripsActivity.this, MyTripsActivity.class);
                intent.putExtra("buton1", "organizare");
                intent.putExtra("buton2", "prezente");
                startActivity(intent);
                overridePendingTransition(0, 0);

            } else {
                Intent intent = new Intent(MyTripsActivity.this, MyTripsActivity.class);
                intent.putExtra("buton1", "organizare");
                intent.putExtra("buton2", "viitoare");
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        }
        if (buton1.equals("participare")) {

            if (buton2.equals("trecute")) {
                Intent intent = new Intent(MyTripsActivity.this, MyTripsActivity.class);
                intent.putExtra("buton1", "participare");
                intent.putExtra("buton2", "trecute");
                startActivity(intent);
                overridePendingTransition(0, 0);


            } else if (buton2.equals("prezente")) {

                Intent intent = new Intent(MyTripsActivity.this, MyTripsActivity.class);
                intent.putExtra("buton1", "participare");
                intent.putExtra("buton2", "prezente");
                startActivity(intent);
                overridePendingTransition(0, 0);

            } else {
                Intent intent = new Intent(MyTripsActivity.this, MyTripsActivity.class);
                intent.putExtra("buton1", "participare");
                intent.putExtra("buton2", "viitoare");
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        }
    }

    //nav_drawer

    public void ClickMenu(View view) {
        //Open drawer
        openDrawer(drawerLayout);
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view) {
        redirectActivity(this, MainActivity.class);
    }

    private static void closeDrawer(DrawerLayout drawerLayout) {

        if (drawerLayout.isDrawerOpen((GravityCompat.START))) {

            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickProfile(View view) {
        redirectActivity(this, MyProfileActivity.class);
    }

    public void ClickTrips(View view) {
        //Recreate activity
        redirectActivity(this, MainActivity.class);
    }

    public void ClickCreateTrip(View view) {
        redirectActivity(this, CreateTrip.class);
    }

    public void ClickMyTrips(View view) {
        //Redirect activity to feed
//        Intent intent = new Intent(MainActivity.this, MyTripsActivity.class);
//        intent.putExtra("buton1", "organizare");
//        intent.putExtra("buton2", "prezente");
//        startActivity(intent);
        recreate();
    }


    public void ClickHelp(View view) {
        redirectActivity(this, HelpActivity.class);
    }

    public void ClickSuggestions(View view) {
        redirectActivity(this, SugestiiActivity.class);
    }

    public void ClickLogout(View view) {
        //Close app
        logout(this);
    }

    private void logout(Activity activity) {
        //Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Set Title
        builder.setTitle("Deconectare");
        //Set message
        builder.setMessage("Sigur vre??i s?? v?? deconecta??i?");
        //Positive yes button
        builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Finish activity
                Intent login_intent = new Intent(activity, LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(login_intent);
            }
        });

        //Negative no button
        builder.setNegativeButton("NU", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Dismis dialog
                dialogInterface.dismiss();
            }
        });

        //Show dialog
        builder.show();

    }

    private static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity, aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity((intent));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }
}