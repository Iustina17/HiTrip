
package com.tripshare.hitrip.Forum.Sectiuni;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Impresii.Impresie;
import com.tripshare.hitrip.ProfileRelated.User;
import com.tripshare.hitrip.Trips.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseDatabaseHelperForumSectiuni {

    private FirebaseDatabase database;
    private DatabaseReference referenceSectiuni;
    private List<SectiuneForum> sectiuniList = new ArrayList<>();
    private List<String> keys = new ArrayList<String>();
    String uid_organizator, data_start, data_fin, titlu;


    public interface DataStatus {
        void DataIsLoaded(List<SectiuneForum> sectiuniList, List<String> keys);
    }


    FirebaseDatabaseHelperForumSectiuni(String uid_organizator, String data_start, String data_fin, String titlu) {
        this.uid_organizator = uid_organizator;
        this.data_start = data_start;
        this.data_fin = data_fin;
        this.titlu = titlu;
        this.database = FirebaseDatabase.getInstance();
        this.referenceSectiuni = database.getReference("Sectiuni");
    }


    void showSectiuni(final DataStatus dataStatus) {

        Query query = referenceSectiuni.orderByChild("titlu_excursie").equalTo(titlu);
        query.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sectiuniList.clear();
                keys.clear();
                int i = 0;
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    SectiuneForum sectiuneForum = keyNode.getValue(SectiuneForum.class);
                    String uid_user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    if (sectiuneForum.uid_organizator.equals(uid_organizator)
                            && sectiuneForum.data_final.equals(data_fin)
                            && sectiuneForum.data_inceput.equals(data_start)) {
                        sectiuniList.add(sectiuneForum);
                        keys.add(String.valueOf(i) + "_key");
                        i++;
                    }

                }
                dataStatus.DataIsLoaded(sectiuniList, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
