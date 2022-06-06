package com.tripshare.hitrip.Impresii.OferireImpresii;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.tripshare.hitrip.Impresii.Feedback;
import com.tripshare.hitrip.R;
import com.tripshare.hitrip.Trips.Trip;

import java.util.List;

public class AdaugaImpresiePtParticipantiActivity extends AppCompatActivity {

    TextView titlu, data;
    RecyclerView recyclerView_impresie_pt_participanti;
    ImageButton button_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga_impresie_pt_participanti);
        titlu = findViewById(R.id.titlu);
        button_close = findViewById(R.id.buton_close);
        data = findViewById(R.id.data);
        recyclerView_impresie_pt_participanti = findViewById(R.id.recyclerView_impresie_pt_participanti);

        String data_start, data_fin, titluS;
        titluS = getIntent().getStringExtra("titlu");
        data_start = getIntent().getStringExtra("data_inceput");
        data_fin = getIntent().getStringExtra("data_final");

        titlu.setText(titluS);
        data.setText(data_start + " - "+data_fin);
        button_close.bringToFront();



        new FirebaseDatabaseHelperImpresiiPtParticipanti(titluS, data_start, data_fin).showTrips(new FirebaseDatabaseHelperImpresiiPtParticipanti.DataStatus() {

            @Override
            public void DataIsLoaded(List<Feedback> feedbackList, List<String> keys, Trip trip) {
                new RecyclerViewConfigAdaugaImpresiePtParticipanti().setconfig(recyclerView_impresie_pt_participanti, AdaugaImpresiePtParticipantiActivity.this, feedbackList, keys, trip);
            }
        });



    }
}