package mooltipass.automatedTests.pageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class GitHub extends AbstractPage{
	
	public GitHub (WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	@FindBy(xpath = "//a[@href='/login']")
	private WebElement loginBtn;
	
	@FindBy(id = "login_field")
	private WebElement email;

	@FindBy(id = "password")
	private WebElement password;
	
	@FindBy(xpath = "//input[@class='btn btn-primary btn-block'][@value='Sign in']")
	private WebElement submitLogin;
	
	
	@FindBy(xpath = "//form[@class='logout-form']//button[@class='dropdown-item dropdown-signout']")
	private WebElement logoutBtn;
							
	@FindBy(xpath = "//details[contains(@class,'dropdown-details d-flex pl-2 flex-items-center')]")
	private WebElement dashBoard;
	
	public void enterEmail(String value){
		email.sendKeys(value);
	}

	public void enterPassword(String value){
		waitUntilAppears(password);
		password.sendKeys(value);
	}
	
	public void goToLogin(){
		loginBtn.click();
	}
	
	public void submit(){
		submitLogin.click();
	}
	public void goTodDashboard()
	{
		waitUntilAppears(dashBoard);
		dashBoard.click();
		
	}
	public void logout(){
		logoutBtn.click();
	}
	
	public boolean checkLogin(){		        
		waitUntilAppears(By.xpath( "//details[contains(@class,'dropdown-details d-flex pl-2 flex-items-center')]"));
		return isElementPresent(By.xpath( "//details[contains(@class,'dropdown-details d-flex pl-2 flex-items-center')]"));
	}
	public boolean checkAtLoginPage(){
		return isElementPresent(By.id("login_field"));
	}

}
