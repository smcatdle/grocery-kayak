/** SuperShop Price Engine   **/


package com.company.supershop.priceengine;

import com.company.supershop.model.IProduct;
import com.company.supershop.model.DealzProduct;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Node;

import com.company.supershop.model.*;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;


public class BootsPriceEngine extends PriceEngine {

    protected static final String EQUALS_STRING = "ID=";
    protected static final String OUTPUT_FILE = "DealzProducts.xml";
    public static final String DECIMAL_FORMAT =  "%.2f";
    public static final String DEALZ_CHAIN = "d";
    private final Logger logger = Logger.getLogger(BootsPriceEngine.class.getName());
    
    protected int pageIncrement;
    protected String GROCERY_PAGE = "http://www.dealz.ie/food-and-drink";
    
    public BootsPriceEngine() {
    	
        super(DEALZ_CHAIN);
        logger.log(Level.INFO, "DealzPriceEngine()");
        outputFileName = "DealzProducts.xml";
    }

    public String retrieveInitialHTMLPage(String urlString) {
    	
        //webClient = new WebClient(BrowserVersion.FIREFOX_17);
        System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "trace");
        
        try {
        	
            webClient.setThrowExceptionOnFailingStatusCode(false);
            webClient.setThrowExceptionOnScriptError(Boolean.FALSE.booleanValue());
            //webClient.waitForBackgroundJavaScriptStartingBefore(10000L);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.closeAllWindows();
            HtmlPage page1 = (HtmlPage)webClient.getPage(GROCERY_PAGE);
            return page1.asXml();
            
        } catch(MalformedURLException muex) {
        	logger.severe("Exception : " + muex);
            muex.printStackTrace();
        } catch(IOException ioex) { 
        	logger.severe("Exception : " + ioex);
            ioex.printStackTrace();
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

    public boolean hasNextPage(String htmlPage) {
        return false;
    }

    public Set findPageLinks(String pageLink, List searchLinks) {
    	
    	Set pageLinks = null;
        String xpathPattern = null;
        
        if(pageLink.equals(GROCERY_PAGE)) {
        	
            xpathPattern = "//div [contains(@class,'block-content')]//ul/li/a";
            return getSubLinks(pageLink, xpathPattern);
            
        } else {
            xpathPattern = "//ul [contains(@class,'level0-ul')]/li/a";
            pageLinks = getSubLinks(pageLink, xpathPattern);
                        
            return pageLinks;
        }
    }

    public List findProductElements(String pageLink, String id) {
    	
        List productElements = null;
        productElements = new ArrayList();
        
        try {
        	
            webClient.closeAllWindows();
            HtmlPage page = (HtmlPage)webClient.getPage(pageLink);
            Thread.sleep(2000L);
            
            // The products load dynamically on Dealz, we need to invoke the javascript to scroll down the screen.
            page.executeJavaScript("loadMore();");
            Thread.sleep(2000L);
            page.executeJavaScript("loadMore();");  
            Thread.sleep(2000L);  
            page.executeJavaScript("loadMore();");
            Thread.sleep(2000L);  
            page.executeJavaScript("loadMore();");
            Thread.sleep(2000L);  
            page.executeJavaScript("loadMore();");
            Thread.sleep(2000L);  
            page.executeJavaScript("loadMore();");
            Thread.sleep(2000L);  
            page.executeJavaScript("loadMore();");
            Thread.sleep(2000L);  
            page.executeJavaScript("loadMore();");
            HtmlElement htmlElement = page.getDocumentElement();
            List<HtmlElement> list = (List<HtmlElement>)htmlElement.getByXPath("//ul[contains(@class,'products-grid')]/li [contains(@class,'item')]");
            
            ProductElement productElement;
            
            for (HtmlElement element : list) {
                productElement = new ProductElement(element.asXml());
            	productElements.add(productElement);
            }

        } catch(MalformedURLException e) {
            logger.log(Level.SEVERE, e.toString());
            e.printStackTrace();
        } catch(FailingHttpStatusCodeException e) {
            logger.log(Level.SEVERE, e.toString());
            e.printStackTrace();
        } catch(IOException e) {
            logger.log(Level.SEVERE, e.toString());
            e.printStackTrace();
        } catch(InterruptedException e) {
            e.printStackTrace();
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        
        return productElements;
    }

    public IProduct extractProductElement(ProductElement productElement) {
    	
        DealzProduct product = null;
        
        try {
        	
            String name = null;
            String price = "";
            String priceDecimal = "";
            String imageUrl = "";
            	
        	// Check if the name is a 'details' link
        	if (getProductItemNode(productElement, "//h2/a") != null) {
	            Node nameNode = getProductItemNode(productElement, "//h2/a").item(0);
	            name = nameNode.getTextContent().trim();
        	} 
            
            Node imageNode = getProductItemNode(productElement, "//img [contains(@src,'small_image')]").item(0);
            imageUrl = getNodeAttribute(imageNode, "src");
	        
            Node priceNode = getProductItemNode(productElement, "//a [contains(@class,'product-image')]/img").item(0);
	        price = getNodeAttribute(priceNode, "alt");
	        
	        // there are two 'alts' with possible prices (pick the one which is not null)
	        if (price == null || "".equals(price)) {
	            priceNode = getProductItemNode(productElement, "//a [contains(@class,'product-image')]/img").item(1);
		        price = getNodeAttribute(priceNode, "alt");
	        }

            
            String totalPrice = price.replace("€" , "");
            
            // check for multibuys
            if (totalPrice != null && totalPrice.contains("for")) {
            	int amount = new Integer(totalPrice.substring(0,1)).intValue();
            	String[] split = totalPrice.split("for");
            	String[] split3 = split[1].split("\\.");
            	String decimalAmount = split3[0] + "." + split3[1].substring(0,2);
            	double multiplePrice = new Double(decimalAmount.trim()).doubleValue();
            	double singlePrice = multiplePrice/amount;
            	price = String.format("%.2f", singlePrice);
            }
            
            product = new DealzProduct();
            
            // Use the product name as identifier until productid is available
            product.setName(replaceSpecialHtmlChars(name));
            product.setPrice(price.substring(1));
            product.setQuantity(totalPrice.substring(0,1));
            product.setUnitPrice(totalPrice);
            product.setImageURL(imageUrl);
            product.setChain(chain);
            
        } catch(Exception ex) {
            logger.log(Level.SEVERE, ex.toString());
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
            ex.printStackTrace();
        }
        
        return pageLinks;
    }

    @Override
    public String getGroceryPage() { 
        return GROCERY_PAGE;
    }
    
    @Override
    public boolean findRecursiveLinks() {
    	return false;
    }

}
