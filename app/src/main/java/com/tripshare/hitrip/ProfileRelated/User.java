package com.tripshare.hitrip.ProfileRelated;

import com.tripshare.hitrip.Impresii.Impresie;

import java.util.HashMap;

public class User {
    public String UID;
    public String email, nume, prenume;
    public Integer varsta;
    public String poza_profil="";
    public Float rating_organizator = 0f, rating_participant=0f;
    public Integer nr_exc_organiz = 0, nr_exc_partic = 0;
    public Integer nr_impresii_organizator = 0, nr_impresii_participant= 0;
    public String sex, data_nasterii, nationalitate;
    public Integer nr_mobil_verificat, acreditare1_vierificat;
    public String descriere, preferinte, locuri_vizitate, limbi_vorbite;
    public HashMap<String, Impresie> impresie_organizare_user = null;
    public HashMap<String, Impresie> impresie_participare_user = null;
    public Integer nr_excursii_organizate=0, nr_excursii_participare=0;


    public User(){

    }

    public User(String UID, String email, String nume, String prenume, Integer varsta, String poza_profil,Float rating_organizator,
                Float rating_participant, Integer nr_exc_organiz, Integer getNr_exc_partic, Integer nr_impresii_organizator, Integer nr_impresii_participant,
                String sex, String data_nasterii, String nationalitate, Integer nr_mobil_verificat,
                Integer acreditare1_vierificat, String descriere, String preferinte,
                String locuri_vizitate, String limbi_vorbite, HashMap<String, Impresie> impresie_organizare_user, HashMap<String, Impresie> impresie_participare_user,
                Integer nr_excursii_organizate, Integer nr_excursii_participare) {
        this.UID = UID;
        this.email = email;
        this.nume = nume;
        this.prenume = prenume;
        this.varsta = varsta;
        this.poza_profil = poza_profil;
        this.rating_organizator = rating_organizator;
        this.rating_participant = rating_participant;
        this.nr_exc_organiz = nr_exc_organiz;
        this.nr_exc_partic = getNr_exc_partic;
        this.nr_impresii_organizator = nr_impresii_organizator;
        this.nr_impresii_participant = nr_impresii_participant;
        this.sex = sex;
        this.data_nasterii = data_nasterii;
        this.nationalitate = nationalitate;
        this.nr_mobil_verificat = nr_mobil_verificat;
        this.acreditare1_vierificat = acreditare1_vierificat;
        this.descriere = descriere;
        this.preferinte = preferinte;
        this.locuri_vizitate = locuri_vizitate;
        this.limbi_vorbite = limbi_vorbite;
        this.impresie_participare_user = impresie_participare_user;
        this.impresie_organizare_user = impresie_organizare_user;
        this.nr_excursii_participare = nr_excursii_participare;
        this.nr_excursii_organizate = nr_excursii_organizate;

    }

    public User(String UID, String prenume, String poza_profil) {
        this.UID = UID;
        this.prenume = prenume;
        this.poza_profil = poza_profil;
    }
}
