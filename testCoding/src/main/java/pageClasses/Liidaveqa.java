package pageClasses;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import io.github.bonigarcia.wdm.WebDriverManager;



public class Liidaveqa {
	
	
	
	
	public static WebDriver driver;
	public static  WebDriver inititateBrowser() {
		System.setProperty("webdriver.chrome.driver","chromedriver.exe");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("network.proxy.http", "93.180.7.246");
        capabilities.setCapability("network.proxy.http_port", "8080");
        ChromeDriverService service =
        	    new ChromeDriverService.Builder().withWhitelistedIps("").withVerbose(true).build();
        	driver = new ChromeDriver(service, capabilities);
       
		return driver;
		
	}
	public static  WebDriver inititateFirefoxBrowser() {
		System.setProperty("webdriver.gecko.driver","geckodriver.exe");
		FirefoxOptions options = new FirefoxOptions();
		options.addArguments("--headless");
		options.setLogLevel(FirefoxDriverLogLevel.TRACE);
		driver = new FirefoxDriver(options);
		return driver=new FirefoxDriver();
		
	}
	public WebDriver inititateBrowserUsingManager() {
		WebDriverManager.chromedriver().setup();
		return driver=new ChromeDriver();
	}
	public static Map<String,String> testDataReader(int sheetno) throws IOException {
		String path=System.getProperty("user.dir","\\TestData.xlsx");
		FileInputStream fis=new FileInputStream(path);
		Workbook wb=new XSSFWorkbook(fis);
		Sheet s =wb.getSheetAt(sheetno);
		//int celcount=s.getLastRowNum();
		Map<String,String> inputData=new HashMap<String,String>();
		for(int i=0;i<=1;i++) {
			Row r =s.getRow(i);
			String cvalue=r.getCell(i).getStringCellValue();
			String ckey=r.getCell(0).getStringCellValue();
			inputData.put(ckey,cvalue);
		}
		return inputData;
		
	}
	public static void setUserNamePassword() throws IOException {
		Map<String,String> inputData1=testDataReader(0);
		Set<String> keys=inputData1.keySet();
		for(String key:keys) {
			if(key.equalsIgnoreCase("Username")) {
					driver.findElement(By.id("j_username")).sendKeys(inputData1.get(key));
			}
			else if(key.equalsIgnoreCase("Password")) {					
					driver.findElement(By.id("j_password")).sendKeys(inputData1.get(key));
			}
		}
	}
	public static String menuNavigation() throws IOException {
		Map<String,String> inputData2=testDataReader(1);
		Set<String> keys=inputData2.keySet();
		for(String key:keys) {
			if(key.equalsIgnoreCase("MainMenu")) {
				driver.findElement(By.xpath("//a[text()='"+inputData2.get(key)+"']")).click();
			}
			else if(key.equalsIgnoreCase("Password")) {		
				driver.findElement(By.xpath("//a[text()='"+inputData2.get(key)+"' and @class='aaa']")).click();	
			}
	}
		String title=driver.findElement(By.xpath("//div[@class='description'/p]")).getText();
		return title;
	}
	
	public static void verifyProductDetails(Map<String,String> details) throws IOException {
		Map<String,String> inputData3=testDataReader(3);
		Set<String> keys=inputData3.keySet();
		Map<String,String> actualProductDetails=new HashMap<String,String>();
		if(inputData3.size()==details.size()) {
		for(String key:keys) {
			Assert.assertEquals(inputData3.get(key),actualProductDetails.get(key));
			test.log(LogStatus.PASS,"Product found with matching details");
		}
		}
		else {
			test.log(LogStatus.FAIL,"No in correct size");
		}
	
	}
	@Test
	public void execution() throws IOException {
//		Liidaveqa l=new Liidaveqa();
		//l.inititateBrowserUsingManager();
		WebDriver driver = inititateBrowserUsingManager();
		driver.get("liidaveqa.com");
		driver.findElement(By.xpath("//a[contains(text(),'Sign In')")).click();
		Liidaveqa.setUserNamePassword();		
		driver.findElement(By.id("loginSubmit")).click();
		driver.findElement(By.xpath("//a[@href='#navigation']/i")).click();
		String landingPage="Shop for HVAC compressors on LennoxPros.com.";
		Assert.assertEquals(landingPage,Liidaveqa.menuNavigation());
		driver.findElement(By.xpath("//a[@href='/compressors/compressors/c/p261' and text()='Compressors']")).click();
		WebElement nextButton=driver.findElement(By.className("next icon icon-arrow-right"));
		boolean flag=false;
		Map<String,String> productDetails=new HashMap<String,String>();
		do {			  
		List<WebElement> elements=driver.findElements(By.xpath("//span[text()='Model/Part #: ']"));
		for(WebElement element:elements) {
			if(element.getText().equalsIgnoreCase("10T46 ")) {
				flag=true;
				productDetails.put("1. Product Description", driver.findElement(By.xpath("//div/span[text()='10T46'/ancestor::h2[@class='title']")).getText());
				productDetails.put("2. Model/Part # ", driver.findElement(By.xpath("//span[contains(text(),'10T46']")).getText());
				productDetails.put("3. Your Price",driver.findElement(By.xpath("//div/span[text()='10T46'/descendant::span[@class='your-price-text']")).getText());
				productDetails.put("4. List Price", driver.findElement(By.xpath("//div/span[text()='10T46'/descendant::span[contains(text()='List Price:')]")).getText());
				productDetails.put("5. Standard up Availability", driver.findElement(By.xpath("//div/span[text()='10T46'/parent::div/following::div[contains(@class='ship-to-availability availability')]")).getText());
				productDetails.put("6. Pick up store in Availability", driver.findElement(By.xpath("//div/span[text()='10T46'/parent::div/following::div[contains(@class='local-availability availability')]")).getText());
				productDetails.put("7. Zip Code", driver.findElement(By.xpath("//div/span[text()='10T46'/parent::div/following::div[contains(@class='pickup')]")).getText());
				productDetails.put("8. Add to Cart Status",Boolean.toString(driver.findElement(By.xpath("//div/span[text()='10T46'/parent::div/following::div[contains(@class='pickup')]")).isEnabled()));
			}
		}
		}while(nextButton.isEnabled() && flag==false);
		if(flag==false) {
			test.log(LogStatus.FAIL,"No Such Product found");
		}
		
		
		verifyProductDetails(productDetails);
		
	}
	static ExtentTest test;
	static ExtentReports report;
@BeforeClass
public static void startTest() {
	report =new ExtentReports(System.getProperty("user.dir")+"ExtentReport.html");
	test =report.startTest("Demo");
}
@AfterClass
public static void endTest() {
	report.endTest(test);
	report.flush();
}
	
}