package com.tripshare.hitrip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AdaugaImpresiePtParticipantiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga_impresie_pt_participanti);

        String data_start, data_fin, titlu;
        titlu = getIntent().getStringExtra("titlu");
        data_start = getIntent().getStringExtra("data_inceput");
        data_fin = getIntent().getStringExtra("data_final");



    }
}