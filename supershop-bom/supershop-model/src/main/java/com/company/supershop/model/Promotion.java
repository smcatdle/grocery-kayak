/**
 * 
 */
package com.company.supershop.model;

import java.util.Date;
import java.util.List;

/**
 * @author smcardle
 *
 */
public class Promotion {
	
	private int id;	
	private String name;	
	private int priority;
	private List<PromotionString> promotionStrings;
	private Date dateCreated;
	private Date dateUpdated;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public List<PromotionString> getPromotionStrings() {
		return promotionStrings;
	}
	public void setPromotionStrings(List<PromotionString> promotionStrings) {
		this.promotionStrings = promotionStrings;
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
	
}
