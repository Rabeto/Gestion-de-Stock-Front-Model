/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.entity;

/**
 *
 * @author Mac Carthy
 */
public class Produit {
    
    String numProduit ;
    String designProduit ;
    int stockProduit ;

    public Produit(String numProduit, String designProduit, int stock) {
        this.numProduit = numProduit;
        this.designProduit = designProduit;
        this.stockProduit = stock;
    }

    public String getNumProduit() {
        return numProduit;
    }

    public void setNumProduit(String numProduit) {
        this.numProduit = numProduit;
    }

    public String getDesignProduit() {
        return designProduit;
    }

    public void setDesignProduit(String designProduit) {
        this.designProduit = designProduit;
    }

    public int getStockProduit() {
        return stockProduit;
    }

    public void setStockProduit(int stock) {
        this.stockProduit = stock;
    }  
    
}
