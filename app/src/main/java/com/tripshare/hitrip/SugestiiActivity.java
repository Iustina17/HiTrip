package com.tripshare.hitrip;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SugestiiActivity extends AppCompatActivity {

    EditText continut;
    EditText titlu;
    Button trimite;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sugestii);

        titlu = findViewById(R.id.sugestii_sesizare_probleme_titlu_editText);
        continut = findViewById(R.id.sugestii_sesizare_probleme_continut_editText);
        trimite = (Button)findViewById(R.id.sugestii_sesizare_probleme_buton);

        trimite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adaugareSugestie();
                Toast.makeText(SugestiiActivity.this, "Mesajul dvs. a fost trimis. Mulțumim!", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void adaugareSugestie(){
        final String titluS = titlu.getText().toString().trim();
        final String continutS = continut.getText().toString().trim();
        @SuppressLint("SimpleDateFormat") final String dataS = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime());
        final String utilizatorCurentUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Sugestie sugestie = new Sugestie(titluS, continutS, utilizatorCurentUID, dataS);
        reference = FirebaseDatabase.getInstance().getReference().child("Sugestii si reclamatii");
        reference.push().setValue(sugestie).isSuccessful();
    }
}