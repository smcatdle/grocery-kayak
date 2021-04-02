/** SuperShop Price Engine   **/

package com.company.supershop.model;


public class Product implements IProduct {


    private String name;
    private String productId;
    private String barcode;
    private String quantity;
    private String imageURL;
    private String linkURL;
    private String maxQuantity;
    private String increment;
    private String price;
    private String lastPrice;
    private String abbr;
    private String unitPrice;
    private String catchWeight;
    private String superdepartment;
    private String shelfName;
    private String superdepartmentID;
    private boolean promotion;
    private String promotionText;
    private String promotionDiscount;
    private String promotionType;
    private int promotionRanking;
    private String xsiType;
    private String baseProductId;
    private String isPermanentlyUnavailable;
    private String chain;
    
    
    public Product() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getLinkURL() {
		return linkURL;
	}

	public void setLinkURL(String linkURL) {
		this.linkURL = linkURL;
	}

	public String getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(String maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public String getIncrement() {
        return increment;
    }

    public void setIncrement(String increment) {
        this.increment = increment;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String price) {
        this.lastPrice = price;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCatchWeight() {
        return catchWeight;
    }

    public void setCatchWeight(String catchWeight) {
        this.catchWeight = catchWeight;
    }

    public String getSuperdepartment() {
        return superdepartment;
    }

    public void setSuperdepartment(String superdepartment) {
        this.superdepartment = superdepartment;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    public String getSuperdepartmentID() {
        return superdepartmentID;
    }

    public void setSuperdepartmentID(String superdepartmentID) {
        this.superdepartmentID = superdepartmentID;
    }

    public boolean isPromotion() {
        return promotion;
    }

    public void setPromotion(boolean promotion) {
        this.promotion = promotion;
    }

    public String getPromotionText() {
        return promotionText;
    }

    public void setPromotionText(String promotionText) {
        this.promotionText = promotionText;
    }

    public String getPromotionDiscount() {
        return promotionDiscount;
    }

    public void setPromotionDiscount(String promotionDiscount) {
        this.promotionDiscount = promotionDiscount;
    }

    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }

    public int getPromotionRanking() {
        return promotionRanking;
    }

    public void setPromotionRanking(int promotionRanking) {
        this.promotionRanking = promotionRanking;
    }


    public String getXsiType() {
        return xsiType;
    }

    public void setXsiType(String xsiType) {
        this.xsiType = xsiType;
    }

    public String getBaseProductId() {
        return baseProductId;
    }

    public void setBaseProductId(String baseProductId) {
        this.baseProductId = baseProductId;
    }

    public String getIsPermanentlyUnavailable() {
        return isPermanentlyUnavailable;
    }

    public void setIsPermanentlyUnavailable(String isPermanentlyUnavailable) {
        this.isPermanentlyUnavailable = isPermanentlyUnavailable;
    }
    
    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

}
