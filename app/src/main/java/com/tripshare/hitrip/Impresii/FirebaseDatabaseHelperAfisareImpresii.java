
package com.tripshare.hitrip.Impresii;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Trips.Trip;
import com.tripshare.hitrip.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class FirebaseDatabaseHelperAfisareImpresii {

    private FirebaseDatabase database;
    private DatabaseReference referenceUsers;
    private List<Impresie> impresii = new ArrayList<>();
    private List<String> keys = new ArrayList<String>();
    private String stare;


    public interface DataStatus {
        void DataIsLoaded(List<Impresie> impresiiList, List<String> keys);
    }


    FirebaseDatabaseHelperAfisareImpresii(String stare) {
        this.stare = stare;
        this.database = FirebaseDatabase.getInstance();
        this.referenceUsers = database.getReference("Utilizatori");
    }


    void showImpresii(final DataStatus dataStatus) {

        referenceUsers.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                impresii.clear();
                keys.clear();
                int i = 0;
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    User user = keyNode.getValue(User.class);
                    String uid_user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    if (user.UID.equals(uid_user)) {
                        if (stare.equals("organizator")) {
                            for (Map.Entry<String, Impresie> entry : user.impresie_organizare_user.entrySet()) {
                                Impresie impresie = entry.getValue();
                                impresii.add(impresie);
                                keys.add(String.valueOf(i) + "_key");
                                i++;
                            }
                        } else if (stare.equals("participare")) {
                            for (Map.Entry<String, Impresie> entry : user.impresie_participare_user.entrySet()) {
                                Impresie impresie = entry.getValue();
                                impresii.add(impresie);
                                keys.add(String.valueOf(i) + "_key");
                                i++;
                            }
                        }


                    }

                }
                dataStatus.DataIsLoaded(impresii, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
