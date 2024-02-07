package com.myfirstmavenproject;

import java.sql.Connection;
import java.util.Scanner;

public class Menu {

    public static void mainMenu(Connection connection) {
        Scanner myscanner = new Scanner(System.in);

        while (true) {




            System.out.println("*************************");
            System.out.println("        Menu :");
            System.out.println("*************************");
            System.out.printf("%-2s %-22s %s%n", "1 :", "Ajouter un appareils", "*");
            System.out.printf("%-2s %-22s %s%n", "2 :", "afficher les appareils", "*");
            System.out.printf("%-2s %-22s %s%n", "3 :", "mettre a jour un appareils", "*");
            System.out.printf("%-2s %-22s %s%n", "4 :", "supprimer um appareils", "*");
            System.out.printf("%-2s %-22s %s%n", "5 :", "Quitter le programme", "*");
            System.out.println("*************************");
          
            System.out.print("choisissez une option : ");
            int choice = myscanner.nextInt();
            myscanner.nextLine(); // Consommer la nouvelle ligne

            switch (choice) {
                case 1:
                    ajouterAppareil(connection);
                    break;
                case 2:
                    Traitement.afficherAppareil(connection);
                    break;
                case 3:
                    MetreAjourAppreil(connection);
                    break;
                case 4:
                    supprimerAppareil(connection);
                    break;
                case 5:
                    DatabaseConnection.closeConnection(connection);
                    System.out.println("Programme terminé");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Option non valide, faut  choisir à nouveau");
            }
        }
    }

    private static void ajouterAppareil(Connection connection) {
        Scanner myscanner = new Scanner(System.in);
        System.out.print("Entrez le nom de l'appareil : ");
        String name = myscanner.nextLine();

        System.out.print("Entrez le type de l'appareil : ");
        String type = myscanner.nextLine();

        System.out.print("Quel est l'état de fonctionnement ?  ");
        String etatFonctionnement = myscanner.nextLine();

    
        myscanner.nextLine(); // Consommer la nouvelle ligne

        Traitement.ajouterAppareil(connection, name, type, etatFonctionnement);
    }

    private static void MetreAjourAppreil(Connection connection) {
        Scanner myscanner = new Scanner(System.in);
        System.out.print("Entrez le nom de l'appareil dont vous souhaitez mettre à jour les informations : ");
        String name = myscanner.nextLine();
        if(!Traitement.appareilExiste(connection, name)){
            System.out.println("l'appareil \"" +name + "\" n'existe pas, impossible de faire la mise a jour ");
            return;
        }


        System.out.print("Entrez le nouveau type de l'appareil : ");
        String newType = myscanner.nextLine();

        System.out.print("Quel est le nouvel état de fonctionnement ? ");
        String newEtatFonctionnement = myscanner.nextLine();

        
        myscanner.nextLine(); // Consommer la nouvelle ligne

        Traitement.MetreAjourAppreil(connection, name, newType, newEtatFonctionnement);
    }

    private static void supprimerAppareil(Connection connection) {
        Scanner myscanner = new Scanner(System.in);
        System.out.print("Entrez le nom de l'appareil que vous souhaitez supprimer : ");
        String name = myscanner.nextLine();

         if(!Traitement.appareilExiste(connection, name)){
            System.out.println("l'appareil \"" +name + "\" n'existe pas, impossible de faire la suppression  ");
             return;
         }

        Traitement.supprimerAppareil(connection, name);
    }
}
