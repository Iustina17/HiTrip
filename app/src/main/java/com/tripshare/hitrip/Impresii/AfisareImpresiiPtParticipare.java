package com.tripshare.hitrip.Impresii;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.tripshare.hitrip.R;

import java.util.List;

public class AfisareImpresiiPtParticipare extends AppCompatActivity {

    TextView titlu, data;
    RecyclerView recyclerView_impresii;
    EditText text_impresie;
    RatingBar adauga_impresie_rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pag_impresii);
//        titlu = findViewById(R.id.titlu);
//        data = findViewById(R.id.data);
//        text_impresie = findViewById(R.id.text_impresie);
//        adauga_impresie_rating = findViewById(R.id.adauga_impresie_rating);

        recyclerView_impresii = findViewById(R.id.recyclerview_pag_impresii);

//        String data_start, data_fin, titlu;
//        titlu = getIntent().getStringExtra("titlu");
//        data_start = getIntent().getStringExtra("data_inceput");
//        data_fin = getIntent().getStringExtra("data_final");

        new FirebaseDatabaseHelperAfisareImpresii("participare").showImpresii(new FirebaseDatabaseHelperAfisareImpresii.DataStatus() {
            @Override
            public void DataIsLoaded(List<Impresie> impresiiList, List<String> keys) {
                new RecyclerViewConfigAfisareImpresii().setconfig(recyclerView_impresii, AfisareImpresiiPtParticipare.this , impresiiList, keys);
            }
        });

    }
}