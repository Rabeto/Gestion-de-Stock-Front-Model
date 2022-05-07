package app.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;

import dashboard.FXMLDocumentController;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class AppController implements Initializable {
    @FXML
    private JFXButton btnDashboard;
    
    @FXML
    private AnchorPane holderPane;

    @FXML
    private JFXButton btnProduit;

    @FXML
    private JFXButton btnBonEntree;

    @FXML
    private JFXButton btnBonSortie;

    @FXML
    private JFXButton btnEtatStock;

    AnchorPane produit,bonEntree,bonSortie,etatStock,mvtStock, dashBoard, login;

    @FXML
    private JFXButton btnMvtStock;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
        try {
        	FXMLLoader produitLoader = new FXMLLoader(getClass().getResource("/app/views/Produit.fxml"));
        	FXMLLoader dashBoardLoader = new FXMLLoader(getClass().getResource("/app/views/Dashboard.fxml"));
        	FXMLLoader etatStLoader = new FXMLLoader(getClass().getResource("/app/views/EtatStock.fxml"));
                FXMLLoader beLoader = new FXMLLoader(getClass().getResource("/app/views/BonEntree.fxml"));
        	FXMLLoader bsLoader = new FXMLLoader(getClass().getResource("/app/views/BonSortie.fxml"));
        	FXMLLoader mvtLoader = new FXMLLoader(getClass().getResource("/app/views/MvtStock.fxml"));
        	produit = produitLoader.load();
        	dashBoard = dashBoardLoader.load();
        	etatStock = etatStLoader.load();
                bonEntree = beLoader.load();
                bonSortie = bsLoader.load();
                mvtStock = mvtLoader.load();
        	ProduitController prdController = produitLoader.getController();
        	DashboardController dshController = dashBoardLoader.getController();
        	EtatStockController etsController = etatStLoader.getController();
                BonEntreeController beController = beLoader.getController();
                BonSortieController bsController = bsLoader.getController();
                MvtStockController msController = mvtLoader.getController();
                
        	prdController.init(dshController, etsController, beController, bsController, msController);
                beController.init(dshController, msController, prdController);
                bsController.init(dshController, msController, prdController);
                
           setNode(dashBoard);
       } catch (IOException ex) {
           Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
       }		
	}

    @FXML
    private void switchProduit(ActionEvent event) {
        setNode(produit);
    }

    @FXML
    void switchDashboard(ActionEvent event) {
        setNode(dashBoard);
    }
    
    @FXML
    private void switchBonEntree(ActionEvent event) {
        setNode(bonEntree);
    }

    @FXML
    private void switchBonSortie(ActionEvent event) {
        setNode(bonSortie);
    }

    @FXML
    private void switchEtatStock(ActionEvent event) {
        setNode(etatStock);
    }

    @FXML
    private void switchMvtStock(ActionEvent event) {
        setNode(mvtStock);
    }

	private void setNode(Node node) {
        holderPane.getChildren().clear();
        holderPane.getChildren().add((Node) node);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.00);
        FadeTransition ft = new FadeTransition(Duration.millis(1500));
        ft.setNode(node);
        ft.setFromValue(0.1);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
	}
}
