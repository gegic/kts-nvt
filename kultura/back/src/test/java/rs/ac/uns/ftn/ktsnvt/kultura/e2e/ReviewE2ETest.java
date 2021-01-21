package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.LoginPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.ReviewPage;

import static org.junit.Assert.assertEquals;

public class ReviewE2ETest {

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
        justWait(300);

        loginPage = PageFactory.initElements(driver, LoginPage.class);
        loginPage.getEmail().sendKeys(userEmail);
        loginPage.getNextBtn().click();
        justWait(300);
        loginPage.getPassword().sendKeys(userPassword);
        loginPage.getLoginBtn().click();
        justWait(200);
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
        reviewPage.getCommentReview().clear();
        reviewPage.getCommentReview().sendKeys(E2EConstants.REVIEW_COMMENT);
        reviewPage.getSubmitBtn().click();
        justWait(100);
        logout();

        login(E2EConstants.USER1_EMAIL, E2EConstants.USER1_PASSWORD);

        driver.get("http://localhost:4200/cultural-offering/1/reviews");

        reviewPage = PageFactory.initElements(driver, ReviewPage.class);
        justWait();
        driver.findElements(By.xpath("//p")).forEach(el-> System.out.println(el.getText()));
        assertEquals(1, driver.findElements(By.xpath("//p[contains(text(),'0 people subscribed.')]")).size());
        assertEquals(1, driver.findElements(By.xpath("//p[contains(text(),'" + E2EConstants.REVIEW_COMMENT + "')]")).size());
        assertEquals(E2EConstants.USER_NAME, driver.findElement(By.xpath("//p[contains(text(),'" + E2EConstants.REVIEW_COMMENT + "')]/parent::div/preceding-sibling::div[3]/div/h4")).getText());
        logout();
    }


    @Test
    public void PostReviewSuccess() throws InterruptedException {

        login(E2EConstants.USER1_EMAIL, E2EConstants.USER1_PASSWORD);
        driver.get("http://localhost:4200/cultural-offering/1/reviews");
        justWait();
        reviewPage = PageFactory.initElements(driver, ReviewPage.class);
        reviewPage.getCommentReview().sendKeys(E2EConstants.REVIEW_COMMENT);
        reviewPage.getSubmitBtn().click();
        justWait(100);
        logout();

        login(E2EConstants.USER1_EMAIL, E2EConstants.USER1_PASSWORD);

        driver.get("http://localhost:4200/cultural-offering/1/reviews");

        reviewPage = PageFactory.initElements(driver, ReviewPage.class);
        justWait();
        assertEquals(1, driver.findElements(By.xpath("//p[text()='" + E2EConstants.REVIEW_COMMENT + "']")).size());
        logout();
    }

    private static void logout() throws InterruptedException {
        driver.findElement(By.id("user-menu")).click();
        driver.findElement(By.xpath("//*[text()='Logout']")).click();
        justWait(100);
    }


    private static void justWait() throws InterruptedException {
        synchronized (driver) {
            driver.wait(1000);
        }
    }
}
