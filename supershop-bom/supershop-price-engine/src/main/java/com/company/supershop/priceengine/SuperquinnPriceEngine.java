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


public class SuperquinnPriceEngine extends PriceEngine {

    protected static final String SUPERQUINN_CATEGORY_HTML = "categoryListing1$repCategoryListing$ctl";
    protected static final String NEXT_PAGE_STRING = "&ProductPage=";
    protected static final String PRODUCT_LIST_SCREEN = "TESCO_PRODUCT_LIST_SCREEN";
    protected static final String NEXT_PAGE_LAST_SCREEN = "Sorry";
    protected static final String PRODUCT_STRING = "new TESCO.sites.UI.entities.Product";
    protected static final String EQUALS_STRING = "ID=";
    protected static final String DEPARTMENT_STRING = "ShopDepID";
    protected static final String CATEGORY_STRING = "ShopCatID";
    protected static final String CATEGORY_LINK = "http://www.superquinn.ie/shopping/Shopping/Shop.aspx?t=2#&&ShopCatID=";
    protected static final String GROCERY_PAGE = "http://www.superquinn.ie/shopping/ShopHome.aspx";
    protected static final String PRODUCTS_TABLE_HTML = "ctl00_ctl00_cphContent_cphMain_productBrowser_productListing_pnlProductListings";
    protected static final String OUTPUT_FILE = "SuperquinnProducts.xml";
    public static final String SUPERQUINN_CHAIN = "q";
    private final Logger logger = Logger.getLogger(SuperquinnPriceEngine.class.getName());
    protected int pageIncrement;
    
    
    public SuperquinnPriceEngine() {
    	
        super("q");
        pageIncrement = 1;
        nextPageLastScreen = "Sorry";
        nextPageString = "&ProductPage=";
        productListScreen = "TESCO_PRODUCT_LIST_SCREEN";
        productString = "new TESCO.sites.UI.entities.Product";
        outputFileName = "SuperquinnProducts.xml";
    }

    public String retrieveInitialHTMLPage(String urlString) {
    	
        webClient = new WebClient(BrowserVersion.FIREFOX_17);
        System.getProperties().put("org.apache.commons.logging.simplelog.defaultlog", "trace");
        
        try {
        	
            webClient.setThrowExceptionOnFailingStatusCode(false);
            webClient.closeAllWindows();
            HtmlPage page1 = (HtmlPage)webClient.getPage("http://www.superquinn.ie/shopping/ShopHome.aspx");
            HtmlForm form = page1.getFormByName("aspnetForm");
            HtmlElement button = form.getElementById("ctl00_ctl00_cphContent_cphContent_ucntrlLogin_btnGo");
            HtmlTextInput emailField = (HtmlTextInput)form.getInputByName("ctl00$ctl00$cphContent$cphContent$ucntrlLogin$txtEmailAddress");
            HtmlPasswordInput passwordField = (HtmlPasswordInput)form.getInputByName("ctl00$ctl00$cphContent$cphContent$ucntrlLogin$txtPassword");
            emailField.setValueAttribute("s_l_mcardle@yahoo.com");
            passwordField.setValueAttribute("snowboard1");
            HtmlPage page2 = (HtmlPage)button.click();
            HtmlForm form2 = page2.getFormByName("aspnetForm");
            HtmlElement button2 = form2.getElementById("ctl00_ctl00_cphContent_cphContent_addressAreaMappingPopup_btnGo");
            HtmlPage htmlPage = (HtmlPage)button2.click();
            return htmlPage.asXml();
            
        } catch(MalformedURLException e) {
        	
        } catch(IOException e) { 
        	
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
    	
        String menuIdString = "";
        Set pageLinks = null;
        
        if (pageLink.equals("http://www.superquinn.ie/shopping/ShopHome.aspx")) {
        	
            pageLinks = new HashSet();
            for (String link : (List<String>)searchLinks) {
            	pageLinks.add(link);
            }

            return pageLinks;
        }
        
        String splitPageLink[] = pageLink.split("ID=");
        String categorySubString = splitPageLink[1];
        
        if (pageLink.contains("ShopDepID")) {
        	
            int menuId = Integer.parseInt(categorySubString);
            pageLinks = getSubMenuLinks("http://www.superquinn.ie/shopping/Shopping/Shop.aspx?t=2#&&ShopCatID=", getDepartmentIdString(menuId));
        } else if(pageLink.contains("ShopCatID")) {
            pageLinks = getSubMenuLinks("http://www.superquinn.ie/shopping/Shopping/Shop.aspx?t=2#&&ShopCatID=", categorySubString);
        }
        
        return pageLinks;
    }

    public List findProductElements(String pageLink, String id) {
    	
        List productElements = null;
        productElements = new ArrayList();
        
        try {
        	
            webClient.closeAllWindows();
            HtmlPage page = (HtmlPage)webClient.getPage(pageLink);
            Thread.sleep(2000L);
            HtmlElement htmlElement = page.getDocumentElement();
            List<HtmlElement> list = (List<HtmlElement>)htmlElement.getByXPath("//tr [contains(@class,'listing')]");
            List categoryList = htmlElement.getByXPath("//li//a [contains(@id,'ctl00_ctl00_cphContent_cphMain_breadcrumb')]");
            ProductElement productElement;
            
            for (HtmlElement element : list) {
                productElement = new ProductElement(element.asXml());
                productElement.setCategory(((HtmlElement)categoryList.get(0)).getTextContent());
                productElement.setSubCategory(((HtmlElement)categoryList.get(categoryList.size() - 1)).getTextContent());
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
    	
        SuperquinnProduct product = null;
        
        try {
        	
            Node priceNode = getProductItemNode(productElement, "//span [contains(@id,'_lblPrice')]").item(0);
            String priceString = priceNode.getTextContent().trim();
            String price = priceString.substring(1, priceString.indexOf(" "));
            Node pricePerUnitNode = getProductItemNode(productElement, "//td//div//span [contains(@id,'_lblpricePerUnit')]").item(0);
            String pricePerUnit = pricePerUnitNode.getTextContent().trim();
            Node nameNode = getProductItemNode(productElement, "//a [@class='product_title']").item(0);
            String name = nameNode.getTextContent().trim();
            Node productIdNode = getProductItemNode(productElement, "//tr [contains(@class,'product_info_container')]").item(0);
            String productId = getNodeAttribute(productIdNode, "id");
            Node imageUrlNode = getProductItemNode(productElement, "//img [contains(@src,'http://www.superquinn.ie/shopping/')]").item(0);
            String imageUrl = getNodeAttribute(imageUrlNode, "src");
            product = new SuperquinnProduct();
            product.setProductId(productId);
            product.setName(replaceSpecialHtmlChars(name));
            product.setPrice(price);
            product.setImageURL(imageUrl);
            product.setUnitPrice(pricePerUnit);
            product.setSuperdepartment(replaceSpecialHtmlChars(productElement.getCategory()));
            product.setShelfName(replaceSpecialHtmlChars(productElement.getSubCategory()));
            product.setChain(chain);
            
            if (!imageUrl.contains("product_placeholder_small")) {
                product.setBarcode(getBarcode(imageUrl));
            }
            
        } catch(Exception ex) {
            logger.log(Level.SEVERE, ex.toString());
        }
        
        return product;
    }

    public String getBarcode(String imageUrl) {
    	
        String barcode = null;
        String splitHtmlPage[] = imageUrl.split("http://www.superquinn.ie/shopping/images/products/SMALL/");
        String splitHtmlPage1[] = splitHtmlPage[1].split("_1.JPG");
        barcode = splitHtmlPage1[0];
        
        return barcode;
    }

    private Set getSubMenuLinks(String subLink, String params) {
    	
        Set pageLinks = null;
        int missingLinkAttempts = 0;
        int nextMenuId = 1;
        boolean allLinksFound = false;
        pageLinks = new HashSet();
        
        while(!allLinksFound) 
            try
            {
                String categoryIdString = getCategoryIdString(nextMenuId);
                String newPageLink = (new StringBuilder()).append(subLink).append(params).append(categoryIdString).toString();
                webClient.closeAllWindows();
                HtmlPage page = (HtmlPage)webClient.getPage(newPageLink);
                Thread.sleep(2000L);
                HtmlElement htmlElement = page.getDocumentElement();
                List list = htmlElement.getByXPath((new StringBuilder()).append("//a [contains(@id,'_").append(params).append(categoryIdString).append("')]").toString());
                
                if(list.size() > 0) {
                	
                    missingLinkAttempts = 0;
                    if (!previousPageLinks.contains(newPageLink)) {
                    	
                        pageLinks.add(newPageLink);
                        pageLinkCounter++;
                        logger.log(Level.INFO, (new StringBuilder()).append("Found Sub Menu Link : [").append(newPageLink).append("]").toString());
                    }
                    
                } else {
                    missingLinkAttempts++;
                    logger.log(Level.INFO, (new StringBuilder()).append("Invalid Sub Menu Link : [").append(newPageLink).append("]").toString());
                }
                
                if (missingLinkAttempts > 3) {
                    allLinksFound = true;
                }
               
            } catch(Exception ex) {
            	
                missingLinkAttempts++;
                logger.log(Level.SEVERE, ex.toString());
                nextMenuId++;
            }
            
        return pageLinks;
    }

    private String getDepartmentIdString(int id) {
    	
        String departmentIdString = null;
        
        if (id < 10) {
            departmentIdString = (new StringBuilder()).append(id).append("00").toString();
        } else {
            departmentIdString = (new StringBuilder()).append(id).append("0").toString();
        }
        
        return departmentIdString;
    }

    private String getCategoryIdString(int id) {
    	
        String categoryIdString = null;
        
        if (id < 10) {
            categoryIdString = (new StringBuilder()).append("0").append(id).toString();
        } else {
            categoryIdString = (new StringBuilder()).append("").append(id).toString();
        }
        
        return categoryIdString;
    }

	@Override
	public void persist(IProduct product) {
		// TODO Auto-generated method stub
		
	}

    @Override
    public String getGroceryPage() { 
        return GROCERY_PAGE;
    }

}
