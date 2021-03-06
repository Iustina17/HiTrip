package com.tripshare.hitrip.LoginSignUp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.tripshare.hitrip.R;
import com.tripshare.hitrip.Trips.MainActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private Button login_button;
    private Button signup_button;
    private EditText password;
    private EditText username;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_button = findViewById((R.id.login_button));
        signup_button = findViewById((R.id.signup_button));
        username = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);


        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //intra direct în cont dacă s-a autentificat anterior
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null) {// && mFirebaseUser.isEmailVerified()) {
                    //  Toast.makeText(LoginActivity.this, "V-ați autentificat", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    //  } else if (!mFirebaseUser.isEmailVerified()) {
                    //     Toast.makeText(LoginActivity.this, "Un e-mail pentru validare a fost trimis.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Autentificați-vă", Toast.LENGTH_LONG).show();
                }
            }
        };


        login_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onClick(View v) {
                String email = username.getText().toString();
                String pwd = password.getText().toString();
                if (email.isEmpty()) {
                    username.setError("Introduceți email-ul");
                    username.requestFocus();
                } else if (pwd.isEmpty()) {
                    password.setError("Introduceți parola");
                    password.requestFocus();
                } else {
//                    // TESTING PURPOSES ONLY!
//                    Toast.makeText(LoginActivity.this, "V-ați autentificat", Toast.LENGTH_LONG).show();
//                    Intent intToHome = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intToHome);
//                    finish();

                    // the rest:

                    if (!isEmailValid(email)) {
                        username.setError("Nu ați introdus o adresa de e-mail validă");
                        username.requestFocus();
                    } else if (isEmailValid(email)) {
                        mFirebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                                if (isNewUser) {
                                    username.setError(getString(R.string.error_email_not_found));
                                    username.requestFocus();
                                }
                            }
                        });

                        // if (!mFirebaseAuth.getCurrentUser().isEmailVerified()) {
                        //emailID.setError("Adresa de e-mail nu a fost validata. Accesați link-ul primit pe e-mail pentru a confirma adresa de e-mail");
                        //emailID.requestFocus();
                        //} else if(mFirebaseAuth.getCurrentUser().isEmailVerified()) &&task.isSuccessful){
                        mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful() && task.getException().getMessage().contains("The password is invalid or the user does not have a password.")) {
                                    password.setError("Parola introdusă nu corespunde. Încercați din nou");
                                    password.requestFocus();
                                } else if (!task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "A aparut o eroare!", Toast.LENGTH_LONG);
                                } else {
                                    Toast.makeText(LoginActivity.this, "V-ați autentificat", Toast.LENGTH_LONG).show();
                                    Intent intToHome = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intToHome);
                                    finish();
                                }
                            }
                        });
                        //   }
                    } else {
                        Toast.makeText(LoginActivity.this, "A aparut o eroare!", Toast.LENGTH_LONG);
                    }
                }
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(LoginActivity.this, "Nu ai cont?", Toast.LENGTH_LONG).show();
                Intent intToSignUp = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intToSignUp);
            }
        });
    }



    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}