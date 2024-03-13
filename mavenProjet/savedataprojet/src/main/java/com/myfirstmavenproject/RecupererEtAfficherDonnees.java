// RecupererEtAfficherDonnees.java
package com.myfirstmavenproject;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.sql.Connection;

public class RecupererEtAfficherDonnees {
     // File d'attente pour stocker les données des capteurs
     private static Queue<String> fileAttente = new LinkedList<>();

     // Scheduler pour planifier les tâches récurrentes
     private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
 
     // Première instance de microcontrôleur
     private static Microcontroleur microcontroleur1 = new Microcontroleur("192.168.1.124");
 
     // Deuxième instance de microcontrôleur
     private static Microcontroleur microcontroleur2 = new Microcontroleur("192.168.1.137");
 
     // Capteur de température pour le premier microcontrôleur
     private static CapteurTemperature capteurTemperature1 = new CapteurTemperature("CapteurTemp1", microcontroleur1, fileAttente);
     private static Photoresistance photoresistance = new Photoresistance("Photoresistance1", microcontroleur1, fileAttente);
     private static CapteurHumidite capteurHumidite = new CapteurHumidite("CapteurHumidite1", microcontroleur1, fileAttente);
 
 
     // Capteur de température pour le deuxième microcontrôleur
     private static CapteurTemperature capteurTemperature2 = new CapteurTemperature("CapteurTemp2", microcontroleur2, fileAttente);
 
     // Actionneur de porte pour le deuxième microcontrôleur
     private static ActionneurPorte actionneurPorte = new ActionneurPorte("Porte1", microcontroleur2);
 
     // Méthode principale pour récupérer et afficher les données
     public static void recupererEtAfficherDonnees(Connection connection) {
         Scanner scanner = new Scanner(System.in);
         int choix = -1;

        while (choix != 0) {
            System.out.println("Choisissez une option :");
            System.out.println("1. Afficher les données du capteur");
            System.out.println("2. Contrôler l'actionneur LED");
            System.out.println("3. Contrôler l'actionneur de porte");
            System.out.println("0. Quitter");

            choix = scanner.nextInt();
            scanner.nextLine(); // Pour consommer le retour à la ligne

            switch (choix) {
                case 1:
                afficherDonneesCapteur(connection);
                    break;
                case 2:
                    controlerActionneurLED(scanner, connection);
                    break;
                  case 3:
                  controlerActionneurPorte(scanner, connection);
                    break;
                case 0:
                    System.out.println("Programme quitté.");
                    break;
                default:
                    System.out.println("Option invalide !");
                    break;
            }
        }
    }

    private static void afficherDonneesCapteur(Connection connection) {
        scheduler.scheduleAtFixedRate(() -> {
            String temperature1 = capteurTemperature1.lireDonnees() + " °C";
            String temperature2 = capteurTemperature2.lireDonnees() + " °C";
            String luminosite = photoresistance.lireDonnees() + " %";
            String humidite = capteurHumidite.lireDonnees() + " %";
            System.out.println("Température (" + microcontroleur1.getAdresseIP() + ") : " + temperature1);
            System.out.println("Température (" + microcontroleur2.getAdresseIP() + ") : " + temperature2);
            System.out.println("Luminosité (" + microcontroleur1.getAdresseIP() + ") : " + luminosite);
            System.out.println("Humidité (" + microcontroleur1.getAdresseIP() + ") : " + humidite);
            enregistrerDonneesDansBaseDeDonnees(connection, temperature1,temperature2, luminosite, humidite);
        }, 0, 5, TimeUnit.SECONDS);

        // Afficher les données du capteur
        for (String temp : fileAttente) {
            System.out.println(temp);
        }
    }

    private static void controlerActionneurLED(Scanner scanner, Connection connection) {
        System.out.println("Choisissez une action pour l'actionneur LED :");
        System.out.println("1. Allumer LED");
        System.out.println("2. Éteindre LED");
    
        int action = scanner.nextInt();
        scanner.nextLine(); // Pour consommer le retour à la ligne
    
        if (action == 1) {
            // Allumer LED
            ActionneurLED actionneurLED = new ActionneurLED("LED1", microcontroleur1);
            actionneurLED.allumerLED();
            System.out.println("LED allumée !");
            enregistrerDonneesActionneurLedDansBaseDeDonnees(connection, "Allumée");
        } else if (action == 2) {
            // Éteindre LED
            ActionneurLED actionneurLED = new ActionneurLED("LED1", microcontroleur1 );
            actionneurLED.eteindreLED();
            System.out.println("LED éteinte !");
            enregistrerDonneesActionneurLedDansBaseDeDonnees(connection, "Éteinte");
        } else {
            System.out.println("Option invalide !");
        }
    }


     // Méthode pour contrôler l'actionneur de porte
     private static void controlerActionneurPorte(Scanner scanner, Connection connection) {
        System.out.println("Choisissez une action pour l'actionneur de porte :");
        System.out.println("1. Ouvrir la porte");
        System.out.println("2. Fermer la porte");

        int action = scanner.nextInt();
        scanner.nextLine(); // Pour consommer le retour à la ligne

        if (action == 1) {
            // Ouvrir la porte
            actionneurPorte.ouvrirPorte();
            System.out.println("Porte ouverte !");
            // Enregistre l'état de l'actionneur dans la base de données
            enregistrerDonneesActionneurPorteDansBaseDeDonnees(connection, "Ouverte");
        } else if (action == 2) {
            // Fermer la porte
            actionneurPorte.fermerPorte();
            System.out.println("Porte fermée !");
            // Enregistre l'état de l'actionneur dans la base de données
            enregistrerDonneesActionneurPorteDansBaseDeDonnees(connection, "Fermée");
        } else {
            System.out.println("Option invalide !");
        }
    }
    
//enregistrer les donnees du capteur dans la base de donnees 
    private static void enregistrerDonneesDansBaseDeDonnees(Connection connection, String temperature1,String temperature2, String luminosite, String humidite) {
        String query = "INSERT INTO donnees_appareils (donnee, timestamp, nom_app, adresse_ip) VALUES (?, CURRENT_TIMESTAMP, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Insérer la température
            preparedStatement.setString(1, temperature1);
            preparedStatement.setString(2, "Capteur Am2302 Temperature1");
            preparedStatement.setString(3, microcontroleur1.getAdresseIP()); 
            preparedStatement.executeUpdate();

             preparedStatement.setString(1, temperature2);
             preparedStatement.setString(2, "Capteur lm35 Temperature");
             preparedStatement.setString(3, microcontroleur2.getAdresseIP()); 
             preparedStatement.executeUpdate();

            // Insérer la luminosité
            preparedStatement.setString(1, luminosite);
            preparedStatement.setString(2, "Photoresistance1");
            preparedStatement.setString(3, microcontroleur1.getAdresseIP()); 
            preparedStatement.executeUpdate();

            // Insérer l'humidité
            preparedStatement.setString(1, humidite);
            preparedStatement.setString(2, "Capteur Am2302 Humidite1");
            preparedStatement.setString(3, microcontroleur1.getAdresseIP()); 
            preparedStatement.executeUpdate();
            
            System.out.println("Données enregistrées avec succès dans la base de données.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion des données dans la base de données : " + e.getMessage());
        }
    }
// enregistrer l'etat de la led dans la base de donnees 
private static void enregistrerDonneesActionneurLedDansBaseDeDonnees(Connection connection, String etatLED ) {
    String query = "INSERT INTO donnees_actionneur (etat, timestamp, nom_app, adresse_ip) VALUES (?, CURRENT_TIMESTAMP, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        // Insérer l'état de la LED
        preparedStatement.setString(1, etatLED);
        preparedStatement.setString(2, "LED");
        preparedStatement.setString(3, microcontroleur1.getAdresseIP()); 
        preparedStatement.executeUpdate();

        System.out.println("Données de l'actionneur enregistrées avec succès dans la base de données.");
    } catch (SQLException e) {
        System.err.println("Erreur lors de l'insertion des données de l'actionneur dans la base de données : " + e.getMessage());
    }
}


// enregistrer l'etat de la porte 

private static void enregistrerDonneesActionneurPorteDansBaseDeDonnees(Connection connection, String etatPorte ) {
    String query = "INSERT INTO donnees_actionneur (etat, timestamp, nom_app, adresse_ip) VALUES (?, CURRENT_TIMESTAMP, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
      
        // Inserer l'état de la porte 
          preparedStatement.setString(1, etatPorte);
          preparedStatement.setString(2, "porte");
         preparedStatement.setString(3, microcontroleur2.getAdresseIP()); 
        preparedStatement.executeUpdate();
        System.out.println("Données de l'actionneur enregistrées avec succès dans la base de données.");
    } catch (SQLException e) {
        System.err.println("Erreur lors de l'insertion des données de l'actionneur dans la base de données : " + e.getMessage());
    }
}

}
