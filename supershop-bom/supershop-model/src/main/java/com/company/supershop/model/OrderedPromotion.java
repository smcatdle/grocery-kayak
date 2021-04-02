/** Scanshop Price Engine   **/


package com.company.supershop.model;


public class OrderedPromotion {

    private String shelfName;
    private String promotionsKey;
    
    
    public OrderedPromotion() {
        shelfName = null;
        promotionsKey = null;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    public String getPromotionsKey() {
        return promotionsKey;
    }

    public void setPromotionsKey(String promotionsKey) {
        this.promotionsKey = promotionsKey;
    }


}
