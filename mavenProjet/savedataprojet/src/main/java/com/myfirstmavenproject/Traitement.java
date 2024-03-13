package com.myfirstmavenproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Traitement {

    private static Queue<Donnee> fileAttente = new LinkedList<>();
    private static List<Microcontroleur> microcontroleurs = new ArrayList<>();
    private static Connection connection;

    public static void setConnection(Connection connection) {
        Traitement.connection = connection;
    }
     
    
//methode pour ajouter un microcontroleur dans la base de donnée 
    public static void ajouterMicrocontroleur(Connection connection, String nom, String adresseIP) {
        Microcontroleur microcontroleur = new Microcontroleur(nom, adresseIP); // Créer une instance de Microcontroleur
        microcontroleurs.add(microcontroleur); // Ajouter le microcontrôleur à la liste en mémoire

        // Insérer le microcontrôleur dans la base de données
        String query = "INSERT INTO microcontroleur (nom_microcontroleur, adresse_ip) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, adresseIP);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion du microcontrôleur dans la base de données : " + e.getMessage());
        }
    }

    // methode pour ajouter un appareil dans la base de donnée 
    public static void ajouterAppareil(Connection connection, String nomAppareil,  String typeAppareil, String etatFonctionnement, int idMicrocontroleur) {
        String query = "INSERT INTO appareils (nom_app, type, etat_fonctionnement, adresse_ip) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Remplir les paramètres de la requête préparée
            preparedStatement.setString(1, nomAppareil);
            preparedStatement.setString(2, typeAppareil);
            preparedStatement.setString(3, etatFonctionnement);
            
            // Récupérer l'adresse IP du microcontrôleur correspondant à l'id fourni
            String adresseIP = getAdresseIPMicrocontroleur(connection, idMicrocontroleur);
            preparedStatement.setString(4, adresseIP);
            
            // Exécuter la requête d'insertion
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appareil ajouté à la base de données avec succès.");
            } else {
                System.out.println("Échec de l'ajout de l'appareil à la base de données.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'appareil à la base de données : " + e.getMessage());
        }
    }
    
    // Méthode pour récupérer l'adresse IP du microcontrôleur correspondant à l'id fourni
    private static String getAdresseIPMicrocontroleur(Connection connection, int idMicrocontroleur) throws SQLException {
        String query = "SELECT adresse_ip FROM microcontroleur WHERE id = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idMicrocontroleur);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("adresse_ip");
                }
            }
        }
        
        // Retourner une chaîne vide si aucune adresse IP n'a été trouvée
        return "";
    }
/* 
   // Méthode pour ajouter un appareil à la file d'attente
   public static void ajouterAppareil(Connection connection, String nomAppareil,String etatFonctionnement, String typeAppareil, int idMicrocontroleur) {
    // Ajouter l'appareil dans la file avant de l'enregistrer dans la base de données
    Donnee donnee = new Donnee(nomAppareil ,etatFonctionnement, typeAppareil, idMicrocontroleur);
    ajouterDonnee(donnee);
    System.out.println("Appareil ajouté à la file d'attente.");
}

   // Méthode pour enregistrer les appareils de la file d'attente dans la base de données
   public static void enregistrerAppareils(Connection connection) {
    while (!fileAttente.isEmpty()) {
        Donnee donnee = fileAttente.poll();
        // Appel de la méthode pour enregistrer l'appareil dans la base de données
        enregistrerAppareil(connection, donnee);
    }
}

    // Méthode pour enregistrer un appareil dans la base de données
    private static void enregistrerAppareil(Connection connection, Donnee donnee) {
        // Implémentez la logique pour enregistrer l'appareil dans la base de données
        // Utilisez les informations fournies dans l'objet Donnee
    }
*/
    // Méthode pour afficher les appareils liés à chaque microcontrôleur
   /* public static void afficherAppareils(Connection connection) {
        for (Microcontroleur microcontroleur : microcontroleurs) {
            System.out.println("Microcontrôleur " + microcontroleur.getAdresseIP() + " :");
            List<Appareil> appareils = microcontroleur.getAppareils();
            if (appareils.isEmpty()) {
                System.out.println("Aucun appareil lié à ce microcontrôleur.");
            } else {
                for (Appareil appareil : appareils) {
                    System.out.println("Nom : " + appareil.getNom() + ", Type : " + appareil.getType());
                }
            }
            System.out.println();
        }
    }
*/


public static void afficherAppareils(Connection connection) {
    String queryMicrocontroleurs = "SELECT id, nom_microcontroleur, adresse_ip FROM microcontroleur";

    try (PreparedStatement preparedStatementMicrocontroleurs = connection.prepareStatement(queryMicrocontroleurs);
         ResultSet resultSetMicrocontroleurs = preparedStatementMicrocontroleurs.executeQuery()) {

        while (resultSetMicrocontroleurs.next()) {
            int idMicrocontroleur = resultSetMicrocontroleurs.getInt("id");
            String nomMicrocontroleur = resultSetMicrocontroleurs.getString("nom_microcontroleur");
            String adresseIP = resultSetMicrocontroleurs.getString("adresse_ip");
            System.out.println("Microcontrôleur " + nomMicrocontroleur + ":");
            afficherAppareilsMicrocontroleur(connection, adresseIP);
            System.out.println();
        }

    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération des microcontrôleurs depuis la base de données : " + e.getMessage());
    }
}
private static void afficherAppareilsMicrocontroleur(Connection connection, String adresseIPMicrocontroleur) {
    String queryAppareils = "SELECT nom_app, type, etat_fonctionnement FROM appareils WHERE adresse_ip = ?";

    try (PreparedStatement preparedStatementAppareils = connection.prepareStatement(queryAppareils)) {
        preparedStatementAppareils.setString(1, adresseIPMicrocontroleur);
        try (ResultSet resultSetAppareils = preparedStatementAppareils.executeQuery()) {
            while (resultSetAppareils.next()) {
                String nomAppareil = resultSetAppareils.getString("nom_app");
                String typeAppareil = resultSetAppareils.getString("type");
                String etatFonctionnementAppareil = resultSetAppareils.getString("etat_fonctionnement");
                
              
                System.out.println("Nom : " + nomAppareil + ", Type : " +typeAppareil  + ",  etat de fonctionnement: " +etatFonctionnementAppareil );

            }
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération des appareils du microcontrôleur depuis la base de données : " + e.getMessage());
    }
}

     // Méthode pour récupérer un microcontrôleur par son identifiant
    public static Microcontroleur getMicrocontroleurById(Connection connection, int id) {
        Microcontroleur microcontroleur = null;
        try {
            String query = "SELECT * FROM microcontroleur WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String adresseIP = resultSet.getString("adresse_ip");
                        microcontroleur = new Microcontroleur(adresseIP);
                        microcontroleur.setId(id);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du microcontrôleur : " + e.getMessage());
        }
        return microcontroleur;
    }

    //methode pour bobtenir les identifiants des appareils avec le nom specifier 


        // Méthode pour récupérer tous les microcontrôleurs de la base de données
        public static List<Microcontroleur> getMicrocontroleurs(Connection connection) {
            List<Microcontroleur> microcontroleurs = new ArrayList<>();
            try {
                // Préparation de la requête SQL pour récupérer tous les microcontrôleurs
                String sql = "SELECT * FROM microcontroleur";
                try (PreparedStatement statement = connection.prepareStatement(sql);
                     ResultSet resultSet = statement.executeQuery()) {
                    // Parcours des résultats pour créer les objets Microcontroleur
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        String nom = resultSet.getString("nom_microcontroleur");
                        String adresseIP = resultSet.getString("adresse_ip");
                        Microcontroleur microcontroleur = new Microcontroleur(id,nom, adresseIP);
                        microcontroleurs.add(microcontroleur);
                    }
                }
            } catch (SQLException e) {
                // Gestion des exceptions SQL si nécessaire
                e.printStackTrace();
            }
            return microcontroleurs;
        }
 // Méthode pour obtenir l'adresse IP d'un microcontrôleur à partir de son nom
   
        public static String getAdresseIP(String nomMicrocontroleur) {
            String adresseIP = null;
            String query = "SELECT adresse_ip FROM microcontroleur WHERE nom_microcontroleur = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nomMicrocontroleur);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        adresseIP = resultSet.getString("adresse_ip");
                    }
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la récupération de l'adresse IP du microcontrôleur : " + e.getMessage());
            }
            
            return adresseIP;
        }
        
    // Méthode pour afficher les données d'un appareil spécifique
   





 // Méthode pour afficher les données d'un appareil spécifique
 public static void afficherDonneesAppareil(Connection connection, String nomAppareil) {
    String typeAppareil = getTypeAppareil(connection, nomAppareil);
    
    if (typeAppareil.equals("capteur")) {
        afficherDonneesCapteur(connection, nomAppareil);
    } else if (typeAppareil.equals("actionneur")) {
        afficherEtatActuateur(connection, nomAppareil);
    } else {
        System.out.println("Type d'appareil inconnu.");
    }
}

// Méthode pour obtenir le type de l'appareil (capteur ou actuateur)
private static String getTypeAppareil(Connection connection, String nomAppareil) {
    String type = "";
    String query = "SELECT type FROM appareils WHERE nom_app = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, nomAppareil);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                type = resultSet.getString("type");
            }
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération du type d'appareil : " + e.getMessage());
    }
    return type;
}

// Méthode pour afficher les données d'un capteur
private static void afficherDonneesCapteur(Connection connection, String nomAppareil) {
    String query = "SELECT donnee FROM donnees_appareils WHERE nom_app = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, nomAppareil);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String donnee = resultSet.getString("donnee");
                System.out.println("Type: Capteur, Données: " + donnee);
            }
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération des données du capteur : " + e.getMessage());
    }
}

// Méthode pour afficher l'état d'un actionneur avec l'heure
private static void afficherEtatActuateur(Connection connection, String nomAppareil) {
    String query = "SELECT etat, timestamp FROM donnees_actionneur WHERE nom_app = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, nomAppareil);
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String etat = resultSet.getString("etat");
                String timestamp = resultSet.getString("timestamp");
                System.out.println("Type: Actuateur, État: " + etat + ", Heure: " + timestamp);
            }
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération de l'état de l'actionneur : " + e.getMessage());
    }
}






//METHODE POUR LA MISE A JOUR DES APPAREILS ET MICROCONTROLEUR 


    // Méthode pour mettre à jour un appareil
    public static void mettreAJourAppareil(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrez le nom de l'appareil que vous souhaitez mettre à jour : ");
        String nomAppareil = scanner.nextLine();

        // Demander les nouvelles informations
        System.out.println("Entrez le nouveau type de l'appareil : ");
        String nouveauType = scanner.nextLine();
        System.out.println("Entrez le nouvel état de fonctionnement de l'appareil : ");
        String nouvelEtat = scanner.nextLine();
        System.out.println("Entrez la nouvelle adresse IP de l'appareil : ");
        String nouvelleAdresseIP = scanner.nextLine();

        // Mettre à jour l'appareil dans la base de données
        String query = "UPDATE appareils SET type = ?, etat_fonctionnement = ?, adresse_ip = ? WHERE nom_app = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nouveauType);
            preparedStatement.setString(2, nouvelEtat);
            preparedStatement.setString(3, nouvelleAdresseIP);
            preparedStatement.setString(4, nomAppareil);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("L'appareil '" + nomAppareil + "' a été mis à jour avec succès");
            } else {
                System.out.println("L'appareil '" + nomAppareil + "' n'existe pas");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'appareil : " + e.getMessage());
        }
    }

    // Méthode pour mettre à jour un microcontrôleur
    public static void mettreAJourMicrocontroleur(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrez l'adresse IP du microcontrôleur que vous souhaitez mettre à jour : ");
        String adresseIP = scanner.nextLine();

        // Demander les nouvelles informations
        System.out.println("Entrez le nouveau nom du microcontrôleur : ");
        String nouveauNom = scanner.nextLine();

        // Mettre à jour le microcontrôleur dans la base de données
        String query = "UPDATE microcontroleur SET nom_microcontroleur = ? WHERE adresse_ip = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nouveauNom);
            preparedStatement.setString(2, adresseIP);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Le microcontrôleur avec l'adresse IP '" + adresseIP + "' a été mis à jour avec succès.");
            } else {
                System.out.println("Le microcontrôleur avec l'adresse IP '" + adresseIP + "' n'existe pas.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du microcontrôleur : " + e.getMessage());
        }
    }






// MISE A JOUR ET SUPPRESSION DES APPAREILS ET MICROCONTROLEUR 


 
    // Méthode pour supprimer un appareil
    public static void supprimerAppareil(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrez le nom de l'appareil que vous souhaitez supprimer : ");
        String nomAppareil = scanner.nextLine();

        // Demande de confirmation
        System.out.println("Êtes-vous sûr de vouloir supprimer l'appareil '" + nomAppareil + "' ? (O/N)");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("O")) {
            // Suppression de l'appareil
            String query = "DELETE FROM appareils WHERE nom_app = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nomAppareil);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("L'appareil '" + nomAppareil + "' a été supprimé avec succès");
                } else {
                    System.out.println("L'appareil '" + nomAppareil + "' n'existe pas");
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la suppression de l'appareil : " + e.getMessage());
            }
        } else {
            System.out.println("Suppression annulée.");
        }
    }

  

    // Méthode pour supprimer un microcontrôleur
    public static void supprimerMicrocontroleur(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Entrez l'adresse IP du microcontrôleur que vous souhaitez supprimer : ");
        String adresseIP = scanner.nextLine();

        // Demande de confirmation
        System.out.println("Êtes-vous sûr de vouloir supprimer le microcontrôleur avec l'adresse IP '" + adresseIP + "' ? (O/N)");
        String confirmation = scanner.nextLine();

        if (confirmation.equalsIgnoreCase("O")) {
            // Suppression du microcontrôleur et des données liées
            String query = "DELETE FROM microcontroleur WHERE adresse_ip = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, adresseIP);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Le microcontrôleur avec l'adresse IP '" + adresseIP + "' a été supprimé avec succès.");
                } else {
                    System.out.println("Le microcontrôleur avec l'adresse IP '" + adresseIP + "' n'existe pas.");
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la suppression du microcontrôleur : " + e.getMessage());
            }
        } else {
            System.out.println("Suppression annulée.");
        }
    }
   
    

    
    public static List<Microcontroleur> getMicrocontroleursFromDatabase(Connection connection) {
        List<Microcontroleur> microcontroleurs = new ArrayList<>();

        String query = "SELECT id, nom_microcontroleur, adresse_ip FROM microcontroleur";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom_microcontroleur");
                String adresseIP = resultSet.getString("adresse_ip");

                Microcontroleur microcontroleur = new Microcontroleur(id, nom, adresseIP);
                microcontroleurs.add(microcontroleur);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des microcontrôleurs depuis la base de données : " + e.getMessage());
        }

        return microcontroleurs;
    }

    
    public static void ajouterDonnee(Donnee donnee) {
        fileAttente.add(donnee);
    }


 

}
