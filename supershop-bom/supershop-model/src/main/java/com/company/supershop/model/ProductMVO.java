package com.company.supershop.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/*import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;*/


@Entity
@Table(name = "product")
@NamedQueries({ @NamedQuery(name = "@ProductMVO.getProductsByName", query = "from ProductMVO where name like :name order by price asc"),
			  @NamedQuery(name = "@ProductMVO.getProductsByNameAndChain", query = "from ProductMVO where name = :name and chain = :chain"),
			  @NamedQuery(name = "@ProductMVO.getProductById", query = "from ProductMVO where id = :id"),
			  @NamedQuery(name = "@ProductMVO.getDepartments", query = "select distinct superDepartment from ProductMVO where chain = :chain")
			  })
//@Indexed
public class ProductMVO {


	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	@Column(name = "product_code")
//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	private String productCode;	
	
	@Column(name = "score")
	@Transient
//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	private double score;	
	
	@Column(name = "price")
//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	private double price;
	
	@Column(name = "min_price")
//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	private double minPrice;
	
	@Column(name = "max_price")
//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	private double maxPrice;
	
	@Column(name = "last_price")
//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	private double lastPrice;
	
	@Column(name = "min_price_date", nullable = false)
//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	private Date minPriceDate;
	
	@Column(name = "max_price_date", nullable = false)
//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	private Date maxPriceDate;
	
	@Column(name = "last_price_date", nullable = false)
//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	private Date lastPriceDate;	
	
	@Column(name = "name")
//	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
	private String name;
	
	@Column(name = "barcode")
//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	private String barcode;
	
	@Column(name = "chain")
//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	private String chain;
	
	@Column(name = "category")
//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	private String category;
	
	@Column(name = "image_url")
//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	private String imageURL;
	
	@Column(name = "link_url")
//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	private String linkURL;
	
	@Column(name = "super_department")
//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
    private String superDepartment;
	
	@Column(name = "shelf_name")
//	@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
    private String shelfName;
	
	@Column(name = "date_created", nullable = false)
//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	private Date dateCreated;
	
	@Column(name = "date_updated", nullable = false)
//	@Field(index=Index.YES, analyze=Analyze.NO, store=Store.YES)
	private Date dateUpdated;
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getBarcode() {
		return barcode;
	}
	public double getPrice() {
		return price;
	}
	/**
	 * @return the minPrice
	 */
	public double getMinPrice() {
		return minPrice;
	}
	/**
	 * @param minPrice the minPrice to set
	 */
	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}
	/**
	 * @return the maxPrice
	 */
	public double getMaxPrice() {
		return maxPrice;
	}
	/**
	 * @param maxPrice the maxPrice to set
	 */
	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}
	/**
	 * @return the lastPrice
	 */
	public double getLastPrice() {
		return lastPrice;
	}
	/**
	 * @param lastPrice the lastPrice to set
	 */
	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}
	/**
	 * @return the minPriceDate
	 */
	public Date getMinPriceDate() {
		return minPriceDate;
	}
	/**
	 * @param minPriceDate the minPriceDate to set
	 */
	public void setMinPriceDate(Date minPriceDate) {
		this.minPriceDate = minPriceDate;
	}
	/**
	 * @return the maxPriceDate
	 */
	public Date getMaxPriceDate() {
		return maxPriceDate;
	}
	/**
	 * @param maxPriceDate the maxPriceDate to set
	 */
	public void setMaxPriceDate(Date maxPriceDate) {
		this.maxPriceDate = maxPriceDate;
	}
	/**
	 * @return the lastPriceDate
	 */
	public Date getLastPriceDate() {
		return lastPriceDate;
	}
	/**
	 * @param lastPriceDate the lastPriceDate to set
	 */
	public void setLastPriceDate(Date lastPriceDate) {
		this.lastPriceDate = lastPriceDate;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getChain() {
		return chain;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	public void setChain(String chain) {
		this.chain = chain;
	}
	public String getSuperDepartment() {
		return superDepartment;
	}
	public void setSuperDepartment(String superDepartment) {
		this.superDepartment = superDepartment;
	}
	public String getShelfName() {
		return shelfName;
	}
	public void setShelfName(String shelfName) {
		this.shelfName = shelfName;
	}
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	public Date getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}	
	
    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 29;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((barcode == null) ? 0 : barcode.hashCode());
        result = prime * result + ((chain == null) ? 0 : chain.hashCode());
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((superDepartment == null) ? 0 : superDepartment.hashCode());
        result = prime * result + ((shelfName == null) ? 0 : shelfName.hashCode());
        return result;
    }
    
    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ProductMVO)) {
            return false;
        }
        ProductMVO other = (ProductMVO) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (chain == null) {
            if (other.chain != null) {
                return false;
            }
        } else if (!chain.equals(other.chain)) {
            return false;
        }
        return true;
    }
    
    
	public void setProduct(IProduct product) {  	
        
        Date todaysDate = new Date();
        double newPrice = new Double(product.getPrice()).doubleValue();
        
        // Record the old price if changed
        if (price != newPrice) {
	    	this.setLastPriceDate(todaysDate);
	    	this.setLastPrice(this.price);
        }
    	
        this.setPrice(newPrice);	        
        this.setName(product.getName());
        this.setBarcode(product.getBarcode());
        this.setChain(product.getChain());
        this.setCategory("S");
        this.setImageURL(product.getImageURL());
        this.setSuperDepartment(product.getSuperdepartment());
        this.setShelfName(product.getShelfName());
        this.setDateCreated(new Date());
        this.setDateUpdated(new Date());
    	
        // Set the product history
        if (this.maxPrice == 0) {   // New product
        	this.setMaxPrice(this.price);
        	this.setMinPrice(this.price);
        	this.setMaxPriceDate(todaysDate);
        	this.setMinPriceDate(todaysDate);
        	this.setLastPriceDate(todaysDate);
	    	this.setLastPrice(this.price);
        } else {
        	if (maxPrice < price) {
        		maxPrice = price;
        		maxPriceDate = todaysDate;
        	}
        	
        	if (minPrice > price) {
        		minPrice = price;
        		minPriceDate = todaysDate;
        	}
        }
        
	}
	
}
