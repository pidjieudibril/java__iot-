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
            System.out.printf("%-2s %-22s %s%n", "4 :", "Afficher les données d'un appareil", "*");
            System.out.printf("%-2s %-22s %s%n", "5 :", "Mettre à jour un appareil", "*");
            System.out.printf("%-2s %-22s %s%n", "6 :", "Supprimer un appareil", "*");
            System.out.printf("%-2s %-22s %s%n", "7 :", "Mettre à jour un microcontrôleur", "*");
            System.out.printf("%-2s %-22s %s%n", "8 :", "Supprimer un microcontrôleur", "*");
            System.out.printf("%-2s %-22s %s%n", "9 :", "Quitter le programme", "*");
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
                        afficherDonneesAppareil(connection);
                        break;
                    case 5:
                        mettreAJourAppareil(connection);
                        break;
                    case 6:
                        supprimerAppareil(connection);
                        break;
                    case 7:
                        mettreAJourMicrocontroleur(connection);
                        break;
                    case 8:
                        supprimerMicrocontroleur(connection);
                        break;
                    case 9:
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
    

    private static void ajouterAppareil(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choisissez le microcontrôleur auquel l'appareil sera lié : ");
        afficherMicrocontroleurs(connection);
        int choixMicrocontroleur = scanner.nextInt();
        scanner.nextLine(); // Consommer la nouvelle ligne
        Microcontroleur microcontroleur = Traitement.getMicrocontroleurById(connection, choixMicrocontroleur);
        if (microcontroleur != null) {
            System.out.print("Entrez le nom de l'appareil : ");
            String nomAppareil = scanner.nextLine();
            System.out.print("Entrez le type de l'appareil : ");
            String typeAppareil = scanner.nextLine();
            Traitement.ajouterAppareil(connection, nomAppareil, typeAppareil, microcontroleur.getId());
        } else {
            System.out.println("Microcontrôleur non trouvé.");
        }
    }

    private static void afficherAppareils(Connection connection) {
        Traitement.afficherAppareils(connection);
    }

    private static void afficherDonneesAppareil(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez le nom de l'appareil dont vous voulez afficher les données : ");
        String nomAppareil = scanner.nextLine();
        List<Integer> ids = Traitement.getAppareilIds(connection, nomAppareil);
        if (!ids.isEmpty()) {
            if (ids.size() > 1) {
                System.out.println("Plusieurs appareils trouvés avec ce nom. Choisissez l'ID de l'appareil : ");
                for (Integer id : ids) {
                    System.out.println("ID : " + id);
                }
                int choixId = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne
                Traitement.afficherDonneesAppareil(connection, choixId);
            } else {
                Traitement.afficherDonneesAppareil(connection, ids.get(0));
            }
        } else {
            System.out.println("Aucun appareil trouvé avec ce nom.");
        }
    }

    private static void mettreAJourAppareil(Connection connection) {
        // À compléter
    }

    private static void supprimerAppareil(Connection connection) {
        // À compléter
    }

    private static void mettreAJourMicrocontroleur(Connection connection) {
        // À compléter
    }

    private static void supprimerMicrocontroleur(Connection connection) {
        // À compléter
    }

    private static void afficherMicrocontroleurs(Connection connection) {
        List<Microcontroleur> microcontroleurs = Traitement.getMicrocontroleurs(connection);
        for (Microcontroleur microcontroleur : microcontroleurs) {
            System.out.println("ID : " + microcontroleur.getId() + " NOM : " + microcontroleur.getNom()+" | Adresse IP : " + microcontroleur.getAdresseIP());
        }
    }
// methode pour la verification de la validité de l'adresse ip 
    private static boolean validerAdresseIP(String adresseIP) {
        String pattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return adresseIP.matches(pattern);
    }
}
