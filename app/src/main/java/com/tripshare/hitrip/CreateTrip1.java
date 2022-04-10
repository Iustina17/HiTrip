package com.tripshare.hitrip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class CreateTrip1 extends AppCompatActivity implements View.OnClickListener {
    static EditText date_start;
    static EditText date_end;
    DatePickerDialog.OnDateSetListener setListener;
    Spinner spinner1, spinner2, spinner3;
    LinearLayout layoutGradDif;

    ArrayAdapter<CharSequence> adapter1,adapter2, adapter3;
    LinearLayout layoutList;
    Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip1);

        date_start = findViewById(R.id.date_start);
        date_end = findViewById(R.id.date_end);


        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add_stops);

        buttonAdd.setOnClickListener(this);

        layoutGradDif = findViewById(R.id.layout_grad_dificultate);

//        Calendar calendar = Calendar.getInstance();
//        final int year = calendar.get(Calendar.YEAR);
//        final int month = calendar.get(Calendar.MONTH);
//        final int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        date_start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DatePickerDialog datePickerDialog = new DatePickerDialog(
//                        CreateTrip1.this, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int day) {
//                        month = month+1;
//                        String date = day+"/"+month+"/"+year;
//                        date_start.setText(date);
//                    }
//                }, year, month, day);
//                datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
//                datePickerDialog.show();
//            }
//        });

//        setListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                month = month+1;
//                String date = day+"/"+month+"/"+year+"/";
//                date_start.setText(date);
//            }
//        };

        date_start = (EditText) findViewById(R.id.date_start);
        date_end = (EditText) findViewById(R.id.date_end);
        date_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTruitonDatePickerDialog(view);
                date_start.setError(null);
            }
        });
        date_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = date_start.getText().toString();
                if(input.equals("")){
                    date_start.setError("Introduceţi data de început");
                    Toast.makeText(CreateTrip1.this, "Introduceţi data de început", Toast.LENGTH_LONG).show();
                    date_start.requestFocus();
                    return;
                }else{
                    date_start.setError(null);
                showToDatePickerDialog(view);
                }
            }
        });

        spinner1 = (Spinner) findViewById(R.id.spinner_tip_excursie);
        adapter1 = ArrayAdapter.createFromResource(this, R.array.tip_excursie, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String text = spinner1.getSelectedItem().toString();
                if(text.equals("Drumeţie")){
                    layoutGradDif.setVisibility(View.VISIBLE);
                }else{
                    layoutGradDif.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner2 = (Spinner) findViewById(R.id.grad_dificultate);
        adapter2 = ArrayAdapter.createFromResource(this, R.array.grad_dificultate, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner3 = (Spinner) findViewById(R.id.spinner_moneda);
        adapter3 = ArrayAdapter.createFromResource(this, R.array.moneda, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


//        spinner1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String text = spinner1.getSelectedItem().toString();
//                if(text.equals("Drumeţie")){
//                    layoutGradDif.setVisibility(View.VISIBLE);
//                }else{
//                    layoutGradDif.setVisibility(View.GONE);
//                }
//            }
//        });
    }

    private void showTruitonDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void showToDatePickerDialog(View view) {
        DialogFragment newFragment = new ToDatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog;
            datePickerDialog = new DatePickerDialog(getActivity(), this, year,
                    month, day);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            month = month + 1;
            date_start.setText(day + "/" + month + "/" + year);
        }

    }

    public static class ToDatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        // Calendar startDateCalendar=Calendar.getInstance();
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            String getfromdate = date_start.getText().toString().trim();
            String getfrom[] = getfromdate.split("/");
            int year, month, day;
            year = Integer.parseInt(getfrom[2]);
            month = Integer.parseInt(getfrom[1]);
            month = month - 1;
            day = Integer.parseInt(getfrom[0]);
            final Calendar c = Calendar.getInstance();
            c.set(year, month, day);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            month = month + 1;
            date_end.setText(day + "/" + month + "/" + year);
        }
    }

    @Override
    public void onClick(View v) {
        addView();
    }

    private void addView() {
        View stopView = getLayoutInflater().inflate(R.layout.row_add_stop, null, false);
        EditText editText1 = (EditText) stopView.findViewById(R.id.locatie_item);
        EditText editText2 = (EditText) stopView.findViewById(R.id.descriere_item);
        EditText editText3 = (EditText) stopView.findViewById(R.id.descriere_transport);
        ImageButton stopDelete = (ImageButton) stopView.findViewById(R.id.close_stop_button);

        stopDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(stopView);
            }
        });

        layoutList.addView(stopView);
    }

    private void removeView(View view) {
        layoutList.removeView(view);
    }
}