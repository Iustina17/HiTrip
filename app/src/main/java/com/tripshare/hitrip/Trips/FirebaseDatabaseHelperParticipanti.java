
package com.tripshare.hitrip.Trips;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Trips.Trip;
import com.tripshare.hitrip.User;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelperParticipanti {

    private FirebaseDatabase database;
    private DatabaseReference referenceTrips, referenceUsers;
    private List<User> participanti = new ArrayList<>();
    private List<Trip> trips = new ArrayList<>();
    private List<Integer> keys = new ArrayList<Integer>();
    private String uid_organizator, data_start, data_fin;


    public interface DataStatus {
        void DataIsLoaded(List<User> participanti, List<Integer> keys);
    }


    FirebaseDatabaseHelperParticipanti() {
        this.database = FirebaseDatabase.getInstance();
        this.referenceTrips = database.getReference("Calatorii");
        // this.referenceUsers = database.getReference("Utilizatori");


    }


    void showParticipanti(final DataStatus dataStatus, String uid_organizator, String data_start, String data_fin) {
        this.uid_organizator = uid_organizator;
        this.data_start = data_start;
        this.data_fin = data_fin;
        referenceTrips.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                participanti.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Trip trip = keyNode.getValue(Trip.class);
                    if (uid_organizator.equals(trip.UID_organiztor) && data_start.equals(trip.data_inceput) && data_fin.equals(trip.data_final)) {
                        //for (int i = 0; i < trip.participanti.size();i++)
                        if (trip.participanti != null) {
                            for (User participant : trip.participanti) {
                                keys.add(trip.participanti.indexOf(participant));
                                participanti.add(participant);
                            }
                        } else {
                                Log.d("participanti","Nu exista participanti");
                        }

                    }

                }
                dataStatus.DataIsLoaded(participanti, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
