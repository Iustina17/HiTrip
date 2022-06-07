package com.tripshare.hitrip.MyTrips;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.ProfileRelated.User;
import com.tripshare.hitrip.R;
import com.tripshare.hitrip.Trips.InsideTripActivity;
import com.tripshare.hitrip.Trips.Trip;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class RecyclerViewConfigMyTrip {
    private Context mContext;
    private TripAdaptor adaptorTrip;

    private DatabaseReference referenceTrip;

    void setconfig(RecyclerView recyclerView, Context context, List<Trip> tripsList, List<String> keys) {
        mContext = context;
        Collections.sort(tripsList, new Comparator<Trip>() {
            @Override
            public int compare(Trip a1, Trip a2) {
                return a2.data_inceput.compareTo(a1.data_inceput);
            }
        });

        adaptorTrip = new TripAdaptor(tripsList, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adaptorTrip);
    }

    class TripItemView extends RecyclerView.ViewHolder {
        private TextView prenume_organiztor, nume_organiztor, tara, oras, nr_zile, nume_excursie, data_inceput, data_final, tip_excursie, pret, moneda;
        private TextView pret_min, pret_max, moneda_var;
        LinearLayout linl_pretFix, linl_pretVariabil;
        CardView card_view_trip;

        ImageButton imageButton_stergere_trip, imageButton_anulare_trip, imageButton_finalizare_trip;
//        private TextView mtitlu, mdata, mautor, madresare, mcontinut;
//        private ImageButton mediteaza, msterge;
        //       String key;

        TripItemView(@NonNull final ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.item_trip, parent, false));

            prenume_organiztor = itemView.findViewById(R.id.prenume_organizator);
            nume_organiztor = itemView.findViewById(R.id.nume_organizator);
            tara = itemView.findViewById(R.id.tara);
            oras = itemView.findViewById(R.id.oras);
            nr_zile = itemView.findViewById(R.id.nr_zile);
            nume_excursie = itemView.findViewById(R.id.nume_excursie);
            data_inceput = itemView.findViewById(R.id.data_inceput);
            data_final = itemView.findViewById(R.id.data_final);
            tip_excursie = itemView.findViewById(R.id.tip_excursie);
            moneda = itemView.findViewById(R.id.moneda);
            card_view_trip = itemView.findViewById(R.id.card_view_trip);
            imageButton_stergere_trip = itemView.findViewById(R.id.imageButton_stergere_trip);
            imageButton_anulare_trip = itemView.findViewById(R.id.imageButton_anulare_trip);
            imageButton_finalizare_trip = itemView.findViewById(R.id.imageButton_finalizare_trip);


            pret = itemView.findViewById(R.id.pret);
            pret_min = itemView.findViewById(R.id.pret_min);
            pret_max = itemView.findViewById(R.id.pret_max);
            moneda_var = itemView.findViewById(R.id.moneda_var);
            linl_pretFix = itemView.findViewById(R.id.linl_pretFix);
            linl_pretVariabil = itemView.findViewById(R.id.linl_pretVariabil);

        }

        @SuppressLint("SetTextI18n")
        void bind(Trip trip, String key) {
            prenume_organiztor.setText(trip.prenume);
            nume_organiztor.setText(trip.nume);
            tara.setText(trip.tara);
            oras.setText(trip.oras);
            nr_zile.setText(trip.nr_zile.toString());
            nume_excursie.setText(trip.titlu_excursie);
            data_inceput.setText(trip.data_inceput);
            data_final.setText(trip.data_final);
            tip_excursie.setText(trip.tip);

            pret.setText(trip.pret);
            pret_min.setText(trip.pret_min);
            pret_max.setText(trip.pret_max);
            moneda.setText(trip.tip_moneda);
            moneda_var.setText(trip.tip_moneda);

            imageButton_stergere_trip.setVisibility(View.GONE);
            imageButton_anulare_trip.setVisibility(View.GONE);
            imageButton_finalizare_trip.setVisibility(View.GONE);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user.getUid().equals(trip.UID_organiztor) && trip.participanti == null && !trip.status.equals("incheiata")) {
                imageButton_stergere_trip.setVisibility(View.VISIBLE);
                imageButton_anulare_trip.setVisibility(View.GONE);
                imageButton_finalizare_trip.setVisibility(View.GONE);
            } else if (user.getUid().equals(trip.UID_organiztor) && trip.participanti != null && trip.status.equals("viitoare")) {
                imageButton_stergere_trip.setVisibility(View.GONE);
                imageButton_anulare_trip.setVisibility(View.VISIBLE);
                imageButton_finalizare_trip.setVisibility(View.GONE);
            } else if (user.getUid().equals(trip.UID_organiztor) && trip.participanti != null && trip.status.equals("desfasurare")) {
                imageButton_stergere_trip.setVisibility(View.GONE);
                imageButton_anulare_trip.setVisibility(View.GONE);
                imageButton_finalizare_trip.setVisibility(View.VISIBLE);
                Log.d("status", "bind: " +trip.status);
            }

            functionareButoane(trip, imageButton_stergere_trip, imageButton_anulare_trip, imageButton_finalizare_trip);

            if ((trip.pret_min.isEmpty() && trip.pret_max.isEmpty() && (!trip.pret.isEmpty()))) {
                linl_pretVariabil.setVisibility(View.GONE);
                linl_pretFix.setVisibility(View.VISIBLE);
            }

            if ((!trip.pret_min.isEmpty()) && (!trip.pret_max.isEmpty()) && trip.pret.isEmpty()) {
                linl_pretVariabil.setVisibility(View.VISIBLE);
                linl_pretFix.setVisibility(View.GONE);
            }
            //this.key = key;
        }
    }

    class TripAdaptor extends RecyclerView.Adapter<TripItemView> {
        private List<Trip> tripsLista;
        private List<String> keys;

        TripAdaptor(List<Trip> tripsLista, List<String> keys) {
            this.tripsLista = tripsLista;
            this.keys = keys;
        }

        @NonNull
        @Override
        public TripItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TripItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TripItemView holder, @SuppressLint("RecyclerView") int position) {
            holder.bind(tripsLista.get(position), keys.get(position));

            holder.card_view_trip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, InsideTripActivity.class);
                    intent.putExtra("uid_organizator", tripsLista.get(position).UID_organiztor);
                    intent.putExtra("data_start", tripsLista.get(position).data_inceput);
                    intent.putExtra("data_final", tripsLista.get(position).data_final);
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return tripsLista.size();
        }
    }

    private void functionareButoane(Trip trip, ImageButton msterge, ImageButton manuleaza, ImageButton mfinalizeaza) {
        final Trip tripFinal = trip;

        msterge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertStergere = new AlertDialog.Builder(view.getContext())
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                referenceTrip = FirebaseDatabase.getInstance().getReference().child("Calatorii");
                                Query query = referenceTrip.orderByChild("UID_organiztor").equalTo(tripFinal.UID_organiztor);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            String key = child.getKey();
                                            if (tripFinal.data_inceput.equals(child.child("data_inceput").getValue())
                                                    && tripFinal.data_final.equals(child.child("data_final").getValue())) {
                                                referenceTrip = referenceTrip.child(key);
                                                if (tripFinal.participanti == null) {
                                                    referenceTrip.removeValue();
                                                    DatabaseReference referenceUtilizatori = FirebaseDatabase.getInstance().getReference().child("Utilizatori");
                                                    Query gqery1 = referenceUtilizatori.orderByChild("UID").equalTo(tripFinal.UID_organiztor);

                                                    gqery1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            Log.d("Inainte", "");

                                                            for (DataSnapshot child_user : snapshot.getChildren()) {

                                                                String keyUser = child_user.getKey();
                                                                User user = child_user.getValue(User.class);

                                                                Log.d("Inainte", "" + user.nr_excursii_organizate + " " + keyUser);
                                                                FirebaseDatabase.getInstance().getReference("Utilizatori").child(keyUser).child("nr_excursii_organizate").setValue(user.nr_excursii_organizate - 1);
                                                                Log.d("Dupa", "" + user.nr_excursii_organizate);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Nu", null)
                        .setTitle("Sunteţi sigur că doriţi să stergeţi excursia?")
                        .create();
                alertStergere.show();
            }
        });

        manuleaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertAnulare = new AlertDialog.Builder(view.getContext())
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                referenceTrip = FirebaseDatabase.getInstance().getReference().child("Calatorii");
                                Query query = referenceTrip.orderByChild("UID_organiztor").equalTo(tripFinal.UID_organiztor);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            String key = child.getKey();
                                            if (tripFinal.data_inceput.equals(child.child("data_inceput").getValue())
                                                    && tripFinal.data_final.equals(child.child("data_final").getValue())) {
                                                if (tripFinal.participanti != null) {
                                                    referenceTrip.child(key).child("status").setValue("anulata");
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Nu", null)
                        .setTitle("Sunteţi sigur că doriţi să anulaţi excursia?")
                        .create();
                alertAnulare.show();
            }
        });

        mfinalizeaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertAnulare = new AlertDialog.Builder(view.getContext())
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                referenceTrip = FirebaseDatabase.getInstance().getReference().child("Calatorii");
                                Query query = referenceTrip.orderByChild("UID_organiztor").equalTo(tripFinal.UID_organiztor);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            String key = child.getKey();
                                            if (tripFinal.data_inceput.equals(child.child("data_inceput").getValue())
                                                    && tripFinal.data_final.equals(child.child("data_final").getValue())) {
                                                if (tripFinal.participanti != null) {
                                                    referenceTrip.child(key).child("status").setValue("finalizata");
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Nu", null)
                        .setTitle("Sunteţi sigur că doriţi să finalizaţi excursia?")
                        .create();
                alertAnulare.show();
            }
        });

    }

}
