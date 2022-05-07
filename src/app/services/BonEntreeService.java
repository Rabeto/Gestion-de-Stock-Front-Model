/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.services;

import app.entity.BonEntree;
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
public class BonEntreeService {
    AdresseIp adresse = new AdresseIp();
    private static HttpURLConnection con ; 
    private final String BASE_URL = adresse.url + "bonEntrees/" ;
//    private final String BASE_URL = "http://localhost:8080/bonEntrees/" ;
    private URL url;
    
    public ObservableList<BonEntree> getAllBonEntree() throws IOException, Exception {
        
        this.url = new URL(BASE_URL);
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
            
            return (ObservableList<BonEntree>) JSONtoObservableList(response.toString());
            
        }
        
    }
    
      public void deleteBonEntree(String idBonEntree) throws IOException, Exception {
        
        this.url = new URL(BASE_URL + "remove/" + idBonEntree );
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
   

    public void createBonEntree(BonEntree bde) throws IOException, Exception {

        this.url = new URL(BASE_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonInputString = ObjectToJSON(bde);

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
    
     public void updateBonEntree(BonEntree bde) throws IOException, Exception {

        
        this.url = new URL(BASE_URL + "edit/" + bde.getNumBonEntree());
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonInputString = ObjectToJSON(bde);

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

    public String ObjectToJSON(BonEntree bde1) throws IOException {

        // Creating Object of ObjectMapper define in Jakson Api 
        ObjectMapper Obj = new ObjectMapper();

        // get Produit object as a json string
        String jsonStr = Obj.writeValueAsString(bde1);

        return jsonStr;
    }
    
    public ObservableList<BonEntree> JSONtoObservableList(String js){
        
            ObservableList<BonEntree> bonEntrees ;
            bonEntrees = FXCollections.observableArrayList();
            
            Gson gson = new Gson();
            
            @SuppressWarnings("serial")
            java.lang.reflect.Type collectionType = new TypeToken<List<BonEntree>>() {}.getType();
            List<BonEntree> bdes = gson.fromJson(js, collectionType);
            
            for(int i = 0 ; i< bdes.size() ; i++ ){
                
                BonEntree bde =  (BonEntree) bdes.get(i);
                
                bonEntrees.add(bde);
                
                
            }
            return bonEntrees ;
            
    }
           

    public ObservableList<String> JSONtoObservableListProduct(String js){

        ObservableList<String> produits ;
        produits = FXCollections.observableArrayList();

        Gson gson = new Gson();

        @SuppressWarnings("serial")
        java.lang.reflect.Type collectionType = new TypeToken<List<Produit>>() {}.getType();
        List<Produit> pdts = gson.fromJson(js, collectionType);

        for(int i = 0 ; i< pdts.size() ; i++ ){

            Produit pdt =  (Produit) pdts.get(i);

            produits.add(pdt.getNumProduit());


        }
        return produits ;
            
    }
           

    
    public ObservableList<String> getAllProductComboBox() throws IOException, Exception {
        
        this.url = new URL(adresse.url + "/produits/");
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
            
            return JSONtoObservableListProduct(response.toString());
            
        }
        
    }
    
    
}
