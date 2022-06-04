package com.tripshare.hitrip.Impresii;

public class Feedback {
    public String uid_participant;
    public String stare_feedback;
    public String nume, prenume, poza;

    public Feedback(String uid_participant, String stare_feedback, String nume, String prenume, String poza) {
        this.uid_participant = uid_participant;
        this.stare_feedback = stare_feedback;
        this.nume = nume;
        this.prenume = prenume;
        this.poza = poza;
    }

    public Feedback() {
    }
}
