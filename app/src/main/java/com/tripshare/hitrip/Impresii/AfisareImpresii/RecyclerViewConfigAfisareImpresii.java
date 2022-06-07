//TODO
package com.tripshare.hitrip.Impresii.AfisareImpresii;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tripshare.hitrip.Impresii.Impresie;
import com.tripshare.hitrip.R;

import java.util.List;

class RecyclerViewConfigAfisareImpresii {
    private Context mContext;
    private ImpresieAdaptor adaptorImpresie;

    void setconfig(RecyclerView recyclerView, Context context, List<Impresie> impresiiList, List<String> keys) {
        mContext = context;
        adaptorImpresie = new ImpresieAdaptor(impresiiList, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adaptorImpresie);
//        Log.d("Impresie in ", "setconfig: " + impresiiList.get(0).prenume + " " + impresiiList.get(0).titlu_excursie);

    }

    class ImpresieItemView extends RecyclerView.ViewHolder {
        TextView prenume_pers_care_a_dat_feedback, nume_pers_care_a_dat_feedback;
        TextView titlu_excursie;
        TextView data_adaugare_impresie;
        ImageView poza;
        RatingBar impresie_rating;
        TextView text_impresie;
        String poza_string;


        ImpresieItemView(@NonNull final ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.item_impresie_afisare, parent, false));

            prenume_pers_care_a_dat_feedback = itemView.findViewById(R.id.prenume_pers_care_a_dat_feedback);
            nume_pers_care_a_dat_feedback = itemView.findViewById(R.id.nume_pers_care_a_dat_feedback);
            titlu_excursie = itemView.findViewById(R.id.titlu_excursie);
            data_adaugare_impresie = itemView.findViewById(R.id.data_adaugare_impresie);
            impresie_rating = itemView.findViewById(R.id.impresie_rating);
            text_impresie = itemView.findViewById(R.id.text_impresie);
            poza = itemView.findViewById(R.id.poza_profil_item);
        }

        @SuppressLint("SetTextI18n")
        void bind(Impresie impresie, String key) {
            prenume_pers_care_a_dat_feedback.setText(impresie.prenume);
            nume_pers_care_a_dat_feedback.setText(impresie.nume);
            titlu_excursie.setText(impresie.titlu_excursie);
            data_adaugare_impresie.setText(impresie.data);
            impresie_rating.setRating(impresie.nota);
            text_impresie.setText(impresie.continut);
            poza_string = impresie.imagineAutor;
            Log.d("Impresie in bind", "bind: " + impresie.prenume + " " + impresie.titlu_excursie);
        }
    }

    class ImpresieAdaptor extends RecyclerView.Adapter<ImpresieItemView> {
        private List<Impresie> impresiiLista;
        private List<String> keys;

        ImpresieAdaptor(List<Impresie> impresiiLista, List<String> keys) {
            this.impresiiLista = impresiiLista;
            this.keys = keys;
        }

        @NonNull
        @Override
        public ImpresieItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ImpresieItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ImpresieItemView holder, int position) {
            holder.bind(impresiiLista.get(position), keys.get(position));
            Log.d("Impresie in ", "onBindViewHolder: " + impresiiLista.get(position).prenume + " " + impresiiLista.get(position).titlu_excursie);

        }

        @Override
        public int getItemCount() {
            return impresiiLista.size();
        }
    }
}
