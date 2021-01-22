package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.CulturalOfferingDetailsPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.ListViewPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.LoginPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserFunctionsE2ETest {
    public static final String BASE_URL = "http://localhost:4200";
    private static E2EUtils utils;
    private static WebDriver driver;
    private static CulturalOfferingDetailsPage detailsPage;

    @BeforeClass
    public static void setUp() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        utils = new E2EUtils(driver);

        driver.manage().window().maximize();
        LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);

        driver.get("http://localhost:4200/login");

        loginPage.ensureDisplayed(loginPage.getEmail()).sendKeys("user@mail.com");
        loginPage.getNextBtn().click();

        loginPage.ensureDisplayed(loginPage.getPassword()).sendKeys("admin123");

        loginPage.getLoginBtn().click();

        utils.ensureLoggedIn();
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

    @Test
    public void subscribeAndUnsubscribe() throws InterruptedException {
        driver.get("http://localhost:4200/cultural-offering/1");
        CulturalOfferingDetailsPage detailsPage = PageFactory.initElements(driver, CulturalOfferingDetailsPage.class);

        utils.ensureDisplayed(detailsPage.getSubscribeBtn()).click();

        WebElement unsubscribeButton = utils.ensureDisplayed(detailsPage.getUnsubscribeBtn());

        String unsubscribeButtonText = unsubscribeButton.getText();
        assertEquals("Unsubscribe", unsubscribeButtonText);

        unsubscribeButton.click();

        String subscribeText = utils.ensureDisplayed(detailsPage.getSubscribeBtn()).getText();

        assertEquals("Subscribe", subscribeText);
    }

    @Test
    public void logout() throws InterruptedException {
        // any page
        utils.ensureDisplayed("avatar").click();

        utils.ensureDisplayed("logout-btn").click();

        assertEquals("http://localhost:4200/login", driver.getCurrentUrl());
    }
}
