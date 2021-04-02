/** SuperShop Price Engine   **/


package com.company.supershop.priceengine;

import com.company.supershop.model.IProduct;
import com.company.supershop.model.TescoProduct;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.gae.GAEUtils;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class TescoPriceEngine extends PriceEngine {

    public static final String GROCERY_PAGE = "http://www.tesco.ie/groceries/";
    public static final String TESCO_CHAIN = "t";
    private final Logger logger = Logger.getLogger(TescoPriceEngine.class.getName());


	public TescoPriceEngine() {
    	
        super("t");
        logger.log(Level.INFO, "TescoPriceEngine()");
    }
    
    
    public void writeProductInfo(IProduct product) {
    	
        super.writeProductInfo(product);
        TescoProduct tescoProduct = null;
        tescoProduct = (TescoProduct)product;
    }

    public Set findPageLinks(String pageLink, List searchLinks) {
    	
        Set pageLinks = null;
        pageLinks = new HashSet();
        String xpathPattern = null;
        if(pageLink.equals("http://www.tesco.ie/groceries/")) {
        	
            xpathPattern = "//ul//li//a [contains(@id,'product-TO_')]";
            return getSubLinks(pageLink, xpathPattern);
        } else {
            return pageLinks;
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
            	
                HtmlPage mouseOverPage = (HtmlPage)element.click();
                Thread.sleep(2000L);
                HtmlElement mouseOverElement = page.getDocumentElement();
                List<HtmlElement> mouseOverElements = (List<HtmlElement>)mouseOverPage.getByXPath("//ul [contains(@class,'tertNav')] //li/a [contains(@href,'http')]");
                
                for (HtmlElement elem : mouseOverElements) {
                	
                    String link = elem.getAttribute("href");
                    if (!previousPageLinks.contains(link) && !link.contains("lvl")) {
                    	
                        pageLinks.add(link);
                        pageLinkCounter++;
                    }
                    
                    link = null;
                }

                mouseOverPage = null;
                mouseOverElement = null;
                mouseOverElements = null;
            }

            page = null;
            htmlElements = null;
            htmlElement = null;
        }
        catch(Exception ex) {
            logger.log(Level.SEVERE, ex.toString());
        }
        
        return pageLinks;
    }

    public String retrieveInitialHTMLPage(String urlString) {
    	
        try {
        	
            //webClient = new WebClient(BrowserVersion.FIREFOX_17);
            webClient.setThrowExceptionOnFailingStatusCode(false);
            webClient.setThrowExceptionOnScriptError(Boolean.FALSE.booleanValue());
            //webClient.waitForBackgroundJavaScriptStartingBefore(10000L);
            webClient.getOptions().setJavaScriptEnabled(false);
            
            if (GAEUtils.isGaeMode()) {
                webClient.setWebConnection(new UrlFetchWebConnection(webClient));
            }
            
        } catch(Exception ex) {
            logger.log(Level.SEVERE, (new StringBuilder()).append("retrieveInitialHTMLPage error : ").append(ex.toString()).toString());
        }
        
        return retrieveHTMLPage(urlString);
    }

    public void addPromotion(IProduct product, String promotionString) {
    	
        product.setPromotion(true);
        product.setPromotionText(promotionString);
        
        if (promotionString.contains("SAVE") && promotionString.contains("%")) {
        	
            product.setPromotionType("SAVE");
            String discount = (new Double(round(1.0D - Double.parseDouble((new StringBuilder()).append("0.").append(promotionString.substring(5, 7)).toString()), 2))).toString().substring(2);
            String totalDiscount = discount.length() <= 1 ? (new StringBuilder()).append(discount).append("0").toString() : discount;
            product.setPromotionDiscount(totalDiscount);
            
        } else if (promotionString.contains("SAVE") && promotionString.contains("/")) {
        	
            String promotionStringSplit[] = promotionString.split(" ");
            String fractionSplit[] = promotionStringSplit[1].split("/");
            double fraction = Double.parseDouble(fractionSplit[0]) / Double.parseDouble(fractionSplit[1]);
            String discount = (new Double(round(1.0D - fraction, 2))).toString().substring(2);
            String totalDiscount = discount.length() <= 1 ? (new StringBuilder()).append(discount).append("0").toString() : discount;
            product.setPromotionType("SAVE");
            product.setPromotionDiscount(totalDiscount);
            
        } else if (promotionString.contains("SAVE") && !promotionString.contains("/") && !promotionString.contains("%")) {

            String promotionStringSplit[] = promotionString.split(" ");
            double savings = 0.0D;
            double originalPrice = 0.0D;
            
            if (promotionStringSplit[1].contains("c")) {
                savings = Double.parseDouble((new StringBuilder()).append("0.").append(promotionStringSplit[1].substring(0, 2)).toString());
            } else if (promotionStringSplit[1].contains(".")) {
                savings = Double.parseDouble(promotionStringSplit[1]);
            }
            
            if (promotionStringSplit[4].contains("c")) {
                originalPrice = Double.parseDouble((new StringBuilder()).append("0.").append(promotionStringSplit[4].substring(0, 2)).toString());
            } else if (promotionStringSplit[4].contains(".")) {
                originalPrice = Double.parseDouble(promotionStringSplit[4]);
            }
            
            String discount = (new Double(round(1.0D - savings / originalPrice, 2))).toString().substring(2);
            String totalDiscount = discount.length() <= 1 ? (new StringBuilder()).append(discount).append("0").toString() : discount;
            product.setPromotionType("SAVE");
            product.setPromotionDiscount(totalDiscount);
            
        } else if (promotionString.contains("HALF")) {
            product.setPromotionType("HALF PRICE");
            product.setPromotionDiscount("50");
        } else if (promotionString.contains("For")) {
        	
            String promotionStringSplit[] = promotionString.split(" ");
            int amount = Integer.parseInt(promotionStringSplit[0].trim());
            double discountAmount = Double.parseDouble(promotionStringSplit[2].trim());
            double originalAmount = Double.parseDouble(product.getPrice()) * (double)amount;
            String discount = (new Double(round(1.0D - (originalAmount - discountAmount) / originalAmount, 2))).toString().substring(2);
            String totalDiscount = discount.length() <= 1 ? (new StringBuilder()).append(discount).append("0").toString() : discount;
            product.setPromotionType("MULTI");
            product.setPromotionDiscount(totalDiscount);
            
        } else if (promotionString.contains("Buy")) {
            product.setPromotionType("BUY1GET1FREE");
            product.setPromotionDiscount("50");
        }
    }

    @Override
    public String getGroceryPage() { 
        return GROCERY_PAGE;
    }
}
