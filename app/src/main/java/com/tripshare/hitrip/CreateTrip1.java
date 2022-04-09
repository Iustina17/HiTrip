package com.tripshare.hitrip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.tripshare.hitrip.Trips.MainActivity;

import java.util.Calendar;

public class CreateTrip1 extends AppCompatActivity implements View.OnClickListener {
    EditText date_start;
    DatePickerDialog.OnDateSetListener setListener;
    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    LinearLayout layoutList;
    Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip1);

        date_start = findViewById(R.id.date_start);

        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add_stops);

        buttonAdd.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        date_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateTrip1.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year+"/";
                        date_start.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

//        setListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                month = month+1;
//                String date = day+"/"+month+"/"+year+"/";
//                date_start.setText(date);
//            }
//        };

        spinner = (Spinner) findViewById(R.id.spinner_tip_excursie);
        adapter = ArrayAdapter.createFromResource(this, R.array.tip_excursie, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        addView();
    }

    private void addView() {
        View stopView = getLayoutInflater().inflate(R.layout.row_add_stop,null,false);
        EditText editText1 = (EditText)stopView.findViewById(R.id.locatie_item);
        EditText editText2 = (EditText)stopView.findViewById(R.id.descriere_item);
        ImageButton stopDelete = (ImageButton)stopView.findViewById(R.id.close_stop_button);

        stopDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(stopView);
            }
        });

        layoutList.addView(stopView);
    }

    private void removeView(View view){
        layoutList.removeView(view);
    }
}