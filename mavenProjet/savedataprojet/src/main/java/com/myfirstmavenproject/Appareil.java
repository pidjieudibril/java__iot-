package com.myfirstmavenproject;

import java.util.Queue;

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
/*
class CapteurTemperature extends Appareil {
    public CapteurTemperature(String nom, Microcontroleur microcontroleur) {
        super(nom, microcontroleur);
    }

    @Override
    public String lireDonnees() {
        // Logique pour lire les données de température depuis le serveur HTTP
        String adresseIP = microcontroleur.getAdresseIP();
        String url = "http://" + adresseIP + "/temperature";
        return serveurHTTP.envoyerRequeteGET(url);
    }

    @Override
    public String getType() {
        return "Capteur de Température";
    }
}
*/


 class CapteurTemperature extends Appareil {
    private Queue<String> fileAttente;

    public CapteurTemperature(String nom, Microcontroleur microcontroleur, Queue<String> fileAttente) {
        super(nom, microcontroleur);
        this.fileAttente = fileAttente;
    }

   
@Override
public String lireDonnees() {
    // Logique pour lire les données de température depuis le serveur HTTP
    String adresseIP = microcontroleur.getAdresseIP();
    String url = "http://" + adresseIP + "/temperature";
    String reponseServeur = serveurHTTP.envoyerRequeteGET(url);

    // Retourner la réponse brute
    return reponseServeur;
}


  

    @Override
    public String getType() {
        return "Capteur de Température";
    }
}

// humidité 
class CapteurHumidite extends Appareil {
    private Queue<String> fileAttente;

    public CapteurHumidite(String nom, Microcontroleur microcontroleur, Queue<String> fileAttente) {
        super(nom, microcontroleur);
        this.fileAttente = fileAttente;
    }

    @Override
    public String lireDonnees() {
        // Logique pour lire les données d'humidité depuis le serveur HTTP
        String adresseIP = microcontroleur.getAdresseIP();
        String url = "http://" + adresseIP + "/humidite";
        String reponseServeur = serveurHTTP.envoyerRequeteGET(url);

        // Retourner la réponse brute
        return reponseServeur;
    }

    @Override
    public String getType() {
        return "Capteur d'Humidité";
    }
}

// photoresistance 

class Photoresistance extends Appareil {
    private Queue<String> fileAttente;

    public Photoresistance(String nom, Microcontroleur microcontroleur, Queue<String> fileAttente) {
        super(nom, microcontroleur);
        this.fileAttente = fileAttente;
    }

    @Override
    public String lireDonnees() {
        // Logique pour lire les données de luminosité depuis le serveur HTTP
        String adresseIP = microcontroleur.getAdresseIP();
        String url = "http://" + adresseIP + "/luminosite";
        String reponseServeur = serveurHTTP.envoyerRequeteGET(url);

        // Retourner la réponse brute
        return reponseServeur;
    }

    @Override
    public String getType() {
        return "Photoresistance";
    }
}

class ActionneurLED extends Appareil {
    public ActionneurLED(String nom, Microcontroleur microcontroleur) {
        super(nom, microcontroleur);
    }

    @Override
    public String lireDonnees() {
        // Les actionneurs n'ont pas de fonction pour lire des données
        return "Les actionneurs ne lisent pas de données.";
    }

    @Override
    public String getType() {
        return "Actionneur LED";
    }

    public void allumerLED() {
        // Logique pour allumer la LED via le serveur HTTP
        String adresseIP = microcontroleur.getAdresseIP();
        String url = "http://" + adresseIP + "/allumerLED";
        serveurHTTP.envoyerRequetePOST(url);
    }

    public void eteindreLED() {
        // Logique pour éteindre la LED via le serveur HTTP
        String adresseIP = microcontroleur.getAdresseIP();
        String url = "http://" + adresseIP + "/eteindreLED";
        serveurHTTP.envoyerRequetePOST(url);
    }
}

class ActionneurPorte extends Appareil {
    public ActionneurPorte(String nom, Microcontroleur microcontroleur) {
        super(nom, microcontroleur);
    }

    @Override
    public String lireDonnees() {
       
        return "Les actionneurs de porte ne lisent pas de données.";
    }

    @Override
    public String getType() {
        return "Actionneur de Porte";
    }

    public void ouvrirPorte() {
        // Logique pour ouvrir la porte via le serveur HTTP
        String adresseIP = microcontroleur.getAdresseIP();
        String url = "http://" + adresseIP + "/ouvrirPorte";
        serveurHTTP.envoyerRequetePOST(url);
    }

    public void fermerPorte() {
        // Logique pour fermer la porte via le serveur HTTP
        String adresseIP = microcontroleur.getAdresseIP();
        String url = "http://" + adresseIP + "/fermerPorte";
        serveurHTTP.envoyerRequetePOST(url);
    }
}
