//TODO
package com.tripshare.hitrip.Forum.Comentarii;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Forum.Sectiuni.SectiuneForum;
import com.tripshare.hitrip.Impresii.Feedback;
import com.tripshare.hitrip.ProfileRelated.User;
import com.tripshare.hitrip.R;

import java.util.List;
import java.util.Map;

class RecyclerViewConfigSectiuneComentarii {
    private Context mContext;
    private ComentariuAdaptor comentariuAdaptor;
    private String uid_organizator, data_start, data_fin, titlu, denumire_sectiune;

    void setconfig(RecyclerView recyclerView, Context context, List<Comentariu> comentarii, List<String> keys, String uid_organizator, String data_start, String data_fin, String titlu, String denumire_sectiune) {
        mContext = context;
        comentariuAdaptor = new ComentariuAdaptor(comentarii, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(comentariuAdaptor);
        this.uid_organizator = uid_organizator;
        this.data_start = data_start;
        this.data_fin= data_fin;
        this.titlu=titlu;
        this.denumire_sectiune=denumire_sectiune;
    }

    class ComentariuItemView extends RecyclerView.ViewHolder {
        TextView prenume_pers_comentariu, nume_pers_comenatriu;
        TextView text_comentariu;
        ImageView poza_pers_comentariu;
        String poza_string;
        ImageButton imageButton_sterge_comentariu;
        LinearLayout layout_sterge_comm;


        ComentariuItemView(@NonNull final ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.item_comentariu, parent, false));

            prenume_pers_comentariu = itemView.findViewById(R.id.prenume_pers_comentariu);
            nume_pers_comenatriu = itemView.findViewById(R.id.nume_pers_comenatriu);
            text_comentariu = itemView.findViewById(R.id.text_comentariu);
            poza_pers_comentariu = itemView.findViewById(R.id.poza_pers_comentariu);
            imageButton_sterge_comentariu = itemView.findViewById(R.id.imageButton_sterge_comentariu);
            layout_sterge_comm = itemView.findViewById(R.id.layout_sterge_comm);
        }

        @SuppressLint("SetTextI18n")
        void bind(Comentariu comentariu, String key) {
            prenume_pers_comentariu.setText(comentariu.prenumeAutor);
            nume_pers_comenatriu.setText(comentariu.numeAutor);
            text_comentariu.setText(comentariu.continut);
            poza_string = comentariu.poza;
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (comentariu.uidAutor.equals(uid) || uid.equals(uid_organizator)) {
                layout_sterge_comm.setVisibility(View.VISIBLE);
            } else {
                layout_sterge_comm.setVisibility(View.GONE);
            }

            imageButton_sterge_comentariu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog alertSectiuneEdit = new AlertDialog.Builder(mContext)
                            .setPositiveButton("Da", null)
                            .setNegativeButton("Nu", null)
                            .setTitle("Esti sigur ca vrei să ştergi comentariul?")
                            .create();

                    //liniar layout pentru AlertDialog
                    LinearLayout layout = new LinearLayout(mContext);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(10, 10, 10, 10);

                    alertSectiuneEdit.setView(layout);

                    alertSectiuneEdit.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            Button butonAnulare = alertSectiuneEdit.getButton(Dialog.BUTTON_NEGATIVE);
                            butonAnulare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertSectiuneEdit.dismiss();
                                }
                            });

                            Button butonSalvare = alertSectiuneEdit.getButton(Dialog.BUTTON_POSITIVE);
                            butonSalvare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference referenceSectiuni = FirebaseDatabase.getInstance().getReference("Sectiuni");
                                    Query query = referenceSectiuni.orderByChild("uid_organizator").equalTo(uid_organizator);
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                                                SectiuneForum sectiuneForum = keyNode.getValue(SectiuneForum.class);
                                                if (sectiuneForum.titlu_excursie.equals(titlu)
                                                        && sectiuneForum.data_final.equals(data_fin)
                                                        && sectiuneForum.data_inceput.equals(data_start)
                                                        && sectiuneForum.denumire_sectiune.equals(denumire_sectiune)) {
                                                    String keySectiune = keyNode.getKey();
                                                    for (Map.Entry<String, Comentariu> entry : sectiuneForum.comentarii.entrySet()) {
                                                        Comentariu comentariu1 = entry.getValue();
                                                        if(comentariu1.dataComentariu.equals(comentariu.dataComentariu)){
                                                            String keyComentariu = entry.getKey();
                                                            referenceSectiuni.child(keySectiune).child("comentarii").child(keyComentariu).removeValue();
                                                            Log.d("keys", "onDataChange: "+keySectiune+ " "+keyComentariu);
                                                        }
                                                    }
                                                }

//                                                DatabaseReference referenceComentariu = referenceSectiuni.child(keyNode.getKey());
//                                                Query queryComentariu = referenceComentariu.orderByChild("dataComentariu").equalTo(comentariu.dataComentariu);
//                                                queryComentariu.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                        Log.d("DupaQuery2", "onClick: "+queryComentariu);
//                                                        for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
//                                                            Comentariu comentariu1 = keyNode.getValue(Comentariu.class);
//                                                            SectiuneForum sectiuneForum = keyNode.getValue(SectiuneForum.class);
//
//                                                            Log.d("DupaQuery2 - comment", "onClick: "+comentariu1.continut+ " "+comentariu1.prenumeAutor);
//                                                            Log.d("DupaQuery1 - sectiune", "onClick: "+sectiuneForum.denumire_sectiune+ " "+sectiuneForum.titlu_excursie);
//
//                                                            String key = keyNode.getKey();
//                                                            referenceComentariu.child(key).removeValue();
//                                                        }
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                                    }
//                                                });


                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    alertSectiuneEdit.dismiss();
                                }
                            });
                        }
                    });

                    alertSectiuneEdit.show();
                }
            });
        }
    }

    class ComentariuAdaptor extends RecyclerView.Adapter<ComentariuItemView> {
        private List<Comentariu> comentarii;
        private List<String> keys;

        ComentariuAdaptor(List<Comentariu> comentarii, List<String> keys) {
            this.comentarii = comentarii;
            this.keys = keys;
        }

        @NonNull
        @Override
        public ComentariuItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ComentariuItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ComentariuItemView holder, int position) {
            holder.bind(comentarii.get(position), keys.get(position));
        }

        @Override
        public int getItemCount() {
            return comentarii.size();
        }
    }
}
