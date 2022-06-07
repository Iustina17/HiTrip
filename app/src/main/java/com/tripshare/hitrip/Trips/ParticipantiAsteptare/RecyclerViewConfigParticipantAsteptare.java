//TODO
package com.tripshare.hitrip.Trips.ParticipantiAsteptare;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Forum.Comentarii.Comentariu;
import com.tripshare.hitrip.Forum.Sectiuni.SectiuneForum;
import com.tripshare.hitrip.ProfileRelated.ProfileActivity;
import com.tripshare.hitrip.ProfileRelated.User;
import com.tripshare.hitrip.R;
import com.tripshare.hitrip.Trips.InsideTripActivity;
import com.tripshare.hitrip.Trips.Trip;

import java.util.List;
import java.util.Map;

class RecyclerViewConfigParticipantAsteptare {
    private Context mContext;
    private ParticipantAdaptor adaptorParticipant;

    LinearLayoutManager HorizontalLayout;
    private String uid_organizator, data_start, data_fin, titlu, denumire_sectiune;
    private String uid_user_curent;



    void setconfig(RecyclerView recyclerView, Context context, List<User> participantList, List<Integer> keys, String uid_organizator, String data_start, String data_fin) {
        mContext = context;
        adaptorParticipant = new ParticipantAdaptor(participantList, keys);
        HorizontalLayout = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(HorizontalLayout);
        recyclerView.setAdapter(adaptorParticipant);
        this.uid_organizator = uid_organizator;
        this.data_start = data_start;
        this.data_fin = data_fin;
    }

    class ParticipantItemView extends RecyclerView.ViewHolder {
        private TextView prenume_particip;
        ImageView poza;
        String poza_string;
        private Button accepta, refuza;
        CardView cardview_asteptare;


        ParticipantItemView(@NonNull final ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.item_asteptare_participant, parent, false));

            prenume_particip = itemView.findViewById(R.id.denumire_pers_asteptare);
            poza = itemView.findViewById(R.id.poza_profil_item);
            accepta = itemView.findViewById(R.id.buton_accepta_alaturare);
            refuza = itemView.findViewById(R.id.buton_refuză_alaturare);
            cardview_asteptare = itemView.findViewById(R.id.cardview_asteptare);

        }

        @SuppressLint("SetTextI18n")
        void bind(User user, Integer key) {
            prenume_particip.setText(user.prenume + " " + user.nume);
            // poza.setText(user.poza_profil); TODO

            uid_user_curent = user.UID;
            cardview_asteptare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("uid", uid_user_curent);
                    mContext.startActivity(intent);
                }
            });
            accepta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog alertAcepta = new AlertDialog.Builder(mContext)
                            .setPositiveButton("Da", null)
                            .setNegativeButton("Nu", null)
                            .setTitle("Esti sigur ca vrei să accepţi cererea?")
                            .create();

                    //liniar layout pentru AlertDialog
                    LinearLayout layout = new LinearLayout(mContext);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(10, 10, 10, 10);

                    alertAcepta.setView(layout);

                    alertAcepta.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            Button butonAnulare = alertAcepta.getButton(Dialog.BUTTON_NEGATIVE);
                            butonAnulare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertAcepta.dismiss();
                                }
                            });

                            Button butonSalvare = alertAcepta.getButton(Dialog.BUTTON_POSITIVE);
                            butonSalvare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d("Before if", "onDataChange: Organizator " + uid_organizator);

                                    DatabaseReference referenceCalatorii = FirebaseDatabase.getInstance().getReference("Calatorii");
                                    Query query = referenceCalatorii.orderByChild("UID_organiztor").equalTo(uid_organizator);
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                                                Log.d("if 2", "onDataChange: Organizator " + uid_organizator);

                                                Trip trip = keyNode.getValue(Trip.class);
                                                if (trip.data_final.equals(data_fin)
                                                        && trip.data_inceput.equals(data_start)) {
                                                    String keyCalatorie = keyNode.getKey();
                                                    for (Map.Entry<String, User> entry : trip.participantiAsteptare.entrySet()) {
                                                        User user1 = entry.getValue();
                                                        if (user1.UID.equals(user.UID)) {
                                                            Log.d("if 2", "onDataChange: " + user1.UID + " " + user.UID);
                                                            String keyUtilizator = entry.getKey();
                                                            referenceCalatorii.child(keyCalatorie).child("participantiAsteptare").child(keyUtilizator).removeValue();
                                                            referenceCalatorii.child(keyCalatorie).child("participanti").push().setValue(user);
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    alertAcepta.dismiss();
                                }
                            });
                        }
                    });

                    alertAcepta.show();
                }
            });

            refuza.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog alertAcepta = new AlertDialog.Builder(mContext)
                            .setPositiveButton("Da", null)
                            .setNegativeButton("Nu", null)
                            .setTitle("Esti sigur ca vrei să refuzi cererea?")
                            .create();

                    //liniar layout pentru AlertDialog
                    LinearLayout layout = new LinearLayout(mContext);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(10, 10, 10, 10);

                    alertAcepta.setView(layout);

                    alertAcepta.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            Button butonAnulare = alertAcepta.getButton(Dialog.BUTTON_NEGATIVE);
                            butonAnulare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertAcepta.dismiss();
                                }
                            });

                            Button butonSalvare = alertAcepta.getButton(Dialog.BUTTON_POSITIVE);
                            butonSalvare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference referenceCalatorii = FirebaseDatabase.getInstance().getReference("Calatorii");
                                    Query query = referenceCalatorii.orderByChild("UID_organizator").equalTo(uid_organizator);
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                                                Trip trip = keyNode.getValue(Trip.class);
                                                if (trip.data_final.equals(data_fin)
                                                        && trip.data_inceput.equals(data_start)) {
                                                    String keyCalatorie = keyNode.getKey();
                                                    for (Map.Entry<String, User> entry : trip.participantiAsteptare.entrySet()) {
                                                        User user1 = entry.getValue();
                                                        if (user1.UID.equals(user.UID)) {
                                                            String keyUtilizator = entry.getKey();
                                                            referenceCalatorii.child(keyCalatorie).child("participantiAsteptare").child(keyUtilizator).removeValue();
                                                        }
                                                    }
                                                }



                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    alertAcepta.dismiss();
                                }
                            });
                        }
                    });

                    alertAcepta.show();
                }
            });

        }
    }

    class ParticipantAdaptor extends RecyclerView.Adapter<ParticipantItemView> {
        private List<User> participantLista;
        private List<Integer> keys;

        ParticipantAdaptor(List<User> participantLista, List<Integer> keys) {
            this.participantLista = participantLista;
            this.keys = keys;
        }

        @NonNull
        @Override
        public ParticipantItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ParticipantItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ParticipantItemView holder, int position) {
            holder.bind(participantLista.get(position), keys.get(position));
        }

        @Override
        public int getItemCount() {
            return participantLista.size();
        }
    }
}
