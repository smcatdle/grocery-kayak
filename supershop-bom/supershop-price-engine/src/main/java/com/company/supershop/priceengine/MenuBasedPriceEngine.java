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


public abstract class MenuBasedPriceEngine extends PriceEngine {

    private final Logger logger = Logger.getLogger(MenuBasedPriceEngine.class.getName());
    protected static final String GROCERY_PAGE_PREFIX = "http://shop.supervalu.ie/shopping/";
    
    
    public MenuBasedPriceEngine() {
        super("");
    }

    public String retrieveInitialHTMLPage(String urlString) {
        webClient = new WebClient(BrowserVersion.FIREFOX_17);
        System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "trace");
        try {
            webClient.setThrowExceptionOnFailingStatusCode(false);
            HtmlPage page = (HtmlPage)webClient.getPage(initialGroceryPage);
            HtmlForm form = page.getFormByName("aspnetForm");
            HtmlElement button = form.getElementById("ctl00_ctl00_cphContent_cphMain_ucntrlLogin_btnLogin");
            HtmlTextInput emailField = (HtmlTextInput)form.getInputByName("ctl00$ctl00$cphContent$cphMain$ucntrlLogin$txtEmailAddress");
            HtmlPasswordInput passwordField = (HtmlPasswordInput)form.getInputByName("ctl00$ctl00$cphContent$cphMain$ucntrlLogin$txtPassword");
            emailField.setValueAttribute("stephen.mcardle@gmail.com");
            passwordField.setValueAttribute("snowboard1");
            HtmlPage page2 = (HtmlPage)button.click();
            return page2.asXml();
        }
        catch(MalformedURLException e) { 
        	
        }
        catch(IOException e) { 
        	
        }
        
        return null;
    }

    public Set findPageLinks(String pageLink, List searchLinks) {
        String xpathPattern = null;
        
        if(pageLink.equals(initialGroceryPage)) {
            xpathPattern = "//li//ul//li//a [contains(@class,'AspNet-MegaMenu-Link')]";
            return getSubLinks(pageLink, xpathPattern);
        } else {
            return null;
        }
    }

    private Set getSubLinks(String pageLink, String xpathPattern) {
        Set pageLinks = null;
        pageLinks = new HashSet();
        
        try {
        	
            HtmlPage page = (HtmlPage)webClient.getPage(pageLink);
            Thread.sleep(2000L);
            HtmlElement htmlElement = page.getDocumentElement();
            List<HtmlElement> htmlElements = (List<HtmlElement>) htmlElement.getByXPath(xpathPattern);
            for (HtmlElement element : htmlElements) {
                String link = (new StringBuilder()).append("http://shop.supervalu.ie/shopping/").append(element.getAttribute("href")).toString();
                if(!previousPageLinks.contains(link)) {
                    pageLinks.add(link);
                    pageLinkCounter++;
                    logger.log(Level.INFO, (new StringBuilder()).append("Found Sub Menu Link : [").append(link).append("]").toString());
                }
            }
        }
        catch(Exception ex) {
            logger.log(Level.SEVERE, ex.toString());
        }
        return pageLinks;
    }

    public List<ProductElement> findProductElements(String pageLink, String id) 
    {
    	List<ProductElement> productElements = null;
        productElements = new ArrayList();
        
        try {
            HtmlPage page = (HtmlPage)webClient.getPage(pageLink);
            Thread.sleep(2000L);
            HtmlElement htmlElement = page.getDocumentElement();
            List<HtmlElement> list = (List<HtmlElement>) htmlElement.getByXPath("//div [@class='listing_row']");
            for(HtmlElement element : list) {
            	productElements.add(new ProductElement(element));
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
        SuperquinnProduct product = null;
        HtmlElement htmlElement = productElement.getProductAsHtml();
        
        try {
        	
            List<HtmlElement> priceElementList = (List<HtmlElement>) htmlElement.getByXPath("//span [@class='unit_price']");
            HtmlElement priceElement = priceElementList.get(0);
            String price = priceElement.getTextContent();
            List<HtmlElement> pricePerUnitElementList = (List<HtmlElement>) htmlElement.getByXPath("//span [contains(@id,'_lblpricePerUnit'])");
            HtmlElement pricePerUnitElement = (HtmlElement)pricePerUnitElementList.get(0);
            String pricePerUnit = pricePerUnitElement.getTextContent();
            List<HtmlElement> nameElementList = (List<HtmlElement>) htmlElement.getByXPath("//a [contains(@href,'http://shop.supervalu.ie/shopping/shopping/shop.aspx?prodid=')]");
            HtmlElement nameElement = (HtmlElement)nameElementList.get(0);
            String name = nameElement.getTextContent();
            List<HtmlElement> productIdElementList = (List<HtmlElement>) htmlElement.getByXPath("//table [@class='product_info_container']");
            HtmlElement productIdElement = (HtmlElement)productIdElementList.get(0);
            String productId = productIdElement.getAttribute("id");
            List<HtmlElement> imageUrlElementList = (List<HtmlElement>) htmlElement.getByXPath("//img [contains(@id,'_imgProductSmall')]");
            HtmlElement imageUrlElement = (HtmlElement)imageUrlElementList.get(0);
            String imageUrl = imageUrlElement.getAttribute("src");
            
            product = new SuperquinnProduct();
            product.setPrice(price);
            product.setProductId(productId);
            product.setBarcode(getBarcode(imageUrl));
            product.setName(name);
            product.setImageURL(imageUrl);
            product.setUnitPrice(pricePerUnit);
            
        } catch(Exception ex) {
            logger.log(Level.SEVERE, ex.toString());
        }
        
        return product;
    }

    public String getBarcode(String imageUrl)
    {
        String barcode = null;
        String splitHtmlPage[] = imageUrl.split("http://shop.supervalu.ie/shopping/images/products/small/");
        String splitHtmlPage1[] = splitHtmlPage[1].split("_1.JPG");
        barcode = splitHtmlPage1[0];
        
        return barcode;
    }

}
