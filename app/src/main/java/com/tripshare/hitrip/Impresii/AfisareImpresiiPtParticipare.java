package com.tripshare.hitrip.Impresii;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.tripshare.hitrip.R;

public class AfisareImpresiiPtParticipare extends AppCompatActivity {

    TextView titlu, data;
    EditText text_impresie;
    RatingBar adauga_impresie_rating;

    RecyclerView recyclerView_impresii;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pag_impresii);
        titlu = findViewById(R.id.titlu);
        data = findViewById(R.id.data);
        text_impresie = findViewById(R.id.text_impresie);
        adauga_impresie_rating = findViewById(R.id.adauga_impresie_rating);

        recyclerView_impresii = findViewById(R.id.recyclerView_impresie_pt_participanti);

        String data_start, data_fin, titlu;
        titlu = getIntent().getStringExtra("titlu");
        data_start = getIntent().getStringExtra("data_inceput");
        data_fin = getIntent().getStringExtra("data_final");

//        new FirebaseDatabaseHelperImpresiiPtParticipanti(titlu, data_start, data_fin).showTrips(new FirebaseDatabaseHelperImpresiiPtParticipanti.DataStatus() {
//
//            @Override
//            public void DataIsLoaded(List<Feedback> feedbackList, List<String> keys) {
//                new RecyclerViewConfigImpresiiPtParticipanti().setconfig(recyclerView_impresii, , feedbackList, keys);
//            }
//        });


    }
}