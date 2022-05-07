/**
 * 
 */
package app.entity;

/**
 * @author Finaritra
 *
 */
public class EtatStock {
    String numProduit ;
    String designProduit ;
    int stockProduit ;

    public EtatStock(String numProduit, String designProduit, int stock) {
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
