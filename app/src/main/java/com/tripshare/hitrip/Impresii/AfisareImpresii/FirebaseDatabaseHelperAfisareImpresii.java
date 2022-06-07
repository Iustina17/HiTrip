
package com.tripshare.hitrip.Impresii.AfisareImpresii;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Impresii.Impresie;
import com.tripshare.hitrip.ProfileRelated.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseDatabaseHelperAfisareImpresii {

    private FirebaseDatabase database;
    private DatabaseReference referenceUsers;
    private List<Impresie> impresii = new ArrayList<>();
    private List<String> keys = new ArrayList<String>();
    private String stare, uid_user;


    public interface DataStatus {
        void DataIsLoaded(List<Impresie> impresiiList, List<String> keys);
    }


    FirebaseDatabaseHelperAfisareImpresii(String stare, String uid_user) {
        this.stare = stare;
        this.database = FirebaseDatabase.getInstance();
        this.referenceUsers = database.getReference("Utilizatori");
        this.uid_user = uid_user;
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
                    if (user.UID.equals(uid_user)) {
                        if (stare.equals("organizator")) {
                            if (user.impresie_organizare_user != null)
                            for (Map.Entry<String, Impresie> entry : user.impresie_organizare_user.entrySet()) {
                                Impresie impresie = entry.getValue();
                                impresii.add(impresie);

                                keys.add(String.valueOf(i) + "_key");
                                i++;
                            }
                        } else if (stare.equals("participare")) {
                            if (user.impresie_participare_user != null)
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
