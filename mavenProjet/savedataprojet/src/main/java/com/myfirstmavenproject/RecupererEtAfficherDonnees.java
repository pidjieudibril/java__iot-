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
    private static Queue<String> fileAttente = new LinkedList<>();
    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static Microcontroleur microcontroleur = new Microcontroleur("192.168.1.124");
    private static CapteurTemperature capteurTemperature = new CapteurTemperature("CapteurTemp1", microcontroleur, fileAttente);
    private static Photoresistance photoresistance = new Photoresistance("Photoresistance1", microcontroleur, fileAttente);
    private static CapteurHumidite capteurHumidite = new CapteurHumidite("CapteurHumidite1", microcontroleur, fileAttente);

    public static void recupererEtAfficherDonnees(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        int choix = -1;

        while (choix != 0) {
            System.out.println("Choisissez une option :");
            System.out.println("1. Afficher les données du capteur");
            System.out.println("2. Contrôler l'actionneur LED");
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
            String temperature = capteurTemperature.lireDonnees() + " °C";
            String luminosite = photoresistance.lireDonnees() + " %";
            String humidite = capteurHumidite.lireDonnees() + " %";
            System.out.println("Température (" + microcontroleur.getAdresseIP() + ") : " + temperature);
            System.out.println("Luminosité (" + microcontroleur.getAdresseIP() + ") : " + luminosite);
            System.out.println("Humidité (" + microcontroleur.getAdresseIP() + ") : " + humidite);
            enregistrerDonneesDansBaseDeDonnees(connection, temperature, luminosite, humidite);
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
            ActionneurLED actionneurLED = new ActionneurLED("LED1", microcontroleur);
            actionneurLED.allumerLED();
            System.out.println("LED allumée !");
            enregistrerDonneesActionneurDansBaseDeDonnees(connection, "Allumée");
        } else if (action == 2) {
            // Éteindre LED
            ActionneurLED actionneurLED = new ActionneurLED("LED1", microcontroleur);
            actionneurLED.eteindreLED();
            System.out.println("LED éteinte !");
            enregistrerDonneesActionneurDansBaseDeDonnees(connection, "Éteinte");
        } else {
            System.out.println("Option invalide !");
        }
    }
    
//enregistrer les donnees du capteur dans la base de donnees 
    private static void enregistrerDonneesDansBaseDeDonnees(Connection connection, String temperature, String luminosite, String humidite) {
        String query = "INSERT INTO donnees_appareils (donnee, timestamp, nom_app, adresse_ip) VALUES (?, CURRENT_TIMESTAMP, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Insérer la température
            preparedStatement.setString(1, temperature);
            preparedStatement.setString(2, "Capteur Am2302 Temperature1");
            preparedStatement.setString(3, microcontroleur.getAdresseIP()); 
            preparedStatement.executeUpdate();

            // Insérer la luminosité
            preparedStatement.setString(1, luminosite);
            preparedStatement.setString(2, "Photoresistance1");
            preparedStatement.setString(3, microcontroleur.getAdresseIP()); 
            preparedStatement.executeUpdate();

            // Insérer l'humidité
            preparedStatement.setString(1, humidite);
            preparedStatement.setString(2, "Capteur Am2302 Humidite1");
            preparedStatement.setString(3, "192.168.1.124"); 
            preparedStatement.executeUpdate();
            
            System.out.println("Données enregistrées avec succès dans la base de données.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion des données dans la base de données : " + e.getMessage());
        }
    }
// enregistrer l'etat de la led dans la base de donnees 
private static void enregistrerDonneesActionneurDansBaseDeDonnees(Connection connection, String etatLED) {
    String query = "INSERT INTO donnees_actionneur (etat, timestamp, nom_app, adresse_ip) VALUES (?, CURRENT_TIMESTAMP, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        // Insérer l'état de la LED
        preparedStatement.setString(1, etatLED);
        preparedStatement.setString(2, "LED");
        preparedStatement.setString(3, "192.168.1.124"); 
        preparedStatement.executeUpdate();
        System.out.println("Données de l'actionneur enregistrées avec succès dans la base de données.");
    } catch (SQLException e) {
        System.err.println("Erreur lors de l'insertion des données de l'actionneur dans la base de données : " + e.getMessage());
    }
}


}
