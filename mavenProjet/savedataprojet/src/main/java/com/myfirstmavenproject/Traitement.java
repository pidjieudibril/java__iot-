package com.myfirstmavenproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Traitement {

    // Méthode pour insérer des données dans la base de données
    public static void ajouterAppareil(Connection connection, String name, String type, String etat_fonctionnement) {
        try {
            String sql = "INSERT INTO appareils (name, type, etat_fonctionnement) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, name);
                statement.setString(2, type);
                statement.setString(3, etat_fonctionnement);
    
                int rowsAffected = statement.executeUpdate();
                System.out.println(rowsAffected + " lignes insérées avec succès");
    
                // Créer une instance de l'appareil et simuler les données si nécessaire
                Appareil appareil = null;
                if (type.equalsIgnoreCase("capteur")) {
                    appareil = new Capteur(1,"unite");
                } else if (type.equalsIgnoreCase("actionneur")) {
                    appareil = new Actionneur();
                }
    
                if (appareil != null) {
                    // Simuler plusieurs données pour l'appareil nouvellement créé
                    for (int i = 0; i < 20; i++) { // Changez 3 au nombre de données que vous voulez simuler
                        appareil.simulerDonnee();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de données : " + e.getMessage());
        }
    }
    
    


    // Méthode pour afficher toutes les données de la base de données
    public static void afficherAppareil(Connection connection) {
        try {
            // Exécution d'une requête de sélection
            String sql = "SELECT * FROM appareils";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                 boolean appareilFind = false; 
                
                // Traitement des résultats de la requête
                while (resultSet.next()) {
                    appareilFind = true;
                    String name = resultSet.getString("name");
                    String type =  resultSet.getString("type");
                    String etat_fonctionnement = resultSet.getString("etat_fonctionnement");
                    System.out.println("appareils : " + name + ", type : " + type + ", etat_fonctionnement " + etat_fonctionnement );
                }
                if (!appareilFind) {
                    System.out.println("nous avons pas encore d'appareil enregistre");
                    
                }

                
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sélection de données : " + e.getMessage());
        }
    }

    // Méthode pour mettre à jour l'âge d'un utilisateur
    // Méthode pour mettre à jour les données d'un appareil dans la base de données
public static void MetreAjourAppreil(Connection connection, String name, String newName, String newType, String newEtatFonctionnement) {
        try {
        // Préparation de la requête SQL pour la mise à jour des donnees
        String sql = "UPDATE appareils SET name = ?, type = ?, etat_fonctionnement = ? WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Remplacement des paramètres dans la requête
            statement.setString(1, newName);
            statement.setString(2, newType);
            statement.setString(3, newEtatFonctionnement);
            statement.setString(4, name);

            // Exécution de la requête
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(rowsAffected + " ligne miose a jour ");
            } else {
                System.out.println("aucun appareils trouve avec le nom " + name);
            }
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la mise à jour des donnees : " + e.getMessage());
    }
}


    // Méthode pour supprimer un utilisateur de la base de données
    // Méthode pour supprimer un appareil de la base de données
    public static void supprimerAppareil(Connection connection, int id) {
        try {
            // Préparation de la requête SQL pour la suppression d'un appareil par identifiant
            String sql = "DELETE FROM appareils WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // Remplacement des paramètres dans la requête
                statement.setInt(1, id);
    
                // Exécution de la requête
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println(rowsAffected + " ligne supprimée");
                } else {
                    System.out.println("Aucun appareil trouvé avec l'identifiant : " + id);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'appareil : " + e.getMessage());
        }
    }


// methode pour obtnir les identifiant avec les nom specifique 
     public static List<Integer> getAppareilIds(Connection connection, String name) {
        List<Integer> ids = new ArrayList<>();
        try {
            // Préparation de la requête SQL pour obtenir les identifiants des appareils
            String sql = "SELECT id FROM appareils WHERE name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // Remplacement du paramètre dans la requête
                statement.setString(1, name);

                // Exécution de la requête
                try (ResultSet resultSet = statement.executeQuery()) {
                    // Traitement des résultats de la requête
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
                        ids.add(id);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des identifiants d'appareil : " + e.getMessage());
        }
        return ids;
    }
// methode qui permet de verifier si un appareil existe ou nom 
public static boolean appareilExiste(Connection connection, String name){
    boolean existe = false;
    try {
        // nous allons preparer la requette sql pour verifier l'existence de l'appareil
        String sql = "SELECT COUNT(*) FROM appareils WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);

            //execution de la requette 
            try (ResultSet resultSet = statement.executeQuery()){
              // si une ligne est retourne cela signifie que l'appareil existe 
              if(resultSet.next()){
                int count = resultSet.getInt(1);
                existe = count > 0 ;
              }  
            }
        }
    } catch (SQLException e) {
        // TODO: handle exception
        System.out.println("erreur laors de la verification de l'appareil " + e.getMessage());
    }


    return existe;
}

}
