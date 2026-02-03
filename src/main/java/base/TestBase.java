package base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.microsoft.playwright.Page;
import org.testng.ITestResult;
import org.testng.annotations.*;
import pageObjects.IntializePages;
import utils.DriverManager;
import utils.ExtentManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.logging.Logger;

public class TestBase {

    public static final Logger log = Logger.getLogger(TestBase.class.getName());
    String propertiesFile = String.valueOf(Paths.get(System.getProperty("user.dir"),"/src/test/resources/properties/Test.properties"));
    public Properties properties;
    public IntializePages initializePages;
    public Page page;
    public static ExtentReports extent;
    String reportPath;

    public static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<>();
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        log.info(">>> [Suite] Global Setup: Cleaning old videos...");
        deleteOldVideos();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
        String formattedDate = now.format(formatter);
        reportPath = System.getProperty("user.dir")+String.format("/reports/Test-report_%s.html",formattedDate);
        log.info("Extent report path -> "+reportPath);
        extent = ExtentManager.getInstance(reportPath);
    }

    @BeforeClass(alwaysRun = true)
    public void setUp() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(propertiesFile);
        properties = new Properties();
        properties.load(fileInputStream);
        String browserName = properties.getProperty("browser");

        // Initializes the driver for the thread assigned to this specific class
        DriverManager.init(browserName);
        page = DriverManager.getPage();
        initializePages = new IntializePages(page);

        ExtentTest parent = extent.createTest(this.getClass().getSimpleName());
        parentTest.set(parent);
    }

    @BeforeMethod
    public void beforeMethod(Method method){
        System.out.println(method.getName());
        ExtentTest child = parentTest.get().createNode(method.getName());
        test.set(child);
    }

    @AfterMethod
    public void afterMethod(ITestResult result){
        if(result.getStatus() == ITestResult.FAILURE){

            test.get().fail("Test Failed: " + result.getName());
            test.get().fail(result.getThrowable());

            try {
                String screenShotPath = System.getProperty("user.dir") + "/screenshots/" + result.getName() +  "_" + System.currentTimeMillis() + ".png";
                page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get(screenShotPath)));
                Thread.sleep(Duration.ofSeconds(10));
                test.get().addScreenCaptureFromPath(screenShotPath);
            }catch (Exception e){
                test.get().warning("Test failed, but could not capture screenshot: " + e.getMessage());
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            test.get().pass("Test Passed: "+result.getName());
        } else if (result.getStatus() == ITestResult.SKIP) {
            test.get().skip("Test Skipped: "+result.getName());
        }
    }

    // Runs once after all @Test methods in the class are finished
    @AfterClass(alwaysRun = true)
    public void tearDown() {
        // Closes the browser for this specific thread/class
        DriverManager.quit();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        log.info(">>> [Suite] Global Teardown: Generating Report...");
        extent.flush();
    }

    private void deleteOldVideos() {
        try {
            File dir = new File(System.getProperty("user.dir") + "/resultVideos");
            if (dir.exists()) {
                for (File file : dir.listFiles()) {
                    if (!file.isDirectory()) file.delete();
                }
            }
        } catch (Exception e) {
            log.info("Warning: Could not clean video directory");
        }
    }
}