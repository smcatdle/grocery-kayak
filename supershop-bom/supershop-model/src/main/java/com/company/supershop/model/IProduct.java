/** SuperShop Price Engine   **/


package com.company.supershop.model;


public interface IProduct {

    public String getName();

    public void setName(String s);

    public String getProductId();

    public void setProductId(String s);

    public String getBarcode();

    public void setBarcode(String s);

    public String getQuantity();

    public void setQuantity(String s);

    public String getImageURL();

    public void setImageURL(String s);

    public String getMaxQuantity();

    public void setMaxQuantity(String s);

    public String getIncrement();

    public void setIncrement(String s);

    public String getPrice();

    public void setPrice(String s);

    public String getAbbr();

    public void setAbbr(String s);

    public String getUnitPrice();

    public void setUnitPrice(String s);

    public String getCatchWeight();

    public void setCatchWeight(String s);

    public String getSuperdepartment();

    public void setSuperdepartment(String s);

    public String getShelfName();

    public void setShelfName(String s);

    public String getSuperdepartmentID();

    public void setSuperdepartmentID(String s);

    public boolean isPromotion();

    public void setPromotion(boolean flag);

    public String getPromotionText();

    public void setPromotionText(String s);

    public String getPromotionDiscount();

    public void setPromotionDiscount(String s);

    public String getPromotionType();

    public void setPromotionType(String s);

    public int getPromotionRanking();

    public void setPromotionRanking(int i);

    public String getChain();

    public void setChain(String s);
}
