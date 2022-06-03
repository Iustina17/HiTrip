package com.tripshare.hitrip.MyTrips;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.tripshare.hitrip.HelpActivity;
import com.tripshare.hitrip.LoginActivity;
import com.tripshare.hitrip.MessagesActivity;
import com.tripshare.hitrip.MyProfileActivity;
import com.tripshare.hitrip.R;
import com.tripshare.hitrip.SugestiiActivity;
import com.tripshare.hitrip.Trips.CreateTrip1;
import com.tripshare.hitrip.Trips.MainActivity;
import com.tripshare.hitrip.Trips.Trip;

import java.util.Calendar;
import java.util.List;

public class MyTripsActivity extends AppCompatActivity {
    Button organizare, participare;
    Button trecute, prezente, viitoare;
    RecyclerView mRecyclerView;
    TextView text_nuExista_excursii;

    DrawerLayout drawerLayout;

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

        text_nuExista_excursii = findViewById(R.id.text_nuExista_excursii);

        drawerLayout = findViewById((R.id.drawer_layout));

        String buton1 = "", buton2 = "";
        Log.d("Buton1 - intitial", "" + buton1);
        Log.d("Buton2 - initial", "" + buton2);
        if (getIntent().hasExtra("buton1"))
            buton1 = getIntent().getStringExtra("buton1");
        if (getIntent().hasExtra("buton2"))
            buton2 = getIntent().getStringExtra("buton2");

        new com.tripshare.hitrip.MyTrips.FirebaseDatabaseHelperMyTrips(buton1, buton2, text_nuExista_excursii).showTrips(new FirebaseDatabaseHelperMyTrips.DataStatus() {

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

        if (buton2.equals("trecute")) {
            prezente.setBackgroundColor(getResources().getColor(R.color.green6));
            viitoare.setBackgroundColor(getResources().getColor(R.color.green6));
            trecute.setBackgroundColor(getResources().getColor(R.color.highlight2));
            text_nuExista_excursii.setText("Nu există excursii încheiate");
        } else if (buton2.equals("prezente")) {
            trecute.setBackgroundColor(getResources().getColor(R.color.green6));
            viitoare.setBackgroundColor(getResources().getColor(R.color.green6));
            prezente.setBackgroundColor(getResources().getColor(R.color.highlight2));
            text_nuExista_excursii.setText("Nu există excursii în desfăşurare");
        } else if (buton2.equals("viitoare")) {
            trecute.setBackgroundColor(getResources().getColor(R.color.green6));
            prezente.setBackgroundColor(getResources().getColor(R.color.green6));
            viitoare.setBackgroundColor(getResources().getColor(R.color.highlight2));
            text_nuExista_excursii.setText("Nu există excursii viitoare");
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

    public void ClickMenu(View view) {
        //Open drawer
        openDrawer(drawerLayout);
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view) {
        //Close drawer
        redirectActivity(this, MainActivity.class);
    }

    private static void closeDrawer(DrawerLayout drawerLayout) {
        //Close drawer layout
        //Check condition
        if (drawerLayout.isDrawerOpen((GravityCompat.START))) {
            //When drawer is open
            //Close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickProfile(View view) {
        redirectActivity(this, MyProfileActivity.class);
    }

    public void ClickTrips(View view) {
        //Recreate activity
        redirectActivity(this, MainActivity.class);
    }

    public void ClickCreateTrip(View view) {
        redirectActivity(this, CreateTrip1.class);
    }

    public void ClickMyTrips(View view) {
        //Redirect activity to feed
//        Intent intent = new Intent(MainActivity.this, MyTripsActivity.class);
//        intent.putExtra("buton1", "organizare");
//        intent.putExtra("buton2", "prezente");
//        startActivity(intent);
        recreate();
    }


    public void ClickHelp(View view) {
        redirectActivity(this, HelpActivity.class);
    }

    public void ClickSuggestions(View view) {
        redirectActivity(this, SugestiiActivity.class);
    }

    public void ClickLogout(View view) {
        //Close app
        logout(this);
    }

    private void logout(Activity activity) {
        //Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //Set Title
        builder.setTitle("Deconectare");
        //Set message
        builder.setMessage("Sigur vreţi să vă deconectaţi?");
        //Positive yes button
        builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Finish activity
                Intent login_intent = new Intent(activity, LoginActivity.class);
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(login_intent);
            }
        });

        //Negative no button
        builder.setNegativeButton("NU", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Dismis dialog
                dialogInterface.dismiss();
            }
        });

        //Show dialog
        builder.show();

    }

    private static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity, aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity((intent));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        closeDrawer(drawerLayout);
    }
}