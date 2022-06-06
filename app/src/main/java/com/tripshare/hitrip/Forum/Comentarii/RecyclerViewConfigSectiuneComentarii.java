//TODO
package com.tripshare.hitrip.Forum.Comentarii;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tripshare.hitrip.R;

import java.util.List;

class RecyclerViewConfigSectiuneComentarii {
    private Context mContext;
    private ComentariuAdaptor comentariuAdaptor;

    void setconfig(RecyclerView recyclerView, Context context, List<Comentariu> comentarii, List<String> keys) {
        mContext = context;
        comentariuAdaptor = new ComentariuAdaptor(comentarii, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(comentariuAdaptor);
    }

    class ComentariuItemView extends RecyclerView.ViewHolder {
        TextView prenume_pers_comentariu, nume_pers_comenatriu;
        TextView text_comentariu;
        ImageView poza_pers_comentariu;
        String poza_string;


        ComentariuItemView(@NonNull final ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.item_comentariu, parent, false));

            prenume_pers_comentariu = itemView.findViewById(R.id.prenume_pers_comentariu);
            nume_pers_comenatriu = itemView.findViewById(R.id.nume_pers_comenatriu);
            text_comentariu = itemView.findViewById(R.id.text_comentariu);
            poza_pers_comentariu = itemView.findViewById(R.id.poza_pers_comentariu);
        }

        @SuppressLint("SetTextI18n")
        void bind(Comentariu comentariu, String key) {
            prenume_pers_comentariu.setText(comentariu.prenumeAutor);
            nume_pers_comenatriu.setText(comentariu.numeAutor);
            text_comentariu.setText(comentariu.continut);
            poza_string = comentariu.poza;
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
