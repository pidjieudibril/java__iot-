package com.myfirstmavenproject;

import java.sql.Connection;

public class Main {

    public static void main(String[] args) {
        String dbURL = "jdbc:postgresql://localhost:5432/appJavaTest4";
        String dbUser = "postgres";
        String dbPassword = "admin";

        try (Connection connection = DatabaseConnection.connectionAlaBaseDedonnee(dbURL, dbUser, dbPassword)) {
            Menu.mainMenu(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    
}
// trouver un serveur 