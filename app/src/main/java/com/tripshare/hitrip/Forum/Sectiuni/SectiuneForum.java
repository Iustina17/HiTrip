package com.tripshare.hitrip.Forum.Sectiuni;

import com.tripshare.hitrip.Forum.Comentarii.Comentariu;

import java.util.HashMap;

public class SectiuneForum {

    public String uid_organizator;
    public String titlu_excursie;
    public String data_inceput;
    public String data_final;
    public String denumire_sectiune;
    public HashMap<String, Comentariu> comentarii;

    public SectiuneForum() {
    }

    public SectiuneForum(String uid_organizator, String titlu_excursie, String data_inceput, String data_final, String denumire_sectiune, HashMap<String, Comentariu> comentarii) {
        this.uid_organizator = uid_organizator;
        this.titlu_excursie = titlu_excursie;
        this.data_inceput = data_inceput;
        this.data_final = data_final;
        this.denumire_sectiune = denumire_sectiune;
        this.comentarii = comentarii;
    }

}
