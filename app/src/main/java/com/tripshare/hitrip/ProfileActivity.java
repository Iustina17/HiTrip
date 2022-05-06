package com.tripshare.hitrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Trips.Trip;

public class ProfileActivity extends AppCompatActivity {

    //   private String uid_organizator;
    String uid_user;
    TextView nume_1, prenume_1, varsta_1;
    TextView nr_exc_organiz, nr_exc_perticip;
    TextView nr_pers_rating_organiz1, nr_pers_rating_particip1;
    TextView descriere, preferinte, locuri_vizitate, limbi_vorbite;
    LinearLayout nr_tel_verificat;
    LinearLayout nu_exista_info_verificate;
    Integer nr_mobil_verificatS; ///daca e sau nu verificat

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference referenceUsers = database.getReference("Utilizatori");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        uid_user = getIntent().getStringExtra("uid");

        nume_1 = findViewById(R.id.nume1);
        prenume_1 = findViewById(R.id.prenume1);
        varsta_1 = findViewById(R.id.varsta1);
        nr_exc_organiz = findViewById(R.id.nr_exc_organizate1);
        nr_exc_perticip = findViewById(R.id.nr_exc_particip1);
        nr_pers_rating_organiz1 = findViewById(R.id.nr_pers_rating_organiz1);
        nr_pers_rating_particip1 = findViewById(R.id.nr_pers_rating_particip1);
        descriere = findViewById(R.id.descriere1);
        preferinte = findViewById(R.id.preferinte1);
        locuri_vizitate = findViewById(R.id.locuri_vizitate1);
        limbi_vorbite = findViewById(R.id.locuri_vizitate1);
        nr_tel_verificat = findViewById(R.id.layout_nr_tel_verificat1);
        nu_exista_info_verificate = findViewById(R.id.layout_nu_exista_info_verificate1);

        Query query = referenceUsers.orderByChild("UID").equalTo(uid_user);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    User user = child.getValue(User.class);

   //                 uid_user = FirebaseAuth.getInstance().getCurrentUser().getUid(); //TODO // trebuie sa iau utilizatorul pe a carui profil intru

                    //if (user.UID.equals(uid_user)) {
                        //imagine_excursie.setAdjustViewBounds(trip.imagine_excursie);
                        nume_1.setText(user.nume);
                        prenume_1.setText(user.prenume);
                        varsta_1.setText(user.varsta.toString());
                        nr_exc_organiz.setText(user.nr_exc_organiz.toString());
                        nr_exc_perticip.setText(user.nr_exc_partic.toString());
                        nr_pers_rating_organiz1.setText(user.rating_organizator.toString());
                        nr_pers_rating_particip1.setText(user.rating_participant.toString());
                        descriere.setText(user.descriere);
                        preferinte.setText(user.preferinte);
                        locuri_vizitate.setText(user.locuri_vizitate);
                        limbi_vorbite.setText(user.limbi_vorbite);
//                        nr_tel_verificat.setText(user.nume);
//                        nu_exista_info_verificate.setText(user.nume);
                   // }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

}
