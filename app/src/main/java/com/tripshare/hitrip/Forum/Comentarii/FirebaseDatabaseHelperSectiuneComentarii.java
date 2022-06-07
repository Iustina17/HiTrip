
package com.tripshare.hitrip.Forum.Comentarii;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Forum.Sectiuni.SectiuneForum;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class FirebaseDatabaseHelperSectiuneComentarii {

    private FirebaseDatabase database;
    private DatabaseReference referenceSectiuni;
    private List<Comentariu> comentarii = new ArrayList<>();
    private List<String> keys = new ArrayList<String>();
    public String uid_organizator, data_start, data_fin, titlu, denumire;


    public interface DataStatus  {
        void DataIsLoaded(List<Comentariu> comentarii, List<String> keys);
    }


    FirebaseDatabaseHelperSectiuneComentarii(String uid_organizator, String data_start, String data_fin, String titlu, String denumire) {
        this.uid_organizator = uid_organizator;
        this.data_start = data_start;
        this.data_fin = data_fin;
        this.titlu = titlu;
        this.denumire = denumire;
        this.database = FirebaseDatabase.getInstance();
        this.referenceSectiuni = database.getReference("Sectiuni");
    }


    void showSectiuni(final DataStatus dataStatus) {
        Query query = referenceSectiuni.orderByChild("titlu_excursie").equalTo(titlu);
        query.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comentarii.clear();
                keys.clear();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    SectiuneForum sectiuneForum = keyNode.getValue(SectiuneForum.class);
                    if (uid_organizator.equals(sectiuneForum.uid_organizator) && data_start.equals(sectiuneForum.data_inceput)
                            && data_fin.equals(sectiuneForum.data_final) && denumire.equals(sectiuneForum.denumire_sectiune)) {
                        if (sectiuneForum.comentarii != null) {
                            sectiuneForum.comentarii.forEach(new BiConsumer<String, Comentariu>() {
                                @Override
                                public void accept(String s, Comentariu comentariu) {
                                    comentarii.add(comentariu);
                                }
                            });
                            for (int i = 0; i < sectiuneForum.comentarii.size(); i++) {
                                keys.add(String.valueOf(i)+"_key");
                            }
                        } else {
                            Log.d("participanti", "Nu exista participanti");
                        }
                    }

                }
                dataStatus.DataIsLoaded(comentarii, keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
