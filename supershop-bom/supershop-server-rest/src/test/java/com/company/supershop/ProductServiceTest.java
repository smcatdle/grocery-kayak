package com.company.supershop;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.company.supershop.model.ProductMVO;
import com.company.supershop.model.Promotion;
import com.company.supershop.services.ProductService;

import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:business-config.xml")
public class ProductServiceTest {

    private static final Log logger = LogFactory.getLog(ProductServiceTest.class);
    
    @Autowired
    private ProductService productService;

    /*@Test
    //@Transactional
    public void testFind()
    {
        List<ProductMVO> products = productService.searchProductEntries("Gel");
        logger.info("Found [" + products.size() + "] products.");
        assertNotNull(products);
    }*/
    
    @Test
    //@Transactional
    public void testGetAlternativeProducts()
    {
    	productService.indexProducts();
    	
        List<ProductMVO> alternatives = productService.getAlternativeProducts("Ben & Jerrys Chocolate Fudge Brownie Ice Cream (150 Millilitre)");
        logger.info("Found [" + alternatives.size() + "] alternatives.");
        
		for (ProductMVO alternative: alternatives) {
			logger.info("Retrieved alternative [" + alternative.getName() + "]");
			
		}
        assertNotNull(alternatives);
    }
}
