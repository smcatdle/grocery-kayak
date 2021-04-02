/** SuperShop Price Engine   **/

package com.company.supershop.priceengine;

import com.company.supershop.dao.impl.ProductDAOImpl;
import com.company.supershop.model.*;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.*;
import javax.xml.xpath.*;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class PriceEngine implements IPriceEngine {

 private final Logger logger = Logger.getLogger(PriceEngine.class.getName());

 	public static final int PAGE_INDEX_INCREMENT = 20;
 
    public Gson gson;
    protected static final String AMP = "&";
    protected static final String AMP_SAFE = "&amp;";
    protected static final String IMAGE_END = "/IDShot_90x90.jpg";
    protected static final String VIEW_MODE_PARAM = "viewMode";
    protected static final String ACTION_PARAM = "action";
    protected static final String HTTP_STRING = "http://";
    protected static final String NEXT_PAGE_STRING = "&Nao=";
    protected static final String NEXT_PAGE_LAST_SCREEN = "Refine your search";
    protected static final String PRODUCT_STRING = "new TESCO.sites.UI.entities.Product";
    protected static final String PRODUCT_LIST_SCREEN = "next";
    protected static final String TESCO_DEPARTMENT_LINK = "www.tesco.ie/groceries/department/default.aspx?";
    protected static final String TESCO_PRODUCT_LINK = "www.tesco.ie/groceries/product/browse/default.aspx?";
    protected static final String OUTPUT_FILE = "TescoProducts.xml";

    protected static final int MAX_PRODUCT_NAME_LENGTH = 249;


    protected String GROCERY_PAGE = "NA";
    protected Set previousPageLinks;
    private Set previousProducts;
    protected int pageLinkCounter;
    private int productCounter;
    private int counter;
    protected int pageIncrement;
    protected File outputFile;
    protected String outputFileName;
    private BufferedOutputStream outputStream;
    protected PrintWriter writer;
    protected String nextPageLastScreen;
    protected String nextPageString;
    protected String productListScreen;
    protected String productString;
    protected WebClient webClient;
    protected String initialGroceryPage;
    protected XPath xpath;
    protected DocumentBuilder xmlDocumentBuilder;
    protected String chain;
    

    @Autowired
    private ProductDAOImpl productDAO;

    
    public void setProductDAO(ProductDAOImpl productDAO) {
        this.productDAO = productDAO;
    }
	

    public PriceEngine(String chain) {
    	
        gson = new Gson();
        
        reset();
        
        this.chain = chain;
        xpath = null;
        xmlDocumentBuilder = null;
        logger.log(Level.INFO, "PriceEngine()");
        nextPageLastScreen = "Refine your search";
        nextPageString = "&Nao=";
        productListScreen = "Nao=";
        productString = "new TESCO.sites.UI.entities.Product";
        outputFileName = "TescoProducts.xml";
        
        try {
        	
            XPathFactory factory = XPathFactory.newInstance();
            xpath = factory.newXPath();
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            xmlDocumentBuilder = documentBuilderFactory.newDocumentBuilder();
             
        } catch(ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void reset() { 
    	
    	logger.log(Level.INFO, "Initializing WebClient...");
    	
        try {

        	webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_9);
			webClient.setUseInsecureSSL(true);
			
		} catch (GeneralSecurityException ex) {
			logger.log(Level.SEVERE, ex.toString());
			ex.printStackTrace();
		} 
        
        previousPageLinks = new HashSet();
        previousProducts = new HashSet();
        pageLinkCounter = 0;
        productCounter = 0;
        counter = 0;
    }
    
    public void createOutputFile() {
    	
        try {
        	
            outputFile = new File(outputFileName);
            outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
            writer = new PrintWriter(outputStream);
            writer.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            writer.print("<A>");
            
        } catch(FileNotFoundException ex) {
            logger.log(Level.SEVERE, ex.toString());
            ex.printStackTrace();
        }
    }

    public void closeOutputFile() {
    	
        try {
            writer.print("</A>");
            writer.close();
            outputStream.close();
            
        } catch(FileNotFoundException ex) {
            logger.log(Level.SEVERE, ex.toString());
            ex.printStackTrace();
        } catch(IOException ioex) {
            logger.log(Level.SEVERE, ioex.toString());
            ioex.printStackTrace();
        }
    }

    public String retrieveInitialHTMLPage(String urlString) {
        return retrieveHTMLPage(urlString);
    }

    public String retrieveHTMLPage(String urlString) {
    	
        String pageHTML = null;
        String line = null;
        URL url = null;
        
        try {
            url = new URL(urlString);
            HttpURLConnection uc = (HttpURLConnection)url.openConnection();
            uc.connect();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            for(pageHTML = ""; (line = in.readLine()) != null; pageHTML = (new StringBuilder()).append(pageHTML).append(line).toString());
            uc.disconnect();
            in = null;
            uc = null;
            url = null;
            
        } catch(IOException e) {
            logger.log(Level.SEVERE, e.toString());
            e.printStackTrace();
        }
        
        return pageHTML;
    }

    public Set findPageLinks(String pageLink, List searchLinks) { 
    	
        Set pageLinks = null;
        String newPageLink = null;
        String htmlPage = retrieveHTMLPage(pageLink);
        pageLinks = new HashSet();

        for (String link : (List<String>)searchLinks) {
        	
            String splitHtmlPage[] = htmlPage.split(link);
            if(splitHtmlPage.length > 1) {
            	
                for(int i = 1; i < splitHtmlPage.length; i++) {
                	
                    String currentSplitHtmlPage = splitHtmlPage[i];
                    if(currentSplitHtmlPage.substring(1, 2).equals("N")) {
                    	
                        String extractedLink = splitHtmlPage[i].substring(1, splitHtmlPage[i].indexOf("\"")).replace("&amp;", "&");
                        String extractedLinkActionRemoved[] = extractedLink.split("action");
                        String extractedLinkViewModeRemoved[] = extractedLinkActionRemoved[0].split("viewMode");
                        newPageLink = (new StringBuilder()).append("http://").append(link).append(extractedLinkViewModeRemoved[0]).toString();
                        String formattedPageLink = newPageLink;
                        
                        if(!previousPageLinks.contains(formattedPageLink)) {
                            pageLinks.add(formattedPageLink);
                            pageLinkCounter++;
                        }
                        
                        extractedLink = null;
                        extractedLinkActionRemoved = null;
                        extractedLinkViewModeRemoved = null;
                        newPageLink = null;
                        formattedPageLink = null;
                    }
                    currentSplitHtmlPage = null;
                }

                splitHtmlPage = null;
            }
        }
        
        htmlPage = null;
        return pageLinks;
    }

    public List findProductElements(HtmlPage htmlPage, String pageLink, String id) {
    	
        List htmlElements = null;
        htmlElements = new ArrayList();
        String page = retrieveHTMLPage(pageLink);
        String splitHtmlPage[] = page.split(id);
        if(splitHtmlPage.length > 1) {
            for(int i = 1; i < splitHtmlPage.length; i++) {
            	
                String productAsString = splitHtmlPage[i].substring(0, splitHtmlPage[i].indexOf(");"));
                ProductElement product = new ProductElement(productAsString);
                if(page.contains("promo")) {
                    product.setHtmlPage(page);
                    product.setPageLink(pageLink);
                    htmlElements.add(product);
                }
                productAsString = null;
            }

        }
        
        htmlPage = null;
        splitHtmlPage = null;
        return htmlElements;
    }

    public void start(List searchLinks) {
        
    	reset();
    	
        logger.log(Level.INFO, "Engine Started with initial link [" + getGroceryPage() + "]");
        retrieveInitialHTMLPage(getGroceryPage());
        findProducts(getGroceryPage(), searchLinks);
        webClient.closeAllWindows();
        
        logger.log(Level.INFO, "The engine has completed.");
        
        System.gc();
        /*logger.log(Level.INFO, "The following pages have no links : ");
        for(String link : (List<String>)noLinksFound) {
        	logger.log(Level.INFO, (new StringBuilder()).append("[").append(link).append("]").toString());
        }*/

    }

    public void findProducts(String pageLink, List searchLinks) {
        
    	try {   		
            webClient.closeAllWindows();
            int pageIndex = 0;
            Set<String> pageLinks = findPageLinks(pageLink, searchLinks);
            int numberLinks = pageLinks.size();
            int numberProcessedLinks = 0;
            
            if (pageLinks != null) {
	            for (String currentPageLink : pageLinks) {
	            	numberProcessedLinks++;
	            	
	            	try {
		            	logger.info("Current Memory : Free [" + Runtime.getRuntime().freeMemory() + "] Total [" + Runtime.getRuntime().totalMemory() + "]");
		            	
		                if(!previousPageLinks.contains(currentPageLink)) {
		                	
		                    previousPageLinks.add(currentPageLink);

		                    webClient.closeAllWindows();
		                    HtmlPage page = (HtmlPage)webClient.getPage(currentPageLink);
		                    Thread.sleep(2000L);
		                    String htmlPage = page.asXml();
		                    
		                    logger.log(Level.WARNING, (new StringBuilder()).append("currentPageLink : [").append(currentPageLink).append("]").toString());
		                    logger.log(Level.WARNING, "Current Memory : Free [" + Runtime.getRuntime().freeMemory() + "] Total [" + Runtime.getRuntime().totalMemory() + "]");
	
		                        List<ProductElement> productElements = findProductElements(page, currentPageLink, getProductString());
		                        for (ProductElement productElement : productElements) {
		                        	
		                            IProduct product = extractProductElement(productElement);
		                            if(product != null && !previousProducts.contains(product.getProductId())) {
		                            	logger.info("Current Memory : Free [" + Runtime.getRuntime().freeMemory() + "] Total [" + Runtime.getRuntime().totalMemory() + "]");
		                            	previousProducts.add(product.getProductId());                           	
		                            }
		                            
		                            if (product != null) persist(product);
		                        } 
		
		                        // Iterate through all paginated links
	                        	pageIndex = getPageIndexIncrement();
	                        	String htmlPageTmp = htmlPage;
	                            while (hasNextPage(htmlPageTmp, pageIndex)) {
	                            	htmlPageTmp = findProductsOnNextScreen(page, currentPageLink, pageIndex);
	                            	pageIndex = pageIndex + getPageIndexIncrement();
	                            }
	                        
		                        htmlPage = null;
		                        productElements = null;
		                    
		                        Thread.sleep(2000);
	                            System.gc();
		                        if (findRecursiveLinks()) findProducts(currentPageLink, searchLinks);
		                }      
		                
	                } catch(Exception ex) {
	                    logger.log(Level.SEVERE, ex.toString());
	                    ex.printStackTrace();
	                }
	            }
            }
            
            logger.info("Processed [" + numberProcessedLinks + "] out of [" + numberLinks + "] links.");
            
        } catch(Exception ex) {
            logger.log(Level.SEVERE, ex.toString());
            ex.printStackTrace();
        }
    }

    public boolean hasNextPage(String htmlPage, int pageIndex) {
        return htmlPage.contains(productListScreen + pageIndex);
    }

    public String findProductsOnNextScreen(HtmlPage htmlPage, String pageLink, int pageIndex) {

        List<ProductElement> productElements;
        int newPageIndex = 0;
        String currentPageLink = null;
        webClient.closeAllWindows();
        HtmlPage page = null;
        String pageText = null;
        
		try {
            currentPageLink = getNextPageLink(htmlPage, pageLink, pageIndex);
			/*page = (HtmlPage)webClient.getPage(currentPageLink);
	
	        HtmlElement htmlElement = page.getDocumentElement();
			pageText = page.asXml();
	        List htmlElements = htmlElement.getByXPath(getHtmlElementXPath());
	        
	        if (htmlElements.size() > 0) {*/

                productElements = findProductElements(htmlPage, pageLink, getProductString());
	        
                if (productElements.size() > 0) {

                    for (ProductElement productElement : productElements) {
                        IProduct product = extractProductElement(productElement);
                
                        if(!previousProducts.contains(product.getProductId())) {
                            previousProducts.add(product.getProductId());
                        }

                        if (product != null) persist(product);
                    }
                }
            //} 

            newPageIndex = pageIndex + pageIncrement;
            
        } catch(Exception ex) {
            newPageIndex = -1;
        	logger.log(Level.SEVERE, ex.toString());
        	ex.printStackTrace();
        }
        
        return pageText;
    }

    public String getNextPageLink(HtmlPage htmlPage, String originalPageLink, int pageIndex) {
    	return (new StringBuilder()).append(originalPageLink).append(getNextPageString()).append(pageIndex).toString();
    }
    
    public String getProductString() {
    	return productString;
    }
    
    public String getNextPageString() {
    	return nextPageString;
    }
    
    public IProduct extractProductElement(ProductElement productElement) {
    	
        IProduct product = new Product();
        TescoSiteProduct tescoSiteProduct = null;
        String htmlText = null;
        htmlText = productElement.getProductAsString();
        
        try {
        	
            webClient.closeAllWindows();
            tescoSiteProduct = gson.fromJson(htmlText.substring(1), TescoSiteProduct.class);
            product.setBarcode(getBarcode(tescoSiteProduct.getImageURL()));
            product.setImageURL(tescoSiteProduct.getImageURL());
            product.setPrice(tescoSiteProduct.getPrice());
            product.setProductId(tescoSiteProduct.getProductId());
            product.setUnitPrice(tescoSiteProduct.getUnitPrice());
            
            
            /*if (productElement.getHtmlPage().contains((new StringBuilder()).append(product.getProductId()).append("-promo").toString())) {
            	
                HtmlPage page = (HtmlPage)webClient.getPage(productElement.getPageLink());
                Thread.sleep(2000L);
                HtmlElement htmlElement = page.getDocumentElement();
                List htmlElements = htmlElement.getByXPath((new StringBuilder()).append("//li [contains(@id,'").append(product.getProductId()).append("')]//div [contains(@class,'promo')]//em").toString());
                String promotionString = ((HtmlElement)htmlElements.get(0)).getTextContent().trim();
                addPromotion(product, promotionString);
                page = null;
                promotionString = null;
                htmlElement = null;
                htmlElements = null;
            }*/
            
            htmlText = null;
            product.setName(replaceSpecialHtmlChars(tescoSiteProduct.getName()));
            product.setSuperdepartment(replaceSpecialHtmlChars(tescoSiteProduct.getSuperdepartment().trim()));
            product.setShelfName(replaceSpecialHtmlChars(tescoSiteProduct.getShelfName().trim()));
            product.setChain(chain);
            
        } catch(Exception ex) {
        	logger.log(Level.SEVERE, "PRODUCT PARSE ERROR for chain [" + chain + "] pageLink [" + productElement.getPageLink() + "]" + ex.toString());
            ex.printStackTrace();
        }
        
        return product;
    }

    public void writeProductInfo(IProduct product) {
    	
        initialiseProductOutput(product);
        writeProductOutput(product);
        finalizeProductOutput(product);
    }

    public void initialiseProductOutput(IProduct product) {
        counter++;
        writer.print("<X>");
    }

    public void writeProductOutput(IProduct product) {
    	
        writer.print((new StringBuilder()).append("<N>").append(product.getName()).append("</N>").toString());
        writer.print((new StringBuilder()).append("<I>").append(product.getProductId()).append("</I>").toString());
        writer.print((new StringBuilder()).append("<Q>").append(product.getQuantity()).append("</Q>").toString());
        writer.print((new StringBuilder()).append("<B>").append(product.getBarcode()).append("</B>").toString());
        writer.print((new StringBuilder()).append("<M>").append(product.getMaxQuantity()).append("</M>").toString());
        writer.print((new StringBuilder()).append("<C>").append(product.getIncrement()).append("</C>").toString());
        writer.print((new StringBuilder()).append("<P>").append(product.getPrice()).append("</P>").toString());
        writer.print((new StringBuilder()).append("<E>").append(product.getAbbr()).append("</E>").toString());
        writer.print((new StringBuilder()).append("<U>").append(product.getUnitPrice()).append("</U>").toString());
        writer.print((new StringBuilder()).append("<W>").append(product.getCatchWeight()).append("</W>").toString());
        writer.print((new StringBuilder()).append("<D>").append(product.getSuperdepartment()).append("</D>").toString());
        writer.print((new StringBuilder()).append("<F>").append(product.getShelfName()).append("</F>").toString());
        writer.print((new StringBuilder()).append("<R>").append(product.getSuperdepartmentID()).append("</R>").toString());
    }

    public void finalizeProductOutput(IProduct product) {
        writer.print("</X>");
    }

    public String getBarcode(String imageUrl) {
    	
        String barcode = null;
        int imageUrlLength = 0;
        String splitHtmlPage[] = imageUrl.split("/IDShot_90x90.jpg");
        imageUrlLength = splitHtmlPage[0].length();
        barcode = splitHtmlPage[0].substring(imageUrlLength - 13, imageUrlLength);
        splitHtmlPage = null;
        return barcode;
    }

    public void addPromotion(IProduct iproduct, String s) {
    	
    }

    public String replaceSpecialHtmlChars(String text) {
    	
        String replacedText = null;
        replacedText = text;
        replacedText.replace("&", "&amp;");
        return replacedText;
    }

    protected NodeList getProductItemNode(ProductElement productElement, String xpathString) {
    	
        XPathExpression expression = null;
        NodeList nodes = null;
        String htmlString = productElement.getProductAsString();
        
        try {
        	
            ByteArrayInputStream input = new ByteArrayInputStream(htmlString.getBytes("UTF-8"));
            org.w3c.dom.Document doc = xmlDocumentBuilder.parse(input);
            expression = xpath.compile(xpathString);
            Object object = expression.evaluate(doc, XPathConstants.NODESET);
            nodes = (NodeList)object;
            input = null;
            doc = null;
            expression = null;
            
        } catch(XPathExpressionException e) {
            e.printStackTrace();
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch(SAXException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
        
        return nodes;
    }

    protected String getNodeAttribute(Node node, String attributeName)  {
    	
        String attributeValue = null;
        NamedNodeMap attributes = node.getAttributes();
        int count = 0;
        
        while (attributes != null && attributes.getLength() > count) {
            Node attributeNode = attributes.item(count);
            if(attributeNode.getNodeName().equals(attributeName)) {
                attributeValue = attributeNode.getNodeValue();
            }
            
            count++;
        } 
        
        attributes = null;
        return attributeValue;
    }

    public static double round(double valueToRound, int numberOfDecimalPlaces) {
    	
        double multipicationFactor = Math.pow(10D, numberOfDecimalPlaces);
        double interestedInZeroDPs = valueToRound * multipicationFactor;
        return (double)Math.round(interestedInZeroDPs) / multipicationFactor;
    }

    @Override
    public void persist(IProduct product) {     
        
    	try {
	        String gsonString = gson.toJson(((Product)product));
	        logger.log(Level.INFO, "ProductMVO json is [" + gsonString + "]");
	        
	        // Check if product has already been added, then update. Otherwise add it.
	        List<ProductMVO> existingProducts = productDAO.findByNameAndChain(product.getName(), product.getChain());
	        ProductMVO productMVO = (existingProducts.size() > 0) ? existingProducts.get(0) : new ProductMVO();
	        productMVO.setProduct(product);
	        
	        if (existingProducts.size() == 0) {  		        
		        productDAO.add(productMVO);
	        } else {
	        	productMVO.setDateUpdated(new Date());
	        	productDAO.update(productMVO);
	        }
	        
    	} catch(Exception ex) {
            logger.log(Level.SEVERE, ex.toString());
            ex.printStackTrace();
    	}
    	
    }

    public String getGroceryPage() { 
        return GROCERY_PAGE;
    }
    
    public int getPageIndexIncrement() {
    	return PAGE_INDEX_INCREMENT;
    }
    
    public boolean findRecursiveLinks() {
    	return false;
    }

    public String getHashValue() {
        return null;
    }
}
