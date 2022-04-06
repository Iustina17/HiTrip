package com.tripshare.hitrip.Trips;

public class Trip {
    public String data_final;
    public String data_inceput;
    public String id_organiztor;
    public String nr_zile;
    public String nume;
    public String oras;
    public String prenume;
    public String pret;
    public String tara;
    public String tip;
    public String tip_moneda;
    public String titlu_excursie;

    public Trip() {
    }

    public Trip(String data_final, String data_inceput, String id_organiztor,
                String nr_zile, String nume, String oras, String prenume, String pret, String tara,
                String tip, String tip_moneda, String titlu_excursie) {
        this.data_final = data_final;
        this.data_inceput = data_inceput;
        this.id_organiztor = id_organiztor;
        this.nr_zile = nr_zile;
        this.nume = nume;
        this.oras = oras;
        this.prenume = prenume;
        this.pret = pret;
        this.tara = tara;
        this.tip = tip;
        this.tip_moneda = tip_moneda;
        this.titlu_excursie = titlu_excursie;
    }
}
