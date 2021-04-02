/**
 * 
 */
package com.mymobilebasket;


import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mymobilebasket.lookup.LivePriceLookup;
import com.mymobilebasket.util.ProductExtractor;


/**
 * @author smcardle
 *
 */
public class ProductExtractorTest {
	
	private final static Logger logger = Logger.getLogger(ProductExtractorTest.class.getName());

	private ProductExtractor extractor = null;
	
	private LivePriceLookup priceEngine = null;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		priceEngine = LivePriceLookup.getInstance();
		extractor = ProductExtractor.getInstance();
		extractor.setPriceEngine(priceEngine);
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Test
	public void testRetrievePageHTML() throws Exception {
		
		String url = "http://www.tesco.ie/groceries/product/browse/default.aspx?N=4294953445&Ne=4294954028&lvl=3";
		String pageText = null;
		
		extractor.recursiveProductSearch();
		
		pageText = extractor.retrievePageHTML(url);
		
		logger.log(Level.INFO, "The returned url string is : " + pageText);
	}
	
	/**
	 * @throws java.lang.Exception
	 */
	/*@Test
	public void testFindPageLinks() throws Exception {
		
		String url = ShopScanBatch.TESCO_GROCERY_PAGE;
		String pageText = null;
		List<String> searchLinks = new ArrayList<String>();
		
		searchLinks.add(ShopScanBatch.TESCO_DEPARTMENT_LINK);
		searchLinks.add(ShopScanBatch.TESCO_PRODUCT_LINK);
		
		pageText = extractor.RetrievePageHTML(url);
		
		Set<String> pageLinks = URLHelper.findPageLinks(pageText, searchLinks);
		
		for (String page : pageLinks) {
			logger.log(Level.INFO, "Found Page : " + page);
		}
		
	}*/
	
	/**
	 * @throws java.lang.Exception
	 */
	/*@Test
	public void testFindPageData() throws Exception {
		
		String url = ShopScanBatch.TESCO_GROCERY_PAGE;
		String id = ShopScanBatch.TESCO_PRODUCT_STRING;
		String pageText = null;
		List<String> searchLinks = new ArrayList<String>();
		
		searchLinks.add(ShopScanBatch.TESCO_DEPARTMENT_LINK);
		searchLinks.add(ShopScanBatch.TESCO_PRODUCT_LINK);
		
		pageText = extractor.RetrievePageHTML(url);
		
		Set<String> pageLinks = URLHelper.findPageLinks(pageText, searchLinks);
		
		for (String page : pageLinks) {
			
			List<String> pageStrings = URLHelper.findPageData(page, id);

		}	
	}*/
	
	/**
	 * @throws java.lang.Exception
	 */
	/*@Test
	public void testExtractPageData() throws Exception {
		
		String url = ShopScanBatch.TESCO_TEST_PAGE;
		String id = ShopScanBatch.TESCO_PRODUCT_STRING;
		String pageText = null;
		List<String> searchLinks = new ArrayList<String>();
		
		searchLinks.add(ShopScanBatch.TESCO_DEPARTMENT_LINK);
		searchLinks.add(ShopScanBatch.TESCO_PRODUCT_LINK);
		
		pageText = extractor.RetrievePageHTML(url);
		
		Set<String> pageLinks = URLHelper.findPageLinks(pageText, searchLinks);
		
		for (String page : pageLinks) {
			
			List<String> pageStrings = URLHelper.findPageData(page, id);
			for (String pageString : pageStrings) {
				PageData pageData = URLHelper.extractPageData(pageString);
				
				logger.log(Level.INFO, "productId : [" + pageData.getName() + "]");
				logger.log(Level.INFO, "barcode : [" + pageData.getBarcode() + "]");
				logger.log(Level.INFO, "description : [" + pageData.getDescription() + "]");
				logger.log(Level.INFO, "price : [" + pageData.getPrice() + "]");
			}
		}	
	}*
	
	/**
	 * @throws java.lang.Exception
	 */
	/*@Test
	public void testRecursiveProductSearch() throws Exception {
		
		extractor.recursiveProductSearch();
	}*/
	
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

}
