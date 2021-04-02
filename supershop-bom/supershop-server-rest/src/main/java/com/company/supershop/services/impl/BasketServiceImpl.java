package com.company.supershop.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.supershop.BasketCache;
import com.company.supershop.dao.BasketDAO;
import com.company.supershop.dao.ProductDAO;
import com.company.supershop.exception.NoBasketsFoundException;
import com.company.supershop.model.Basket;
import com.company.supershop.model.BasketItem;
import com.company.supershop.model.ProductMVO;
import com.company.supershop.services.BasketService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Service
@Transactional
public class BasketServiceImpl implements BasketService {

	final SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyy-MM-dd" );
    
    
    private final Logger logger = Logger.getLogger(BasketServiceImpl.class.getName());
    
    
    @Autowired
    private BasketDAO basketDAO;

    @Autowired
    private ProductDAO productDAO;
    
	@Autowired
    private BasketCache basketCache;
	
	// Record the time the server restarts (once a day).
	private Date todaysBaseDate;
	
	public BasketServiceImpl() {
		Date todaysDate= new Date();
		try {
			String month =  (todaysDate.getMonth()+1) > 9 ? (todaysDate.getMonth()+1)+"" : "0" + (todaysDate.getMonth()+1);
			logger.log(Level.INFO, "Updating todays date - Month..." + (1900 + todaysDate.getYear()) + "-" + month + "-" + todaysDate.getDate());
			todaysBaseDate = dateFormatter.parse((1900 + todaysDate.getYear()) + "-" + month + "-" + todaysDate.getDate());
		} catch (ParseException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
		}
	}
	
    @Override
    public List<Basket> getBaskets() {
    	List<Basket> baskets = basketCache.getBaskets();
        if(baskets == null)
        {
            throw new NoBasketsFoundException();
        }
        return baskets;
    }

    @Override
    public List<BasketItem> swapBasketItem(ProductMVO newProductMVO, int oldBasketId, int oldBasketItemId, int accountId, boolean refresh) {
    	
    	List<BasketItem> basketItems = null;
    	
    	deleteBasketItem(oldBasketId, oldBasketItemId, accountId, false);
    	basketItems = addBasketItem(newProductMVO, accountId, false);

        if (refresh) {
            basketItems = this.getBasketItems(oldBasketId, accountId);
        } else {
            basketItems = null;
        }
    	
    	return basketItems;
    }
    
    
    @Override
    public List<BasketItem> addBasketItem(ProductMVO productMVO, int accountId, boolean refresh) {

    	List<BasketItem> basketItems = null;
    	
    	// First get the product to be added.
    	ProductMVO lookupProduct = productDAO.findById(productMVO.getId()).get(0);
    	
    	// Now find the basket the product belongs to (there is a basket for each registered chain)
    	Basket lookupBasket = basketDAO.findByChain(lookupProduct.getChain()).get(0);
    	
        logger.log(Level.INFO, "Adding basketItem with productId : [" + productMVO.getId() + "]");

    	BasketItem basketItem = new BasketItem();
    	basketItem.setAccountId(accountId);
    	basketItem.setProductId(productMVO.getId());
    	basketItem.setBasketId(lookupBasket.getId());
    	basketItem.setPrice(lookupProduct.getPrice());
    	basketItem.setBarcode(lookupProduct.getBarcode());
    	basketItem.setAmount(1);
    	basketItem.setChain(lookupProduct.getChain());
    	basketItem.setImageURL(lookupProduct.getImageURL());
    	basketItem.setName(lookupProduct.getName());
    	basketItem.setUrl("");
    	basketItem.setAvailable(true);
    	basketItem.setDateCreated(new Date());
    	basketItem.setDateUpdated(new Date());

    	basketDAO.attachDirty(basketItem);
    	
        logger.log(Level.INFO, "Adding basket item Id : [" + basketItem.getId() + "] basketId : [" + basketItem.getBasketId() + "] productId [" + basketItem.getProductId() + "]");

    	if (refresh) {
    		basketItems = this.getBasketItems(lookupBasket.getId(), accountId);
    	} else {
    		basketItems = null;
    	}
    	
    	return basketItems;
    	
    }
    
    @Override
    public List<BasketItem> updateBasketItem(BasketItem basketItem, int accountId, boolean refresh) {
    	
    	logger.log(Level.INFO, "Updating BasketItem id : [" + basketItem.getId() + "] to be [" + basketItem.getAmount() + "] items.");
    	BasketItem updatedBasketItem = basketDAO.getBasketItem(basketItem.getId(), accountId);
    	
    	updatedBasketItem.setAmount(basketItem.getAmount());
        updatedBasketItem.setStrikethrough(basketItem.getStrikethrough());
    	
    	basketDAO.update(updatedBasketItem);
    	
    	if (refresh) {
    		return this.getBasketItems(basketItem.getBasketId(), accountId);
    	} else {
    		return null;
    	}
    }
    
    @Override
    public List<BasketItem> deleteBasketItem(int basketId, int basketItemId, int accountId, boolean refresh) {
    	
    	List<BasketItem> basketItems = null;
    	
        logger.log(Level.INFO, "Deleting basket item Id : [" + basketItemId + "] basketId : [" + basketId + "]");

    	basketDAO.deleteBasketItem(basketItemId, accountId);
    	
    	if (refresh) {
    		basketItems = getBasketItems(basketId, accountId);
    	} else {
    		basketItems = null;
    	}
    	
    	return basketItems;
    }
    
    @Override
	public List<BasketItem> getBasketItems(int basketId, int accountId) {

        List<BasketItem> basketItems = null;

        try {

        	logger.log(Level.INFO, "getBasketItems " + todaysBaseDate.toString());
        	
            Hashtable<String, ProductMVO> updatedProductsHash = new Hashtable<String, ProductMVO>();
            List<String> names = new ArrayList<String>();
            
            // If first basket, load all basket items for all baskets (lets user see complete list)
            if (basketId == 0) {
                basketItems = basketDAO.getAllBasketItems(accountId);
            } else {
    		  basketItems = basketDAO.getBasketItems(basketId, accountId);
            }

    		Date dateUpdated = (basketItems != null) ? basketItems.get(0).getDateUpdated() : null;
    		
    		// Only update the basket items on the DB once a day (as prices only update once a day).
            if (basketItems != null && basketItems.size() > 0 && dateUpdated != null && dateUpdated.before(todaysBaseDate)) {

            	logger.log(Level.INFO, "Check which products are still available");
            			
                // Check which products are still available (the product for this basket item could have been deleted in last cleanup )
                for (BasketItem basketItem : basketItems) {
                    String nameString = basketItem.getName() + "-" + basketItem.getChain();
                    names.add(nameString);
                    logger.log(Level.INFO, "Adding to hash [" + nameString + "]");
                }

                List<ProductMVO> updatedProducts = productDAO.getAvailableProducts(names);
                
                logger.log(Level.INFO, "Found [" + updatedProducts + "] updatedProducts");
                
                // Convert the list to a hashtable to save time.
                for (ProductMVO updatedProduct : updatedProducts) {
                    updatedProductsHash.put(updatedProduct.getName() + "-" + updatedProduct.getChain(), updatedProduct);
                    logger.log(Level.INFO, "The product [" + updatedProduct.getName() + "-" + updatedProduct.getChain() + "] is still available.");
                }

                logger.log(Level.INFO, "Updating ...");
                
                // Set the available flag on certain basket items
                if (updatedProducts != null && updatedProducts.size() > 0) {

                    for (BasketItem basketItem : basketItems) {

                        logger.log(Level.INFO, "Updating the basketItem [" + basketItem.getId() + "] with productId [" + basketItem.getProductId() + "] ");

                        boolean available = updatedProductsHash.containsKey(basketItem.getName() + "-" + basketItem.getChain());
  
                        logger.log(Level.INFO, "Setting updated flag on the basketItem [" + basketItem.getId() + "] with key [" + basketItem.getName() + "-" + basketItem.getChain() + "] ");
                        basketItem.setAvailable(available);

                        // Update to the new price
                        if (available) {
                            ProductMVO updatedProductItem = updatedProductsHash.get(basketItem.getName() + "-" + basketItem.getChain());

                            if (updatedProductItem != null) {
                                basketItem.setPrice(updatedProductItem.getPrice());
                            }
                            basketItem.setDateUpdated(todaysBaseDate);
                        
                            // Store the updated basket item in the database
                            basketDAO.update(basketItem);
                        }

                    }
                }
            }

        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            ex.printStackTrace();
        }

        return basketItems;
	}


}
