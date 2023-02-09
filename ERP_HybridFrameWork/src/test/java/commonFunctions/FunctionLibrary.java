package commonFunctions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import utilities.PropertyFileUtil;

public class FunctionLibrary {
public static WebDriver driver;
public static String Expected_Data="";
public static String Actual_Data="";
//method for lauunch browser
public static WebDriver startBrowser()throws Throwable
{
	if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("chrome"))
	{
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}
	else if(PropertyFileUtil.getValueForKey("Browser").equalsIgnoreCase("firefox"))
	{
		driver = new FirefoxDriver();
	}
	else
	{
		System.out.println("Browser Value is not matching");
	}
	return driver;
}
//method for lauching url
public static void openUrl(WebDriver driver) throws Throwable
{
	driver.get(PropertyFileUtil.getValueForKey("Url"));
}
//method for wait for elements
public static void waitForElement(WebDriver driver,String LocatorType,String LocatorValue,String TestData)
{
	WebDriverWait myWait = new WebDriverWait(driver, Integer.parseInt(TestData));
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		myWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(LocatorValue)));
	}
	else if(LocatorType.equalsIgnoreCase("id"))
	{
		myWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(LocatorValue)));
	}
	else if(LocatorType.equalsIgnoreCase("name"))
	{
		myWait.until(ExpectedConditions.visibilityOfElementLocated(By.name(LocatorValue)));
		
	}
}
public static void typeAction(WebDriver driver,String LocatorType,String LocatorValue,String TestData)
{
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(LocatorValue)).clear();
		driver.findElement(By.xpath(LocatorValue)).sendKeys(TestData);
	}
	else if(LocatorType.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(LocatorValue)).clear();
		driver.findElement(By.id(LocatorValue)).sendKeys(TestData);
	}
	else if(LocatorType.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(LocatorValue)).clear();
		driver.findElement(By.name(LocatorValue)).sendKeys(TestData);
	}
}
//method for click action
public static void clickAction(WebDriver driver,String LocatorType,String LocatorValue)
{
	if(LocatorType.equalsIgnoreCase("xpath"))
	{
		driver.findElement(By.xpath(LocatorValue)).click();
	}
	else if(LocatorType.equalsIgnoreCase("id"))
	{
		driver.findElement(By.id(LocatorValue)).sendKeys(Keys.ENTER);
	}
	else if(LocatorType.equalsIgnoreCase("name"))
	{
		driver.findElement(By.name(LocatorValue)).click();
	}
}
public static void validateTitle(WebDriver driver,String ExpectedTitle)
{
	String actualTitle = driver.getTitle();
	try {
	Assert.assertEquals(actualTitle, ExpectedTitle,"Title is Not Matching");
	}catch(Throwable t)
	{
		System.out.println(t.getMessage());
	}
}
public static void closeBrowser(WebDriver driver)
{
	driver.quit();
}
//method for mouse click
public static void mouseClick(WebDriver driver)throws Throwable
{
	Actions ac = new Actions(driver);
	ac.moveToElement(driver.findElement(By.xpath("//a[text()='Stock Items ']"))).perform();
	Thread.sleep(4000);
	ac.moveToElement(driver.findElement(By.xpath("(//a[contains(.,'Stock Categories')])[2]"))).click().perform();
	
}
//method for stock table
public static void stockTable(WebDriver driver,String TestData)throws Throwable
{
	if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-panel"))).click();
	Thread.sleep(4000);
	driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).sendKeys(TestData);
	driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-button"))).click();
	String actualdata = driver.findElement(By.xpath("//table[@id='tbl_a_stock_categorieslist']/tbody/tr[1]/td[4]/div/span/span")).getText();
	System.out.println(TestData+"      "+actualdata);
	try {
	Assert.assertEquals(TestData, actualdata,"Category Name Not Found in table");
	}catch(Throwable t)
	{
		System.out.println(t.getMessage());
	}
}
//method capture supplier number
public static void captureData(WebDriver driver,String LocatorType,String LocatorValue)
{
 Expected_Data = driver.findElement(By.name(LocatorValue)).getAttribute("value");
}
//method for supplier table
public static void supplierTable(WebDriver driver)throws Throwable
{
	if(!driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).isDisplayed())
		driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-panel"))).click();
	Thread.sleep(4000);
	driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-textbox"))).sendKeys(Expected_Data);
	driver.findElement(By.xpath(PropertyFileUtil.getValueForKey("search-button"))).click();
	Actual_Data =driver.findElement(By.xpath("//table[@id='tbl_a_supplierslist']/tbody/tr[1]/td[6]/div/span/span")).getText();
	System.out.println(Expected_Data+"      "+Actual_Data);
	Assert.assertEquals(Expected_Data, Actual_Data,"Supplier Number Not Found in Table");
}
//method for generate date
public static String generateDate()
{
	Date date = new Date();
	DateFormat df = new SimpleDateFormat("YYYY_MM_dd");
	return df.format(date);
}
public static void add()
{
	int a=456789,b=42312123,c;
	c=a/b;
	System.out.println(c);
}
}








