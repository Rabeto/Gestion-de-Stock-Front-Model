/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 *
 * @author Finaritra
 */

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.Initializable;

import app.entity.Produit;
import app.services.EtatStockService;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

public class EtatStockController implements Initializable {

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private TableView<Produit> tableViewEtatStock;

    @FXML
    private TableColumn<Produit, String> columnDeesignation;

    @FXML
    private TableColumn<Produit, Integer> columnStock;

    @FXML
    private JFXTextField txtNumProduit;

    @FXML
    private JFXTextField txtQteSortie;

    EtatStockService ess = new EtatStockService();
    
    ///***** CHERCHER ET FILTRER LA LISTE DES PRODUITS DANS ETAT STOCK *****\\\
    
    @SuppressWarnings("static-access")
	@FXML
    private void searchProduit(KeyEvent ke) throws SQLException, Exception{
        
        FilteredList<Produit> filterData = new FilteredList<>(ess.getAllProduits(),p->true);
        txtSearch.textProperty().addListener((observable,oldvalue,newvalue)->{
        
           filterData.setPredicate( produit->{
               
               if(newvalue == null || newvalue.isEmpty()){
                   return true;
               }
               String typedText = newvalue.toLowerCase();
               
               if(produit.getNumProduit().toLowerCase().indexOf(typedText) != -1){
                    return true;
               }
               if(produit.getDesignProduit().toLowerCase().indexOf(typedText) != -1){
                    return true;
               }
               if( new String().valueOf(produit.getStockProduit()).toLowerCase().indexOf(typedText) != -1){
                    return true;
               }
               return false;
           });
           
           SortedList<Produit> sortedList = new SortedList<>(filterData);
           sortedList.comparatorProperty().bind(tableViewEtatStock.comparatorProperty());
           tableViewEtatStock.setItems(sortedList);
           
       });
    }
 
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // TABLEAU
        columnDeesignation.setCellValueFactory(new PropertyValueFactory<Produit,String>("designProduit"));
        columnStock.setCellValueFactory(new PropertyValueFactory<Produit, Integer>("stockProduit"));
      chargerEtatStock();
  }    
    
    
    public void chargerEtatStock() {
        
        // Charger le tableau
        try{
      	  tableViewEtatStock.setItems(ess.getAllProduits());
      	  //System.out.println(ess.getAllProduits());
        }catch(Exception e)
        {
            System.out.println(e) ;
        }
    }
    
}
