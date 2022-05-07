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
import app.entity.BonSortie;
import app.entity.Produit;
import app.services.BonSortieService;
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
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;

public class BonSortieController implements Initializable {

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private TableView<BonSortie> tableViewBonSortie;

    @FXML
    private TableColumn<BonSortie, String> columnNumBonSorite;

    @FXML
    private TableColumn<BonSortie, String> columnNumProduit;

    @FXML
    private TableColumn<BonSortie, Integer> columnQteSortie;

    @FXML
    private TableColumn<BonSortie, String> columnDateSorite;

    @FXML
    private JFXTextField txtNumBonSortie;

    @FXML
    private JFXComboBox<String> cbxNumProduit;

    @FXML
    private JFXTextField txtQteSortie;

    @FXML
    private JFXDatePicker dtpickerDateSortie;

    @FXML
    private JFXButton btnAjouter;

    @FXML
    private JFXButton btnSupprimer;

    @FXML
    private JFXButton btnModifier;
        
    @FXML
    private Label lblBonSortieError;

    @FXML
    private Label lblNumProdError;

    @FXML
    private Label lblQteError;

    @FXML
    private Label lblDateError;
    
    @FXML
    private JFXButton btnRefresh;
        
    // services
    BonSortieService bdsService = new BonSortieService() ;
    ProduitService pdsService = new ProduitService();
    DashboardController dc;
    MvtStockController mc;
    ProduitController pc;
    
    @FXML
    void ajouter(ActionEvent event) throws Exception  {
    	
        if(isValid()) {
            
            if(checkStockMin(cbxNumProduit.getValue(), Integer.parseInt(txtQteSortie.getText()))){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("La quantité en stock est insuffisante");
                alert.setHeaderText("Stock insuffisante");
                alert.show();
            } else {
                            
            BonSortie bds = new BonSortie(txtNumBonSortie.getText(), cbxNumProduit.getValue(), Integer.parseInt(txtQteSortie.getText()), String.valueOf(dtpickerDateSortie.getValue()));
                try {
                    bdsService.createBonSortie(bds);
                    tableViewBonSortie.setItems(bdsService.getAllBonSortie());
                } catch (Exception error) {
                    System.out.print(error + "Ajout ereur!");
                }
                //update stock
                updateStockProduct(cbxNumProduit.getValue(), Integer.parseInt(txtQteSortie.getText()));
                resetStockProdForm();
                dc.chargerGraph();
                mc.chargerMvtStock();
                pc.chargerProduit();
            }
                
    	} else {
    		checkFields();
    	}
    }

    @FXML
    void modifier(ActionEvent event) {
    	if(isValid()) {
            String nbs = tableViewBonSortie.getSelectionModel().getSelectedItem().getNumBonSortie();
            String np = tableViewBonSortie.getSelectionModel().getSelectedItem().getNumProduit();
            int qs = tableViewBonSortie.getSelectionModel().getSelectedItem().getQteSortie();
            String ds = tableViewBonSortie.getSelectionModel().getSelectedItem().getDateSortie();
    		
            String inputNbSortie = txtNumBonSortie.getText();
            String inputNumProd = (String) cbxNumProduit.getSelectionModel().getSelectedItem();
            int inputQteSortie = Integer.parseInt(txtQteSortie.getText());
            String inputDateSortie = new String().valueOf(dtpickerDateSortie.getValue());
            
            if(nbs.equals(inputNbSortie) && np.equals(inputNumProd) && (new String().valueOf(qs).equals(inputDateSortie))) {
                System.out.println("Modification echoué!");
            } else {
                BonSortie bds;
                bds = new BonSortie(inputNbSortie, inputNumProd, inputQteSortie, inputDateSortie);
                try {
                    //updateStockProduct(cbxNumProduit.getValue(), Integer.parseInt(txtQteEntree.getText()));
                    bdsService.updateBonSortie(bds);
                    tableViewBonSortie.setItems(bdsService.getAllBonSortie());
                    dc.chargerGraph();
                    mc.chargerMvtStock();
                    pc.chargerProduit();
                } catch (Exception ex) {
                    System.out.println("Erreur de modification!");
                }
            }
            resetStockProdForm();
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
                  //updateStockProductSuppr(cbxNumProduit.getValue(), Integer.parseInt(txtQteSortie.getText()));
                    bdsService.deleteBonSortie(txtNumBonSortie.getText());
                    tableViewBonSortie.setItems(bdsService.getAllBonSortie());
                    resetStockProdForm();
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
        columnNumBonSorite.setCellValueFactory(new PropertyValueFactory<BonSortie,String>("numBonSortie"));
        columnNumProduit.setCellValueFactory(new PropertyValueFactory<BonSortie,String>("numProduit"));
        columnQteSortie.setCellValueFactory(new PropertyValueFactory<BonSortie, Integer>("qteSortie"));
        columnDateSorite.setCellValueFactory(new PropertyValueFactory<BonSortie,String>("dateSortie"));
        
        chargerBS();
    }    
    
    public void chargerBS(){
                // put all product number to combo box
        getProductsToComboBox();
        // Charger le tableau
        try{
            tableViewBonSortie.setItems(bdsService.getAllBonSortie());
        }catch(Exception e)
        {
            System.out.println(e) ;
        }
        
        // Event lors du click sur une ligne du tableau
        tableViewBonSortie.setOnMousePressed(new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent event){
                
                if( !(tableViewBonSortie.getItems().isEmpty()) ){
               
                   if( !(tableViewBonSortie.getSelectionModel().isEmpty()) ){
	                   	lblBonSortieError.setVisible(false);
	                	lblNumProdError.setVisible(false);
	                	lblQteError.setVisible(false);
	                	lblDateError.setVisible(false);
                         //txtSearch.setDisable(true);
                         
                         //retour_produit.setVisible(true);
                         txtNumBonSortie.setText((tableViewBonSortie.getSelectionModel().getSelectedItem().getNumBonSortie()));
                         cbxNumProduit.setValue((tableViewBonSortie.getSelectionModel().getSelectedItem().getNumProduit()));
                         txtQteSortie.setText(new String().valueOf(tableViewBonSortie.getSelectionModel().getSelectedItem().getQteSortie()));
                         dtpickerDateSortie.setValue(LocalDate.parse(tableViewBonSortie.getSelectionModel().getSelectedItem().getDateSortie()));
                         
                         //disable input
                         txtNumBonSortie.setDisable(true);
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
    	lblBonSortieError.setVisible(false);
    	lblDateError.setVisible(false);
    	lblQteError.setVisible(false);
    	lblDateError.setVisible(false);
    	if( txtNumBonSortie.getText().equals("") || 
            cbxNumProduit.getSelectionModel().isEmpty() || 
            txtQteSortie.getText().equals("") || 
            dtpickerDateSortie.getValue() == null)
    		valide = false;
    	else valide = true;
    	
    	return valide;
    }
    
    private void checkFields() {
    	if(txtNumBonSortie.getText().isEmpty()) lblBonSortieError.setVisible(true);
    	if(cbxNumProduit.getSelectionModel().isEmpty()) lblNumProdError.setVisible(true);
    	if(txtQteSortie.getText().isEmpty()) lblQteError.setVisible(true);
    	if(!dtpickerDateSortie.validate()) lblQteError.setVisible(true);    	
    }
    
    // Reset Forms
    @FXML
    void resetStockProdForm(){
        
        //txtSearch.setDisable(false);
                         
        //retour_produit.setVisible(true);

        cbxNumProduit.setValue(null);
        txtNumBonSortie.setText("");
        txtQteSortie.setText("");
        dtpickerDateSortie.setValue(null);
        
    	lblBonSortieError.setVisible(false);
    	lblDateError.setVisible(false);
    	lblNumProdError.setVisible(false);
    	lblQteError.setVisible(false);
        
        txtNumBonSortie.setDisable(false);
        cbxNumProduit.setDisable(false);
        cbxNumProduit.getSelectionModel().clearSelection();
        
    }
    
        
    // get all produits to
    @FXML
    private void getProductsToComboBox() {
        try {
            cbxNumProduit.setItems(bdsService.getAllProductComboBox());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
        ///***** CHERCHER ET FILTRER LA LISTE DES PRODUITS *****\\\
    
     @FXML
     private void searchBonSortie(KeyEvent ke) throws SQLException, Exception{
         
         FilteredList<BonSortie> filterData = new FilteredList<>(bdsService.getAllBonSortie(),p->true);
         txtSearch.textProperty().addListener((observable,oldvalue,newvalue)->{
         
            filterData.setPredicate( bde->{
                
                if(newvalue == null || newvalue.isEmpty()){
                	
                    return true;
                }
                String typedText = newvalue.toLowerCase();
                if(bde.getNumBonSortie().toLowerCase().indexOf(typedText) != -1){
                     return true;
                }                
                if(bde.getNumProduit().toLowerCase().indexOf(typedText) != -1){
                     return true;
                }
                if(new String().valueOf(bde.getQteSortie()).toLowerCase().indexOf(typedText) != -1){
                     return true;
                }
                if(bde.getDateSortie().toLowerCase().indexOf(typedText) != -1) {
                    return true ;
                }
                return false;
            });
            
            SortedList<BonSortie> sortedList = new SortedList<>(filterData);
            sortedList.comparatorProperty().bind(tableViewBonSortie.comparatorProperty());
            tableViewBonSortie.setItems(sortedList);
            
        });
     }
      
           
    public void updateStockProduct(String idProduit, int StockRetire) throws Exception {
        Produit pdt;
        int nouveauStock;
        pdt = pdsService.getOneProduit(idProduit);
        nouveauStock = pdt.getStockProduit() - StockRetire;
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

    private void updateStockProductSuppr(String idProduit, int StockRetire) throws Exception {
        Produit pdt;
        int nouveauStock;
        pdt = pdsService.getOneProduit(idProduit);
        nouveauStock = pdt.getStockProduit() + StockRetire;
        pdt.setStockProduit(nouveauStock);
        pdsService.updateProduit(pdt);    
    }
    
    private boolean checkStockMin(String idProduit, int stockRetire) throws Exception{
        Produit pdt;
        boolean impossible = false;
        pdt = pdsService.getOneProduit(idProduit);
        if(pdt.getStockProduit() < stockRetire) impossible = true;
        return impossible;
    }
    
    @FXML
    void checkQte(KeyEvent event) throws Exception {
        Produit pdt;
        pdt = pdsService.getOneProduit(cbxNumProduit.getValue());
    	txtQteSortie.textProperty().addListener((observable,oldvalue,newvalue)->{
            try {
                if(checkStockMin(cbxNumProduit.getValue(), Integer.parseInt(newvalue))){
                    lblQteError.setVisible(true);
                } else {
                    lblQteError.setVisible(false);
                }
            } catch (Exception ex) {
                lblQteError.setVisible(true);
            }
        });
    }
}
