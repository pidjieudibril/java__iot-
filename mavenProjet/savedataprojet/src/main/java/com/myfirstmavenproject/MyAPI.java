package com.myfirstmavenproject;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyAPI {

    private static final Logger logger = Logger.getLogger(MyAPI.class.getName());

    public static void main(String[] args) {
        String dbURL = "jdbc:postgresql://localhost:5432/appJavaTest4";
        String dbUser = "postgres";
        String dbPassword = "admin";
        Connection connection = null;
        try {
            // Obtenir la connexion à la base de données à partir de DatabaseConnection
            connection = DatabaseConnection.connectionAlaBaseDedonnee(dbURL, dbUser, dbPassword);
            if (connection != null) {
                startHttpServer(connection);
            } else {
                logger.severe("La connexion à la base de données a échoué.");
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            logger.log(Level.SEVERE, "Une erreur s'est produite lors de l'initialisation du serveur HTTP.", e);
        } finally {
            // Fermer la connexion à la base de données
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Impossible de fermer la connexion à la base de données.", e);
                }
            }
        }
    }

    private static void startHttpServer(Connection connection) throws IOException {
        // Création du serveur HTTP et ajout des gestionnaires
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/microcontroleur/ajouter", new AjouterMicrocontroleurHandler(connection));
        server.createContext("/appareil/ajouter", new AjouterAppareilHandler(connection));
        server.createContext("/appareils", new AfficherAppareilsHandler(connection));
        server.createContext("/microcontroleurs", new AfficherMicrocontroleursHandler(connection));
        server.createContext("/donnees", new RecupererEtAfficherDonneesHandler(connection));
        server.createContext("/appareil/donnees", new AfficherDonneesAppareilHandler(connection));
        server.createContext("/appareil/mettre-a-jour", new MettreAJourAppareilHandler(connection));
        server.createContext("/appareil/supprimer", new SupprimerAppareilHandler(connection));
        server.createContext("/microcontroleur/mettre-a-jour", new MettreAJourMicrocontroleurHandler(connection));
        server.createContext("/microcontroleur/supprimer", new SupprimerMicrocontroleurHandler(connection));
        server.setExecutor(null);
        server.start();
        logger.info("Serveur HTTP démarré sur le port 8000.");
    }

    // Reste du code des gestionnaires de requêtes HTTP...



    static class AjouterMicrocontroleurHandler implements HttpHandler {
        private final Connection connection;

        public AjouterMicrocontroleurHandler(Connection connection) {
            this.connection = connection;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Récupérer les données du corps de la requête
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                String[] parts = requestBody.split("&");
                String nom = parts[0].split("=")[1];
                String adresseIP = parts[1].split("=")[1];

                // Ajouter le microcontrôleur à la base de données
                ajouterMicrocontroleur(nom, adresseIP);

                // Envoyer une réponse HTTP 200 (OK)
                String response = "Microcontrôleur ajouté avec succès.";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // Envoyer une réponse HTTP 405 (Méthode non autorisée)
                exchange.sendResponseHeaders(405, -1);
            }
        }

        private void ajouterMicrocontroleur(String nom, String adresseIP) {
            // Insérer le microcontrôleur dans la base de données en utilisant les méthodes de la classe Traitement
            Traitement.ajouterMicrocontroleur(connection, nom, adresseIP);
        }
    }


    static class AjouterAppareilHandler implements HttpHandler {
          private final Connection connection;
            
                public AjouterAppareilHandler(Connection connection) {
                    this.connection = connection;
                }
            
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                // Récupérer les données du corps de la requête
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                String[] parts = requestBody.split("&");
                String nomAppareil = parts[0].split("=")[1];
                String typeAppareil = parts[1].split("=")[1];
                String etatFonctionnement = parts[2].split("=")[1];
                int idMicrocontroleur = Integer.parseInt(parts[3].split("=")[1]);
                // Ajouter l'appareil à la base de données
                ajouterAppareil(connection,nomAppareil, typeAppareil, etatFonctionnement, idMicrocontroleur);

                // Envoyer une réponse HTTP 200 (OK)
                String response = "Appareil ajouté avec succès.";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // Envoyer une réponse HTTP 405 (Méthode non autorisée)
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    // Méthode pour ajouter un microcontrôleur dans la base de données
    private static void ajouterMicrocontroleur(Connection connection, String nom, String adresseIP) {
        // Insérer le microcontrôleur dans la base de données en utilisant les méthodes de la classe Traitement
        Traitement.ajouterMicrocontroleur(connection, nom, adresseIP);
    }

    // Méthode pour ajouter un appareil dans la base de données
    private static void ajouterAppareil(Connection connection,String nomAppareil, String typeAppareil, String etatFonctionnement, int idMicrocontroleur) {
        // Insérer l'appareil dans la base de données en utilisant les méthodes de la classe Traitement
        Traitement.ajouterAppareil(connection, nomAppareil, typeAppareil, etatFonctionnement, idMicrocontroleur);
    }

    static class AfficherAppareilsHandler implements HttpHandler {
        private final Connection connection;
    
        public AfficherAppareilsHandler(Connection connection) {
            this.connection = connection;
        }
    
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                // Implémenter la logique pour récupérer les appareils depuis la base de données
                // et les afficher sous forme de réponse HTTP
            } else {
                // Envoyer une réponse HTTP 405 (Méthode non autorisée)
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
    

    static class AfficherMicrocontroleursHandler implements HttpHandler {
         private final Connection connection;
                    
                        public AfficherMicrocontroleursHandler(Connection connection) {
                            this.connection = connection;
                        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                // Récupérer les données du corps de la requête
                // Implémenter la logique pour afficher un microcontrôleur spécifique
                // Envoyer une réponse HTTP 200 (OK)
                String response = "Microcontrôleur affiché avec succès.";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // Envoyer une réponse HTTP 405 (Méthode non autorisée)
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    static class RecupererEtAfficherDonneesHandler implements HttpHandler {
        private final Connection connection;
    
        public RecupererEtAfficherDonneesHandler(Connection connection) {
            this.connection = connection;
        }
    
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                // Implémenter la logique pour récupérer les données depuis la base de données
                // et les afficher sous forme de réponse HTTP
            } else {
                // Envoyer une réponse HTTP 405 (Méthode non autorisée)
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
    

    static class AfficherDonneesAppareilHandler implements HttpHandler {
          private final Connection connection;
            
                public AfficherDonneesAppareilHandler(Connection connection) {
                    this.connection = connection;
                }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                // Récupérer les données du corps de la requête
                // Implémenter la logique pour afficher les données d'un appareil spécifique
                // Envoyer une réponse HTTP 200 (OK)
                String response = "Données de l'appareil affichées avec succès.";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // Envoyer une réponse HTTP 405 (Méthode non autorisée)
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }

    static class MettreAJourAppareilHandler implements HttpHandler {
        private final Connection connection;
    
        public MettreAJourAppareilHandler(Connection connection) {
            this.connection = connection;
        }
    
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("PUT".equals(exchange.getRequestMethod())) {
                // Implémenter la logique pour mettre à jour un appareil dans la base de données
            } else {
                // Envoyer une réponse HTTP 405 (Méthode non autorisée)
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
    

    static class SupprimerAppareilHandler implements HttpHandler {
        private final Connection connection;
    
        public SupprimerAppareilHandler(Connection connection) {
            this.connection = connection;
        }
    
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("DELETE".equals(exchange.getRequestMethod())) {
                // Implémenter la logique pour supprimer un appareil de la base de données
            } else {
                // Envoyer une réponse HTTP 405 (Méthode non autorisée)
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
    

    static class MettreAJourMicrocontroleurHandler implements HttpHandler {
                private final Connection connection;
            
                public MettreAJourMicrocontroleurHandler(Connection connection) {
                    this.connection = connection;
                }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("PUT".equals(exchange.getRequestMethod())) {
                // Récupérer les données du corps de la requête
                // Implémenter la logique pour mettre à jour un microcontrôleur
                // Envoyer une réponse HTTP 200 (OK)
                String response = "Microcontrôleur mis à jour avec succès.";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // Envoyer une réponse HTTP 405 (Méthode non autorisée)
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
    
    static class SupprimerMicrocontroleurHandler implements HttpHandler {
    private final Connection connection;
            
                public SupprimerMicrocontroleurHandler(Connection connection) {
                    this.connection = connection;
                }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("DELETE".equals(exchange.getRequestMethod())) {
                // Récupérer les données du corps de la requête
                // Implémenter la logique pour supprimer un microcontrôleur
                // Envoyer une réponse HTTP 200 (OK)
                String response = "Microcontrôleur supprimé avec succès.";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } else {
                // Envoyer une réponse HTTP 405 (Méthode non autorisée)
                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
}
