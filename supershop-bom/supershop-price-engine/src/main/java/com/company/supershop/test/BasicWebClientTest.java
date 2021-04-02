/**
 * 
 */
package com.company.supershop.test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author smcardle
 * 
 */
public class BasicWebClientTest {
    
    
    public static final String LOGIN_PAGE = "http://shop.supervalu.ie/shopping/StartShopping/login.aspx";

    
    private static final Logger logger = Logger.getLogger(BasicWebClientTest.class
	    .getName());
    
    
    
    public static void main(String[] args) {
	
	try {

	    WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_9);
	    webClient.setThrowExceptionOnFailingStatusCode(false);
	    webClient.getOptions().setThrowExceptionOnScriptError(false);
	    webClient.closeAllWindows();
	    webClient.getOptions().setJavaScriptEnabled(true);
	    webClient.setUseInsecureSSL(true);
	    webClient.setJavaScriptTimeout(50);
	    webClient.setCssEnabled(false);
	    webClient.getOptions().setCssEnabled(false);

	    webClient.closeAllWindows();

	    HtmlPage page = (HtmlPage) webClient.getPage(LOGIN_PAGE);
	   
	    logger.log(Level.INFO, page.asXml());
	    
	} catch (FailingHttpStatusCodeException e) {
	      
	    e.printStackTrace();
	} catch (MalformedURLException e) {
	      
	    e.printStackTrace();
	} catch (IOException e) {
	      
	    e.printStackTrace();
	} catch (GeneralSecurityException e) {
	      
	    e.printStackTrace();
	}
	
    }
    
    
}
