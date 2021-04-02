/**
 * 
 */
package com.company.priceengine;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import com.company.supershop.priceengine.SuperValuPriceEngine;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * @author smcardle
 *
 */
public class SupervaluPriceEngineTest {

	private final Logger logger = Logger
			.getLogger(SupervaluPriceEngineTest.class.getName());

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	
	 @Test public void testSuperValuPriceEngine() {
	 
		 SuperValuPriceEngine engine = new SuperValuPriceEngine();
		 ArrayList<String> searchLinks = new ArrayList<String>();
	 
		 engine.start(searchLinks);
	  
	 }
	 

	/*@Test
	public void testLogin() {

		WebClient webClient = new WebClient();
		//System.getProperties().put(
				//"org.apache.commons.logging.simplelog.defaultlog", "trace");

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


		} catch (MalformedURLException e) {
			logger.log(Level.SEVERE, e.toString());
			e.printStackTrace();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.toString());
			e.printStackTrace();
		}
	}*/
}
