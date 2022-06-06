package com.tripshare.hitrip.Sugestii;

public class Sugestie {
    public String continut;
    public String autor;
    public String data;
    public String titlu;

    public Sugestie() {
    }

    public Sugestie(String titlu, String continut, String autor, String data) {
        this.titlu = titlu;
        this.continut = continut;
        this.autor = autor;
        this.data = data;
    }

}
