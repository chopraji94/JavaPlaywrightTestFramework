package pageObjects;

import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.AdminPage;
import pages.DashBoardPage;
import pages.LoginPage;
import pages.SideMenu;

public class IntializePages {

    private static final Logger log = LoggerFactory.getLogger(IntializePages.class);
    Page page;
    public LoginPage loginPage;
    public AdminPage adminPage;
    public SideMenu sideMenu;
    public DashBoardPage dashBoardPage;

    public IntializePages(Page page){
        this.page = page;

        loginPage = new LoginPage(page);
        adminPage = new AdminPage(page);
        sideMenu = new SideMenu(page);
        dashBoardPage = new DashBoardPage(page);
    }
}
