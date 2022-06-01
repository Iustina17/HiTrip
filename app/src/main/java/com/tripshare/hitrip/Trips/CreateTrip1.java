package com.tripshare.hitrip.Trips;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.R;
import com.tripshare.hitrip.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CreateTrip1 extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference_utilizatori;

    DatePickerDialog.OnDateSetListener setListener;
    Spinner spinner1, spinner2, spinner3;
    LinearLayout layoutGradDif;

    ArrayAdapter<CharSequence> adapter1, adapter2, adapter3;
    LinearLayout layoutList;
    Button buttonAdd;

    String UID_organiztor;
    static String prenume, nume;
    EditText titlu;
    CheckBox natura, sport, relaxare, divertisment, gastronomie, muzica, arhitectura, industrie, istorie, etnografie, arta, literatura, altele;
    EditText altele_text;
    //spinner 1 e tip_excursie
    TextView tip_excursie_text, grad_dificultate_text;
    static EditText data_inceput, data_final;
    EditText tara, oras, descriere_plecare;
    EditText descriere_excursie, regulament, echipament_necesar, documente_necesare;
    EditText nr_min_particpip, nr_max_particpip;
    LinearLayout layout_nr_particip;
    EditText cost;
    //spinner 2 e grad_dificultate
//spinner3 e tip_moneda
    Button button_finalizeaza;
    ImageView imagine_excursie;

    String locatieV, descriere_oprireV, descriere_transportV;

    Integer index_opriri = 0;
    String tematica;
    HashMap<String, Oprire> vect_opriri;
    HashMap<String, User> participanti;

    String natura1 = "", sport1 = "", relaxare1 = "", divertisment1 = "", gastronomie1 = "", muzica1 = "", arhitectura1 = "", industrie1 = "", istorie1 = "", etnografie1 = "", arta1 = "", literatura1 = "", altele1 = "";

    Switch switch_tip_pret;
    LinearLayout layout_pret_variabil, layout_pret_fix;
    EditText pret_min, pret_max;
    EditText detalii_pret;

    String status = "viitoare";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_trip1);

        titlu = findViewById(R.id.titlu_excursie);
        natura = findViewById(R.id.natura);
        sport = findViewById(R.id.sport);
        relaxare = findViewById(R.id.relaxare);
        divertisment = findViewById(R.id.divertisment);
        gastronomie = findViewById(R.id.gastronomie);
        muzica = findViewById(R.id.muzica);
        arhitectura = findViewById(R.id.arhitectura);
        industrie = findViewById(R.id.industrie);
        istorie = findViewById(R.id.istorie);
        etnografie = findViewById(R.id.etnografie);
        arta = findViewById(R.id.arta);
        literatura = findViewById(R.id.literatura);
        altele = findViewById(R.id.altele);
        altele_text = findViewById(R.id.altele_text);
        data_inceput = findViewById(R.id.date_start);
        data_final = findViewById(R.id.date_end);
        tara = findViewById(R.id.tara);
        oras = findViewById(R.id.oras);
        descriere_plecare = findViewById(R.id.descriere_plecare);
        descriere_excursie = findViewById(R.id.descriere_excursie);
        regulament = findViewById(R.id.regulament);
        echipament_necesar = findViewById(R.id.echipament_necesar);
        documente_necesare = findViewById(R.id.documente_necesare);
        nr_min_particpip = findViewById(R.id.min_participanti);
        nr_max_particpip = findViewById(R.id.max_participanti);
        layout_nr_particip = findViewById(R.id.layout_nr_particip);
        cost = findViewById(R.id.cost);
        button_finalizeaza = findViewById(R.id.button_finalizare);
        tip_excursie_text = findViewById(R.id.tip_excursie_text);
        grad_dificultate_text = findViewById(R.id.tip_excursie_text);

        switch_tip_pret = findViewById(R.id.switch_tip_pret);
        String tip_pret = "fix";
        layout_pret_variabil = findViewById(R.id.layout_pret_variabil);
        layout_pret_fix = findViewById(R.id.layout_pret_fix);
        pret_min = findViewById(R.id.pret_min);
        pret_max = findViewById(R.id.pret_max);
        detalii_pret = findViewById(R.id.detalii_pret);

        altele_text.setVisibility(View.GONE);

        altele.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (altele.isChecked()) {
                    altele_text.setVisibility(View.VISIBLE);
                } else {
                    altele_text.setVisibility(View.GONE);
                }
            }
        });

        layoutList = findViewById(R.id.layout_list);
        buttonAdd = findViewById(R.id.button_add_stops);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView();
            }
        });

        layoutGradDif = findViewById(R.id.layout_grad_dificultate);

        database = FirebaseDatabase.getInstance();
        reference_utilizatori = database.getReference("Utilizatori");
        UID_organiztor = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = reference_utilizatori.orderByChild("UID").equalTo(UID_organiztor);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User userCurent = data.getValue(User.class);
                    nume = userCurent.nume;
                    prenume = userCurent.prenume;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        switch_tip_pret.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layout_pret_variabil.setVisibility(View.VISIBLE);
                    layout_pret_fix.setVisibility(View.GONE);
                } else {
                    layout_pret_variabil.setVisibility(View.GONE);
                    layout_pret_fix.setVisibility(View.VISIBLE);
                }
            }
        });

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

        button_finalizeaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (natura.isChecked()) {
                    natura1 = natura.getText().toString() + ", ";
                }
                if (sport.isChecked()) {
                    sport1 = sport.getText().toString() + ", ";
                }
                if (relaxare.isChecked()) {
                    relaxare1 = relaxare.getText().toString() + ", ";
                }
                if (divertisment.isChecked()) {
                    divertisment1 = divertisment.getText().toString() + ", ";
                }
                if (gastronomie.isChecked()) {
                    gastronomie1 = gastronomie.getText().toString() + ", ";
                }
                if (muzica.isChecked()) {
                    muzica1 = muzica.getText().toString() + ", ";
                }
                if (arhitectura.isChecked()) {
                    arhitectura1 = arhitectura.getText().toString() + ", ";
                }
                if (industrie.isChecked()) {
                    industrie1 = industrie.getText().toString() + ", ";
                }
                if (istorie.isChecked()) {
                    istorie1 = istorie.getText().toString() + ", ";
                }
                if (etnografie.isChecked()) {
                    etnografie1 = etnografie.getText().toString() + ", ";
                }
                if (arta.isChecked()) {
                    arta1 = arta.getText().toString() + ", ";
                }
                if (literatura.isChecked()) {
                    literatura1 = literatura.getText().toString() + ", ";
                }
                if (altele.isChecked()) {
                    altele1 = altele_text.getText().toString() + ", ";
                } else {
                    altele1 = "";
                }

                String mcost, mpretMin, mpretMax;
                if (switch_tip_pret.isChecked()) {
                    mpretMin = pret_min.getText().toString();
                    mpretMax = pret_max.getText().toString();
                    mcost = "";
                } else {
                    mcost = cost.getText().toString();
                    mpretMin = "";
                    mpretMax = "";
                }

                String mDetalii_pret = detalii_pret.getText().toString();

                String mUID_organiztor = UID_organiztor.toString();
                String mimagine_excursie = "TODO"; //TODO
                String mprenume = prenume.toString();
                String mnume = nume.toString();
                String mtitlu = titlu.getText().toString();
                String mtematica = natura1 + sport1 + relaxare1 + divertisment1 +
                        gastronomie1 + muzica1 + arhitectura1 + industrie1 + istorie1 +
                        etnografie1 + arta1 + literatura1 + altele1;////////////////////////////////////////trebuie sa-l fac string din checkbox-uri

                mtematica = mtematica.substring(0, mtematica.length() - 2); //sterg ultimele doua caractere ca sa nu am ", " la final

                String mtip = spinner1.getSelectedItem().toString();
                String mdata_inceput = data_inceput.getText().toString();
                String mdata_final = data_final.getText().toString();

                String dataFinal = data_final.getText().toString();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                Date datef = null;
                try {
                    datef = df.parse(dataFinal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                String dataIncep = data_inceput.getText().toString();
                SimpleDateFormat di = new SimpleDateFormat("dd/MM/yyyy");
                Date datei = null;
                try {
                    datei = di.parse(dataIncep);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long diff = datef.getTime() - datei.getTime();
                //System.out.println ("Days: " + Integer.parseInt(String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))));

                //int nr_zile  = 0;
                Integer mnr_zile = Integer.parseInt(String.valueOf(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))) + 1;
                String mtara = tara.getText().toString();
                String moras = oras.getText().toString();
                String mdescriere_plecare = descriere_plecare.getText().toString();
                String mdescriere_excursie = descriere_excursie.getText().toString();
                String mregulament = regulament.getText().toString();
                String mechipament_necesar = echipament_necesar.getText().toString();
                String mdocumente_necesare = documente_necesare.getText().toString();
                String mnr_min_particpip = nr_min_particpip.getText().toString();
                String mnr_max_particpip = nr_max_particpip.getText().toString();
                String mtip_moneda = spinner3.getSelectedItem().toString();
                String dificultate = spinner2.getSelectedItem().toString();
                int childcount = layoutList.getChildCount();
                vect_opriri = new HashMap<String, Oprire>();

                for (int i=0; i < childcount; i++){
                    View v = layoutList.getChildAt(i);

                    EditText locatie = (EditText) v.findViewById(R.id.locatie_item);
                    EditText descriere_oprire = (EditText) v.findViewById(R.id.descriere_item);
                    EditText descriere_transport = (EditText) v.findViewById(R.id.descriere_transport);

                    locatieV = locatie.getText().toString();
                    descriere_oprireV = descriere_oprire.getText().toString();
                    descriere_transportV = descriere_transport.getText().toString();
                    Log.d("vect_opriri", locatieV);
                    Log.d("vect_opriri", descriere_oprireV);
                    Log.d("vect_opriri", descriere_transportV);

                    Oprire oprire = new Oprire(index_opriri, locatieV, descriere_oprireV, descriere_transportV);
                    vect_opriri.put(String.valueOf(i)+"_key", oprire);

                    //        Log.d("Locatie1", oprire.index_opriri);
                    //        Log.d("DOprire2", descriere_oprireV);
                    //        Log.d("Dtransport3", descriere_transportV);
                }

                if (mtitlu.isEmpty()) {
                    titlu.setError("Stabiliţi titlul");
                    titlu.requestFocus();
                } else if (mtip.isEmpty()) {
                    tip_excursie_text.setError("Stabiliţi tipul excursiei");
                    spinner1.requestFocus();
                } else if (mtip.equals("Drumeţie") && dificultate.isEmpty()) {
                    grad_dificultate_text.setError("Selectaţi gradul de dificultate");
                    spinner2.requestFocus();
                } else if (dataIncep.isEmpty()) {
                    data_inceput.setError("Selectaţi data de început a excursiei");
                    data_inceput.requestFocus();
                } else if (mtara.isEmpty() || moras.isEmpty()) {
                    tara.setError("Selectaţi locul de plecare");
                    tara.requestFocus();
                } else if (mnr_min_particpip.isEmpty() || mnr_max_particpip.isEmpty()) {
                    nr_max_particpip.setError("Selectaţi numărul participanţilor");
                    layout_nr_particip.requestFocus();
                } else if (mdescriere_excursie.isEmpty()) {
                    descriere_excursie.setError("Completaţi descrierea excursiei");
                    descriere_excursie.requestFocus();
                } else if (mregulament.isEmpty()) {
                    regulament.setError("Completaţi regulamentul excursiei");
                    regulament.requestFocus();
                } else if (mtip.equals("Drumeţie") && mechipament_necesar.isEmpty()) {
                    echipament_necesar.setError("Stabiliţi echipamenul necesar");
                    echipament_necesar.requestFocus();
                } else if ((!switch_tip_pret.isChecked()) && mcost.isEmpty()) {
                    cost.setError("Selectaţi costul per participant");
                    cost.requestFocus();
                } else if ((switch_tip_pret.isChecked()) && mpretMin.isEmpty()) {
                    pret_min.setError("Selectaţi costul minim");
                    pret_min.requestFocus();
                } else if ((switch_tip_pret.isChecked()) && mpretMax.isEmpty()) {
                    pret_max.setError("Selectaţi costul maxim");
                    pret_max.requestFocus();
                } else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Calatorii");
                    Trip trip = new Trip(mUID_organiztor, mimagine_excursie, mprenume, mnume, mtitlu, mtematica,
                            mtip, mdata_inceput, mdata_final, mnr_zile, mtara,
                            moras, mdescriere_plecare, layoutList.getChildCount(), vect_opriri,
                            mdescriere_excursie, mregulament, mechipament_necesar,
                            mdocumente_necesare, mnr_min_particpip, mnr_max_particpip, mcost, mtip_moneda, dificultate, participanti, mpretMin, mpretMax, mDetalii_pret, status);
                    reference.push().setValue(trip);
                    redirectActivity(CreateTrip1.this, MainActivity.class);
                }
            }
        });

        data_inceput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTruitonDatePickerDialog(view);
                data_inceput.setError(null);
            }
        });
        data_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = data_inceput.getText().toString();
                if (input.equals("")) {
                    data_inceput.setError("Introduceţi data de început");
                    Toast.makeText(CreateTrip1.this, "Introduceţi data de început", Toast.LENGTH_LONG).show();
                    data_inceput.requestFocus();
                    return;
                } else {
                    data_inceput.setError(null);
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
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                String text = spinner1.getSelectedItem().toString();
                if (text.equals("Drumeţie")) {
                    layoutGradDif.setVisibility(View.VISIBLE);
                } else {
                    layoutGradDif.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner2 = (Spinner)

                findViewById(R.id.grad_dificultate);

        adapter2 = ArrayAdapter.createFromResource(this, R.array.grad_dificultate, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner3 = (Spinner)

                findViewById(R.id.spinner_moneda);

        adapter3 = ArrayAdapter.createFromResource(this, R.array.moneda, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
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
            data_inceput.setText(day + "/" + month + "/" + year);
        }

    }

    public static class ToDatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {
        // Calendar startDateCalendar=Calendar.getInstance();
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            String getfromdate = data_inceput.getText().toString().trim();
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
            data_final.setText(day + "/" + month + "/" + year);
        }
    }

    private void addView() {
        View stopView = getLayoutInflater().inflate(R.layout.row_add_stop, null, false);
        index_opriri++;

        layoutList.addView(stopView);
//
//        EditText locatie = (EditText) stopView.findViewById(R.id.locatie_item);
//        EditText descriere_oprire = (EditText) stopView.findViewById(R.id.descriere_item);
//        EditText descriere_transport = (EditText) stopView.findViewById(R.id.descriere_transport);
//
//        locatieV = locatie.getText().toString();
//        descriere_oprireV = descriere_oprire.getText().toString();
//        descriere_transportV = descriere_transport.getText().toString();
//        Log.d("vect_opriri", locatieV);
//        Log.d("vect_opriri", descriere_oprireV);
//        Log.d("vect_opriri", descriere_transportV);
//
//        Oprire oprire = new Oprire(index_opriri, locatieV, descriere_oprireV, descriere_transportV);
//        vect_opriri = new ArrayList<Oprire>();
//        vect_opriri.add(oprire);
//
////        Log.d("Locatie1", oprire.index_opriri);
////        Log.d("DOprire2", descriere_oprireV);
////        Log.d("Dtransport3", descriere_transportV);
        ImageButton stopDelete = (ImageButton) stopView.findViewById(R.id.close_stop_button);

        stopDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView(stopView);
            }
        });
    }

    private void removeView(View view) {
        layoutList.removeView(view);
    }

    private static void redirectActivity(Activity activity, Class aClass) {
        //Initialize intent
        Intent intent = new Intent(activity, aClass);
        //Set flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //Start activity
        activity.startActivity((intent));
    }

}