//TODO
package com.tripshare.hitrip.Forum.Sectiuni;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Forum.Comentarii.InsideSectionForumActivity;
import com.tripshare.hitrip.R;

import java.util.List;

class RecyclerViewConfigForumSectiuni {
    private Context mContext;
    private SectiuneAdaptor adaptorSectiune;


    void setconfig(RecyclerView recyclerView, Context context, List<SectiuneForum> sectiuniList, List<String> keys) {
        mContext = context;
        adaptorSectiune = new SectiuneAdaptor(sectiuniList, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adaptorSectiune);
    }

    class SectiuneItemView extends RecyclerView.ViewHolder {
        TextView sectiune_denumire;
        CardView card_view_sectiune;
        ImageButton imageButton_editeaza_sectiune, imageButton_sterge_sectiune;
        LinearLayout organiz_edit_sect_layout;


        SectiuneItemView(@NonNull final ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.item_sectiune_formular_colaborativ, parent, false));

            sectiune_denumire = itemView.findViewById(R.id.sectiune_denumire);
            card_view_sectiune = itemView.findViewById(R.id.card_view_sectiune);
            imageButton_editeaza_sectiune = itemView.findViewById(R.id.imageButton_editeaza_sectiune);
            imageButton_sterge_sectiune = itemView.findViewById(R.id.imageButton_sterge_sectiune);
            organiz_edit_sect_layout = itemView.findViewById(R.id.organiz_edit_sect_layout);

        }

        @SuppressLint("SetTextI18n")
        void bind(SectiuneForum sectiune, String key) {
            sectiune_denumire.setText(sectiune.denumire_sectiune);
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (sectiune.uid_organizator.equals(uid)) {
                organiz_edit_sect_layout.setVisibility(View.VISIBLE);
            } else {
                organiz_edit_sect_layout.setVisibility(View.GONE);
            }

            imageButton_editeaza_sectiune.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog alertSectiuneEdit = new AlertDialog.Builder(mContext)
                            .setPositiveButton(R.string.alert_dialog_buton_salvare, null)
                            .setNegativeButton(R.string.alert_dialog_buton_anulare, null)
                            .setTitle("Editează denumire secţiune")
                            .create();

                    //liniar layout pentru AlertDialog
                    LinearLayout layout = new LinearLayout(mContext);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setPadding(10, 10, 10, 10);

                    final EditText denumire_sectiune_edit = new EditText(mContext);
                    denumire_sectiune_edit.setHint("Denumire nouă");
                    layout.addView(denumire_sectiune_edit);

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
                                    String sectiuneS = denumire_sectiune_edit.getText().toString().trim();
                                    DatabaseReference referenceSectiuni = FirebaseDatabase.getInstance().getReference("Sectiuni");
                                    Query query = referenceSectiuni.orderByChild("titlu_excursie").equalTo(sectiune.titlu_excursie);
                                    query.addValueEventListener(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                                                SectiuneForum sectiuneForum = keyNode.getValue(SectiuneForum.class);
                                                String uid_user = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                if (sectiuneForum.uid_organizator.equals(sectiune.uid_organizator)
                                                        && sectiuneForum.data_final.equals(sectiune.data_final)
                                                        && sectiuneForum.data_inceput.equals(sectiune.data_inceput)
                                                        && sectiuneForum.denumire_sectiune.equals(sectiune.denumire_sectiune)) {
                                                    String key = keyNode.getKey();
                                                    referenceSectiuni.child(key).child("denumire_sectiune").setValue(sectiuneS);
                                                }

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

            imageButton_sterge_sectiune.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog alertSectiuneEdit = new AlertDialog.Builder(mContext)
                            .setPositiveButton("Da", null)
                            .setNegativeButton("Nu", null)
                            .setTitle("Esti sigur ca vrei să ştergi secţiunea?")
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
                                    Query query = referenceSectiuni.orderByChild("titlu_excursie").equalTo(sectiune.titlu_excursie);
                                    query.addValueEventListener(new ValueEventListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.N)
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                                                SectiuneForum sectiuneForum = keyNode.getValue(SectiuneForum.class);
                                                if (sectiuneForum.uid_organizator.equals(sectiune.uid_organizator)
                                                        && sectiuneForum.data_final.equals(sectiune.data_final)
                                                        && sectiuneForum.data_inceput.equals(sectiune.data_inceput)
                                                        && sectiuneForum.denumire_sectiune.equals(sectiune.denumire_sectiune)) {
                                                    String key = keyNode.getKey();
                                                    referenceSectiuni.child(key).removeValue();
                                                }

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

    class SectiuneAdaptor extends RecyclerView.Adapter<SectiuneItemView> {
        private List<SectiuneForum> sectiuniLista;
        private List<String> keys;

        SectiuneAdaptor(List<SectiuneForum> sectiuniLista, List<String> keys) {
            this.sectiuniLista = sectiuniLista;
            this.keys = keys;
        }

        @NonNull
        @Override
        public SectiuneItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SectiuneItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SectiuneItemView holder, @SuppressLint("RecyclerView") int position) {
            holder.bind(sectiuniLista.get(position), keys.get(position));
            holder.card_view_sectiune.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, InsideSectionForumActivity.class);
                    intent.putExtra("uid_organizator", sectiuniLista.get(position).uid_organizator);
                    intent.putExtra("data_start", sectiuniLista.get(position).data_inceput);
                    intent.putExtra("data_final", sectiuniLista.get(position).data_final);
                    intent.putExtra("titlu_excursie", sectiuniLista.get(position).titlu_excursie);
                    intent.putExtra("denumire_sectiune", sectiuniLista.get(position).denumire_sectiune);
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return sectiuniLista.size();
        }
    }
}
