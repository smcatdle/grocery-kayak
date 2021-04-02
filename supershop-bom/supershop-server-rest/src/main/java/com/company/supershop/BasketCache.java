/**
 * 
 */
package com.company.supershop;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.company.supershop.dao.impl.BasketDAOImpl;
import com.company.supershop.model.Basket;

/**
 * @author smcardle
 *
 */
@Component
@Order(value=2)
public class BasketCache {

	private final Logger logger = Logger.getLogger(BasketCache.class.getName());
	
	
    private BasketDAOImpl basketDAO;
	
	
	private List<Basket> basketCache = null;
	
	
	@Autowired
	private BasketCache(BasketDAOImpl basketDAO) {
		this.basketDAO = basketDAO;
		loadBaskets();
	}
	
	public List<Basket> getBaskets() {
		return basketCache;
		
	}
	
	private void loadBaskets() {
		
		basketCache = basketDAO.getBaskets();

	}
	
}
