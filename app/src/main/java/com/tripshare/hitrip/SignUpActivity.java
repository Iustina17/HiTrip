package com.tripshare.hitrip;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class SignUpActivity extends AppCompatActivity {

//    private Button date_button;
//    Bundle b;
    DatePicker date_picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        date_picker = findViewById((R.id.date_picker));

    }


}
