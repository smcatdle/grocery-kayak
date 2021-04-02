/** SuperShop Price Engine   **/


package com.company.supershop.priceengine;

import com.company.supershop.model.*;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class AldiPriceEngine extends PriceEngine {

	
	
    protected static final String EQUALS_STRING = "ID=";
    protected static final String WINE_CELLAR_PAGE = "https://www.aldi.ie/en/product-range/drinks/wine-cellar/white-wine/";
    protected static final String OUTPUT_FILE = "AldiProducts.xml";
    public static final String GROCERY_PAGE = "https://www.aldi.ie/en/product-range/";
    public static final String ALDI_CHAIN = "a";
    private final Logger logger = Logger.getLogger(AldiPriceEngine.class.getName());
    protected int pageIncrement;
    
    
    public AldiPriceEngine() {
    	
        super(ALDI_CHAIN);
        outputFileName = "AldiProducts.xml";
    }

    public String retrieveInitialHTMLPage(String urlString) {
    	
        System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "trace");
        
        try {
        	
            webClient.setThrowExceptionOnFailingStatusCode(false);
            webClient.setThrowExceptionOnScriptError(Boolean.FALSE.booleanValue());
            //webClient.waitForBackgroundJavaScriptStartingBefore(10000L);
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage page1 = (HtmlPage)webClient.getPage(GROCERY_PAGE);
            return page1.asXml();
            
        } catch(MalformedURLException muex) {
        	logger.severe("Exception : " + muex);
        } catch(IOException ioex) { 
        	logger.severe("Exception : " + ioex);
        }
        
        return null;
    }

    public String retrieveHTMLPage(String urlString) {
    	
        HtmlPage page1 = null;
        
        try {
            webClient.closeAllWindows();
            page1 = (HtmlPage)webClient.getPage(urlString);
            Thread.sleep(2000L);
        } catch(FailingHttpStatusCodeException e) {
            logger.log(Level.SEVERE, e.toString());
            e.printStackTrace();
        } catch(MalformedURLException e) {
            logger.log(Level.SEVERE, e.toString());
            e.printStackTrace();
        } catch(IOException e) {
            logger.log(Level.SEVERE, e.toString());
            e.printStackTrace();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        
        return page1.asXml();
    }

    public boolean hasNextPage(String htmlPage, int pageIndex) {
        return false;
    }

    public Set findPageLinks(String pageLink, List searchLinks) {
    	
    	Set pageLinks = null;
        String xpathPattern = null;
        
        if(pageLink.equals(GROCERY_PAGE)) {
        	
            xpathPattern = "//a [contains(@href,'product-range')] [contains(@class,'dropdown--list--link')]";
            return getSubLinks(pageLink, xpathPattern);
            
        } else {
            xpathPattern = "//div [contains(@class,'csc-textpic-imagewrap')]//a [contains(@href,'" + pageLink + "')]";
            pageLinks = getSubLinks(pageLink, xpathPattern);
            
            // EXCEPTION for Wine Cellar!!
            if (pageLinks == null || pageLinks.isEmpty()) {
            	pageLinks = getSubLinks(pageLink, "//div[@class='productworld']/ul/li/a");
            }
            
            // Wine Cellar type breakdown
            if (pageLink.equals(WINE_CELLAR_PAGE) && (pageLinks == null || pageLinks.isEmpty())) {
            	pageLinks = getSubLinks(pageLink, "//ul/li/a [@href='" + WINE_CELLAR_PAGE + "']");
            }
            
            return pageLinks;
        }
    }

    public List findProductElements(HtmlPage htmlPage, String pageLink, String id) {
    	
        List productElements = null;
        productElements = new ArrayList();
        
        try {
        	
            HtmlElement htmlElement = htmlPage.getDocumentElement();
            List<HtmlElement> list = (List<HtmlElement>)htmlElement.getByXPath("//div[contains(@class,'tx-aldi-products')]//div[contains(@class,'box--wrapper')]");
            
            ProductElement productElement;
            
            for (HtmlElement element : list) {
                productElement = new ProductElement(element.asXml());
                productElement.setPageLink(pageLink);
            	productElements.add(productElement);
            }

        } catch(Exception e) {
            logger.log(Level.SEVERE, e.toString());
            e.printStackTrace();
        } 
        
        return productElements;
    }

    public IProduct extractProductElement(ProductElement productElement) {
    	
        LidlProduct product = null;
        
        try {
        	
            String name = null;
            String price = "";
            String priceDecimal = "";
            	
            // Check if this is a product node
            if (getProductItemNode(productElement, "//h2") != null && getProductItemNode(productElement, "//h2").getLength() > 0) {
	            
	        	// Check if the name is a 'details' link
	        	if (getProductItemNode(productElement, "//h2/a") != null) {
		            Node nameNode = getProductItemNode(productElement, "//h2/a").item(0);
		            name = nameNode.getTextContent().trim();
	        	} else {
		            Node nameNode = getProductItemNode(productElement, "//h2").item(0);
		            name = nameNode.getTextContent().trim();
	        	}
	            
	            NodeList priceNodes = getProductItemNode(productElement, "//div [contains(@class,'price')]/span [contains(@class,'value')]");
	            Node priceNode = null;
	            if (priceNodes != null) {
	            	priceNode = priceNodes.item(0);
	            	if (priceNodes.item(0) != null) {
	            		// Handle cents 
	            		price = priceNode.getTextContent().contains("€") ? price + priceNode.getTextContent().trim() : "0." + priceNode.getTextContent().trim();
	            	}
	            }
	            
	            // Check if there is a decimal value
	            if (getProductItemNode(productElement, "//div [contains(@class,'price')]/span [contains(@class,'decimal')]") != null) {
		            Node priceNodeDecimal = getProductItemNode(productElement, "//div [contains(@class,'box--price')]/span [contains(@class,'box--decimal')]").item(0);
		            if (priceNodeDecimal != null) {
		            	priceDecimal = priceNodeDecimal.getTextContent().trim();
		            }
	            }
	            
	            String totalPrice = price.replace("€", " ") + priceDecimal.replace("c", "");
	            Node amountNode = getProductItemNode(productElement, "//div [contains(@class,'price')]/span [contains(@class,'amount')]").item(0);
	            String amount = amountNode.getTextContent().trim();
	            Node unitPriceNode = getProductItemNode(productElement, "//div [contains(@class,'price')]/span [contains(@class,'baseprice')]").item(0);
	            String unitPrice = unitPriceNode.getTextContent().trim();
	            /*String productIdString = getNodeAttribute(productElement.getProductAsHtml(), "href");
	            String productId = productIdString.substring(productIdString.length()-5);*/
	            Node imageNode = getProductItemNode(productElement, "//a [contains(@class,'box--image--link')]//img").item(0);
	            String imageUrl = getNodeAttribute(imageNode, "src");
	            product = new LidlProduct();
	            
	            // Use the product name as identifier until productid is available
	            product.setName(replaceSpecialHtmlChars(name));
	            product.setPrice(totalPrice);
	            product.setQuantity(amount);
	            product.setUnitPrice(unitPrice);
	            product.setImageURL(imageUrl);
	            product.setChain(chain);
            }
        } catch(Exception ex) {
        	logger.log(Level.SEVERE, "PRODUCT PARSE ERROR for chain [" + super.chain + "] pageLink [" + productElement.getPageLink() + "]" + ex.toString());
        	ex.printStackTrace();
        }
        
        return product;
    }

    private Set getSubLinks(String pageLink, String xpathPattern) {
    	
        Set pageLinks = null;
        pageLinks = new HashSet();
        
        try {
        	
            webClient.closeAllWindows();
            HtmlPage page = (HtmlPage)webClient.getPage(pageLink);
            Thread.sleep(2000L);
            HtmlElement htmlElement = page.getDocumentElement();
            List<HtmlElement> htmlElements = (List<HtmlElement>)htmlElement.getByXPath(xpathPattern);
            
            for (HtmlElement element : htmlElements) {
            	
                String link = element.getAttribute("href");
                if (!previousPageLinks.contains(link)) {
                	
                    pageLinks.add(link);
                    pageLinkCounter++;
                    logger.log(Level.INFO, (new StringBuilder()).append("Found Sub Menu Link : [").append(link).append("]").toString());
                }
            }
            
        } catch(Exception ex) {
            logger.log(Level.SEVERE, ex.toString());
        }
        
        return pageLinks;
    }

    @Override
    public String getGroceryPage() { 
        return GROCERY_PAGE;
    }
    
    @Override
    public boolean findRecursiveLinks() {
    	return true;
    }

}
