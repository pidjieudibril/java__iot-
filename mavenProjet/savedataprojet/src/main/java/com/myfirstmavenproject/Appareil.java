package com.myfirstmavenproject;

public abstract class Appareil {
    protected String nom;
    protected Microcontroleur microcontroleur;
    protected ServeurHTTP serveurHTTP;

    public Appareil(String nom, Microcontroleur microcontroleur) {
        this.nom = nom;
        this.microcontroleur = microcontroleur;
        this.serveurHTTP = new ServeurHTTP();
    }

    public String getNom() {
        return nom;
    }

    public abstract String getType();

    public abstract String lireDonnees();
}

class CapteurTemperature extends Appareil {
    public CapteurTemperature(String nom, Microcontroleur microcontroleur) {
        super(nom, microcontroleur);
    }

    @Override
    public String lireDonnees() {
        // Logique pour lire les données de température depuis le serveur HTTP
        String url = "http://" + microcontroleur.getAdresseIP() + "/temperature";
        return serveurHTTP.envoyerRequeteGET(url);
    }

      @Override
        public String getType() {
            return "Capteur de Température";
        }
}

class Photoresistance extends Appareil {
    public Photoresistance(String nom, Microcontroleur microcontroleur) {
        super(nom, microcontroleur);
    }

    @Override
    public String lireDonnees() {
        // Logique pour lire les données de luminosité depuis le serveur HTTP
        String url = "http://" + microcontroleur.getAdresseIP() + "/luminosite";
        return serveurHTTP.envoyerRequeteGET(url);
    }

      @Override
        public String getType() {
            return "Photoresistance";
        }
}
