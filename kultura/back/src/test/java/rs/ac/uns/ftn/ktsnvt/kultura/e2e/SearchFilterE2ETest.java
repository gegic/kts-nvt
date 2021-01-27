package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.AddCulturalOfferingPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.ListViewPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.LoginPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SearchFilterE2ETest {
    public static final String BASE_URL = "https://localhost:4200";
    private static E2EUtils utils;
    private static WebDriver driver;
    private static ListViewPage listViewPage;

    @BeforeClass
    public static void setUp() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        utils = new E2EUtils(driver);

        driver.manage().window().maximize();
        LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);

        driver.get("https://localhost:4200/login");

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
    public void testSearch() throws InterruptedException {
        driver.get("https://localhost:4200/list-view");

        String searchQuery = "sajam";

        ListViewPage listViewPage = PageFactory.initElements(driver, ListViewPage.class);

        utils.ensureDisplayed(listViewPage.getSearchInput()).sendKeys(searchQuery + Keys.ENTER);

        JavascriptExecutor jse = (JavascriptExecutor) driver;

        int oldNumNames = 0;
        int newNumNames = listViewPage.getOfferingNames().size();

        while (newNumNames > oldNumNames) {
            jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            utils.waitFor(3000);

            oldNumNames = newNumNames;
            newNumNames = listViewPage.getOfferingNames().size();
        }

        assertTrue(listViewPage.getOfferingNames().stream().allMatch(we -> we.getText().toLowerCase()
                .contains(searchQuery.toLowerCase())));

        WebElement searchInput = utils.ensureDisplayed(listViewPage.getSearchInput());
        searchInput.sendKeys(Keys.CONTROL + "a");
        searchInput.sendKeys(Keys.DELETE);
        searchInput.sendKeys(Keys.ENTER);
    }

    @Test
    public void testFilterRating3() throws InterruptedException {
        driver.get("https://localhost:4200/list-view");

        ListViewPage listViewPage = PageFactory.initElements(driver, ListViewPage.class);

        utils.ensureDisplayed(listViewPage.getFilterButton()).click();

        WebElement leftHandle = utils.ensureDisplayed(listViewPage.getLeftHandle());


        Actions moveSlider = new Actions(driver);
        moveSlider.click(leftHandle).moveByOffset(25, 0).build().perform();

        listViewPage.getSaveFilterBtn().click();
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        utils.ensureDisplayed(listViewPage.getFilterButton());

        int oldNumNames = 0;
        int newNumNames = listViewPage.getReviews().size();

        while (newNumNames > oldNumNames) {
            jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            utils.waitFor(3000);

            oldNumNames = newNumNames;
            newNumNames = listViewPage.getReviews().size();
        }

        assertTrue(listViewPage.getReviews().stream()
                .allMatch(we ->  {
                    boolean noReviews = we.getText().equals("No reviews so far.");
                    if (noReviews) return true;
                    return Float.parseFloat(we.getText().substring(0, 3)) > 2.9;
                }));

        jse.executeScript("window.scrollTo(0, -document.body.scrollHeight)");

        utils.ensureDisplayed(listViewPage.getResetFilterBtn()).click();

        utils.waitFor(1000);
    }


    @Test
    public void testFilterOnlyReviews() throws InterruptedException {
        driver.get("https://localhost:4200/list-view");

        ListViewPage listViewPage = PageFactory.initElements(driver, ListViewPage.class);

        utils.ensureDisplayed(listViewPage.getFilterButton()).click();

        utils.ensureDisplayed(listViewPage.getOnlyReviewsCbx()).click();

        listViewPage.getSaveFilterBtn().click();

        JavascriptExecutor jse = (JavascriptExecutor) driver;


        utils.ensureDisplayed(listViewPage.getFilterButton());

        int oldNumNames = 0;
        int newNumNames = listViewPage.getReviews().size();

        while (newNumNames > oldNumNames) {
            jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            utils.waitFor(3000);

            oldNumNames = newNumNames;
            newNumNames = listViewPage.getReviews().size();
        }

        assertTrue(listViewPage.getReviews().stream()
                .noneMatch(we -> we.getText().equals("No reviews so far.")));

        jse.executeScript("window.scrollTo(0, -document.body.scrollHeight)");

        utils.ensureDisplayed(listViewPage.getResetFilterBtn()).click();

        utils.waitFor(1000);
    }

    @Test
    public void testFilterRating3OnlyReviews() throws InterruptedException {
        driver.get("https://localhost:4200/list-view");

        ListViewPage listViewPage = PageFactory.initElements(driver, ListViewPage.class);

        utils.ensureDisplayed(listViewPage.getFilterButton()).click();

        WebElement leftHandle = utils.ensureDisplayed(listViewPage.getLeftHandle());

        Actions moveSlider = new Actions(driver);
        moveSlider.click(leftHandle).moveByOffset(25, 0).build().perform();

        utils.ensureDisplayed(listViewPage.getOnlyReviewsCbx()).click();

        listViewPage.getSaveFilterBtn().click();
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        utils.ensureDisplayed(listViewPage.getFilterButton());

        int oldNumNames = 0;
        int newNumNames = listViewPage.getReviews().size();

        while (newNumNames > oldNumNames) {
            jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            utils.waitFor(3000);

            oldNumNames = newNumNames;
            newNumNames = listViewPage.getReviews().size();
        }

        assertTrue(listViewPage.getReviews().stream()
                .allMatch(we ->  {
                    return Float.parseFloat(we.getText().substring(0, 3)) > 2.9;
                }));

        jse.executeScript("window.scrollTo(0, -document.body.scrollHeight)");

        utils.ensureDisplayed(listViewPage.getResetFilterBtn()).click();

        utils.waitFor(1000);
    }

    @Test
    public void testFilterCategory() throws InterruptedException {
        driver.get("https://localhost:4200/list-view");
    
        ListViewPage listViewPage = PageFactory.initElements(driver, ListViewPage.class);
    
        utils.ensureDisplayed(listViewPage.getFilterButton()).click();
    
        WebElement leftHandle = utils.ensureDisplayed(listViewPage.getLeftHandle());
    
        Actions moveSlider = new Actions(driver);
        moveSlider.click(leftHandle).moveByOffset(25, 0).build().perform();
    
        utils.ensureDisplayed(listViewPage.getCategorySelect()).click();
    
        WebElement firstCategory = utils.ensureDisplayed(listViewPage.getFirstCategory());
    
        String categoryName = firstCategory.getText();
    
        firstCategory.click();
    
        listViewPage.getSaveFilterBtn().click();
        JavascriptExecutor jse = (JavascriptExecutor) driver;
    
        utils.ensureDisplayed(listViewPage.getFilterButton());
    
        int oldNumNames = 0;
        int newNumNames = listViewPage.getCategoriesSubcategories().size();
    
        while (newNumNames > oldNumNames) {
            jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            utils.waitFor(3000);
        
            oldNumNames = newNumNames;
            newNumNames = listViewPage.getCategoriesSubcategories().size();
        }
    
        assertTrue(listViewPage.getCategoriesSubcategories().stream()
                .allMatch(we ->  {
                    String catSub = we.getText();
                    int split = catSub.indexOf(">");
                    return catSub.substring(0, split).trim().equalsIgnoreCase(categoryName);
                }));
    
        jse.executeScript("window.scrollTo(0, -document.body.scrollHeight)");
    
        utils.ensureDisplayed(listViewPage.getResetFilterBtn()).click();
    
        utils.waitFor(1000);
    }

    @Test
    public void testFilterSubcategory() throws InterruptedException {
        driver.get("https://localhost:4200/list-view");

        ListViewPage listViewPage = PageFactory.initElements(driver, ListViewPage.class);

        utils.ensureDisplayed(listViewPage.getFilterButton()).click();

        WebElement leftHandle = utils.ensureDisplayed(listViewPage.getLeftHandle());

        Actions moveSlider = new Actions(driver);
        moveSlider.click(leftHandle).moveByOffset(25, 0).build().perform();

        utils.ensureDisplayed(listViewPage.getCategorySelect()).click();

        utils.ensureDisplayed(listViewPage.getFirstCategory()).click();

        utils.waitFor(3000);

        utils.ensureDisplayed(listViewPage.getSubcategorySelect()).click();

        WebElement firstSubcategory = utils.ensureDisplayed(listViewPage.getFirstSubcategory());

        String subcategoryName = firstSubcategory.getText();

        firstSubcategory.click();

        listViewPage.getSaveFilterBtn().click();
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        utils.ensureDisplayed(listViewPage.getFilterButton());

        int oldNumNames = 0;
        int newNumNames = listViewPage.getCategoriesSubcategories().size();

        while (newNumNames > oldNumNames) {
            jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            utils.waitFor(3000);

            oldNumNames = newNumNames;
            newNumNames = listViewPage.getCategoriesSubcategories().size();
        }

        assertTrue(listViewPage.getCategoriesSubcategories().stream()
                .allMatch(we ->  {
                    String catSub = we.getText();
                    int split = catSub.indexOf(">");
                    return catSub.substring(split + 1, catSub.length()).trim().equalsIgnoreCase(subcategoryName);
                }));

        jse.executeScript("window.scrollTo(0, -document.body.scrollHeight)");

        utils.ensureDisplayed(listViewPage.getResetFilterBtn()).click();

        utils.waitFor(1000);
    }

    @Test
    public void testFilterOnlyMySubscriptions() throws InterruptedException {
        driver.get("https://localhost:4200/list-view");

        ListViewPage listViewPage = PageFactory.initElements(driver, ListViewPage.class);

        utils.ensureDisplayed(listViewPage.getFilterButton()).click();

        utils.ensureDisplayed(listViewPage.getOnlySubscriptionsCbx()).click();
        listViewPage.getSaveFilterBtn().click();

        utils.ensureDisplayed(listViewPage.getFilterButton());

        JavascriptExecutor jse = (JavascriptExecutor) driver;

        int oldNumOfferings = 0;
        int newNumOfferings = listViewPage.getCategoriesSubcategories().size();

        while (newNumOfferings > oldNumOfferings) {
            jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
            utils.waitFor(3000);

            oldNumOfferings = newNumOfferings;
            newNumOfferings = listViewPage.getUnsubscribeButtons().size();
        }

        assertEquals(0, listViewPage.getSubscribeButtons().size());

        jse.executeScript("window.scrollTo(0, -document.body.scrollHeight)");

        utils.ensureDisplayed(listViewPage.getResetFilterBtn()).click();

        utils.waitFor(1000);
    }


}
