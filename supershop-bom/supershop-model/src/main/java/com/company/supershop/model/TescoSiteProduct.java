/** Scanshop Price Engine   **/

package com.company.supershop.model;

public class TescoSiteProduct {

	/*
	 * {name:"Eagle Hawk Cabernet Sauvignon 75Cl",xsiType:"QuantityOnlyProduct",
	 * productId
	 * :"257785460",baseProductId:"50183697",quantity:1,isPermanentlyUnavailable
	 * :true,imageURL:
	 * "http://img.tesco.ie/Groceries/pi/551/9300770043551/IDShot_90x90.jpg"
	 * ,maxQuantity
	 * :99,increment:1,price:9,abbr:"cl",unitPrice:9,catchWeight:"0",
	 * shelfName:"Australian"
	 * ,superdepartment:"Drinks",superdepartmentID:"TO_1569125000"}
	 */

	private String name;    
	private String xsiType;         
	private String productId;  
	private String baseProductId;   
	private String quantity;    
	private String isPermanentlyUnavailable;    
	private String imageURL;   
	private String maxQuantity;    
	private String increment;     
	private String price;     
	private String abbr;    
	private String unitPrice;    
	private String catchWeight;    
	private String shelfName;    
	private String superdepartment;    
	private String superdepartmentID;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getXsiType() {
		return xsiType;
	}
	public void setXsiType(String xsiType) {
		this.xsiType = xsiType;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getBaseProductId() {
		return baseProductId;
	}
	public void setBaseProductId(String baseProductId) {
		this.baseProductId = baseProductId;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getIsPermanentlyUnavailable() {
		return isPermanentlyUnavailable;
	}
	public void setIsPermanentlyUnavailable(String isPermanentlyUnavailable) {
		this.isPermanentlyUnavailable = isPermanentlyUnavailable;
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
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
	public String getShelfName() {
		return shelfName;
	}
	public void setShelfName(String shelfName) {
		this.shelfName = shelfName;
	}
	public String getSuperdepartment() {
		return superdepartment;
	}
	public void setSuperdepartment(String superdepartment) {
		this.superdepartment = superdepartment;
	}
	public String getSuperdepartmentID() {
		return superdepartmentID;
	}
	public void setSuperdepartmentID(String superdepartmentID) {
		this.superdepartmentID = superdepartmentID;
	}    


	

}
