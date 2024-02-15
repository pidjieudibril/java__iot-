package com.myfirstmavenproject;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Menu {

    private static final int MAX_DATA_COUNT = 30;

    public static void mainMenu(Connection connection) {
        Scanner myscanner = new Scanner(System.in);

        int choice;

        do {

            System.out.println("*************************");
            System.out.println("        Menu :");
            System.out.println("*************************");
            System.out.printf("%-2s %-22s %s%n", "1 :", "Ajouter un appareils", "*");
            System.out.printf("%-2s %-22s %s%n", "2 :", "afficher les appareils", "*");
            System.out.printf("%-2s %-22s %s%n", "3 :", "mettre a jour un appareils", "*");
            System.out.printf("%-2s %-22s %s%n", "4 :", "supprimer um appareils", "*");
            System.out.printf("%-2s %-22s %s%n", "5 :", "Afficher la file et la vider", "*");
            System.out.printf("%-2s %-22s %s%n", "6 :", "Quitter le programme", "*");
            System.out.println("*************************");
          
            System.out.print("Choisissez une option : ");
            try{
            choice = myscanner.nextInt();
            myscanner.nextLine(); 

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

                case 5:
                    afficherEtViderFile();
                    break;
                case 6:
                    DatabaseConnection.closeConnection(connection);
                    System.out.println("Programme terminé");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Option non valide, veiller choisir les option du menu ");
            }
        }catch (InputMismatchException e ) {
            System.out.println("Entrée invalide, veuillez saisir un nombre");
                myscanner.nextLine(); // Consommer la nouvelle ligne pour éviter une boucle infinie
                choice = 0; // Réinitialiser choice pour continuer la boucle
          
        }
        }while (choice != 6);
        myscanner.close();
    }
/* 
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
*/private static void ajouterAppareil(Connection connection) {
    Scanner myscanner = new Scanner(System.in);
    System.out.print("Entrez le nom de l'appareil : ");
    String name = myscanner.nextLine();

    System.out.print("Entrez le type de l'appareil : ");
    String type = myscanner.nextLine();

    System.out.print("Quel est l'état de fonctionnement ?  ");
    String etatFonctionnement = myscanner.nextLine();

    // Appel à la méthode pour ajouter un appareil
    Traitement.ajouterAppareil(connection, name, type, etatFonctionnement);

    // Maintenant, vérifiez si cet appareil est un capteur ou un actionneur
    if (type.equalsIgnoreCase("capteur")) {
        // Si c'est un capteur, simulez ses données
        Capteur capteur = new Capteur(1,"unite");
        capteur.simulerDonnee();
    } else if (type.equalsIgnoreCase("actionneur")) {
        // Si c'est un actionneur, simulez ses données
        Actionneur actionneur = new Actionneur();
        actionneur.simulerDonnee();
    } else {
        System.out.println("Type d'appareil non pris en charge pour la simulation de données.");
    }

    // Maintenant, affichez la file de données simulées
    SimulationDonnee.afficherFile();
}

    private static void MetreAjourAppreil(Connection connection) {
        Scanner myscanner = new Scanner(System.in);
        System.out.print("Entrez le nom de l'appareil dont vous souhaitez mettre à jour les informations : ");
        String name = myscanner.nextLine();
        if(!Traitement.appareilExiste(connection, name)){
            System.out.println("l'appareil \"" +name + "\" n'existe pas, impossible de faire la mise a jour ");
            return;
        }

         System.out.print("Entrez le nouveau nom de l'appareil : ");
        String newName = myscanner.nextLine();
        System.out.print("Entrez le nouveau type de l'appareil : ");
        String newType = myscanner.nextLine();

        System.out.print("Quel est le nouvel état de fonctionnement ? ");
        String newEtatFonctionnement = myscanner.nextLine();

        
        myscanner.nextLine(); // Consommer la nouvelle ligne

        Traitement.MetreAjourAppreil(connection,name, newName, newType, newEtatFonctionnement);
    }
// methode pour effectuer la suppresion des donnees dans la base de donnee
  private static void supprimerAppareil(Connection connection) {
    Scanner myscanner = new Scanner(System.in);
    System.out.print("Entrer le nom de l'appareil que vous voulez supprimer : ");
    String name = myscanner.nextLine();

    // on verifie si l'appareil existe et s/il y en a avec plusieur nom 
    List<Integer> appareilIds = Traitement.getAppareilIds(connection, name);
    if (appareilIds.isEmpty()) {
        System.out.println("L'appareil \"" + name + "\" n'existe pas, pas de suppression possible");
        return;
    } else if (appareilIds.size() > 1) {
        // Afficher la liste des appareils avec leur identifiant
        System.out.println("Plusieurs appareils trouvés avec le nom \"" + name + "\":");
        for (Integer id : appareilIds) {
            System.out.println("ID : " + id);
        }
        // Demander à l'utilisateur de choisir l'appareil à supprimer par son identifiant
        System.out.print("Veuillez saisir l'identifiant de l'appareil que vous souhaitez supprimer : ");
        int selectedId = myscanner.nextInt();
        myscanner.nextLine(); // Consommer la nouvelle ligne
        // Demander la confirmation
        System.out.print("Confirmez-vous la suppression de l'appareil (oui/non) ? ");
        String confirmation = myscanner.nextLine();
        if (confirmation.equalsIgnoreCase("oui")) {
            Traitement.supprimerAppareil(connection, selectedId);
        } else {
            System.out.println("la suppression a été annulee");
        }
    } else {
        // Demander confirmation pour un seul appareil
        System.out.print("Confirmez-vous la suppression de l'appareil \"" + name + "\" (oui/non) ? ");
        String confirmation = myscanner.nextLine();
        if (confirmation.equalsIgnoreCase("oui")) {
            Traitement.supprimerAppareil(connection, appareilIds.get(0));
        } else {
            System.out.println("la suppression a été annulee");
        }
    }
}
private static void afficherEtViderFile() {
    // Méthode pour afficher et vider la pile
    SimulationDonnee.afficherFile();
   
}

}
