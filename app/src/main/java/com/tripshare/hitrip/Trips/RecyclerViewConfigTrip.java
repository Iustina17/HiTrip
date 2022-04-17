package com.tripshare.hitrip.Trips;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tripshare.hitrip.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class RecyclerViewConfigTrip {
    private Context mContext;
    private TripAdaptor adaptorTrip;

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
//        private TextView mtitlu, mdata, mautor, madresare, mcontinut;
//        private ImageButton mediteaza, msterge;
//        String key;

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
            pret = itemView.findViewById(R.id.pret);
            moneda = itemView.findViewById(R.id.moneda);

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
            moneda.setText(trip.tip_moneda);
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
        public void onBindViewHolder(@NonNull TripItemView holder, int position) {
            holder.bind(tripsLista.get(position), keys.get(position));
        }

        @Override
        public int getItemCount() {
            return tripsLista.size();
        }
    }


}
