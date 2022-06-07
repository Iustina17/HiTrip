package com.tripshare.hitrip.Impresii.OferireImpresii;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Impresii.Feedback;
import com.tripshare.hitrip.Impresii.Impresie;
import com.tripshare.hitrip.MyTrips.MyTripsActivity;
import com.tripshare.hitrip.R;
import com.tripshare.hitrip.Trips.Trip;
import com.tripshare.hitrip.ProfileRelated.User;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RecyclerViewConfigAdaugaImpresiePtParticipanti extends AppCompatActivity {
    private Context mContext;
    private FeedbackAdaptor adaptorFeedback;
    private Trip trip;

    void setconfig(RecyclerView recyclerView, Context context, List<Feedback> feedbackList, List<String> keys, Trip trip) {
        mContext = context;

        adaptorFeedback = new FeedbackAdaptor(feedbackList, keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adaptorFeedback);
        this.trip = trip;
    }

    class FeedbackItemView extends RecyclerView.ViewHolder {
        ImageView poza_profil;
        TextView nume, prenume;
        RatingBar ratingBar;
        EditText editText;
        Button button;

        FeedbackItemView(@NonNull final ViewGroup parent) {
            super(LayoutInflater.from(mContext).
                    inflate(R.layout.item_adauga_impresie_pt_participanti, parent, false));

            poza_profil = itemView.findViewById(R.id.poza_profil_participant);
            prenume = itemView.findViewById(R.id.prenume_particpip_rating);
            nume = itemView.findViewById(R.id.nume_particpip_rating);
            button = itemView.findViewById(R.id.adauga_impresie_buton_trimite);
            ratingBar = itemView.findViewById(R.id.adauga_impresie_rating);
            editText = itemView.findViewById(R.id.text_impresie);
        }

        @SuppressLint("SetTextI18n")
        void bind(Feedback feedback, String key) {
            prenume.setText(feedback.prenume);
            nume.setText(feedback.nume);
            //TODO poza

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference referenceUsers = FirebaseDatabase.getInstance().getReference().child("Utilizatori");
                    Query query = referenceUsers.orderByChild("UID").equalTo(feedback.uid_participant);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                User participant = child.getValue(User.class);
                                String key = child.getKey();

                                Impresie impresie = new Impresie(trip.nume, trip.prenume, trip.UID_organiztor, trip.titlu_excursie,
                                        Calendar.getInstance().getTime().toString(), ratingBar.getRating(), trip.poza, editText.getText().toString());
                                referenceUsers.child(key).child("impresie_participare_user").push().setValue(impresie);

                                referenceUsers.child(key).child("nr_impresii_participant").setValue(participant.nr_impresii_participant + 1);
                                referenceUsers.child(key).child("rating_participant").setValue(((participant.rating_participant * participant.nr_impresii_participant) + ratingBar.getRating()) / (participant.nr_impresii_participant + 1));

                                DatabaseReference referenceTrip = FirebaseDatabase.getInstance().getReference().child("Calatorii");

                                Query query = referenceTrip.orderByChild("UID_organiztor").equalTo(trip.UID_organiztor);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            String key = child.getKey();
                                            Trip trip_nou = child.getValue(Trip.class);

                                            if (trip_nou.titlu_excursie.equals(trip.titlu_excursie) && trip.data_inceput.equals(trip_nou.data_inceput) && trip.data_final.equals(trip_nou.data_final)) {
                                                DatabaseReference referenceFeedback = referenceTrip.child(key).child("impresii_date_de_organizator");
                                                Query query2 = referenceFeedback.orderByChild("uid_participant").equalTo(feedback.uid_participant);
                                                query2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                            Trip trip = child.getValue(Trip.class);
                                                            HashMap<String, Feedback> hashMapFeedbacks;
                                                            if (trip.impresii_date_de_organizator != null) {
                                                                hashMapFeedbacks = trip.impresii_date_de_organizator;
                                                                //referenceTrip.child(key).child("impresii_date_de_organizator").removeValue();
                                                                if (hashMapFeedbacks != null) {
                                                                    for (Map.Entry<String, Feedback> entry : hashMapFeedbacks.entrySet()) {
                                                                        if (entry == null) {
                                                                            Intent intent = new Intent(mContext, MyTripsActivity.class);
                                                                            startActivity(intent);
                                                                        }
                                                                        Feedback feedback1 = entry.getValue();
                                                                        String keyFeedback = entry.getKey();
                                                                        Log.d("ENTRYs", "nume "+ feedback1.nume+ " "+ keyFeedback);
                                                                        if (feedback1.uid_participant.equals(feedback.uid_participant)) {
                                                                            referenceTrip.child(key).child("impresii_date_de_organizator").child(keyFeedback).removeValue();
                                                                            // hashMapFeedbacks.entrySet().remove(entry);
                                                                            Feedback feedback2 = new Feedback(feedback.uid_participant, "da", feedback.nume, feedback.prenume, feedback.poza);
                                                                            referenceTrip.child(key).child("impresii_date_de_organizator").push().setValue(feedback2);

                                                                        }
                                                                    }
                                                                } else {
                                                                    Intent intent = new Intent(mContext, MyTripsActivity.class);
                                                                    startActivity(intent);
                                                                }


                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });

                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }
            });

        }
    }

    class FeedbackAdaptor extends RecyclerView.Adapter<FeedbackItemView> {
        private List<Feedback> feedbackLista;
        private List<String> keys;

        FeedbackAdaptor(List<Feedback> feedbackLista, List<String> keys) {
            this.feedbackLista = feedbackLista;
            this.keys = keys;
        }

        @NonNull
        @Override
        public FeedbackItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FeedbackItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull FeedbackItemView holder, @SuppressLint("RecyclerView") int position) {
            holder.bind(feedbackLista.get(position), keys.get(position));
        }

        @Override
        public int getItemCount() {
            return feedbackLista.size();
        }
    }
}
