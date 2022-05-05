package com.tripshare.hitrip;

public class User {
    public String UID;
    public String email, nume, prenume;
    public Integer varsta;
    public String poza_profil;
    public Float rating_organizator, rating_participant;
    public Integer nr_exc_organiz, nr_exc_partic;
    public Integer nr_impresii;
    public String sex, data_nasterii, nationalitate;
    public Integer nr_mobil_verificat, acreditare1_vierificat;
    public String descriere, preferinte, locuri_vizitate, limbi_vorbite;


    public User(){

    }

    public User(String UID, String email, String nume, String prenume, Integer varsta, String poza_profil,Float rating_organizator,
                Float rating_participant, Integer nr_exc_organiz, Integer getNr_exc_partic, Integer nr_impresii,
                String sex, String data_nasterii, String nationalitate, Integer nr_mobil_verificat,
                Integer acreditare1_vierificat, String descriere, String preferinte,
                String locuri_vizitate, String limbi_vorbite) {
        this.UID = UID;
        this.email = email;
        this.nume = nume;
        this.prenume = prenume;
        this.varsta = varsta;
        this.poza_profil = "";
        this.rating_organizator = rating_organizator;
        this.rating_participant = rating_participant;
        this.nr_exc_organiz = nr_exc_organiz;
        this.nr_exc_partic = getNr_exc_partic;
        this.nr_impresii = nr_impresii;
        this.sex = sex;
        this.data_nasterii = data_nasterii;
        this.nationalitate = nationalitate;
        this.nr_mobil_verificat = nr_mobil_verificat;
        this.acreditare1_vierificat = acreditare1_vierificat;
        this.descriere = descriere;
        this.preferinte = preferinte;
        this.locuri_vizitate = locuri_vizitate;
        this.limbi_vorbite = limbi_vorbite;
    }

    public User(String UID, String prenume, String poza_profil) {
        this.UID = UID;
        this.prenume = prenume;
        this.poza_profil = poza_profil;
    }
}
