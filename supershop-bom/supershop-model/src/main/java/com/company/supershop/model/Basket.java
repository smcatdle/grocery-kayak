/**
 * 
 */
package com.company.supershop.model;

import java.util.Date;

/**
 * @author smcardle
 *
 */
public class Basket {
	
	private int id;	
	private String name;	
	private String chain;
	private Date dateCreated;
	private Date dateUpdated;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the chain
	 */
	public String getChain() {
		return chain;
	}

	/**
	 * @param chain the chain to set
	 */
	public void setChain(String chain) {
		this.chain = chain;
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
