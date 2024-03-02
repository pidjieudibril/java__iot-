package com.myfirstmavenproject;

public class DonneeReel {
    protected int id;
    private static int prochainId = 1;

    // Constructeur pour initialiser l'ID automatiquement
    public DonneeReel() {
        this.id = prochainId++;
    }

    public int getId() {
        return id;
    }

    // Méthode pour recevoir les données via HTTP
    public void recevoirDonneeHTTP(String donnee) {
        // À implémenter pour traiter les données reçues via HTTP
    }
    
}


class Capteur extends DonneeReel {
    private String type;

    // Constructeur pour initialiser l'ID et le type de capteur
    public Capteur(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public Capteur() {
        super(); // Appel au constructeur par défaut de la classe Appareil
    }

    @Override
    public void recevoirDonneeHTTP(String donnee) {
        // Traitement des données reçues via HTTP pour le capteur
        // Stockage des données dans une file ou une base de données
    }
}

class Actionneur extends DonneeReel {
    private String type;

    // Constructeur pour initialiser l'ID et le type d'actionneur
    public Actionneur(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public Actionneur() {
        super(); // L'appel à super() est implicite s'il n'est pas spécifié
    }

    @Override
    public void recevoirDonneeHTTP(String donnee) {
        // Traitement des données reçues via HTTP pour l'actionneur
        // Stockage des données dans une file ou une base de données
    }
}