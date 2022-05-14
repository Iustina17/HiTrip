package com.tripshare.hitrip.Trips;

import com.tripshare.hitrip.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Trip {
    public String UID_organiztor;
    public String imagine_excursie;
    public String prenume;
    public String nume;
    public String titlu_excursie;
    public String tematica;
    public String tip;
    public String data_inceput;
    public String data_final;
    public Integer nr_zile;
    public String tara;
    public String oras;
    public String descriere_plecare;
    public Integer nr_opriri;

    public ArrayList<Oprire> vect_opriri;
    public HashMap<String, User> participanti;
    public String descriere_excursie;
    public String regulament;
    public String echipament_necesar;
    public String documente_necesare;
    public String nr_min_particip;
    public String nr_max_particip;


    public String pret;
    public String pret_min, pret_max;
    public String detalii_pret;
    public String tip_moneda;

    public String dificultate;

    public String status;
//TODO grad_dificultate

    public Trip(String UID_organiztor, String imagine_excursie, String prenume, String nume, String titlu_excursie, String tematica, String tip, String data_inceput, String data_final, Integer nr_zile, String tara, String oras, String descriere_plecare, Integer nr_opriri, ArrayList<Oprire> vect_opriri, String descriere_excursie, String regulament, String echipament_necesar, String documente_necesare, String nr_min_particip, String nr_max_particip, String pret, String pret_min, String pret_max, String detalii_pret, String tip_moneda, String dificultate) {
        this.UID_organiztor = UID_organiztor;
        this.imagine_excursie = imagine_excursie;
        this.prenume = prenume;
        this.nume = nume;
        this.titlu_excursie = titlu_excursie;
        this.tematica = tematica;
        this.tip = tip;
        this.data_inceput = data_inceput;
        this.data_final = data_final;
        this.nr_zile = nr_zile;
        this.tara = tara;
        this.oras = oras;
        this.descriere_plecare = descriere_plecare;
        this.nr_opriri = nr_opriri;
        this.vect_opriri = vect_opriri;
        this.descriere_excursie = descriere_excursie;
        this.regulament = regulament;
        this.echipament_necesar = echipament_necesar;
        this.documente_necesare = documente_necesare;
        this.nr_min_particip = nr_min_particip;
        this.nr_max_particip = nr_max_particip;
        this.pret = pret;
        this.pret_min = pret_min;
        this.pret_max = pret_max;
        this.detalii_pret = detalii_pret;
        this.tip_moneda = tip_moneda;
        this.dificultate = dificultate;
    }

    public Trip() {
    }

    public Trip(String UID_organiztor, String imagine_excursie, String prenume, String nume, String titlu_excursie, String tematica, String tip, String data_inceput, String data_final, Integer nr_zile, String tara, String oras, String descriere_plecare, Integer nr_opriri, ArrayList<Oprire> vect_opriri, String descriere_excursie, String regulament, String echipament_necesar, String documente_necesare, String nr_min_particip, String nr_max_particip, String pret, String tip_moneda, String dificultate, HashMap<String, User> participanti, String pret_min, String pret_max, String detalii_pret, String status) {
        this.UID_organiztor = UID_organiztor;
        this.imagine_excursie = imagine_excursie;
        this.prenume = prenume;
        this.nume = nume;
        this.titlu_excursie = titlu_excursie;
        this.tematica = tematica;
        this.tip = tip;
        this.data_inceput = data_inceput;
        this.data_final = data_final;
        this.nr_zile = nr_zile;
        this.tara = tara;
        this.oras = oras;
        this.descriere_plecare = descriere_plecare;
        this.nr_opriri = nr_opriri;
        this.vect_opriri = vect_opriri;
        this.descriere_excursie = descriere_excursie;
        this.regulament = regulament;
        this.echipament_necesar = echipament_necesar;
        this.documente_necesare = documente_necesare;
        this.nr_min_particip = nr_min_particip;
        this.nr_max_particip = nr_max_particip;
        this.pret = pret;
        this.tip_moneda = tip_moneda;
        this.dificultate = dificultate;
        this.participanti = participanti;
        this.pret_min = pret_min;
        this.pret_max = pret_max;
        this.detalii_pret = detalii_pret;
        this.status = status;
    }
}
