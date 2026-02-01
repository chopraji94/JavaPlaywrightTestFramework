package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class AdminPage {

    Page page;

    private final Locator adminPageTitle;

    public AdminPage(Page page){
        this.page = page;
        adminPageTitle = page.locator("//h6[text()='Admin']");
    }

    public boolean checkUserOnAdminTab(){
        return adminPageTitle.isVisible();
    }
}
