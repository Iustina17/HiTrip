package com.tripshare.hitrip.Impresii.OferireImpresii;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Impresii.Feedback;
import com.tripshare.hitrip.Trips.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseDatabaseHelperImpresiiPtParticipanti {

    private FirebaseDatabase database;
    private DatabaseReference referenceTrips;
    private List<Feedback> feedbackList = new ArrayList<>();
    private List<String> keys = new ArrayList<>();
    String titlu, data_start, data_fin;
    Trip trip;


    public interface DataStatus {
        void DataIsLoaded(List<Feedback> feedbackList, List<String> keys, Trip trip);
    }

    FirebaseDatabaseHelperImpresiiPtParticipanti(String titlu, String data_start, String data_fin) {
        this.database = FirebaseDatabase.getInstance();
        this.referenceTrips = database.getReference("Calatorii");
        this.titlu = titlu;
        this.data_start = data_start;
        this.data_fin = data_fin;
    }

    void showTrips(final DataStatus dataStatus) {
        referenceTrips.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                feedbackList.clear();
                keys.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    trip = keyNode.getValue(Trip.class);
                    String uid_user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    if(trip.titlu_excursie.equals(titlu) && trip.data_inceput.equals(data_start) && trip.data_final.equals(data_fin)&& trip.UID_organiztor.equals(uid_user)){
                        for (Map.Entry<String, Feedback> entry : trip.impresii_date_de_organizator.entrySet()) {
                            Feedback feedback = entry.getValue();
                            if(feedback.stare_feedback.equals("nu")) {
                                feedbackList.add(feedback);
                                keys.add(feedback.uid_participant);
                            }
                        }
                    }

                }
                dataStatus.DataIsLoaded(feedbackList, keys, trip);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}


