package test;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import base.TestBase;

public class LoginTest extends TestBase {

    String userName,userPassword,launchUrl;

    @BeforeClass
    public void InitializeData(){
        log.info("Inside LoginTest class BeforeClass");
        launchUrl = properties.getProperty("launchUrl");
        userName = properties.getProperty("userName");
        userPassword = properties.getProperty("userPassword");
    }

    @Test(priority = 1)
    public void NavigateToUrl() throws InterruptedException {
        page.navigate(launchUrl, new Page.NavigateOptions().setWaitUntil(WaitUntilState.DOMCONTENTLOADED).setTimeout(80000));
    }

    @Test(priority = 2)
    public void loginAsAdmin(){
        initializePages.loginPage.enterUserCreds(userName,userPassword);
        initializePages.loginPage.clickLoginButton();
    }

    @Test(priority = 3)
    public void checkUserOnDashBoard(){
        boolean exist = initializePages.dashBoardPage.checkUserOnDashboard();
        Assert.assertTrue(exist);
    }

    @AfterClass
    public void afterClass(){
        log.info("Process completed");
    }
}
