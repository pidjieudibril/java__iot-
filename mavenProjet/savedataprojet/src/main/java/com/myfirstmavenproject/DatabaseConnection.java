package com.myfirstmavenproject;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    // Méthode pour etablir la connexion a notre base de donnee postgresql
    public static Connection connectionAlaBaseDedonnee(String dbURL, String dbUser, String dbPassword) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        try {
           // Class.forName("org.postgresql.Driver");

            connection =  DriverManager.getConnection(dbURL, dbUser, dbPassword);
            createDbNoExist (connection, dbURL); //vérifie et crée la base de donnée si elle n'existe pas 
            createTableNoExist (connection); //vérifie et crée la table si elle n'existe pas 
        } catch (SQLException e) {
            e.printStackTrace(); // imprime les details de l'erreur 
            throw new SQLException("erreur lors de la connection  a la base de donnees ", e);
        }
        return connection;
    }

    // methode pour fermer la connexion a notre base de donnees 
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


// methode pour creer l base de données si elle exite pas 
private static void createDbNoExist(Connection connection, String dbURL){
    try {
    // faisons d'abord l'extraction du nom de la base de donnees  partir de l'url
    String dbName = extraireDbName(dbURL);
    // puis on verifie si la base de donnee exite 
    String checkIfDbExist = "SELECT EXISTS (SELECT 1 FROM pg_database WHERE datname = ?)";

    try (PreparedStatement preparedStatement = connection.prepareStatement(checkIfDbExist)) {
        preparedStatement.setString(1, dbName);
        ResultSet resultSet = preparedStatement.executeQuery();

        resultSet.next();
        boolean databaseExists = resultSet.getBoolean(1);

        // Si la base de donnees n'existe pas on la creer 
        if (!databaseExists) {
            System.out.println("La base de données '" + dbName + "' n'existe pas");
            createDb(connection, dbName);
        }
    }
} catch (SQLException e) {
    System.err.println("Erreur lors de la vérification de l'existence de la base de données : " + e.getMessage());
}
}



// extraire le nom de la base de donnee a partir de l'url jdbc 
private static String extraireDbName (String dbURL){
    String[] tab = dbURL.split("/");
    return tab[tab.length - 1];

}

// methode permettant la creation de la base de donnees 

private static void createDb (Connection connection, String dbName){
 try{ 
       // cree la base de donnee si elle n'exite pas 
    String queryToCreateDb = "CREATE DATABASE " + dbName;
    
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(queryToCreateDb);
            System.out.println("La base de donnee " + dbName + " a ete creer avec succes");
        }

    
 }catch (SQLException e){
    System.err.println("erreur lors de la creation  de la base de donnee  " + e.getMessage());

 }
}


// creation de la table si elle n'exite pas 


private static void createTableNoExist(Connection connection) {
    try {
        // Vérifie si la table existe dans le schéma d'information de la base de données
        String checkIfTableExist = "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'appareils')";
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkIfTableExist);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            resultSet.next();
            boolean tableExists = resultSet.getBoolean(1);

            // Si la table n'existe pas, créez-la
            if (!tableExists) {
                System.out.println("creation de la table appareils connecte ");
                createTable(connection);
            }
        }
    } catch (SQLException e) {
        System.err.println("la table n'exite pas  " + e.getMessage());
    }
}

private static void createTable(Connection connection) {
    try {
        // Crée la table pour les appareil connecté si elle n'existe pas
        String queryTocreateTable = "CREATE TABLE appareils (id SERIAL PRIMARY KEY, name VARCHAR(255), type VARCHAR(255),etat_fonctionnement VARCHAR(255), quantite INT)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(queryTocreateTable);
            System.out.println("table creer avec succes ");
        }
    } catch (SQLException e) {
        System.err.println("une erreur ses produit lors de la creation des tables : " + e.getMessage());
    }
}
}

