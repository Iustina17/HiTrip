//TODO
//package com.tripshare.hitrip.Trips;
//
//import androidx.annotation.NonNull;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.tripshare.hitrip.User;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class FirebaseDatabaseHelperParticipanti {
//
//    private FirebaseDatabase database;
//    private DatabaseReference referenceTrips;
//    private List<User> particpanti = new ArrayList<>();
//    private List<Trip> trips = new ArrayList<>();
//    private List<String> keys = new ArrayList<>();
//    private String uid_organizator, data_start, data_fin;
//
//
//    public interface DataStatus {
//        void DataIsLoaded(List<User> participanti, List<String> keys,String uid_organizator,String data_start,String data_fin);
//    }
//
//    FirebaseDatabaseHelperParticipanti() {
//        this.database = FirebaseDatabase.getInstance();
//        this.referenceTrips = database.getReference("Calatorii");
//    }
//
//    void showParticipanti(final DataStatus dataStatus) {
//
//        referenceTrips.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                trips.clear();
//                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
//                    Trip trip = keyNode.getValue(Trip.class);
//                    keys.add(keyNode.getKey());
//                    trips.add(trip);
//                }
//                dataStatus.DataIsLoaded(trips, keys);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//}
