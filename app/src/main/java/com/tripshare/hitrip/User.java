package com.tripshare.hitrip;

public class User {
    public String email, nume, prenume;
    public int varsta;

    public User(){

    }

    public User(String email, String nume, String prenume, int varsta) {
        this.email = email;
        this.nume = nume;
        this.prenume = prenume;
        this.varsta = varsta;
    }
}
