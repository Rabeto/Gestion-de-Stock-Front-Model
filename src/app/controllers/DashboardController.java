/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import app.entity.BonEntree;
import app.entity.BonSortie;
import app.entity.Produit;
import app.services.BonEntreeService;
import app.services.BonSortieService;
import app.services.ProduitService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 *
 * @author Finaritra
 */
public class DashboardController implements Initializable{
    
    @FXML
    public PieChart pieChart;
    @FXML
    public Label qteProduit;

    @FXML
    public Label qteEntree;

    @FXML
    public Label qteSortie;
    
    @FXML
    private Button reload;
        
    public ObservableList<PieChart.Data> data = FXCollections. observableArrayList();
    public BonEntreeService bes = new BonEntreeService();
    public BonSortieService bss = new BonSortieService();
    public ProduitService pds = new ProduitService() ;
    public List<Produit> produits;
    public List<BonEntree> bonEntrees;
    public List<BonSortie> bonSorties;    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	chargerGraph();
    }    
    @FXML
    public void chargerGraph() {

    	try {
			produits = pds.getAllProduits();
			bonEntrees = bes.getAllBonEntree();
			bonSorties = bss.getAllBonSortie();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	int totalProduit = 0;
    	int totalBE = 0;
    	int totalBS = 0;
    	
    	for (BonEntree bonEntree : bonEntrees) {
			totalBE = totalBE + bonEntree.getQteEntree();
		}
    	for (BonSortie bonEntree : bonSorties) {
			totalBS = totalBS + bonEntree.getQteSortie();
		}
    	for (Produit bonEntree : produits) {
			totalProduit = totalProduit + bonEntree.getStockProduit();
		}
    	data.clear();
        pieChart.setTitle("Produits 2019");
        pieChart.setData(data);
        data.add(new PieChart.Data("Produits", totalProduit));
        data.add(new PieChart.Data("Entrées", totalBE));
        data.add(new PieChart.Data("Sorties", totalBS));

        qteProduit.setText(Integer.toString(totalProduit));
        qteEntree.setText(Integer.toString(totalBE));
        qteSortie.setText(Integer.toString(totalBS));
    }
    
}
