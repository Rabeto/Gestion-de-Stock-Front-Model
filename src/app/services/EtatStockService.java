/**
 * 
 */
package app.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import app.entity.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Finaritra
 *
 */
public class EtatStockService {
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
