package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.LoginPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.ReviewPage;

import static org.junit.Assert.assertEquals;

public class ReviewE2ETest {
    private static E2EUtils utils;

    private static WebDriver driver;

    private static ReviewPage reviewPage;

    private static LoginPage loginPage;

    private static void justWait(int timeout) throws InterruptedException {
        synchronized (driver) {
            driver.wait(timeout);
        }
    }

    @BeforeClass
    public static void setUp() throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        driver.manage().window().maximize();
        reviewPage = PageFactory.initElements(driver, ReviewPage.class);
        loginPage = PageFactory.initElements(driver, LoginPage.class);


//        login(E2EConstants.USER_EMAIL, E2EConstants.USER_PASSWORD);
    }

    private static void login(String userEmail, String userPassword) throws InterruptedException {
        driver.get("http://localhost:4200/login");
        justWait(1300);

        loginPage = PageFactory.initElements(driver, LoginPage.class);
        loginPage.getEmail().sendKeys(userEmail);
        loginPage.getNextBtn().click();
        justWait(1300);
        loginPage.getPassword().sendKeys(userPassword);
        loginPage.getLoginBtn().click();
        justWait(1200);
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }


    @Test
    public void ChangeReviewSuccess() throws InterruptedException {

        login(E2EConstants.USER_EMAIL, E2EConstants.USER_PASSWORD);
        driver.get("http://localhost:4200/cultural-offering/1/reviews");
        justWait(300);
        reviewPage = PageFactory.initElements(driver, ReviewPage.class);
        reviewPage.getReviewButton().click();
        justWait(200);
        driver.findElement(By.xpath("//h4[contains(text(),'Rating')]/following-sibling::p-rating/div/span[contains(@class, 'p-rating-icon')][5]")).click();
        reviewPage.getCommentReview().clear();
        reviewPage.getCommentReview().sendKeys(E2EConstants.REVIEW_COMMENT);
        reviewPage.getSubmitBtn().click();
        justWait(100);
        logout();

        login(E2EConstants.USER1_EMAIL, E2EConstants.USER1_PASSWORD);

        driver.get("http://localhost:4200/cultural-offering/1/reviews");



        reviewPage = PageFactory.initElements(driver, ReviewPage.class);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,2000)");

        justWait();

        assertEquals(1, driver.findElements(By.xpath("//p[contains(text(),'" + E2EConstants.REVIEW_COMMENT + "')]")).size());
        assertEquals(E2EConstants.USER_NAME, driver.findElement(By.xpath("//p[contains(text(),'" + E2EConstants.REVIEW_COMMENT + "')]/parent::div/preceding-sibling::div[3]/div/h4")).getText());
        assertEquals(1, driver.findElements(By.xpath("//p[contains(text(),'" + E2EConstants.REVIEW_COMMENT + "')]/parent::div/preceding-sibling::div[3]/div/p-rating/div/span[contains(@ng-reflect-ng-class,'pi-star-o')]")).size());
        logout();
    }


    @Test
    public void PostReviewSuccess() throws InterruptedException {

        login(E2EConstants.USER1_EMAIL, E2EConstants.USER1_PASSWORD);
        driver.get("http://localhost:4200/cultural-offering/1/reviews");
        justWait(3000);
        reviewPage = PageFactory.initElements(driver, ReviewPage.class);
        justWait(3000);
        reviewPage.ensureIsDisplayed("review-button");
        reviewPage.getReviewButton().click();
        //driver.findElement(By.className("blue-button")).click();
        justWait(3000);

        driver.findElement(By.xpath("//h4[contains(text(),'Rating')]/following-sibling::p-rating/div/span[contains(@class, 'p-rating-icon')][4]")).click();
        reviewPage.getCommentReview().clear();
        reviewPage.getCommentReview().sendKeys(E2EConstants.REVIEW_COMMENT1);
        justWait(3000);
        reviewPage.getSubmitBtn().click();
        justWait(3000);
        logout();

        justWait(3000);

        login(E2EConstants.USER_EMAIL, E2EConstants.USER_PASSWORD);

        driver.get("http://localhost:4200/cultural-offering/1/reviews");

        reviewPage = PageFactory.initElements(driver, ReviewPage.class);
        justWait();

        assertEquals(1, driver.findElements(By.xpath("//p[contains(text(),'" + E2EConstants.REVIEW_COMMENT1 + "')]")).size());
        assertEquals(E2EConstants.USER_NAME1, driver.findElement(By.xpath("//p[contains(text(),'" + E2EConstants.REVIEW_COMMENT1 + "')]/parent::div/preceding-sibling::div[3]/div/h4")).getText());
        assertEquals(2, driver.findElements(By.xpath("//p[contains(text(),'" + E2EConstants.REVIEW_COMMENT1 + "')]/parent::div/preceding-sibling::div[3]/div/p-rating/div/span[contains(@ng-reflect-ng-class,'pi-star-o')]")).size());
        logout();
    }

    private static void logout() throws InterruptedException {
        justWait(3000);
        //driver.findElement(By.cssSelector("#user-menu")).click();
        WebElement ele = driver.findElement(By.cssSelector("#avatar"));
        JavascriptExecutor executor = (JavascriptExecutor)driver;
        executor.executeScript("arguments[0].click();", ele);
        justWait(3000);
        driver.findElement(By.xpath("//*[text()='Logout']")).click();
        justWait(1000);
    }

//    @Test
//    public void logout() throws InterruptedException {
//        // any page
//        utils.ensureDisplayed("avatar").click();
//
//        utils.ensureDisplayed("logout-btn").click();
//
//    }


    private static void justWait() throws InterruptedException {
        synchronized (driver) {
            driver.wait(1000);
        }
    }
}
