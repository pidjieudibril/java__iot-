package com.myfirstmavenproject;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Microcontroleur {

    public static void ajouterMicrocontroleur(String nomMicrocontroleur) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/appJavaTest1", "postgres", "admin");
            String query = "INSERT INTO microcontroleurs (nom) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nomMicrocontroleur);
                preparedStatement.executeUpdate();
                System.out.println("Microcontrôleur ajouté avec succès");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du microcontrôleur : " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
                }
            }
        }
    }
}
