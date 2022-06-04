package com.tripshare.hitrip.Impresii;

public class Impresie {
    public String nume, prenume;
    public String uidAutor;
    public String titlu_excursie;
    public String data;
    public Float nota;
    public String imagineAutor;
    public String continut;

    public Impresie(){

    }

    public Impresie(String nume, String prenume, String uidAutor, String titlu_excursie, String data, Float nota, String imagineAutor, String continut) {
        this.nume = nume;
        this.prenume = prenume;
        this.uidAutor = uidAutor;
        this.titlu_excursie = titlu_excursie;
        this.data = data;
        this.nota = nota;
        this.imagineAutor = imagineAutor;
        this.continut = continut;
    }
}
