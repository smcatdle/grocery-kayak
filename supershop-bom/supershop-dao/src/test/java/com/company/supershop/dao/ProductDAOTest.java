package com.company.supershop.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.company.supershop.dao.impl.ProductDAOImpl;
import com.company.supershop.dao.impl.PromotionDAOImpl;
import com.company.supershop.model.ProductMVO;
import com.company.supershop.model.Promotion;

import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:business-config.xml")
public class ProductDAOTest {

    private static final Log logger = LogFactory.getLog(ProductDAOTest.class);
    
    @Autowired
    private ProductDAO productDAO;

	@Autowired
    private PromotionDAOImpl promotionDAO;
	
	
    @Test
    //@Transactional
    public void testFind()
    {
        List<ProductMVO> products = productDAO.findByName("Gel");
        logger.info("Found [" + products.size() + "] products.");
        //assertNotNull(products);
    }
    
    @Test
	public void testLoadPromotions() {
		
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
	}
}
