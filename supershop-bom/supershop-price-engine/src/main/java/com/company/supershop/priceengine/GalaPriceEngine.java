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


public class GalaPriceEngine extends PriceEngine {

    private final Logger logger = Logger.getLogger(GalaPriceEngine.class.getName());

    protected static final String OUTPUT_FILE = "MaceProducts.xml";
    public static final String DEFAULT_PAGE = "http://www.mace.ie";
    public static final String MACE_CHAIN = "m";


    protected String GROCERY_PAGE = "http://www.mace.ie/special-offers/special-offers/all-offers";
    protected int pageIncrement;
    
    
    public GalaPriceEngine() {
    	
        super(MACE_CHAIN);
        logger.log(Level.INFO, "MacePriceEngine()");
        outputFileName = "MaceProducts.xml";
    }
    

    public String retrieveInitialHTMLPage(String urlString) {
    	
        //webClient = new WebClient(BrowserVersion.FIREFOX_17);
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
        	
            xpathPattern = "//div [contains(@class,'pagination')]/a";
            return getSubLinks(pageLink, xpathPattern);
            
        } else {
            
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
            HtmlElement htmlElement = page.getDocumentElement();
            List<HtmlElement> list = (List<HtmlElement>)htmlElement.getByXPath("//div [contains(@class,'stgemBoxContainerVcr')]");
            
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
        }
        
        return productElements;
    }

    public IProduct extractProductElement(ProductElement productElement) {
    	
        MaceProduct product = null;
        
        try {
        	
            String name = null;
            String price = "";
            String amountString = "1";
            String unitPrice = "";
            int amount = 0;
            	
            Node nameNode = getProductItemNode(productElement, "//div [contains(@class,'spEntriesListTitleVcr')]/table/tbody/tr/td/span").item(0);
            Node nameNode2 = getProductItemNode(productElement, "//div [contains(@class,'spFieldVcr')]/span/span").item(0);;
            Node nameNode3 = getProductItemNode(productElement, "//div [contains(@class,'spFieldVcr')]/span").item(0);;
            name = nameNode.getTextContent().trim() + " " + nameNode2.getTextContent().trim() + " " + nameNode3.getTextContent().trim();
            name = name.replace("\n", "").replace("  ", "");
            
            NodeList priceNodes = getProductItemNode(productElement, "//div [contains(@class,'field_price_mainVcr')]/table/tbody/tr/td");
	        price = priceNodes.item(0).getTextContent().trim().replace("MEMBERS PAY","");
	        
            Node imageNode = getProductItemNode(productElement, "//img [contains(@src,'entries')]").item(0);
            String imageUrl = getNodeAttribute(imageNode, "src");
            
	        // check for half price items
	        if (priceNodes.item(0).getTextContent().contains("HALF PRICE")) {
	        	priceNodes = getProductItemNode(productElement, "//div [contains(@class,'field_price_saveVcr')]/table/tbody/tr/td");
	        	 price = priceNodes.item(0).getTextContent().trim();
	        }
	        
	        String totalPrice = "";
	        
	        // Check if the 'ONLY' label is used
	        if (price != null && price.toUpperCase().contains("ONLY")) {
		        String[] split = price.toUpperCase().split("ONLY");
	            totalPrice = split[1].replace("€","").trim();
	            unitPrice = totalPrice;
	        } else {
		        String[] split = price.replace("ANY","").toUpperCase().split("FOR");
	            totalPrice = split[1].toUpperCase().replace("€","").trim();
	            amountString = split[0].trim();
	            unitPrice = new Double((new Double(totalPrice).doubleValue())/(new Integer(amountString).intValue())).toString();
	        }
	         
	        product = new MaceProduct();
	        
            // Use the product name as identifier until productid is available
            product.setName(replaceSpecialHtmlChars(name));
            product.setPrice(totalPrice);
            product.setQuantity(amountString);
            product.setUnitPrice(unitPrice);
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
            	
                String link = DEFAULT_PAGE + element.getAttribute("href");
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


}
