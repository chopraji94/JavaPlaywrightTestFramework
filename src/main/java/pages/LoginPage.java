package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;

import java.util.Locale;

public class LoginPage {

    Page page;

    private final Locator userNameField;
    private final Locator passwordField;
    private final Locator loginButton;

    public LoginPage(Page page){
        this.page = page;
        userNameField = page.locator("//input[@name='username']");
        passwordField = page.locator("//input[@name='password']");
        loginButton = page.locator("//button[text()=' Login ']");
    }

    public void enterUserCreds(String userName,String password){
        userNameField.fill(userName);
        passwordField.fill(password);
    }

    public void clickLoginButton(){
        loginButton.click();
        page.waitForLoadState(LoadState.NETWORKIDLE,new Page.WaitForLoadStateOptions().setTimeout(80000));
    }
}
