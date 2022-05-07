package com.tripshare.hitrip.Trips;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FirebaseDatabaseHelperTrips {

    private FirebaseDatabase database;
    private DatabaseReference referenceTrips;
    private List<Trip> trips = new ArrayList<>();
    private List<String> keys = new ArrayList<>();
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
                    if (search.equals("all")) {
                        keys.add(keyNode.getKey());
                        trips.add(trip);
                    } else if (trip.titlu_excursie.toLowerCase(Locale.ROOT).contains(search) || trip.prenume.toLowerCase(Locale.ROOT).contains(search) || trip.nume.contains(search)) {
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
