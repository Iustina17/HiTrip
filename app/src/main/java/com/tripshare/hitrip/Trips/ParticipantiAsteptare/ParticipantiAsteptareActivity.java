package com.tripshare.hitrip.Trips.ParticipantiAsteptare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.tripshare.hitrip.ProfileRelated.User;
import com.tripshare.hitrip.R;
import com.tripshare.hitrip.Trips.FirebaseDatabaseHelperParticipanti;
import com.tripshare.hitrip.Trips.InsideTripActivity;

import java.util.List;

public class ParticipantiAsteptareActivity extends AppCompatActivity {

    RecyclerView recycler_participanti_asteptare;
    String uid_organizator, data_start, data_fin;
    RecyclerView.LayoutManager RecyclerViewLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participanti_asteptare);

        RecyclerViewLayoutManager = new LinearLayoutManager(getApplicationContext());

        uid_organizator = getIntent().getStringExtra("uid_organizator");
        data_start = getIntent().getStringExtra("data_start");
        data_fin = getIntent().getStringExtra("data_final");

        recycler_participanti_asteptare = findViewById(R.id.recycler_participanti_asteptare);
        //recycler_participanti_asteptare.setLayoutManager(RecyclerViewLayoutManager);

        new FirebaseDatabaseHelperParticipantiAsteptare().showParticipanti(new FirebaseDatabaseHelperParticipantiAsteptare.DataStatus() {
            @Override
            public void DataIsLoaded(List<User> participanti, List<Integer> keys) {
                new RecyclerViewConfigParticipantAsteptare().setconfig(recycler_participanti_asteptare, ParticipantiAsteptareActivity.this, participanti, keys,  uid_organizator, data_start, data_fin);
            }
        }, uid_organizator, data_start, data_fin);
    }
}