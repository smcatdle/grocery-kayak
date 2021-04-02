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


public class SuperValuPriceEngine extends PriceEngine {

    private static final Logger logger = Logger.getLogger(SuperValuPriceEngine.class.getName());
    public static final String GROCERY_PAGE = "https://shop.supervalu.ie/shopping/StartShopping/SelectStore.aspx?ReturnUrl=/shopping/Shopping/Shop.aspx";
    public static final String GROCERY_PAGE_LOGIN = "https://shop.supervalu.ie/shopping/StartShopping/login.aspx";
    protected static final String OUTPUT_FILE = "SupervaluProducts.xml";
    public static final String SUPERVALU_CHAIN = "v";
    public static final String NEXT_STRING = "Next";
    
    
    public SuperValuPriceEngine() {
    	
        super("v");
        nextPageString = NEXT_STRING;
        outputFileName = "SupervaluProducts.xml";
    }

    public String retrieveInitialHTMLPage(String urlString) {
    	
        //webClient = new WebClient();
        System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "trace");
        
        try {
        	
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.setUseInsecureSSL(true);

			HtmlPage page = (HtmlPage) webClient
					.getPage(SuperValuPriceEngine.GROCERY_PAGE_LOGIN);
			HtmlElement button = (HtmlElement) page
					.getElementById("ctl00_ctl00_cphContent_cphMain_ucntrlLogin_btnLogin");
			HtmlTextInput emailField = (HtmlTextInput) page
					.getElementByName("ctl00$ctl00$cphContent$cphMain$ucntrlLogin$txtEmailAddress");
			HtmlPasswordInput passwordField = (HtmlPasswordInput) page
					.getElementByName("ctl00$ctl00$cphContent$cphMain$ucntrlLogin$txtPassword");
			emailField.setValueAttribute("DublinMacTester2@gmail.com");
			passwordField.setValueAttribute("snowboard1");

			HtmlPage page2 = (HtmlPage) button.click();

			// Check the user is logged in
			if (page2.asXml().contains("Hello Dub")) {
				logger.log(Level.INFO, "User logged in");
			}
            
            return page2.asXml();
            
        } catch(MalformedURLException e) {
            logger.log(Level.SEVERE, e.toString());
            e.printStackTrace();
        } catch(Exception e) {
            logger.log(Level.SEVERE, e.toString());
            e.printStackTrace();
        }
        
        return null;
    }

    public Set findPageLinks(String pageLink, List searchLinks) {
    	
        String xpathPattern = null;
        
        if(pageLink.equals("https://shop.supervalu.ie/shopping/StartShopping/SelectStore.aspx?ReturnUrl=/shopping/Shopping/Shop.aspx")) {
        	
            xpathPattern = "//li//ul//li [contains(@class,'AspNet-MegaMenu-Leaf')] //a [contains(@class,'AspNet-MegaMenu-Link')]";
            return getSubLinks(pageLink, xpathPattern);
            
        } else {
            return null;
        }
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
            	
                String link = "http://shop.supervalu.ie/shopping/" + element.getAttribute("href");
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

    public List findProductElements(HtmlPage htmlPage, String pageLink, String id) {
    	
        List productElements = null;
        productElements = new ArrayList();
        
        try {
        	
            HtmlElement htmlElement = htmlPage.getDocumentElement();
            List<HtmlElement> list = (List<HtmlElement>)htmlElement.getByXPath("//div [contains(@class,'productListingInnerContainer')]");
            List categoryList = htmlElement.getByXPath("//div//a [@class='breadGrey']");
            
            for (HtmlElement element : list) {
            	ProductElement productElement = null;
                productElement = new ProductElement(element.asXml());
                productElement.setCategory(((HtmlElement)categoryList.get(0)).getTextContent());
                productElement.setSubCategory(((HtmlElement)categoryList.get(categoryList.size() - 1)).getTextContent());
                productElements.add(productElement);
            }

        }  catch(Exception e) {
            logger.log(Level.SEVERE, e.toString());
            e.printStackTrace();
        } 
        
        return productElements;
    }

    public IProduct extractProductElement(ProductElement productElement) {
    	
        SuperValuProduct product = null;
        String lastPriceString = "";

        try {
        	
            Node priceNode = getProductItemNode(productElement, "//div [@id='productPrice']").item(0);
            String priceString = priceNode.getTextContent().trim();
            String price = priceString.substring(1, priceString.indexOf(" "));

            /*try {         
                Node lastPriceNode = getProductItemNode(productElement, "//td [@id='promoYellow']").item(0);
                String lastPriceLabelString = lastPriceNode.getTextContent().trim();
                String[] lastPriceStringArray = lastPriceLabelString.split(",");
                lastPriceString = lastPriceStringArray[0].replace("Was €","").trim();
            } catch (Exception ex) {

            }*/

            Node pricePerUnitNode = getProductItemNode(productElement, "//span [contains(@id,'lblpricePerUnit')]").item(0);
            String pricePerUnit = pricePerUnitNode.getTextContent();
            Node nameNode = getProductItemNode(productElement, "//div [@id='divProductTitle']/a").item(0);
            String name = nameNode.getTextContent().trim();
            Node productIdNode = getProductItemNode(productElement, "//div [@class='productListingInnerContainer']").item(0);
            String productId = getNodeAttribute(productIdNode, "pid");
            Node imageUrlNode = getProductItemNode(productElement, "//div [contains(@id,'divProductImage')]/a/img").item(0);
            String imageUrl = getNodeAttribute(imageUrlNode, "src");
            product = new SuperValuProduct();
            product.setPrice(price);
            //product.setLastPrice(lastPriceString);
            product.setProductId(productId);
            product.setImageURL(imageUrl);
            product.setName(replaceSpecialHtmlChars(name));
            product.setUnitPrice(pricePerUnit);
            product.setSuperdepartment(replaceSpecialHtmlChars(productElement.getCategory().trim()));
            product.setShelfName(replaceSpecialHtmlChars(productElement.getSubCategory().trim()));
            product.setChain(chain);
            
            if (!imageUrl.contains("product_placeholder_small")) {
                product.setBarcode(getBarcode(imageUrl));
            }
            
        } catch(Exception ex) {
            logger.log(Level.SEVERE, ex.toString());
            ex.printStackTrace();
        }
        
        return product;
    }

    public String getBarcode(String imageUrl) {
    	
        String barcode = null;
        String splitHtmlPage[] = imageUrl.split("http://shop.supervalu.ie/shopping/images/products/small/");
        String splitHtmlPage1[] = splitHtmlPage[1].split("_1.JPG");
        barcode = splitHtmlPage1[0];
        
        return barcode;
    }

    @Override
    public String getGroceryPage() { 
        return GROCERY_PAGE;
    }

    public boolean hasNextPage(String htmlPage, int pageIndex) {
        return htmlPage.contains(NEXT_STRING);
    }
    
    public String getNextPageLink(HtmlPage htmlPage, String originalPageLink, int pageIndex) {
    	
        try {

			HtmlElement htmlElement = htmlPage.getDocumentElement();
			List<HtmlElement> list = (List<HtmlElement>)htmlElement.getByXPath("//div [contains(@class,'pagingDisplay')]//a [text()='Next']");
			
			return getNodeAttribute(list.get(0), "href");
			
		}  catch (Exception e) {
			logger.log(Level.SEVERE, e.toString());
			e.printStackTrace();
		} 
        
        return null;
    }
    
    
    public String getProductString() {
    	return "";
    }
    
    public String getNextPageString() {
    	return nextPageString;
    }
    
	public static void main(String[] args) {

		try {
		    WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_9);
		    webClient.setThrowExceptionOnFailingStatusCode(false);
		    webClient.getOptions().setThrowExceptionOnScriptError(false);
		    webClient.getOptions().setJavaScriptEnabled(true);
		    webClient.setUseInsecureSSL(true);
		    webClient.setJavaScriptTimeout(5000);
		    webClient.setCssEnabled(false);
		    webClient.getOptions().setCssEnabled(false);

			HtmlPage page = (HtmlPage) webClient
					.getPage(SuperValuPriceEngine.GROCERY_PAGE_LOGIN);
			HtmlElement button = (HtmlElement) page
					.getElementById("ctl00_ctl00_cphContent_cphMain_ucntrlLogin_btnLogin");
			HtmlTextInput emailField = (HtmlTextInput) page
					.getElementByName("ctl00$ctl00$cphContent$cphMain$ucntrlLogin$txtEmailAddress");
			HtmlPasswordInput passwordField = (HtmlPasswordInput) page
					.getElementByName("ctl00$ctl00$cphContent$cphMain$ucntrlLogin$txtPassword");
			emailField.setValueAttribute("DublinMacTester2@gmail.com");
			passwordField.setValueAttribute("snowboard1");

			HtmlPage page2 = (HtmlPage) button.click();

			// Check the user is logged in
			if (page2.asXml().contains("Hello Dub")) {
				logger.log(Level.INFO, "User logged in");
			} else {
				logger.log(Level.INFO, "User not logged in");
			}


		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.toString());
			e.printStackTrace();
		}
	}
}
