package com.tripshare.hitrip.Trips;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.HelpActivity;
import com.tripshare.hitrip.LoginSignUp.LoginActivity;
import com.tripshare.hitrip.ProfileRelated.MyProfileActivity;
import com.tripshare.hitrip.MyTrips.MyTripsActivity;
import com.tripshare.hitrip.R;
import com.tripshare.hitrip.Sugestii.SugestiiActivity;
import com.tripshare.hitrip.ProfileRelated.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Initialize variable
    DrawerLayout drawerLayout;
    private RecyclerView mRecyclerView;
    //ImageView search_trips;
    String search = "all";
    TextView nume, prenume;
    ImageView poza;
    String uid_user_nav;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference referenceUsers = database.getReference("Utilizatori");

    ImageView cauta_lupa, close_search_button;
    Button cauta_buton;
    EditText editText_search;
    LinearLayout search_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign variable
        drawerLayout = findViewById((R.id.drawer_layout));
        mRecyclerView = findViewById(R.id.recyclerView_trips);

        nume = findViewById(R.id.nume_nav);
        prenume = findViewById(R.id.prenume_nav);
        poza = findViewById(R.id.menu_profile);

        editText_search = findViewById(R.id.editText_search);

        cauta_lupa = findViewById(R.id.button_cauta_lupa);
        cauta_lupa.setVisibility(View.VISIBLE);
        cauta_buton = findViewById(R.id.search_button);
        search_bar = findViewById(R.id.layout_searchbar);
        close_search_button = findViewById(R.id.close_search_button);

        if (getIntent().hasExtra("search"))
            search = getIntent().getStringExtra("search");

        if (!search.equals("all")) {
            search_bar.setVisibility(View.VISIBLE);
            editText_search.setText(search);
        }

        cauta_lupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_bar.setVisibility(View.VISIBLE);
            }
        });

        close_search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_bar.setVisibility(View.GONE);
                search = "all";
                if (!isTaskRoot())
                    finish();
                overridePendingTransition(0, 0);
                editText_search.setText("");
            }
        });

        cauta_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search = editText_search.getText().toString();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("search", search);
                startActivity(intent);
                overridePendingTransition(0, 0);
                search_bar.setVisibility(View.GONE);
                search = "all";
            }
        });

        referenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    User user = keyNode.getValue(User.class);

                    uid_user_nav = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    if (user.UID.equals(uid_user_nav)) {
                        //imagine_excursie.setAdjustViewBounds(trip.imagine_excursie);
                        nume.setText(user.nume);
                        prenume.setText(user.prenume);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        new FirebaseDatabaseHelperTrips(search).showTrips(new FirebaseDatabaseHelperTrips.DataStatus() {

            @Override
            public void DataIsLoaded(List<Trip> trips, List<String> keys) {
                new RecyclerViewConfigTrip().setconfig(mRecyclerView, MainActivity.this, trips, keys);
            }
        });

    }

//    public void SearchTrips(View view){
//        search_trips.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                search(s);
//                return true;
//            }
//        });
//    }
//
//    private void search(String str){
//        List<Trip> trips = new ArrayList<>();
//
//
//    }

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
        closeDrawer(drawerLayout);
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
        recreate();
    }

    public void ClickCreateTrip(View view) {
        redirectActivity(this, CreateTrip1.class);
    }

    public void ClickMyTrips(View view) {
        Intent intent = new Intent(MainActivity.this, MyTripsActivity.class);
        intent.putExtra("buton1", "organizare");
        intent.putExtra("buton2", "prezente");
        startActivity(intent);
        closeDrawer(drawerLayout);
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