package com.tripshare.hitrip.Forum.Sectiuni;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.tripshare.hitrip.R;

import java.util.List;

public class FormularActivity extends AppCompatActivity {

    RecyclerView recycler_sectiuni_forum;
    String uid_organizator,data_start,data_fin, titlu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formular);

        uid_organizator = getIntent().getStringExtra("Uid_organizator");
        data_start = getIntent().getStringExtra("data_inceput");
        data_fin = getIntent().getStringExtra("data_final");
        titlu = getIntent().getStringExtra("titlu");

        recycler_sectiuni_forum = findViewById(R.id.Recycler_sectiuni_forum);

        new FirebaseDatabaseHelperForumSectiuni(uid_organizator,data_start,data_fin,titlu).showSectiuni(new FirebaseDatabaseHelperForumSectiuni.DataStatus() {
            @Override
            public void DataIsLoaded(List<SectiuneForum> sectiuniList, List<String> keys) {
                new RecyclerViewConfigForumSectiuni().setconfig(recycler_sectiuni_forum, FormularActivity.this , sectiuniList, keys);
            }
        });


    }
}