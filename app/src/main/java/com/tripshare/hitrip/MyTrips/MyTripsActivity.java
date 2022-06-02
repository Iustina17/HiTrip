package com.tripshare.hitrip.MyTrips;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.tripshare.hitrip.R;
import com.tripshare.hitrip.Trips.Trip;

import java.util.Calendar;
import java.util.List;

public class MyTripsActivity extends AppCompatActivity {
    Button organizare, participare;
    Button trecute, prezente, viitoare;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);

        mRecyclerView = findViewById(R.id.my_trips1);
        organizare = findViewById(R.id.organizare);
        participare = findViewById(R.id.participare);
        trecute = findViewById(R.id.trecute);
        prezente = findViewById(R.id.prezente);
        viitoare = findViewById(R.id.viitoare);
        String buton1 = "", buton2 = "";
        Log.d("Buton1 - intitial", "" + buton1);
        Log.d("Buton2 - initial", "" + buton2);
        if (getIntent().hasExtra("buton1"))
            buton1 = getIntent().getStringExtra("buton1");
        if (getIntent().hasExtra("buton2"))
            buton2 = getIntent().getStringExtra("buton2");

        new com.tripshare.hitrip.MyTrips.FirebaseDatabaseHelperMyTrips(buton1, buton2).showTrips(new FirebaseDatabaseHelperMyTrips.DataStatus() {

            @Override
            public void DataIsLoaded(List<Trip> trips, List<String> keys) {
                new com.tripshare.hitrip.MyTrips.RecyclerViewConfigMyTrip().setconfig(mRecyclerView, MyTripsActivity.this, trips, keys);
            }
        });
        organizare.setBackgroundColor(getResources().getColor(R.color.dell));
        participare.setBackgroundColor(getResources().getColor(R.color.dell));

        if (buton1.equals("organizare")) {
            organizare.setBackgroundColor(getResources().getColor(R.color.highlight));
            participare.setBackgroundColor(getResources().getColor(R.color.dell));
        } else if (buton1.equals("participare")) {
            participare.setBackgroundColor(getResources().getColor(R.color.highlight));
            organizare.setBackgroundColor(getResources().getColor(R.color.dell));
        }
        Log.d("Buton1", "" + buton1);
        Log.d("Buton2", "" + buton2);

        organizare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apasare_butoane("organizare", "prezente");
            }
        });

        participare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apasare_butoane("participare", "prezente");
                Log.d("Click", "participare" + Calendar.getInstance().getTime());
            }
        });

        String finalButon = buton1;
        trecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apasare_butoane(finalButon, "trecute");
            }
        });
        prezente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apasare_butoane(finalButon, "prezente");
            }
        });
        viitoare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apasare_butoane(finalButon, "viitoare");
            }
        });
    }

    public void apasare_butoane(String buton1, String buton2) {
        if (buton1.equals("organizare")) {
            if (buton2.equals("trecute")) {
                Intent intent = new Intent(MyTripsActivity.this, MyTripsActivity.class);
                intent.putExtra("buton1", "organizare");
                intent.putExtra("buton2", "trecute");
                startActivity(intent);
                overridePendingTransition(0, 0);

            } else if (buton2.equals("prezente")) {
                Intent intent = new Intent(MyTripsActivity.this, MyTripsActivity.class);
                intent.putExtra("buton1", "organizare");
                intent.putExtra("buton2", "prezente");
                startActivity(intent);
                overridePendingTransition(0, 0);

            } else {
                Intent intent = new Intent(MyTripsActivity.this, MyTripsActivity.class);
                intent.putExtra("buton1", "organizare");
                intent.putExtra("buton2", "viitoare");
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        }
        if (buton1.equals("participare")) {
            Log.d("PARTICIPARE", "apasare_butoane: participare");
            if (buton2.equals("trecute")) {
                Intent intent = new Intent(MyTripsActivity.this, MyTripsActivity.class);
                intent.putExtra("buton1", "participare");
                intent.putExtra("buton2", "trecute");
                startActivity(intent);
                overridePendingTransition(0, 0);


            } else if (buton2.equals("prezente")) {
                Log.d("PARTICIPARE", "apasare_butoane: prezente");
                Intent intent = new Intent(MyTripsActivity.this, MyTripsActivity.class);
                intent.putExtra("buton1", "participare");
                intent.putExtra("buton2", "prezente");
                startActivity(intent);
                overridePendingTransition(0, 0);

            } else {
                Intent intent = new Intent(MyTripsActivity.this, MyTripsActivity.class);
                intent.putExtra("buton1", "participare");
                intent.putExtra("buton2", "viitoare");
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        }
    }
}