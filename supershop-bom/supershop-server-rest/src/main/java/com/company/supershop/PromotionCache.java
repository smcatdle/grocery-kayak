/**
 * 
 */
package com.company.supershop;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.company.supershop.dao.impl.ProductDAOImpl;
import com.company.supershop.dao.impl.PromotionDAOImpl;
import com.company.supershop.model.ProductMVO;
import com.company.supershop.model.Promotion;

/**
 * @author smcardle
 *
 */
@Component
@Order(value=2)
public class PromotionCache {

	private final Logger logger = Logger.getLogger(PromotionCache.class.getName());	
	
    private PromotionDAOImpl promotionDAO;	
    
    private ProductDAOImpl productDAO;
	
	private List<ProductMVO> promotionCache = null;
	
	
	@Autowired
	private PromotionCache(PromotionDAOImpl promotionDAO, ProductDAOImpl productDAO) {
		this.promotionDAO = promotionDAO;
		this.productDAO = productDAO;
		//productDAO.indexProducts();
		loadPromotions();
	}
	
	public List<ProductMVO> getPromotions() {
		return promotionCache;
		
	}

	public void loadPromotions() {
		
		List<Promotion> promotions = null;
		List<ProductMVO> promotedProducts = new ArrayList<ProductMVO>();
		ProductMVO product = null;
		
		promotions = promotionDAO.findAll();
		
		for (Promotion promotion: promotions) {
			logger.info("Retrieved promotion [" + promotion.getName() + "]");
			
			product = promotionDAO.getPromotionProduct(promotion.getPromotionStrings());
			
			if (product != null) {
				promotedProducts.add(product);
				logger.info("Matched promoted product [" + product.getName() + "] price [" + product.getPrice() + "]");
			}
		}
		
		// Cache the promoted products
		promotionCache = promotedProducts;
	}
	
}
