package com.company.supershop.selenium;


import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.company.supershop.selenium.pageobjects.LoginPageObject;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:selenium-config.xml")
public class WebLoginTest {
	
    private static final Log logger = LogFactory.getLog(WebLoginTest.class);
    
	@Autowired
	private URI siteBase;

	@Autowired
	private WebDriver firefoxDriver;

	
	public URI getSiteBase() {
		return siteBase;
	}

	public WebDriver getDriver() {
		return firefoxDriver;
	}
    
	@Before
	public void setup() {
		
    	getDriver().get(getSiteBase().toString());
    	firefoxDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	}
	
	
    @Test
	public void testLogin() {
		
		new LoginPageObject(getDriver(), getSiteBase()).login("test9", "password");
	}
    
    @After
    public void tearDown() {
    	firefoxDriver.close();
    }
}
