/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.entity;

import java.util.List;

/**
 *
 * @author Finaritra
 */
public class MvtStock {
	String num;
	String qteEntree;
	String qteSortie;
	String numProduit;
	String designProduit;
	String date;
	
	
	
	public MvtStock(BonEntree data, List<Produit> produit) {
		this.num = data.getNumBonEntree();
		this.qteEntree = Integer.toString(data.getQteEntree());
		this.qteSortie = null;
		this.date = data.getDateEntree();
		for (Produit produit2 : produit) {
			if(data.getNumProduit().equals(produit2.getNumProduit()))
				{
					this.numProduit = data.getNumProduit();
					this.designProduit = produit2.getDesignProduit();
					break;
				}
				
		}
	}
	
	public MvtStock(BonSortie data, List<Produit> produit) {
		this.num = data.getNumBonSortie();
		this.qteSortie = Integer.toString(data.getQteSortie());
		this.qteEntree = null;
		this.date = data.getDateSortie();
		for (Produit produit2 : produit) {
			if(data.getNumProduit().equals(produit2.getNumProduit()))
				{
					this.numProduit = data.getNumProduit();
					this.designProduit = produit2.getDesignProduit();
					break;
				}
				
		}
	}
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public String getQteEntree() {
		return qteEntree;
	}
	public void setQteEntree(String qteEntree) {
		this.qteEntree = qteEntree;
	}
	public String getQteSortie() {
		return qteSortie;
	}
	public void setQteSortie(String qteSortie) {
		this.qteSortie = qteSortie;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
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
	
	
	
}

