/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.entity;

import java.util.Date;

/**
 *
 * @author Poly
 */
public class BonSortie {
    
    String numBonSortie;
    String numProduit;
    int qteSortie;
    String dateSortie;
        
    public BonSortie(String numBonSortie, String numProduit, int qteSortie, String dateSortie) {
        this.numBonSortie = numBonSortie;
        this.numProduit = numProduit;
        this.qteSortie = qteSortie;
        this.dateSortie = dateSortie;
    }
    
    public String getNumBonSortie() {
        return numBonSortie;
    }

    public String getNumProduit() {
        return numProduit;
    }

    public int getQteSortie() {
        return qteSortie;
    }

    public String getDateSortie() {
        return dateSortie;
    }
    
    public void setNumBonSortie(String numBonSortie) {
        this.numBonSortie = numBonSortie;
    }

    public void setNumProduit(String numProduit) {
        this.numProduit = numProduit;
    }

    public void setQteSortie(int qteSortie) {
        this.qteSortie = qteSortie;
    }

    public void setDateSortie(String dateSortie) {
        this.dateSortie = dateSortie;
    }

}
