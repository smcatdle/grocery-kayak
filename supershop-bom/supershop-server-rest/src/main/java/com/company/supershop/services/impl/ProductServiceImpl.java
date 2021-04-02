package com.company.supershop.services.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.company.supershop.PromotionCache;
import com.company.supershop.dao.impl.ProductDAOImpl;
import com.company.supershop.dao.impl.PromotionDAOImpl;
import com.company.supershop.model.ProductMVO;
import com.company.supershop.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	private final static Logger logger = Logger.getLogger(ProductServiceImpl.class .getName());

	private static final int MINIMUM_PRODUCT_TOKEN_LENGTH = 4;
	

	@Autowired
    private ProductDAOImpl productDAO;
	
	@Autowired
    private PromotionDAOImpl promotionDAO;
	
	@Autowired
    private PromotionCache promotionCache;
	
	
	public List<ProductMVO> searchProductEntries(String searchString) {
		
		List<ProductMVO> matchingProducts = null;
		List<String> searchStrings = new ArrayList<String>();
		
		String[] strings = searchString.split(" ");
		for (int i=0; i<strings.length; i++) {
			searchStrings.add(strings[i]);
		}

		matchingProducts = productDAO.getProductsBySearchStrings(searchStrings);
		
		logger.info("Retrieved [" + matchingProducts.size() + "] products");
		
		return matchingProducts;
		
	}


	public List<ProductMVO> getAlternativeProducts(String productString) {
		
		List<ProductMVO> alternativeProducts = null;
		
		// Remove chain/brand from product string to get wider resultset
		String cleanedProductString = cleanProductString(productString);
		
		/*List<String> tokenStrings = new ArrayList<String>();
		
		String[] tokens = productString.split(" ");
		logger.info("Parsed [" + tokens.length + "] tokens");
		
		for (int i=0; i<tokens.length; i++) {
			if (isValidProductToken(tokens[i])) {
				tokenStrings.add(tokens[i]);
			}
		}
		
		logger.info("Using [" + tokenStrings.size() + "] tokens to search alternative products...");
		alternativeProducts = productDAO.getAlternativeProducts(tokenStrings);*/
		
		try {
			alternativeProducts = productDAO.getAlternativeProductsFast(cleanedProductString);
		} catch (Exception e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}
		
		logger.info("Retrieved [" + alternativeProducts.size() + "] alternatives");
		
		return alternativeProducts;
		
	}

	public List<String> getDepartments(String chain) {
		List<String> departments = null;
		
		departments = productDAO.getDepartments(chain);
		
		logger.info("Retrieved [" + departments.size() + "] departments");
		
		return departments;
	}
	
	public List<String> getDepartmentShelves(String department, String chain) {
		List<String> shelves = null;
		
		shelves = productDAO.getDepartmentShelves(department, chain);
		
		logger.info("Retrieved [" + shelves.size() + "] shelves");
		
		return shelves;
	}
	
	public List<ProductMVO> getShelfItems(String chain, String shelf) {
		List<ProductMVO> matchingProducts = null;

		matchingProducts = productDAO.getShelfItems(chain, shelf);
		
		logger.info("Retrieved [" + matchingProducts.size() + "] products for shelf name [" + shelf + "] and chain [" + chain + "]");
		return matchingProducts;
	}
	
	public List<ProductMVO> getDefaultProducts(String chain) {
		
		// Get promotional products if chain not set
		if (chain == null || "x".equals(chain)) {
			return promotionCache.getPromotions();
		} else if (chain != null && ("t".equals(chain) || "v".equals(chain))) {   // Load offers for Tesco and Supervalu
			return productDAO.findOffersByChain(chain);
		}
		
		// Some chains have few products, so all available products can be displayed here.
		return productDAO.findCurrentByChain(chain);
	}

	public List<ProductMVO> getAllProducts(String chain) {
		
		return productDAO.findAllByChain(chain);
	}

	private boolean isValidProductToken(String token) {

		if (token.length() >= MINIMUM_PRODUCT_TOKEN_LENGTH 
				&& !token.matches(".*[0-9]+.*") 
				&& !token.matches("&(?!amp;)|[/,-\\.]")) {
			logger.info("Token [" + token + "] is valid");
			return true;
		} else {
			logger.info("Token [" + token + "] is invalid");
			return false;
		}

	}
	
	public void indexProducts() {
		//productDAO.indexProducts();
		promotionCache.loadPromotions();
	}
	
	private String cleanProductString(String productString) {
		String cleanProductString = null;
		
		cleanProductString = productString.replace("Tesco", "").replace("SuperValu", "").replace("\'", "");
		return cleanProductString;
	}

}
