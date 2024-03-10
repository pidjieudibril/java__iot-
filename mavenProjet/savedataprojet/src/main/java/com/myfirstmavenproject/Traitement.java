package com.myfirstmavenproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Traitement {
     
     private static Queue<Donnee> fileAttente = new LinkedList<>();
    private static List<Microcontroleur> microcontroleurs = new ArrayList<>();
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
   // Méthode pour ajouter un appareil à la file d'attente
   public static void ajouterAppareil(Connection connection, String nomAppareil, String typeAppareil, int idMicrocontroleur) {
    // Ajouter l'appareil dans la file avant de l'enregistrer dans la base de données
    Donnee donnee = new Donnee(nomAppareil, typeAppareil, idMicrocontroleur);
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

    // Méthode pour afficher les appareils liés à chaque microcontrôleur
    public static void afficherAppareils(Connection connection) {
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


     // Méthode pour récupérer un microcontrôleur par son identifiant
    public static Microcontroleur getMicrocontroleurById(Connection connection, int id) {
        Microcontroleur microcontroleur = null;
        try {
            String query = "SELECT * FROM microcontroleurs WHERE id = ?";
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
    public static List<Integer> getAppareilIds(Connection connection, String nomAppareil) {
        List<Integer> ids = new ArrayList<>();
    
        try {
            // Préparez la requête SQL pour obtenir les identifiants des appareils avec le nom spécifié
            String sql = "SELECT id FROM appareils WHERE nom = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // Remplacez le paramètre dans la requête par le nom de l'appareil spécifié
                statement.setString(1, nomAppareil);
    
                // Exécutez la requête
                try (ResultSet resultSet = statement.executeQuery()) {
                    // Parcourez les résultats et ajoutez les identifiants à la liste
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        ids.add(id);
                    }
                }
            }
        } catch (SQLException e) {
            // Gérez les exceptions SQL si nécessaire
            e.printStackTrace();
        }
    
        return ids;
    }


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
                        String nom = resultSet.getString("nom");
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
    // Méthode pour afficher les données d'un appareil spécifique
    public static void afficherDonneesAppareil(Connection connection, int idAppareil) {
        // À compléter
    }



    // Méthodes pour mettre à jour et supprimer des appareils et des microcontrôleurs

    // Méthodes supplémentaires si nécessaire

    public static void ajouterDonnee(Donnee donnee) {
        fileAttente.add(donnee);
    }


}
