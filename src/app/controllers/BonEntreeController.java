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
import app.entity.Produit;
import app.services.BonEntreeService;
import app.services.ProduitService;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;


public class BonEntreeController implements Initializable {

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private TableView<BonEntree> tableViewBonEntree;

    @FXML
    private TableColumn<BonEntree, String> columnNumBonEntree;

    @FXML
    private TableColumn<BonEntree, String> columnNumProduit;

    @FXML
    private TableColumn<BonEntree, Integer> columnQteEntree;

    @FXML
    private TableColumn<BonEntree, String> columnDateEntree;

    @FXML
    private JFXTextField txtNumBonEntree;

    @FXML
    private JFXComboBox<String> cbxNumProduit;
    
    @FXML
    private JFXTextField txtQteEntree;

    @FXML
    private JFXDatePicker dpDateEntree;

    @FXML
    private JFXButton btnAjouter;

    @FXML
    private JFXButton btnSupprimer;

    @FXML
    private JFXButton btnModifier;
    
    @FXML
    private Label lblBonEntreeError;

    @FXML
    private Label lblNumProdError;

    @FXML
    private Label lblQteError;

    @FXML
    private Label lblDateError;
    
    @FXML
    private JFXButton btnRefresh;
    
    
    // services
    BonEntreeService bdeService = new BonEntreeService() ;
    ProduitService pdsService = new ProduitService();
    DashboardController dc;
    MvtStockController mc;
    ProduitController pc;
    
    @FXML
    void ajouter(ActionEvent event) throws Exception {
        Produit pdt;
    	if(isValid()) {
            BonEntree bde = new BonEntree(
                        txtNumBonEntree.getText(), 
                        cbxNumProduit.getValue(),
                        Integer.parseInt(txtQteEntree.getText()), 
                        new String().valueOf(dpDateEntree.getValue()));
                try {
                    bdeService.createBonEntree(bde);
                    tableViewBonEntree.setItems(bdeService.getAllBonEntree());
                } catch (Exception error) {
                    System.out.print(error);
                }
                
                //update stock
                updateStockProduct(cbxNumProduit.getValue(), Integer.parseInt(txtQteEntree.getText()));
                
                resetBdeForm();
                dc.chargerGraph();
                mc.chargerMvtStock();
                pc.chargerProduit();
                
    	} else {
    		checkFields();
    	}
    }

    @FXML
    void modifier(ActionEvent event) {
    	if(isValid()) {
            String nbe = tableViewBonEntree.getSelectionModel().getSelectedItem().getNumBonEntree();
            String np = tableViewBonEntree.getSelectionModel().getSelectedItem().getNumProduit();
            int qe = tableViewBonEntree.getSelectionModel().getSelectedItem().getQteEntree();
            String de = tableViewBonEntree.getSelectionModel().getSelectedItem().getDateEntree();
    		
            String inputNbEntre = txtNumBonEntree.getText();
            String inputNumProd = cbxNumProduit.getSelectionModel().getSelectedItem();
            int inputQteEntree = Integer.parseInt(txtQteEntree.getText());
            String inputDateEntree = new String().valueOf(dpDateEntree.getValue());
            //new SimpleDateFormat("yyyy-MM-dd",   (dpDateEntree.getValue()).format(new Date()));
            
            if(nbe.equals(inputNbEntre) && np.equals(inputNumProd) && (new String().valueOf(qe).equals(inputQteEntree))) {
                System.out.println("Modification echoué!");
            } else {
                BonEntree bde;
                bde = new BonEntree(inputNbEntre, inputNumProd, inputQteEntree, inputDateEntree);
                try {
                    updateStockProduct(cbxNumProduit.getValue(), Integer.parseInt(txtQteEntree.getText()));
                    bdeService.updateBonEntree(bde);
                    tableViewBonEntree.setItems(bdeService.getAllBonEntree());
                    dc.chargerGraph();
                    mc.chargerMvtStock();
                    pc.chargerProduit();
                } catch (Exception ex) {
                    System.out.println("Erreur de modification!");
                }
            }
            resetBdeForm();
    	} else {
            System.out.println("Entrée invalide!");
        } 
    }

    @FXML
    void supprimer(ActionEvent event) {
    	if(isValid()) {
            JOptionPane jop = new JOptionPane();
            int option = jop.showConfirmDialog(null,"Voulez-vous vraiment supprimer ?","Confirmation",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(option == JOptionPane.OK_OPTION) {
                try {
                    bdeService.deleteBonEntree(txtNumBonEntree.getText());
                    tableViewBonEntree.setItems(bdeService.getAllBonEntree());
                    resetBdeForm();
                    dc.chargerGraph();
                    mc.chargerMvtStock();
                    pc.chargerProduit();
                } catch (Exception ex) {
                    System.out.println("Erreur lors de la suppression!");
                }
            }
    	} else {
    		checkFields();
    	}
    }
    
    
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // TABLEAU
        columnNumBonEntree.setCellValueFactory(new PropertyValueFactory<BonEntree,String>("numBonEntree"));
        columnNumProduit.setCellValueFactory(new PropertyValueFactory<BonEntree,String>("numProduit"));
        columnQteEntree.setCellValueFactory(new PropertyValueFactory<BonEntree, Integer>("qteEntree"));
        columnDateEntree.setCellValueFactory(new PropertyValueFactory<BonEntree,String>("dateEntree"));
        
        chargerBE();
    }  
    
    public void chargerBE(){
                
        // put all product number to combo box
        getProductsToComboBox();
        // Charger le tableau
        try{
            tableViewBonEntree.setItems(bdeService.getAllBonEntree());
        }catch(Exception e)
        {
            System.out.println(e) ;
        }
        
        // Event lors du click sur une ligne du tableau
        tableViewBonEntree.setOnMousePressed(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent event){
                
                if( !(tableViewBonEntree.getItems().isEmpty()) ){
               
                   if( !(tableViewBonEntree.getSelectionModel().isEmpty()) ){
	                   	lblBonEntreeError.setVisible(false);
	                	lblNumProdError.setVisible(false);
	                	lblQteError.setVisible(false);
	                	lblDateError.setVisible(false);
                         //txtSearch.setDisable(true);
                         
                         //retour_produit.setVisible(true);
                         txtNumBonEntree.setText((tableViewBonEntree.getSelectionModel().getSelectedItem().getNumBonEntree()));
                         cbxNumProduit.setValue((tableViewBonEntree.getSelectionModel().getSelectedItem().getNumProduit()));
                         txtQteEntree.setText(new String().valueOf(tableViewBonEntree.getSelectionModel().getSelectedItem().getQteEntree()));
                         dpDateEntree.setValue(LocalDate.parse(tableViewBonEntree.getSelectionModel().getSelectedItem().getDateEntree()));
                         
                         //disable input
                         txtNumBonEntree.setDisable(true);
                         cbxNumProduit.setDisable(true);
                         
                   }
                }   
            }
        });
    }
    
    /**
     * Vérifier si champs non vide?
     */
    private boolean isValid() {
    	boolean valide = false;
    	lblBonEntreeError.setVisible(false);
    	lblDateError.setVisible(false);
    	lblQteError.setVisible(false);
    	lblDateError.setVisible(false);
    	if( txtNumBonEntree.getText().equals("") || 
            cbxNumProduit.getSelectionModel().isEmpty() || 
            txtQteEntree.getText().equals("") || 
            dpDateEntree.getValue() == null)
            valide = false;
    	else valide = true;
    	
    	return valide;
    }
    
    private void checkFields() {
    	if(cbxNumProduit.getSelectionModel().isEmpty()) lblNumProdError.setVisible(true);
    	if(txtNumBonEntree.getText().isEmpty()) lblBonEntreeError.setVisible(true);
    	if(txtQteEntree.getText().isEmpty()) lblQteError.setVisible(true);
    	if(!dpDateEntree.validate()) lblQteError.setVisible(true);    	
    }

    
    // Reset Forms
    @FXML
    void resetBdeForm(){

        cbxNumProduit.setValue(null);
        txtNumBonEntree.setText("");
        txtQteEntree.setText("");
        dpDateEntree.setValue(null);
        
    	lblBonEntreeError.setVisible(false);
    	lblDateError.setVisible(false);
    	lblNumProdError.setVisible(false);
    	lblQteError.setVisible(false);
        
        txtNumBonEntree.setDisable(false);
        cbxNumProduit.setDisable(false);
        cbxNumProduit.getSelectionModel().clearSelection();
        
    }
    
    
    // get all produits to
    @FXML
    private void getProductsToComboBox() {
        try {
            cbxNumProduit.setItems(bdeService.getAllProductComboBox());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
        ///***** CHERCHER ET FILTRER LA LISTE DES PRODUITS *****\\\
    
     @FXML
     private void searchBonEntree(KeyEvent ke) throws SQLException, Exception{
         
         FilteredList<BonEntree> filterData = new FilteredList<>(bdeService.getAllBonEntree(),p->true);
         txtSearch.textProperty().addListener((observable,oldvalue,newvalue)->{
         
            filterData.setPredicate( bde->{
                
                if(newvalue == null || newvalue.isEmpty()){
                	
                    return true;
                }
                String typedText = newvalue.toLowerCase();
                if(bde.getNumBonEntree().toLowerCase().indexOf(typedText) != -1){
                     return true;
                }                
                if(bde.getNumProduit().toLowerCase().indexOf(typedText) != -1){
                     return true;
                }
                if(new String().valueOf(bde.getQteEntree()).toLowerCase().indexOf(typedText) != -1){
                     return true;
                }
                if(bde.getDateEntree().toLowerCase().indexOf(typedText) != -1) {
                    return true ;
                }
                return false;
            });
            
            SortedList<BonEntree> sortedList = new SortedList<>(filterData);
            sortedList.comparatorProperty().bind(tableViewBonEntree.comparatorProperty());
            tableViewBonEntree.setItems(sortedList);
            
        });
     }
      
    public void updateStockProduct(String idProduit, int StockAjoute) throws Exception {
        Produit pdt;
        int nouveauStock;
        pdt = pdsService.getOneProduit(idProduit);
        nouveauStock = pdt.getStockProduit() + StockAjoute;
        pdt.setStockProduit(nouveauStock);
        pdsService.updateProduit(pdt);
    }

    void init(DashboardController dshController, MvtStockController msController) {
        dc = dshController;
        mc = msController;
    }

    void init(DashboardController dshController, MvtStockController msController, ProduitController prdController) {
        dc = dshController;
        mc = msController;
        pc = prdController;
    }
    
}
