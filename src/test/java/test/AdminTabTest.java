package test;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import base.TestBase;

public class AdminTabTest extends TestBase {

    String userName,userPassword,launchUrl;

    @BeforeClass
    public void InitializeData(){
        log.info("Inside Admin Tab test class BeforeClass");
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
    public void checkUserOnAdminTab(){
        initializePages.sideMenu.moveToPage("Admin");
        boolean exist = initializePages.adminPage.checkUserOnAdminTab();
        Assert.assertTrue(exist);
    }

    @AfterClass
    public void afterClass(){
        log.info("Admin tab Process completed");
    }
}
