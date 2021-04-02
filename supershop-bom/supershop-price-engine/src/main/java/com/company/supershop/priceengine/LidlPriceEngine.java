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


public class LidlPriceEngine extends PriceEngine {

    protected static final String EQUALS_STRING = "ID=";
    protected static final String OUTPUT_FILE = "LidlProducts.xml";
    public static final String GROCERY_PAGE = "http://www.lidl.ie";
    public static final String LIDL_CHAIN = "l";
    private final Logger logger = Logger.getLogger(LidlPriceEngine.class.getName());
    protected int pageIncrement;
    
    
    public LidlPriceEngine() {
    	
        super(LIDL_CHAIN);
        outputFileName = "LidlProducts.xml";
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
    	
        String xpathPattern = null;
        
        if(pageLink.equals(GROCERY_PAGE)) {
        	
            xpathPattern = "//ul [contains(@class,'secondlevel')]/li/a/img [contains(@onmouseover,'assets')]";
            return getSubLinks(pageLink, xpathPattern);
            
        } else {
            return null;
        }
    }

    public List findProductElements(HtmlPage htmlPage, String pageLink, String id) {
    	
        List productElements = null;
        productElements = new ArrayList();
        
        try {

            HtmlElement htmlElement = htmlPage.getDocumentElement();
            List<HtmlElement> list = (List<HtmlElement>)htmlElement.getByXPath("//section [contains(@class,'widecontent')]/div/a");
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
        	
            Node priceNode = getProductItemNode(productElement, "//div [contains(@class,'priceinfo')]/strong").item(0);
            String price = priceNode.getTextContent().replace("€", " ").trim();
            Node nameNode = getProductItemNode(productElement, "//h2").item(0);
            String name = nameNode.getTextContent().trim();
            Node amountNode = getProductItemNode(productElement, "//div [contains(@class,'priceinfo')]/small [contains(@class,'amount')]").item(0);
            String amount = amountNode.getTextContent().trim();
            /*Node unitPriceNode = getProductItemNode(productElement, "//div [contains(@class,'priceinfo')]/small [contains(@class,'baseprice')]");
            String unitPrice = unitPriceNode.getTextContent().trim();*/
            /*String productIdString = getNodeAttribute(productElement.getProductAsHtml(), "href");
            String productId = productIdString.substring(productIdString.length()-5);*/
            Node imageNode = getProductItemNode(productElement, "//img [contains(@class,'photo')]").item(0);
            String imageUrl = getNodeAttribute(imageNode, "src");
            product = new LidlProduct();
            
            // Use the product name as identifier until productid is available
            product.setName(replaceSpecialHtmlChars(name));
            product.setPrice(price);
            product.setImageURL("http://www.lidl.ie" + imageUrl);
            product.setQuantity(amount);
            //product.setUnitPrice(unitPrice);
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
            List<HtmlElement> rootElements = (List<HtmlElement>)htmlElement.getByXPath("//div [contains(@id,'wrapper')]");
            List<HtmlElement> htmlElements = (List<HtmlElement>)htmlElement.getByXPath(xpathPattern);
            HtmlElement ancestorElement = htmlElements.get(0).getEnclosingElement("ul");
            List<HtmlElement> elements = (List<HtmlElement>)ancestorElement.getByXPath("li/a");
            
            for (HtmlElement element : elements) {
            	
                String link = (new StringBuilder()).append(GROCERY_PAGE).append(element.getAttribute("href")).toString();
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
