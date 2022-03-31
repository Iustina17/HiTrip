package com.tripshare.hitrip;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    //    private Button date_button;
//    Bundle b;
    DatePicker date_picker;
    Button register_button;
    TextView age_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        date_picker = findViewById((R.id.date_picker));
        register_button = findViewById(R.id.registrer_button);
        age_show = findViewById(R.id.age_show);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date_picker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                    Calendar birthdate = Calendar.getInstance();
                    int bYear = date_picker.getYear();
                    int bMonth = date_picker.getMonth();
                    int bDay = date_picker.getDayOfMonth();
                    birthdate.set(bYear, bMonth, bDay);
                    age_show.setText(Integer.toString(calculateAge(birthdate)));
                }
            });
        }


    }

    int calculateAge(Calendar date) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - date.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_MONTH) < date.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }
        return age;
    }


}
