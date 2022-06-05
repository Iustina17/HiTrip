//TODO
package com.tripshare.hitrip.Impresii;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tripshare.hitrip.R;
import com.tripshare.hitrip.User;

import java.util.List;

class RecyclerViewConfigAfisareImpresii {
    private Context mContext;
    private ParticipantAdaptor adaptorParticipant;
    
    void setconfig(RecyclerView recyclerView, Context context, List<Impresie> participantList, List<String> keys) {
        mContext = context;
        adaptorParticipant = new ParticipantAdaptor(participantList, keys);
        recyclerView.setAdapter(adaptorParticipant);
    }

    class ParticipantItemView extends RecyclerView.ViewHolder {
        private TextView prenume_particip;
        ImageView poza;
        String poza_string;
        CardView card_view_participant;
//        private TextView mtitlu, mdata, mautor, madresare, mcontinut;
//        private ImageButton mediteaza, msterge;
//        String key;

        ParticipantItemView(@NonNull final ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.item_profile_inside_trip, parent, false));

            prenume_particip = itemView.findViewById(R.id.prenume_particip);
            poza = itemView.findViewById(R.id.poza_profil_item);
        }

        @SuppressLint("SetTextI18n")
        void bind(User user, Integer key) {
            prenume_particip.setText(user.prenume);
            // poza.setText(user.poza_profil); TODO
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
