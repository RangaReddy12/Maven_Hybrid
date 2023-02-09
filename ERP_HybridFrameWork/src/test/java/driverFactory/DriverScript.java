package driverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import commonFunctions.FunctionLibrary;
import utilities.ExcelFileUtil;

public class DriverScript {
public static WebDriver driver;
String inputpath ="D:\\Ojt_Selenium\\ERP_HybridFrameWork\\FileInput\\DataEngine.xlsx";
String outputpath ="D:\\Ojt_Selenium\\ERP_HybridFrameWork\\FileOutPut\\HybridResults.xlsx";
ExtentReports report;
ExtentTest test;
public void startTest()throws Throwable
{
	//create reference object
	ExcelFileUtil xl = new ExcelFileUtil(inputpath);
	//iterate all rows in mastertestcases sheet
	for(int i=1;i<=xl.rowCount("MasterTestCases");i++)
	{
		String ModuleStatus = "";
		if(xl.getCellData("MasterTestCases", i, 2).equalsIgnoreCase("Y"))
		{
			//define module name
			String TCModule =xl.getCellData("MasterTestCases", i, 1);
			report = new ExtentReports("./ExtentReports/"+TCModule+FunctionLibrary.generateDate()+".html");
			//iterate all rows in each testcases
			for(int j=1;j<=xl.rowCount(TCModule);j++)
			{
				test= report.startTest(TCModule);
				//read all cell from TCModule
				String Description = xl.getCellData(TCModule, j, 0);
				String Object_Type =xl.getCellData(TCModule, j, 1);
				String Locator_Type =xl.getCellData(TCModule, j, 2);
				String Locator_Value = xl.getCellData(TCModule, j, 3);
				String Test_Data =xl.getCellData(TCModule, j, 4);
				try
				{
					if(Object_Type.equalsIgnoreCase("startBrowser"))
					{
						driver =FunctionLibrary.startBrowser();
						test.log(LogStatus.INFO, Description);
					}
					else if(Object_Type.equalsIgnoreCase("openUrl"))
					{
						FunctionLibrary.openUrl(driver);
						test.log(LogStatus.INFO, Description);
					}
					else if(Object_Type.equalsIgnoreCase("waitForElement"))
					{
						FunctionLibrary.waitForElement(driver, Locator_Type, Locator_Value, Test_Data);
						test.log(LogStatus.INFO, Description);
					}
					else if(Object_Type.equalsIgnoreCase("typeAction"))
					{
						FunctionLibrary.typeAction(driver, Locator_Type, Locator_Value, Test_Data);
						test.log(LogStatus.INFO, Description);
					}
					else if(Object_Type.equalsIgnoreCase("clickAction"))
					{
						FunctionLibrary.clickAction(driver, Locator_Type, Locator_Value);
						test.log(LogStatus.INFO, Description);
					}
					else if(Object_Type.equalsIgnoreCase("validateTitle"))
					{
						FunctionLibrary.validateTitle(driver, Test_Data);
						test.log(LogStatus.INFO, Description);
					}
					else if(Object_Type.equalsIgnoreCase("closeBrowser"))
					{
						FunctionLibrary.closeBrowser(driver);
						test.log(LogStatus.INFO, Description);
					}
					else if(Object_Type.equalsIgnoreCase("mouseClick"))
					{
						FunctionLibrary.mouseClick(driver);
						test.log(LogStatus.INFO, Description);
					}
					else if(Object_Type.equalsIgnoreCase("stockTable"))
					{
						FunctionLibrary.stockTable(driver, Test_Data);
						test.log(LogStatus.INFO, Description);
					}
					else if(Object_Type.equalsIgnoreCase("captureData"))
					{
						FunctionLibrary.captureData(driver, Locator_Type, Locator_Value);
						test.log(LogStatus.INFO, Description);
					}
					else if(Object_Type.equalsIgnoreCase("supplierTable"))
					{
						FunctionLibrary.supplierTable(driver);
						test.log(LogStatus.INFO, Description);
					}
					//write as pass into TCModule sheet
					xl.setCellData(TCModule, j, 5, "Pass", outputpath);
					test.log(LogStatus.PASS, Description);
					ModuleStatus="True";
				}catch(Exception e)
				{
					System.out.println(e.getMessage());
					//write as Fail into TCModule sheet
					xl.setCellData(TCModule, j, 5, "Fail", outputpath);
					test.log(LogStatus.FAIL, Description);
					ModuleStatus="False";
					File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					
					FileUtils.copyFile(scrFile, new File("./Screenshots/"+Description+"_"+FunctionLibrary.generateDate()+".jpg"));
					
					String image = test.addScreenCapture("./Screenshots/"+Description+"_"+FunctionLibrary.generateDate()+".jpg");
					
					test.log(LogStatus.FAIL, image);
					
					break;
					
				}
				catch(AssertionError a)
				{
					//write as Fail into TCModule sheet
					xl.setCellData(TCModule, j, 5, "Fail", outputpath);
					test.log(LogStatus.FAIL, Description);
					ModuleStatus="False";
					File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
					
					FileUtils.copyFile(scrFile, new File("./Screenshots/"+Description+"_"+FunctionLibrary.generateDate()+".jpg"));
					
					String image = test.addScreenCapture("./Screenshots/"+Description+"_"+FunctionLibrary.generateDate()+".jpg");
					
					test.log(LogStatus.FAIL, image);
					
					break;
				}
				if(ModuleStatus.equalsIgnoreCase("True"))
				{
				//	write as pass into mastertestcase sheet
					xl.setCellData("MasterTestCases", i, 3, "Pass", outputpath);
				}
				else
				{
//					write as pass into mastertestcase sheet
					xl.setCellData("MasterTestCases", i, 3, "Fail", outputpath);
				}
			}
		}
		else
		{
			//which testcase flag N Write as Blocked into status cell
			xl.setCellData("MasterTestCases", i, 3, "Blocked", outputpath);
		}
		report.endTest(test);
		report.flush();
	}
}
}
