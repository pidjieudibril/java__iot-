package com.myfirstmavenproject;

import java.sql.Connection;
import java.util.InputMismatchException;

import java.util.List;

import java.util.Scanner;





public class Menu {

    public static void mainMenu(Connection connection) {
        Scanner scanner = new Scanner(System.in);

        int choice;

        do {

            System.out.println("*************************");
            System.out.println("        Menu :");
            System.out.println("*************************");
            System.out.printf("%-2s %-22s %s%n", "1 :", "Ajouter un microcontrôleur", "*");
            System.out.printf("%-2s %-22s %s%n", "2 :", "Ajouter un appareil", "*");
            System.out.printf("%-2s %-22s %s%n", "3 :", "Afficher les appareils", "*");
             System.out.printf("%-2s %-22s %s%n", "4 :", "recuperer et afficher donnees ", "*");
            System.out.printf("%-2s %-22s %s%n", "5:", "Afficher les données d'un appareil", "*");
            System.out.printf("%-2s %-22s %s%n", "6 :", "Mettre à jour un appareil", "*");
            System.out.printf("%-2s %-22s %s%n", "7 :", "Supprimer un appareil", "*");
            System.out.printf("%-2s %-22s %s%n", "8 :", "Mettre à jour un microcontrôleur", "*");
            System.out.printf("%-2s %-22s %s%n", "9 :", "Supprimer un microcontrôleur", "*");
            System.out.printf("%-2s %-22s %s%n", "10 :", "Quitter le programme", "*");
            System.out.println("*************************");

            System.out.print("Choisissez une option : ");
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); 

                switch (choice) {
                    case 1:
                        ajouterMicrocontroleur(connection);
                        break;
                    case 2:
                        ajouterAppareil(connection);
                        break;
                    case 3:
                        afficherAppareils(connection);
                        break;

                    case 4:
                  
                    RecupererEtAfficherDonnees.recupererEtAfficherDonnees(connection);
                   
                
                   

                        break;
                    
                    case 5:
                        afficherDonneesAppareil(connection);
                        break;
                    case 6:
                        mettreAJourAppareil(connection);
                        break;
                    case 7:

                        supprimerAppareil(connection);
                        break;
                    case 8:
                        mettreAJourMicrocontroleur(connection);
                        break;
                    case 9:
                        supprimerMicrocontroleur(connection);
                        break;
                    case 10:
                        DatabaseConnection.closeConnection(connection);
                        System.out.println("Programme terminé");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Option non valide, veuillez choisir une option du menu");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrée invalide, veuillez saisir un nombre");
                scanner.nextLine(); // Consommer la nouvelle ligne pour éviter une boucle infinie
                choice = 0; // Réinitialiser choice pour continuer la boucle
            }
        } while (choice != 9);
        scanner.close();
    }
// methode pour ajouter les microcontroleur 
    private static void ajouterMicrocontroleur(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez le nom du microcontrôleur : ");
        String nom = scanner.nextLine();
        String adresseIP;
        do {
            System.out.print("Entrez l'adresse IP du microcontrôleur : ");
            adresseIP = scanner.nextLine();
            if (!validerAdresseIP(adresseIP)) {
                System.out.println("Adresse IP invalide. Veuillez entrer une adresse IP valide.");
            }
        } while (!validerAdresseIP(adresseIP));
    
        Traitement.ajouterMicrocontroleur(connection, nom, adresseIP);
        System.out.println("Microcontrôleur ajouté avec succès.");
    }
    
// methode pour ajouter les appareil dans la base de donnee
    private static void ajouterAppareil(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choisissez le microcontrôleur auquel l'appareil sera lié :\n ");
        afficherMicrocontroleurs(connection);
        int choixMicrocontroleur = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        Microcontroleur microcontroleur = Traitement.getMicrocontroleurById(connection, choixMicrocontroleur);
        if (microcontroleur != null) {
            System.out.print("Entrez le nom de l'appareil : ");
            String nomAppareil = scanner.nextLine();
            String typeAppareil;
            do {
                System.out.print("Entrez le type de l'appareil (capteur/actionneur) : ");
                typeAppareil = scanner.nextLine().toLowerCase(); // Convertir en minuscules pour comparer
                if (!typeAppareil.equals("capteur") && !typeAppareil.equals("actionneur")) {
                    System.out.println("Type d'appareil invalide. Veuillez entrer capteur ou actionneur.");
                }
            } while (!typeAppareil.equals("capteur") && !typeAppareil.equals("actionneur"));
    
            System.out.print("Entrez l'état de fonctionnement de l'appareil : ");
            String etatFonctionnement = scanner.nextLine();
            Traitement.ajouterAppareil(connection, nomAppareil, typeAppareil, etatFonctionnement, microcontroleur.getId());
        } else {
            System.out.println("Microcontrôleur non trouvé.");
        }
    }
    
// methode pour afficharge des appareils 
    private static void afficherAppareils(Connection connection) {
        Traitement.afficherAppareils(connection);
    }

    // methode pour afficher les donnees des appareils 
    private static void afficherDonneesAppareil(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrez le nom de l'appareil dont vous souhaitez voir les données : ");
        String nomAppareil = scanner.nextLine();
        Traitement.afficherDonneesAppareil(connection, nomAppareil);
    }

    // Méthode pour mettre à jour un appareil
    private static void mettreAJourAppareil(Connection connection) {
        Traitement.mettreAJourAppareil(connection);
    }

    // Méthode pour supprimer un appareil
    private static void supprimerAppareil(Connection connection) {
        Traitement.supprimerAppareil(connection);
    }

    // Méthode pour mettre à jour un microcontrôleur
    private static void mettreAJourMicrocontroleur(Connection connection) {
        Traitement.mettreAJourMicrocontroleur(connection);
    }

    // Méthode pour supprimer un microcontrôleur
    private static void supprimerMicrocontroleur(Connection connection) {
        Traitement.supprimerMicrocontroleur(connection);
    }

    private static void afficherMicrocontroleurs(Connection connection) {
        List<Microcontroleur> microcontroleurs = Traitement.getMicrocontroleurs(connection);
        for (Microcontroleur microcontroleur : microcontroleurs) {
            System.out.println("ID : " + microcontroleur.getId() + " | Nom  : " + microcontroleur.getNom()+" | Adresse IP : " + microcontroleur.getAdresseIP());
        }
    }
// methode pour la verification de la validité de l'adresse ip 
    private static boolean validerAdresseIP(String adresseIP) {
        String pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return adresseIP.matches(pattern);
    }
}
