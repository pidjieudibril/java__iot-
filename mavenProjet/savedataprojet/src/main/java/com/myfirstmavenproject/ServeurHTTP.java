package com.myfirstmavenproject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServeurHTTP {

    private HttpClient httpClient;

    public ServeurHTTP() {
        this.httpClient = HttpClients.createDefault();
    }
// methode qui gere l'envoie des requette par get a traver l'url 
    public String envoyerRequeteGET(String url) {
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder responseData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseData.append(line);
            }

            return responseData.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //methode qui gere l<envoie des requettes par post 
   public void envoyerRequetePOST(String url) {
        try {
            // Créer une URL à partir de la chaîne URL spécifiée
            URL urlObject = new URL(url);
            
            // Ouvrir une connexion HTTP avec l'URL
            HttpURLConnection connexion = (HttpURLConnection) urlObject.openConnection();
            
            // Spécifier que la connexion prend en charge les méthodes POST
            connexion.setRequestMethod("POST");
            
            // Activer l'envoi de données dans la connexion
            connexion.setDoOutput(true);
            
            // Créer un flux de sortie pour écrire des données dans la connexion
            OutputStream outputStream = connexion.getOutputStream();
            
            // Vous pouvez écrire des données dans le flux de sortie si nécessaire
            // Par exemple, pour envoyer des données JSON, vous pouvez utiliser un PrintWriter
            
            // Fermer le flux de sortie
            outputStream.close();
            
            // Lire la réponse de la connexion
            BufferedReader reader = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            String ligne;
            StringBuilder réponse = new StringBuilder();
            while ((ligne = reader.readLine()) != null) {
                réponse.append(ligne);
            }
            reader.close();
            
            // Afficher la réponse reçue du serveur
            System.out.println("Réponse du serveur : " + réponse.toString());
            
            // Fermer la connexion
            connexion.disconnect();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
