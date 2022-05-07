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
import app.entity.Produit;
import app.services.ProduitService;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;

public class ProduitController implements Initializable{

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private TableView<Produit> tableViewProduit;

    @FXML
    private TableColumn<Produit, String> columnNumProduit;

    @FXML
    private TableColumn<Produit, String> columnDesignation;

    @FXML
    private TableColumn<Produit, Integer> columnStock;

    @FXML
    private JFXTextField txtNumProduit;

    @FXML
    private JFXTextField txtDesignation;

    @FXML
    private JFXTextField txtStock;

    @FXML
    private JFXButton btnAjouter;

    @FXML
    private JFXButton btnSupprimer;

    @FXML
    private JFXButton btnModifier;
    
    @FXML
    private JFXButton btnRefresh;
    
    @FXML
    private Label lblProduitError;

    @FXML
    private Label lblDesignError;

    @FXML
    private Label lblStockError;
    
    // Services
    ProduitService pds = new ProduitService() ;
    DashboardController dc;
    EtatStockController ec;
    BonEntreeController bec;
    BonSortieController bsc;
    MvtStockController msc;
    
    @FXML
    void ajouter(ActionEvent event) throws IOException,Exception {
        
            if(isValid()){
                        
                      Produit pdt2 = new Produit(txtNumProduit.getText() , txtDesignation.getText() , Integer.parseUnsignedInt(txtStock.getText())) ;

                        pds.createProduit(pdt2);

                        // Rafraichir la liste des produits
                        tableViewProduit.setItems(pds.getAllProduits());

                       // Reset Forms
                       resetStockProdForm();
                       
                       dc.chargerGraph();
                       ec.chargerEtatStock();
                       bsc.chargerBS();
                       bec.chargerBE();
                
            }else{
            	checkFields();
                //notification("error","Stock invalide !");
            }
       
       
        
    }

    @FXML
    void modifier(ActionEvent event) throws Exception {
        
         if(isValid()){
             
                    String np = tableViewProduit.getSelectionModel().getSelectedItem().getNumProduit() ;
                    String dp = tableViewProduit.getSelectionModel().getSelectedItem().getDesignProduit();
                    int sp = tableViewProduit.getSelectionModel().getSelectedItem().getStockProduit() ;

                    // FROM FORMULAIRE
                    String numProd_ud = txtNumProduit.getText() ;
                    String designProd_ud = txtDesignation.getText() ;
                    String stockProd_ud = txtStock.getText() ;


                    if( np.equals(numProd_ud) && dp.equals(designProd_ud) && (new String().valueOf(sp).equals(stockProd_ud))){
                        notification("warning","Vous n'avez effectué aucune modification !");
                    }else{

                        Produit pdt2 = new Produit(txtNumProduit.getText() , txtDesignation.getText() , Integer.parseUnsignedInt(txtStock.getText())) ;

                         pds.updateProduit(pdt2);

                         // Rafraichir la liste des produits
                         tableViewProduit.setItems(pds.getAllProduits());

                         // Reset Forms
                         resetStockProdForm();
                       dc.chargerGraph();
                       ec.chargerEtatStock();
                       bsc.chargerBS();
                       bec.chargerBE();
                    }
             
             
         }
         else{
             checkFields();
             // notification("error","Stock invalide !");
         }
        

    }

    @FXML
    void supprimer(ActionEvent event) throws Exception {
    	
    	//check validation
    	if(isValid()) {
            // Boite de confirmation
            JOptionPane jop = new JOptionPane();
            int option = jop.showConfirmDialog(null,"Voulez-vous vraiment supprimer ce produit ?","Confirmation",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    
            if(option == JOptionPane.OK_OPTION){
                
                pds.deleteProduit(txtNumProduit.getText());
            
                // Rafraichir la liste des produits
                tableViewProduit.setItems(pds.getAllProduits());
           
                 // Reset Forms
                resetStockProdForm();
                       dc.chargerGraph();
                       ec.chargerEtatStock();
                       bsc.chargerBS();
                       bec.chargerBE();
            }
    	}
    	else {            
    		checkFields();
    	}
    }
    
    @FXML
    void resetStockHandle(ActionEvent event) {
        resetStockProdForm();
    }
    
    // Reset Forms
    void resetStockProdForm(){
        
        //txtSearch.setDisable(false);
                         
        //retour_produit.setVisible(true);

        txtNumProduit.setText("");
        txtDesignation.setText("");
        txtStock.setText("");
        
    	lblProduitError.setVisible(false);
    	lblDesignError.setVisible(false);
    	lblStockError.setVisible(false);
        
    }
    
    ///***** CHERCHER ET FILTRER LA LISTE DES PRODUITS *****\\\
    
     @FXML
     private void searchProduit(KeyEvent ke) throws SQLException, Exception{
         
         FilteredList<Produit> filterData = new FilteredList<>(pds.getAllProduits(),p->true);
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
            sortedList.comparatorProperty().bind(tableViewProduit.comparatorProperty());
            tableViewProduit.setItems(sortedList);
            
        });
     }
      
    
    /* txtSearch.setDisable(false);
                         
        //retour_produit.setVisible(true);

        txtNumProduit.setText("");
        txtDesignation.setText("");
        txtStock.setText("");
    */
    
      @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        // TABLEAU
        columnNumProduit.setCellValueFactory(new PropertyValueFactory<Produit,String>("numProduit"));
        columnDesignation.setCellValueFactory(new PropertyValueFactory<Produit,String>("designProduit"));
        columnStock.setCellValueFactory(new PropertyValueFactory<Produit, Integer>("stockProduit"));
        
        chargerProduit();
        
    }    
    
    
    public void chargerProduit(){
        // Charger le tableau
        try{
            tableViewProduit.setItems(pds.getAllProduits());
        }catch(Exception e)
        {
            System.out.println(e) ;
        }
        
        // EvÃ¨nement lors du click sur une ligne du tableau
        tableViewProduit.setOnMousePressed(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent event){
                
             
                
                if( !(tableViewProduit.getItems().isEmpty()) ){
                   
                   if( !(tableViewProduit.getSelectionModel().isEmpty()) ){
	                   	lblProduitError.setVisible(false);
	                	lblDesignError.setVisible(false);
	                	lblStockError.setVisible(false);
                         //txtSearch.setDisable(true);
                         
                         //retour_produit.setVisible(true);

                         txtNumProduit.setText(tableViewProduit.getSelectionModel().getSelectedItem().getNumProduit());
                         txtDesignation.setText(tableViewProduit.getSelectionModel().getSelectedItem().getDesignProduit());
                         txtStock.setText(new String().valueOf(tableViewProduit.getSelectionModel().getSelectedItem().getStockProduit()));
                       
                   }
               
                }
                
            }
            
        });

        
        txtStock.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if(!txtStock.getText().matches("\\d{1,15}")) {
					//txtStock.setStyle("-fx-background-color: #FF514F");
					txtStock.setText("");
				}
				else {
					txtStock.setStyle("-fx-border-color: WHITE");
				}
			}
		});
    }
    
        //****** NOTIFICATION GLOBALE POUR LE RESULTAT DE L'OPERATION *******\\
    
    private void notification(String type,String champ){
        
       Alert alert ;
       
        if(type.equals("info_create")){
            
            
            alert = new Alert(Alert.AlertType.INFORMATION) ;
            
            alert.setTitle("Information");
            alert.setHeaderText(null);

            alert.setContentText(champ + " crÃ©e(e) avec succÃ¨s !");
            
            alert.showAndWait();
           
            
        }
        else if(type.equals("info_update")){
            
            alert = new Alert(Alert.AlertType.INFORMATION) ;
            
            alert.setTitle("Information");
            alert.setHeaderText(null);

            alert.setContentText(champ + " modifiÃ©(e) avec succÃ¨s !");
            
            alert.showAndWait();
            
        }
        else if(type.equals("info_delete")){
            
             alert = new Alert(Alert.AlertType.INFORMATION) ;
            
            alert.setTitle("Information");
            alert.setHeaderText(null);

            alert.setContentText(champ);
            
            alert.showAndWait();
            
        }
        else if(type.equals("warning")){
            
            alert = new Alert(Alert.AlertType.WARNING) ;

            alert.setTitle("Attention");
            alert.setHeaderText(null);
            alert.setContentText(champ);
           
            alert.showAndWait();
            
         
        }
        else if(type.equals("error")){
            
            alert = new Alert(Alert.AlertType.ERROR) ;

            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText(champ);
           
            alert.showAndWait();
            
         
        }
       
    }
    
    //****** VERIFIER NOMBRE ENTIER POSITIF *****\\
    @FXML
    private boolean isIntegerNumber(String str){
        try {
            Integer.parseUnsignedInt(str);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }    
    
    /**
     * Vérifier si champs non vide?
     */
    private boolean isValid() {
    	boolean valide = false;
    	lblProduitError.setVisible(false);
    	lblDesignError.setVisible(false);
    	lblStockError.setVisible(false);
    	if(txtNumProduit.getText().isEmpty() || txtDesignation.getText().isEmpty() || txtStock.getText().isEmpty())
    		valide = false;
    	else valide = true;
    	
    	return valide;
    }
    
    private void checkFields() {
    	if(txtNumProduit.getText().isEmpty()) lblProduitError.setVisible(true);
    	if(txtDesignation.getText().isEmpty()) lblDesignError.setVisible(true);
    	if(txtStock.getText().isEmpty()) lblStockError.setVisible(true);
    }

    public void init(DashboardController dshController, EtatStockController etsController) {
            dc = dshController;		
            ec = etsController;
    }

    void init(DashboardController dshController, EtatStockController etsController, BonEntreeController beController, BonSortieController bsController, MvtStockController msController) {
            dc = dshController;	
            ec = etsController;
            bec = beController;
            bsc = bsController;
            msc = msController;
    }
    
}
