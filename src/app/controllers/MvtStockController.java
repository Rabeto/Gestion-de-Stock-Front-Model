/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

/**
 *
 * @author Finaritra
 */
import app.entity.BonEntree;
import app.entity.BonSortie;
import app.entity.MvtStock;
import app.entity.Produit;
import app.services.BonEntreeService;
import app.services.BonSortieService;
import app.services.ProduitService;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

public class MvtStockController implements Initializable {

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private TableView<MvtStock> tableViewMvtStock;

    @FXML
    private TableColumn<MvtStock, String> columnNumBon;

    @FXML
    private TableColumn<MvtStock, String> columnEntree;

    @FXML
    private TableColumn<MvtStock, String> columnSortie;

    @FXML
    private TableColumn<MvtStock, String> columnDate;
    
    @FXML
    private TableColumn<MvtStock, String> columnNumProd;

    @FXML
    private TableColumn<MvtStock, String> columnDesign;
    
    @FXML
    private Label lblDesignation;
    
    BonEntreeService bes = new BonEntreeService();
    BonSortieService bss = new BonSortieService();
    ProduitService pds = new ProduitService() ;
    List<Produit> produits;
    ObservableList<MvtStock> msData = FXCollections.observableArrayList();    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

        // TABLEAU
		columnNumBon.setCellValueFactory(new PropertyValueFactory<MvtStock,String>("num"));
		columnEntree.setCellValueFactory(new PropertyValueFactory<MvtStock,String>("qteEntree"));
		columnSortie.setCellValueFactory(new PropertyValueFactory<MvtStock,String>("qteSortie"));
		columnDate.setCellValueFactory(new PropertyValueFactory<MvtStock, String>("date"));
		columnNumProd.setCellValueFactory(new PropertyValueFactory<MvtStock, String>("numProduit"));
		columnDesign.setCellValueFactory(new PropertyValueFactory<MvtStock, String>("designProduit"));
		chargerMvtStock();
	}
        
        public void chargerMvtStock(){
            		try {
			produits = pds.getAllProduits();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			for (BonEntree be : bes.getAllBonEntree()) {
				msData.add(new MvtStock(be, produits));
			}
			for (BonSortie bs : bss.getAllBonSortie()) {
				msData.add(new MvtStock(bs, produits));
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // TODO Auto-generated catch block
		tableViewMvtStock.setItems(msData);
        }
	
    ///***** CHERCHER ET FILTRER LA LISTE DES PRODUITS *****\\\
    
    @FXML
    private void searchProduit(KeyEvent ke) throws SQLException, Exception{
        
        FilteredList<MvtStock> filterData = new FilteredList<>(msData,p->true);
        txtSearch.textProperty().addListener((observable,oldvalue,newvalue)->{
        
           filterData.setPredicate( produit->{
               
               if(newvalue == null || newvalue.isEmpty()){
            	   lblDesignation.setText("Désignation : .......................");
                   return true;
               }
               String typedText = newvalue.toLowerCase();
               
               if(produit.getNumProduit().toLowerCase().indexOf(typedText) != -1){
            	   lblDesignation.setText("Désignation : "+produit.getDesignProduit());
                    return true;
               }
               if(produit.getDesignProduit().toLowerCase().indexOf(typedText) != -1){
            	   lblDesignation.setText("Désignation: "+produit.getDesignProduit());
                    return true;
               }
               
//               if( new String().valueOf(produit.getStockProduit()).toLowerCase().indexOf(typedText) != -1){
//                    return true;
//               }
               return false;
           });
           
           SortedList<MvtStock> sortedList = new SortedList<>(filterData);
           sortedList.comparatorProperty().bind(tableViewMvtStock.comparatorProperty());
           tableViewMvtStock.setItems(sortedList);
       });
    }


}
