package com.myfirstmavenproject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Traitement {

    // Méthode pour insérer des données dans la base de données
    public static void insertData(Connection connection, String name, String type, String etat_fonctionnement, int quantite) {
        try {
            //id SERIAL PRIMARY KEY, nom VARCHAR(255), type VARCHAR(255),etat_fonctionnement VARCHAR(255), quantite INT)
            // Préparation de la requête SQL pour l'insertion de données
            String sql = "INSERT INTO appareils (name, type, etat_fonctionnement, quantite) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // Remplacement des paramètres dans la requête
                statement.setString(1, name);
                statement.setString(2, type);
                statement.setString(3, etat_fonctionnement);
                statement.setInt(4, quantite);

                // Exécution de la requête
                int rowsAffected = statement.executeUpdate();
                System.out.println(rowsAffected + " lignes insérées avec succes");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de données : " + e.getMessage());
        }
    }

    // Méthode pour afficher toutes les données de la base de données
    public static void selectData(Connection connection) {
        try {
            // Exécution d'une requête de sélection
            String sql = "SELECT * FROM appareils";
            try (PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                // Traitement des résultats de la requête
                while (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String type =  resultSet.getString("type");
                    String etat_fonctionnement = resultSet.getString("etat_fonctionnement");
                    int quantite = resultSet.getInt("quantite");
                    System.out.println("appareils : " + name + ", type : " + type + ", etat_fonctionnement " + etat_fonctionnement + ", quantite " + quantite );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la sélection de données : " + e.getMessage());
        }
    }

    // Méthode pour mettre à jour l'âge d'un utilisateur
    // Méthode pour mettre à jour les données d'un appareil dans la base de données
public static void updateData(Connection connection, String name, String type, String etat_fonctionnement, int quantite) {
    try {
        // Préparation de la requête SQL pour la mise à jour des donnees
        String sql = "UPDATE appareils SET type = ?, stat_fonctionnement = ?, quantite = ? WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Remplacement des paramètres dans la requête
            statement.setString(1, type);
            statement.setString(2, etat_fonctionnement);
            statement.setInt(3, quantite);
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
public static void deleteData(Connection connection, String name) {
    try {
        // Préparation de la requête SQL pour la suppression d'un appareil
        String sql = "DELETE FROM appareils WHERE name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // Remplacement des paramètres dans la requête
            statement.setString(1, name);

            // Exécution de la requête
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println(rowsAffected + " ligne supprime");
            } else {
                System.out.println("aucun appareil trouvé avec le nom : " + name);
            }
        }
    } catch (SQLException e) {
        System.err.println("erreur lors de la suppression d'appareil : " + e.getMessage());
    }
}

}
