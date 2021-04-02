/** Scanshop Price Engine   **/


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


public class EuroSparPriceEngine extends PriceEngine {

    protected static final String EQUALS_STRING = "ID=";
    protected static final String OUTPUT_FILE = "EuroSparProducts.xml";
    public static final String GROCERY_PAGE = "https://www.eurospar.ie/offers";
    public static final String DEFAULT_PAGE = "https://www.eurospar.ie";
    public static final String EURO_SPAR_CHAIN = "e";
    private final Logger logger = Logger.getLogger(EuroSparPriceEngine.class.getName());
    protected int pageIncrement;
    
    
    public EuroSparPriceEngine() {
    	
        super(EURO_SPAR_CHAIN);
        outputFileName = "EuroSparProducts.xml";
    }

    public String retrieveInitialHTMLPage(String urlString) {
    	
        //webClient = new WebClient(BrowserVersion.FIREFOX_17);
        System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "trace");
        
        try {
        	
            webClient.setThrowExceptionOnFailingStatusCode(false);
            webClient.setThrowExceptionOnScriptError(Boolean.FALSE.booleanValue());
            //webClient.waitForBackgroundJavaScriptStartingBefore(10000L);
            webClient.getOptions().setJavaScriptEnabled(false);
            webClient.closeAllWindows();
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
        	
            xpathPattern = "//div [contains(@class,'orange-button')]/a";
            return getSubLinks(pageLink, xpathPattern);
            
        } else {
            
            return pageLinks;
        }
    }

    public List findProductElements(HtmlPage htmlPage, String pageLink, String id) {
    	
        List productElements = null;
        productElements = new ArrayList();
        
        try {

            HtmlElement htmlElement = htmlPage.getDocumentElement();
            List<HtmlElement> list = (List<HtmlElement>)htmlElement.getByXPath("//div[contains(@class,'offer-box-wrap-tall')]");
            
            ProductElement productElement;
            
            for (HtmlElement element : list) {
                productElement = new ProductElement(element.asXml());
            	productElements.add(productElement);
            }

        } catch(Exception e) {
            logger.log(Level.SEVERE, e.toString());
            e.printStackTrace();
        } 
        
        return productElements;
    }

    public IProduct extractProductElement(ProductElement productElement) {
    	
    	EuroSparProduct product = null;
        
        try {
        	
            String name = null;
            String price = "";
            String amountString = "1";
            String unitPrice = "";
            int amount = 0;
            	
            Node nameNode = getProductItemNode(productElement, "//div [contains(@class,'offer-text-b')]/span").item(0);
            Node nameNode2 = getProductItemNode(productElement, "//div [contains(@class,'offer-text-b')]/div/b").item(0);;
            Node nameNode3 = getProductItemNode(productElement, "//div [contains(@class,'offer-text-b')]/div").item(1);;
            name = nameNode.getTextContent().trim() + " " + nameNode2.getTextContent().trim() + " " + nameNode3.getTextContent().trim();
            String escapedName = replaceSpecialHtmlChars(name);
            int escapedNameLength = (escapedName.length() < MAX_PRODUCT_NAME_LENGTH) ? escapedName.length()-1 : MAX_PRODUCT_NAME_LENGTH;

            Node imageNode = getProductItemNode(productElement, "//img [contains(@class,'resizeOfferList')]").item(0);
            String imageUrl = imageNode != null ? "http://www.eurospar.ie" + getNodeAttribute(imageNode, "src") : "";
            
            NodeList priceNodes = getProductItemNode(productElement, "//div [contains(@class,'price-box-b')]/span");
	        price = (priceNodes != null && priceNodes.getLength() > 0) ? priceNodes.item(0).getTextContent().trim().replace("MEMBERS PAY","") : "";
	        
	        String totalPrice = "";

	        // Exit if '%' drop (difficult to calculate the price)
	        if (price != null && price.contains("%") || price.contains("THE PRICE")) {
	        	
	        	return null;
	        }
	        
	        price = price.replace("Members Pay","");
	        
	        // Check if the 'ONLY' label is used
	        if (price != null && price.contains("ONLY")) {
		        String[] split1 = price.split("ONLY");
	            totalPrice = split1[1].replace("€","").trim();
	            unitPrice = totalPrice;
	        } else if (price != null && price.contains("ANY")) {
		        String[] split = price.replace("ANY","").split("FOR");
	            totalPrice = split[1].replace("€","").trim();
	            amountString = split[0].trim();
	            unitPrice = new Double((new Double(totalPrice).doubleValue())/(new Integer(amountString).intValue())).toString();
	        } /*else if (price != null && price.contains("HALF PRICE")) {

	        }*/


            product = new EuroSparProduct();
            
            // Use the product name as identifier until productid is available
            product.setName(escapedName.substring(0,escapedNameLength));
            product.setPrice(totalPrice);
            product.setQuantity(amountString);
            product.setUnitPrice(unitPrice);
            product.setImageURL(imageUrl);
            product.setChain(chain);
	            
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
            	
                String link = DEFAULT_PAGE + element.getAttribute("href");
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
}
