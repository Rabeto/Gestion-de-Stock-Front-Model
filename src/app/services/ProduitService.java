/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.services;

import app.entity.Produit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.codehaus.jackson.map.ObjectMapper;

/**
 *
 * @author Mac Carthy
 */
public class ProduitService {
    AdresseIp adresse = new AdresseIp();
    private static HttpURLConnection con ; 
    private final String BASE_URL = adresse.url ;
    private URL url;
    
    
    public ObservableList<Produit> getAllProduits() throws IOException, Exception {
        
        this.url = new URL(BASE_URL + "produits");
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            
            return JSONtoObservableList(response.toString());
            
        }
        
    }
    
    public Produit getOneProduit(String idProduct) throws IOException, Exception {
        
        this.url = new URL(BASE_URL + "produits/getOne/" + idProduct);
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            
            return JSONtoObject(response.toString());
            
        }
        
    }
    
    public Produit JSONtoObject(String js) {
        Produit pdt;
        Gson gson = new Gson();
        pdt = gson.fromJson(js, Produit.class);
        return pdt;
    }
    
      public void deleteProduit(String idProduit) throws IOException, Exception {
        
        this.url = new URL(BASE_URL + "produits/remove/" + idProduit );
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            
        }
        
    }
   

    public void createProduit(Produit pdt) throws IOException, Exception {

        
        this.url = new URL(BASE_URL + "produits");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonInputString = ObjectToJSON(pdt);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

    }
    
     public void updateProduit(Produit pdt) throws IOException, Exception {

        
        this.url = new URL(BASE_URL + "produits/edit/" + pdt.getNumProduit());
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonInputString = ObjectToJSON(pdt);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        
        try (BufferedReader br = new BufferedReader(
            new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            
        }

    }

     
    
     
    public String ObjectToJSON(Produit pdt1) throws IOException {

        // Creating Object of ObjectMapper define in Jakson Api 
        ObjectMapper Obj = new ObjectMapper();

        // get Produit object as a json string
        String jsonStr = Obj.writeValueAsString(pdt1);

        return jsonStr;
    }
    
    public ObservableList<Produit> JSONtoObservableList(String js){
        
            ObservableList<Produit> produits ;
            produits = FXCollections.observableArrayList();
            
            Gson gson = new Gson();
            
            @SuppressWarnings("serial")
            java.lang.reflect.Type collectionType = new TypeToken<List<Produit>>() {}.getType();
            List<Produit> pdts = gson.fromJson(js, collectionType);
            
            for(int i = 0 ; i< pdts.size() ; i++ ){
                
                Produit pdt =  (Produit) pdts.get(i);
                
                produits.add(pdt);
                
                
            }
            return produits ;
            
    }
    
    
}
