package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

import java.util.function.Function;

public class SideMenu {

    Page page;

    private final Function<String, Locator> sideMenuOptions;

    public SideMenu(Page page){
        this.page = page;

        this.sideMenuOptions = (text) -> page.locator(String.format("//span[text()='%s']//ancestor::a",text));
    }

    public void moveToPage(String option){
        sideMenuOptions.apply(option).click();
        page.waitForLoadState(LoadState.NETWORKIDLE,new Page.WaitForLoadStateOptions().setTimeout(80000));
    }

}
