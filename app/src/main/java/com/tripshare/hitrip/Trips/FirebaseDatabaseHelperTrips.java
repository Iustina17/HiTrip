package com.tripshare.hitrip.Trips;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Impresii.Feedback;
import com.tripshare.hitrip.ProfileRelated.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FirebaseDatabaseHelperTrips {

    private FirebaseDatabase database;
    private DatabaseReference referenceTrips;
    private List<Trip> trips = new ArrayList<>();
    private List<String> keys = new ArrayList<>();
    DatabaseReference referenceTripss;
    String search;

    public interface DataStatus {
        void DataIsLoaded(List<Trip> trips, List<String> keys);
    }

    FirebaseDatabaseHelperTrips(String search) {
        this.database = FirebaseDatabase.getInstance();
        this.referenceTrips = database.getReference("Calatorii");
        this.search = search;
    }

    void showTrips(final DataStatus dataStatus) {
        referenceTrips.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trips.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Trip trip = keyNode.getValue(Trip.class);

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

                    User user;
                    if (!(trip.status.equals("anulata") || trip.status.equals("finalizata"))){
                        if (date_fin.before(date_now) && trip.status.equals("desfasurare") && !trip.status.equals("anulata")) {
                            trip.status = "incheiata";
                            if (trip.participanti != null && trip.impresii_date_de_organizator == null && trip.impresii_date_de_participanti == null) {
                                for (Map.Entry<String, User> entry : trip.participanti.entrySet()) {
                                    referenceTripss = referenceTrips.child(keyNode.getKey());
                                    user = entry.getValue();
                                    Feedback feedback = new Feedback(user.UID, "nu", user.nume, user.prenume, user.poza_profil);
                                    referenceTripss.child("impresii_date_de_organizator").push().setValue(feedback);
                                    referenceTripss.child("impresii_date_de_participanti").push().setValue(feedback);
                                }
                            }
                        } else if ((date_incep.before(date_now) || date_incep.equals(date_now)) && (date_fin.after(date_now) || date_fin.equals(date_now)) && !trip.status.equals("anulata")) {
                            trip.status = "desfasurare";
                        } else if (date_incep.after(date_now) && !trip.status.equals("anulata")) {
                            trip.status = "viitoare";
                        }
                        if(trip.status.equals("anulata") && trip.participanti==null && trip.participantiAsteptare==null){
                            referenceTrips.child(keyNode.getKey()).removeValue();
                        }
                    }
                    if (trip.status.equals("finalizata") && trip.participanti != null && trip.impresii_date_de_organizator == null && trip.impresii_date_de_participanti == null) {
                        for (Map.Entry<String, User> entry : trip.participanti.entrySet()) {
                            referenceTripss = referenceTrips.child(keyNode.getKey());
                            user = entry.getValue();
                            Feedback feedback = new Feedback(user.UID, "nu", user.nume, user.prenume, user.poza_profil);
                            referenceTripss.child("impresii_date_de_organizator").push().setValue(feedback);
                            referenceTripss.child("impresii_date_de_participanti").push().setValue(feedback);
                        }
                    }


                    final String statusF = trip.status;

                    //if (tripuita.data_inceput.equals(data_start) && tripuita.data_final.equals(data_fin)) {
                    referenceTripss = referenceTrips.child(keyNode.getKey());
                    referenceTripss.child("status").setValue(statusF);
                    // }

                    if (trip.status.equals("viitoare"))
                        if (search.equals("all")) {
                            keys.add(keyNode.getKey());
                            trips.add(trip);
                        } else if (trip.titlu_excursie.toLowerCase(Locale.ROOT).contains(search) ||
                                trip.prenume.toLowerCase(Locale.ROOT).contains(search) ||
                                trip.nume.toLowerCase(Locale.ROOT).contains(search) ||
                                trip.tip.toLowerCase(Locale.ROOT).contains(search)) {
                            keys.add(keyNode.getKey());
                            trips.add(trip);
                        }

                }
                dataStatus.DataIsLoaded(trips, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}


