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
            createDonneesTableNoExist(connection);
            createMicrocontroleurTableNoExist(connection);
            createDonneesTableActionneurNoExist(connection);
        } catch (SQLException e) {
            e.printStackTrace(); // imprime les details de l'erreur 
            throw new SQLException("erreur lors de la connection  a la base de donnees ", e);
        }
        return connection;
    }

   
    private static final String dbURL = "jdbc:postgresql://localhost:5432/appJavaTest4";
    private static final String dbUser = "postgres";
    private static final String dbPassword = "admin";

    

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbURL, dbUser, dbPassword);
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





// creation de la table pour les donnees de chaque appareil 

private static void  createMicrocontroleurTableNoExist(Connection connection) {
    try {
        //verifier si l table existe dans le shema d'informtion de la base de donnee
        String checkIfTableExist = "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'microcontroleur')";
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkIfTableExist);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            resultSet.next();
            boolean tableExists = resultSet.getBoolean(1);
                // si la table n'existe pas crée-la
            if (!tableExists) {
                System.out.println("creation de la table  microcontroleur");
                createMicrocontroleurTable(connection);
            }
        }
    } catch (SQLException e) {
        System.err.println("une erreur s'est produit lors de la creation des tables  " + e.getMessage());
    }
}

//creer la table pour les donnees de chaque appareil si elle n'exite pas 
private static void createMicrocontroleurTable(Connection connection) {
    try {
        String queryToCreateTable = "CREATE TABLE microcontroleur (id SERIAL PRIMARY KEY, nom_microcontroleur VARCHAR(255), adresse_ip VARCHAR(255) UNIQUE)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(queryToCreateTable);
            System.out.println("Table microcontroleur créée avec succès");
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la création de la table microcontroleur : " + e.getMessage());
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

            // Si la table n'existe pas, crée-la
            if (!tableExists) {
                System.out.println("Création de la table appareils connecte");
                createTable(connection);
            } else {
                System.out.println("La table appareils existe déjà");
            }
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la création de la table appareils : " + e.getMessage());
    }
}


private static void createTable(Connection connection) {
    try {
        // Crée la table pour les appareils connectés si elle n'existe pas
        String queryToCreateTable = "CREATE TABLE appareils (id SERIAL PRIMARY KEY, nom_app VARCHAR(255) UNIQUE, type VARCHAR(255), etat_fonctionnement VARCHAR(255), adresse_ip VARCHAR(255) REFERENCES microcontroleur(adresse_ip))";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(queryToCreateTable);
            System.out.println("Table appareils créée avec succès");
        }
    } catch (SQLException e) {
        System.err.println("Une erreur s'est produite lors de la création de la table appareils: " + e.getMessage());
    }
}




// creation de la table pour les donnees de chaque appareil 

private static void createDonneesTableNoExist(Connection connection) {
    try {
        //verifier si l table existe dans le shema d'informtion de la base de donnee
        String checkIfTableExist = "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'donnees_appareils')";
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkIfTableExist);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            resultSet.next();
            boolean tableExists = resultSet.getBoolean(1);
                // si la table n'existe pas crée-la
            if (!tableExists) {
                System.out.println("creation de la table  donnees_appareils");
                createDonneesTable(connection);
            }
        }
    } catch (SQLException e) {
        System.err.println("une erreur s'est produit lors de la creation des tables  " + e.getMessage());
    }
}

//creer la table pour les donnees de chaque appareil si elle n'exite pas 
private static void createDonneesTable(Connection connection) {
    try {
        String queryToCreateTable = "CREATE TABLE donnees_appareils (id SERIAL PRIMARY KEY,  donnee VARCHAR(255), timestamp TIMESTAMP, nom_app VARCHAR(255) REFERENCES appareils(nom_app), adresse_ip VARCHAR(255) REFERENCES microcontroleur(adresse_ip)) " ;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(queryToCreateTable);
            System.out.println("Table donnees_appareils créée avec succès");
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la création de la table donnees_appareils : " + e.getMessage());
    }
}


// creation de la table pour les donnees de chaque appareil 

private static void createDonneesTableActionneurNoExist(Connection connection) {
    try {
        //verifier si l table existe dans le shema d'informtion de la base de donnee
        String checkIfTableExist = "SELECT EXISTS (SELECT FROM information_schema.tables WHERE table_name = 'donnees_Actionneur')";
        try (PreparedStatement preparedStatement = connection.prepareStatement(checkIfTableExist);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            resultSet.next();
            boolean tableExists = resultSet.getBoolean(1);
                // si la table n'existe pas crée-la
            if (!tableExists) {
                System.out.println("creation de la table  donnees_Actionneur");
                createDonneesTableActionneur(connection);
            }
        }
    } catch (SQLException e) {
        System.err.println("une erreur s'est produit lors de la creation des tables  " + e.getMessage());
    }
}

//creer la table pour les donnees de chaque appareil si elle n'exite pas 
private static void createDonneesTableActionneur(Connection connection) {
    try {
        String queryToCreateTable = "CREATE TABLE donnees_Actionneur (id SERIAL PRIMARY KEY,  etat VARCHAR(255), timestamp TIMESTAMP, nom_app VARCHAR(255) REFERENCES appareils(nom_app), adresse_ip VARCHAR(255) REFERENCES microcontroleur(adresse_ip)) " ;
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(queryToCreateTable);
            System.out.println("Table donnees_Actionneur créée avec succès");
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la création de la table donnees_Actionneur : " + e.getMessage());
    }
}







}

