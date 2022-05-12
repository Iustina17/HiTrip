package com.tripshare.hitrip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InsideSectionForumActivity extends AppCompatActivity {

    Button button_adauga_comentariu;
    LinearLayout layout_adauga_comentariu_text;
   // BottomNavigationView adauga_comentariu_BottomNavigationView;
    Button button_enter_posteaza_comentariu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_section_forum);

        //adauga_comentariu_BottomNavigationView.findViewById(R.id.adauga_comentariu_BottomNavigationView);
        button_adauga_comentariu.findViewById(R.id.button_adauga_comentariu);
        layout_adauga_comentariu_text.findViewById(R.id.layout_adauga_comentariu_text);
        button_adauga_comentariu.findViewById(R.id.button_enter_posteaza_comentariu);

        button_adauga_comentariu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_adauga_comentariu.setVisibility(View.GONE);
                layout_adauga_comentariu_text.setVisibility(View.VISIBLE);
            }
        });

        button_enter_posteaza_comentariu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_adauga_comentariu.setVisibility(View.VISIBLE);
                layout_adauga_comentariu_text.setVisibility(View.GONE);
            }
        });
    }
}