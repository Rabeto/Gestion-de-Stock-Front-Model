/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.services;

import app.entity.BonSortie;
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
public class BonSortieService {
    AdresseIp adresse = new AdresseIp();
    private static HttpURLConnection con ; 
    private final String BASE_URL = adresse.url + "bonSorties/" ;
    private URL url;
    
    
    public ObservableList<BonSortie> getAllBonSortie() throws IOException, Exception {
        
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
            
            return (ObservableList<BonSortie>) JSONtoObservableList(response.toString());
            
        }
        
    }
    
      public void deleteBonSortie(String idBonSortie) throws IOException, Exception {
        
        this.url = new URL(BASE_URL + "remove/" + idBonSortie );
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
   

    public void createBonSortie(BonSortie bds) throws IOException, Exception {

        this.url = new URL(BASE_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonInputString = ObjectToJSON(bds);

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
    
     public void updateBonSortie(BonSortie bds) throws IOException, Exception {

        
        this.url = new URL(BASE_URL + "edit/" + bds.getNumBonSortie());
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonInputString = ObjectToJSON(bds);

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

    public String ObjectToJSON(BonSortie bds) throws IOException {

        // Creating Object of ObjectMapper define in Jakson Api 
        ObjectMapper Obj = new ObjectMapper();

        // get Produit object as a json string
        String jsonStr = Obj.writeValueAsString(bds);

        return jsonStr;
    }
    
    public ObservableList<BonSortie> JSONtoObservableList(String js){
        
            ObservableList<BonSortie> bonSorties ;
            bonSorties = FXCollections.observableArrayList();
            
            Gson gson = new Gson();
            
            @SuppressWarnings("serial")
            java.lang.reflect.Type collectionType = new TypeToken<List<BonSortie>>() {}.getType();
            List<BonSortie> bds = gson.fromJson(js, collectionType);
            
            for(int i = 0 ; i< bds.size() ; i++ ){
                
                BonSortie bde =  (BonSortie) bds.get(i);
                
                bonSorties.add(bde);
                
                
            }
            return bonSorties ;
            
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
        
        this.url = new URL(adresse.url + "produits/");
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
