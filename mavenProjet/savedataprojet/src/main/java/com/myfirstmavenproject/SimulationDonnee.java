
package com.myfirstmavenproject;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Queue;
import java.util.LinkedList;

public class SimulationDonnee {
    private static final int MAX_DATA_COUNT = 30;
    private static Queue<String> dataQueue = new LinkedList<>();
    private static Connection connection;

    public static void enregistrerDonneeCapteur(int appareilId, String donnee) {
        enregistrerDonnee(appareilId, donnee);
    }

    public static void enregistrerDonneeActionneur(int appareilId, String donnee) {
        enregistrerDonnee(appareilId, donnee);
    }

    // Méthode pour enregistrer les données dans la file
    private static void enregistrerDonnee(int appareilId, String donnee) {
        if (dataQueue.size() >= MAX_DATA_COUNT) {
            // Si la file est pleine, vider les données
            viderDonnees();
        }
        dataQueue.offer(donnee);
    }

    public static void afficherFile() {
        System.out.println("Contenu de la file de données : ");
        for (String donnee : dataQueue) {
            System.out.println(donnee);
        }
    }

    public static void viderDonnees() {
        if (connection == null || isConnectionClosed()) {
            connection = obtenirConnexion();
        }
        while (!dataQueue.isEmpty()) {
            String donnee = dataQueue.poll();
            insererDonnee(connection, donnee);
        }
    }

    // Méthode pour vérifier si la connexion est fermée
    private static boolean isConnectionClosed() {
        try {
            return connection == null || connection.isClosed();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'état de la connexion : " + e.getMessage());
            return true;
        }
    }

    // Méthode pour obtenir une nouvelle connexion à la base de données
    private static Connection obtenirConnexion() {
        Connection newConnection = null;
        try {
            // Code pour obtenir une connexion à la base de données
            newConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/appJavaTest1", "postgres", "admin");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'obtention de la connexion à la base de données : " + e.getMessage());
        }
        return newConnection;
    }

    

    // Méthode pour insérer une donnée dans la base de données
    private static Connection insererDonnee(Connection connection, String donnee) {
        String query = "INSERT INTO donnees_appareils (nomAppareil, donnee, timestamp) " +
                       "SELECT name, ?, ? FROM appareils WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, donnee);
            preparedStatement.setTimestamp(2, new Timestamp(new Date().getTime()));
            preparedStatement.setInt(3, getAppareilId(donnee));
            preparedStatement.executeUpdate();
            System.out.println("Donnée insérée avec succès");
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'insertion de la donnée dans la base de données : " + e.getMessage());
        }
        return connection;
    }
    

   private static int getAppareilId(String donnee) {
    int indexSeparateur = donnee.indexOf(':');
    if (indexSeparateur != -1) {
        String nomAppareil = donnee.substring(0, indexSeparateur);
        try {
            // Recherche de l'ID de l'appareil à partir de son nom
            String query = "SELECT id FROM appareils WHERE name = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, nomAppareil);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id");
                    } else {
                        System.err.println("Aucun appareil trouvé avec le nom : " + nomAppareil);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'ID de l'appareil : " + e.getMessage());
        }
    }
    return -1;
}

}
