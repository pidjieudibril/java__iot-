package com.myfirstmavenproject;

import java.util.ArrayList;
import java.util.List;

public class Microcontroleur {
    private int id;
    private String nom;
    private String adresseIP;
    private List<Appareil> appareils;

    public Microcontroleur(String adresseIP) {
        this.adresseIP = adresseIP;
        this.appareils = new ArrayList<>();
    }

    public Microcontroleur(String nom, String adresseIP) {
        this.nom = nom;
        this.adresseIP = adresseIP;
        this.appareils = new ArrayList<>();
    }

    public Microcontroleur(int id, String nom, String adresseIP) {
        this.id = id;
        this.nom = nom;
        this.adresseIP = adresseIP;
        this.appareils = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

      public void setNom(String nom) {
            this.nom = nom;
        }

   public void setAdresseIP(String adresseIP) {
               this.adresseIP = adresseIP;
           }
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresseIP() {
        return adresseIP;
    }

    public void ajouterAppareil(Appareil appareil) {
        appareils.add(appareil);
    }

    public List<Appareil> getAppareils() {
        return appareils;
    }



    




}
