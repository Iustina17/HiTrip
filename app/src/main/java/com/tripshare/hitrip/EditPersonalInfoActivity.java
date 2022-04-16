package com.tripshare.hitrip;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class EditPersonalInfoActivity extends AppCompatActivity {

    static EditText birthday_pers_info;
    DatePickerDialog.OnDateSetListener setListener;
    Spinner spinner1;
    ArrayAdapter<CharSequence> adapter1;
    //TextView age_show;////////////////////////////////////
    TextView textCondition;

    static int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);

        birthday_pers_info = findViewById(R.id.birthday_pers_info);
       // age_show = findViewById(R.id.age_show);
        textCondition = findViewById(R.id.textCondition);


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        birthday_pers_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditPersonalInfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        birthday_pers_info.setText(date);
                        Calendar birthdate = Calendar.getInstance();
                        birthdate.set(year, month, day);
                        //age_show.setText(Integer.toString(calculateAge(birthdate)));
                        age = calculateAge(birthdate);
                        if(age<18){
                            textCondition.setError("Trebuie să ai cel puţin 18 ani pentru a utiliza HiTrip");
                            birthday_pers_info.requestFocus();
                            return;
                        }else{
                            textCondition.setError(null);
                        }
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();

            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = day+"/"+month+"/"+year+"/";
                birthday_pers_info.setText(date);
            }
        };

        spinner1 = (Spinner) findViewById(R.id.spinner_sex);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.sex, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    int calculateAge(Calendar date) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - date.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_MONTH) < date.get(Calendar.DAY_OF_MONTH)) {
            age--;
        }
        return age;
    }

    public void ClickConfirmMobile(View view) {
        redirectActivity(this, SendOTPActivity.class);
    }

    private static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity,aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity((intent));
    }
}