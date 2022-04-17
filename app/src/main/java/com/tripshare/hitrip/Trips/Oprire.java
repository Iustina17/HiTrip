package com.tripshare.hitrip.Trips;

public class Oprire {

    public int index_opriri;
    public String locatie_oprire;
    public String descriere_oprire;
    public String descriere_transport;

    public Oprire(int nr_oprire, String nr_locatie_oprire, String descriere_oprire, String descriere_transport) {
        this.index_opriri = nr_oprire;
        this.locatie_oprire = nr_locatie_oprire;
        this.descriere_oprire = descriere_oprire;
        this.descriere_transport = descriere_transport;
    }
}
