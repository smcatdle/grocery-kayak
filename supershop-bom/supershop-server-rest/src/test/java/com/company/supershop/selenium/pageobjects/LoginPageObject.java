package com.company.supershop.selenium.pageobjects;

import java.net.URI;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPageObject {

	@FindBy(xpath="//input [@ng-model = 'account.email']")
	private WebElement emailInput;
	
	@FindBy(xpath = "//input [@ng-model = 'account.password']")
	private WebElement passwordInput;
	
	@FindBy(xpath = "//button [contains(@class, 'btn-success')]")
	private WebElement submitButton;
	
	@FindBy(xpath = "(//div [contains(@class, 'thumbnail-bottom')])[9]//button")
	private WebElement addProductButton;
	
	private WebDriver driver = null;
	private URI siteBase = null;
	
	public LoginPageObject(WebDriver driver, URI siteBase) {
		this.driver = driver;
		this.siteBase = siteBase;
		
		PageFactory.initElements(driver, this);
	}
	
	public boolean login(String email, String password) {
		emailInput.sendKeys(email);
		passwordInput.sendKeys(password);
		submitButton.click();
		
		try {
			Thread.sleep(1000);
			addProductButton.click();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}
	
}
