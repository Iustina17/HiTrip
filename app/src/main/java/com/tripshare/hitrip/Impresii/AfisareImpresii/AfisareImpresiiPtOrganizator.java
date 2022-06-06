package com.tripshare.hitrip.Impresii.AfisareImpresii;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.tripshare.hitrip.Impresii.Impresie;
import com.tripshare.hitrip.R;

import java.util.List;

public class AfisareImpresiiPtOrganizator extends AppCompatActivity {
    RecyclerView recyclerView_impresii;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pag_impresii);

        recyclerView_impresii = findViewById(R.id.recyclerview_pag_impresii);


        new FirebaseDatabaseHelperAfisareImpresii("organizator").showImpresii(new FirebaseDatabaseHelperAfisareImpresii.DataStatus() {
            @Override
            public void DataIsLoaded(List<Impresie> impresiiList, List<String> keys) {
                new RecyclerViewConfigAfisareImpresii().setconfig(recyclerView_impresii, AfisareImpresiiPtOrganizator.this, impresiiList, keys);
            }
        });

    }
}