//TODO
package com.tripshare.hitrip.Forum.Sectiuni;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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



        SectiuneItemView(@NonNull final ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.item_sectiune_formular_colaborativ, parent, false));

            sectiune_denumire = itemView.findViewById(R.id.sectiune_denumire);
            card_view_sectiune = itemView.findViewById(R.id.card_view_sectiune);

        }

        @SuppressLint("SetTextI18n")
        void bind(SectiuneForum sectiune, String key) {
            sectiune_denumire.setText(sectiune.denumire_sectiune);

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
