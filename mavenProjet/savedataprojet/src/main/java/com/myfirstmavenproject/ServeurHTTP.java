package com.myfirstmavenproject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServeurHTTP {

    private HttpClient httpClient;

    public ServeurHTTP() {
        this.httpClient = HttpClients.createDefault();
    }

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
}
