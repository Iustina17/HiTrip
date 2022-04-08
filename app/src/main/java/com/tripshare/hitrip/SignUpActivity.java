package com.tripshare.hitrip;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tripshare.hitrip.Trips.MainActivity;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth fAuth;
    DatePicker date_picker;
    Button register_button;
    TextView age_show, textCondition;
    EditText mEmail,mLastName, mFirstName,mPassword, mConfirm_password;
    static int age;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        date_picker = findViewById((R.id.date_picker));
        register_button = findViewById(R.id.registrer_button);
        age_show = findViewById(R.id.age_show);
        textCondition = findViewById(R.id.textCondition);
        mEmail = findViewById(R.id.editTextEmail);
        mLastName = findViewById(R.id.editTextLastName);
        mFirstName = findViewById(R.id.date_start);
        mPassword = findViewById(R.id.editTextPassword);
        mConfirm_password = findViewById(R.id.editTextConfirmPassowrd);

        fAuth = FirebaseAuth.getInstance();


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
                    age = calculateAge(birthdate);
                }
            });
        }

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confirm_password = mConfirm_password.getText().toString().trim();
                String firstName = mFirstName.getText().toString().trim();
                String lastName = mLastName.getText().toString().trim();


                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Introduceţi o adresă de email");
                    return;
                }

                if (!isEmailValid(email)) {
                    mEmail.setError("Nu ați introdus o adresa de e-mail validă");
                    mEmail.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Introduceţi o parolă");
                    mEmail.requestFocus();
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Parola trebuie să aibă minim 6 caractere");
                    mEmail.requestFocus();
                    return;
                }

                if (!mPassword.getText().toString().equals(mConfirm_password.getText().toString())){
                    mConfirm_password.setError("Parola nu se poriveşte");
                    Toast.makeText(SignUpActivity.this, "Parola nu se poriveşte", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(age<18){
                    textCondition.setError("Trebuie să ai cel puţin 18 ani pentru a utiliza HiTrip");
                    mEmail.requestFocus();
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //addUserToDatabase(email, lastName, firstName, age);
                            reference = FirebaseDatabase.getInstance().getReference().child("Calatorii");
                            User utilizator = new User(email, lastName, firstName, age);
                            reference.push().setValue(utilizator);
                            Toast.makeText(SignUpActivity.this,"Utilizator creat",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            mEmail.setError("Această adresă este deja înregistrată");
                            mEmail.requestFocus();
                            String error = task.getException().toString();
                            String afisare = getString(R.string.utlizator_neinregistart) + error;
                            Toast.makeText(SignUpActivity.this, afisare, Toast.LENGTH_LONG).show();
                            //finish();
                        } else {
                            Toast.makeText(SignUpActivity.this,"Eroare!" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
