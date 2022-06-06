package com.tripshare.hitrip.Forum.Comentarii;

public class Comentariu {
    public String numeAutor, prenumeAutor;
    public String uidAutor;
    public String continut;
    public String poza;
    public String dataComentariu;

    public Comentariu() {
    }

    public Comentariu(String numeAutor, String prenumeAutor, String uidAutor, String continut, String poza, String dataComentariu) {
        this.numeAutor = numeAutor;
        this.prenumeAutor = prenumeAutor;
        this.uidAutor = uidAutor;
        this.continut = continut;
        this.poza = poza;
        this.dataComentariu = dataComentariu;
    }
}
