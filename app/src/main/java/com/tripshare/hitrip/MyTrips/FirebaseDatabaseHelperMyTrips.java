package com.tripshare.hitrip.MyTrips;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Trips.Trip;
import com.tripshare.hitrip.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FirebaseDatabaseHelperMyTrips {

    private FirebaseDatabase database;
    private DatabaseReference referenceTrips;
    private List<Trip> trips = new ArrayList<>();
    private List<String> keys = new ArrayList<>();
    String buton1, buton2;

    TextView text_nuExista_excursii;

    public interface DataStatus {
        void DataIsLoaded(List<Trip> trips, List<String> keys);
    }

    FirebaseDatabaseHelperMyTrips(String buton1, String buton2,    TextView text_nuExista_excursii) {
        this.database = FirebaseDatabase.getInstance();
        this.referenceTrips = database.getReference("Calatorii");
        this.buton1 = buton1;
        this.buton2 = buton2;
        this.text_nuExista_excursii = text_nuExista_excursii;

    }

    void showTrips(final DataStatus dataStatus) {
        referenceTrips.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trips.clear();
                keys.clear();
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
                    if (date_fin.before(date_now)) {
                        trip.status = "incheiata";
                    } else if ((date_incep.before(date_now) || date_incep.equals(date_now)) && (date_fin.after(date_now) || date_fin.equals(date_now))) {
                        trip.status = "desfasurare";
                    } else if (date_incep.after(date_now)) {
                        trip.status = "viitoare";
                    }

                    final String statusF = trip.status;


                    //if (tripuita.data_inceput.equals(data_start) && tripuita.data_final.equals(data_fin)) {
                    DatabaseReference referenceTripss = referenceTrips.child(keyNode.getKey());
                    referenceTripss.child("status").setValue(statusF);
                    // }
                    String uid_user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String status="";
                    if(buton2.equals("trecute"))
                        status = "incheiata";
                    else if(buton2.equals("prezente"))
                        status = "desfasurare";
                    else if(buton2.equals("viitoare"))
                        status = "viitoare";

                    if (trip.status.equals(status)) {
                        if (buton1.equals("organizare") && trip.UID_organiztor.equals(uid_user)) {
                            keys.add(keyNode.getKey());
                            if(trips!=null){
                                text_nuExista_excursii.setVisibility(View.GONE);
                            }
                            trips.add(trip);

                        }

                        if ((buton1.equals("participare") && !trip.UID_organiztor.equals(uid_user))) {
                            if (trip.participanti != null) {
                                for (Map.Entry<String, User> entry : trip.participanti.entrySet()) {
                                    User user = entry.getValue();
                                    if (user.UID.equals(uid_user)) {
                                        keys.add(keyNode.getKey());
                                        if(trips!=null){
                                            text_nuExista_excursii.setVisibility(View.GONE);
                                        }
                                        trips.add(trip);

                                    }
                                }
                            }
                        }
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


