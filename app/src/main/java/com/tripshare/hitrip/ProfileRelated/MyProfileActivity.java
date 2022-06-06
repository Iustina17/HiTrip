package com.tripshare.hitrip.ProfileRelated;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tripshare.hitrip.Impresii.AfisareImpresii.AfisareImpresiiPtOrganizator;
import com.tripshare.hitrip.Impresii.AfisareImpresii.AfisareImpresiiPtParticipare;
import com.tripshare.hitrip.R;

public class MyProfileActivity extends AppCompatActivity {

    String uid_organizator_my;
    TextView nume_my, prenume_my, varsta_my;
    TextView nr_exc_organiz_my, nr_exc_perticip_my;
    TextView nr_pers_rating_organiz_my, nr_pers_rating_particip_my;
    TextView descriere_my, preferinte_my, locuri_vizitate_my, limbi_vorbite_my;
    LinearLayout nr_tel_verificat_my;
    LinearLayout nu_exista_info_verificate_my;
    Integer nr_mobil_verificatS_my; ///daca e sau nu verificat
    RatingBar ratingOrganizatorBar;
    RatingBar ratingParticipantBar;
    Button vezi_impresii_organizator_buton;
    Button vezi_impresii_participant_buton;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference referenceUsers = database.getReference("Utilizatori");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        nume_my = findViewById(R.id.nume_my);
        prenume_my = findViewById(R.id.prenume_my);
        varsta_my = findViewById(R.id.varsta_my);
        nr_exc_organiz_my = findViewById(R.id.nr_excursii_organiz_my);
        nr_exc_perticip_my = findViewById(R.id.nr_excursii_particip_my);
        nr_pers_rating_organiz_my = findViewById(R.id.nr_pers_rating_organiz_my);
        nr_pers_rating_particip_my = findViewById(R.id.nr_pers_rating_particip_my);
        descriere_my = findViewById(R.id.descriere_my);
        preferinte_my = findViewById(R.id.preferinte_my);
        locuri_vizitate_my = findViewById(R.id.locuri_vizitate_my);
        limbi_vorbite_my = findViewById(R.id.limbi_vorbite_my);
        nr_tel_verificat_my = findViewById(R.id.nr_tel_verificat_my);
        nu_exista_info_verificate_my = findViewById(R.id.nu_exista_info_verificate_my);
        vezi_impresii_organizator_buton = findViewById(R.id.vezi_impresii_organizator_buton);
        vezi_impresii_participant_buton = findViewById(R.id.vezi_impresii_participant_buton);
        ratingOrganizatorBar = findViewById(R.id.ratingOrganizatorBar);
        ratingParticipantBar = findViewById(R.id.ratingParticipantBar);

        referenceUsers.addValueEventListener(new ValueEventListener() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyNode : snapshot.getChildren()) {
                    User user = keyNode.getValue(User.class);

                    uid_organizator_my = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    if (user.UID.equals(uid_organizator_my)) {
                        //imagine_excursie.setAdjustViewBounds(trip.imagine_excursie);
                        nume_my.setText(user.nume);
                        prenume_my.setText(user.prenume);
                        varsta_my.setText(user.varsta.toString());
                        nr_exc_organiz_my.setText(user.nr_exc_organiz.toString());
                        nr_exc_perticip_my.setText(user.nr_exc_partic.toString());
                        nr_pers_rating_organiz_my.setText(String.format("%.2f", user.rating_organizator));
                        nr_pers_rating_particip_my.setText(String.format("%.2f", user.rating_participant));
                        descriere_my.setText(user.descriere);
                        preferinte_my.setText(user.preferinte);
                        locuri_vizitate_my.setText(user.locuri_vizitate);
                        limbi_vorbite_my.setText(user.limbi_vorbite);
                        ratingOrganizatorBar.setRating(user.rating_organizator);
                        ratingParticipantBar.setRating(user.rating_participant);
//                        nr_tel_verificat_my.setText(user.nume);
//                        nu_exista_info_verificate_my.setText(user.nume);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        vezi_impresii_organizator_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, AfisareImpresiiPtOrganizator.class);
                startActivity(intent);

            }
        });

        vezi_impresii_participant_buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyProfileActivity.this, AfisareImpresiiPtParticipare.class);
                startActivity(intent);
            }
        });
    }

    public void ClickEditProfile(View view){
        redirectActivity(this, EditProfileActivity.class);
    }
    public void ClickEditPersonalInfo(View view) {
        redirectActivity(this, EditPersonalInfoActivity.class);
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