package com.tripshare.hitrip.Trips;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tripshare.hitrip.LoginActivity;
import com.tripshare.hitrip.MessagesActivity;
import com.tripshare.hitrip.MyProfileActivity;
import com.tripshare.hitrip.MyTripsActivity;
import com.tripshare.hitrip.R;
import com.tripshare.hitrip.RegulationsActivity;
import com.tripshare.hitrip.SuggestionActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Initialize variable
    DrawerLayout drawerLayout;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assign variable
        drawerLayout = findViewById((R.id.drawer_layout));
        mRecyclerView = findViewById(R.id.recyclerView_trips);
        new FirebaseDatabaseHelperTrips().showTrips(new FirebaseDatabaseHelperTrips.DataStatus() {

            @Override
            public void DataIsLoaded(List<Trip> trips, List<String> keys) {
                new RecyclerViewConfigTrip().setconfig(mRecyclerView, MainActivity.this, trips, keys);
            }
        });
    }

    public void ClickMenu(View view){
        //Open drawer
        openDrawer(drawerLayout);
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        //Open drawer layout
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickLogo(View view){
        //Close drawer
        closeDrawer(drawerLayout);
    }

    private static void closeDrawer(DrawerLayout drawerLayout) {
        //Close drawer layout
        //Check condition
        if(drawerLayout.isDrawerOpen((GravityCompat.START))){
            //When drawer is open
            //Close drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickProfile(View view){
        redirectActivity(this, MyProfileActivity.class);
    }

    public void ClickTrips(View view){
        //Recreate activity
        recreate();
    }

    public void ClickCreateTrip(View view){
        redirectActivity(this, CreateTrip1.class);
    }

    public void ClickMyTrips(View view){
        //Redirect activity to feed
        redirectActivity(this, MyTripsActivity.class);
    }

    public void ClickMessages(View view){
        //Redirect actvity to about us
        redirectActivity(this, MessagesActivity.class);
    }

    public void ClickRegulations(View view){
        redirectActivity(this, RegulationsActivity.class);
    }

    public void ClickSuggestions(View view){
        redirectActivity(this, SuggestionActivity.class);
    }

    public void ClickLogout(View view){
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
        Intent intent = new Intent(activity,aClass);
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