package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.time.Duration;

public class DashBoardPage {

    Page page;

    private final Locator dashBoardTitle;

    public DashBoardPage(Page page){
        this.page = page;
        dashBoardTitle = page.locator("//h6[text()='Dashboard']");
    }

    public boolean checkUserOnDashboard(){
        return dashBoardTitle.isVisible();
    }
}
