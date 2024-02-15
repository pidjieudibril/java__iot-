package com.myfirstmavenproject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;



public class Appareil {
    protected int id;
    private static int prochainId = 1;

    // Constructeur pour initialiser l'ID automatiquement
    public Appareil() {
        this.id = prochainId++;
    }

    public int getId() {
        return id;
    }

    public void simulerDonnee() {
        // Méthode pour simuler les données
    }
}


class Capteur extends Appareil {
    private String uniteMesure;

    // Ajout d'un constructeur prenant l'identifiant et l'unité de mesure
    public Capteur(int id, String uniteMesure) {
        this.id = id;
        this.uniteMesure = uniteMesure;
    }

    public Capteur() {
        super(); // Appel au constructeur par défaut de la classe Appareil
    }

   @Override
   public void simulerDonnee() {
       Random random = new Random();
       int valeurMesure = random.nextInt(101); // Valeur aléatoire entre 0 et 100
       String donnee = String.valueOf(valeurMesure) + " " + uniteMesure;
   
       // Enregistrer la donnée simulée dans la file
       SimulationDonnee.enregistrerDonneeCapteur(this.getId(), donnee);
       // Enregistrer la donnée simulée dans la base de données
       try {
           Connection connection = DatabaseConnection.getConnection();
           SimulationDonnee.enregistrerDonneeCapteur( this.getId(), donnee);
           DatabaseConnection.closeConnection(connection);
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }
   
}


 class Actionneur extends Appareil {
    private String action;

    // Constructeur pour initialiser l'ID automatiquement
    public Actionneur() {
        // L'appel à super() est implicite s'il n'est pas spécifié
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public void simulerDonnee() {
        Random random = new Random();
        boolean etat = random.nextBoolean(); // État aléatoire (true/false)
        String donnee = "Action: " + action + ", Etat: " + etat;
    
        // Enregistrer la donnée simulée dans la file
        SimulationDonnee.enregistrerDonneeActionneur(this.getId(), donnee);
        // Enregistrer la donnée simulée dans la base de données
        try {
            Connection connection = DatabaseConnection.getConnection();
            SimulationDonnee.enregistrerDonneeActionneur( this.getId(), donnee);
            DatabaseConnection.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
