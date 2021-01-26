package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.LoginPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.ReviewPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReviewE2ETest {
    private static E2EUtils utils;

    private static WebDriver driver;

    private static ReviewPage reviewPage;

    private static LoginPage loginPage;


    @BeforeClass
    public static void setUp() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        utils = new E2EUtils(driver);

        driver.manage().window().maximize();
        LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
        driver.get("http://localhost:4200/login");

        utils.ensureDisplayed(loginPage.getEmail()).sendKeys("user@mail.com");
        loginPage.getNextBtn().click();
        utils.ensureDisplayed(loginPage.getPassword()).sendKeys("admin123");
        loginPage.getLoginBtn().click();
        utils.ensureLoggedIn();
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }


    @Test
    public void ChangeReviewSuccess() throws InterruptedException {

        driver.get("http://localhost:4200/cultural-offering/1/reviews");
        reviewPage = PageFactory.initElements(driver, ReviewPage.class);

        utils.ensureDisplayed(reviewPage.getReviewButton()).click();

        utils.ensureDisplayed(reviewPage.getFourthStar()).click();
        reviewPage.getCommentReview().clear();
        reviewPage.getCommentReview().sendKeys(E2EConstants.REVIEW_COMMENT);
        reviewPage.getSubmitBtn().click();

        utils.waitFor(1000);

        utils.ensureDisplayed(reviewPage.getReviewButton()).click();
        WebElement fourthStar = utils.ensureDisplayed(reviewPage.getFourthStar());
        WebElement fifthStar = reviewPage.getFifthStar();
        assertTrue(fourthStar.getAttribute("class").contains("pi-star"));
        assertTrue(fifthStar.getAttribute("class").contains("pi-star-o"));
        assertEquals(E2EConstants.REVIEW_COMMENT, reviewPage.getCommentReview().getAttribute("value"));
    }


    @Test
    public void PostReviewSuccess() throws InterruptedException {

        driver.get("http://localhost:4200/cultural-offering/7/reviews");
        reviewPage = PageFactory.initElements(driver, ReviewPage.class);

        utils.ensureDisplayed(reviewPage.getReviewButton()).click();

        utils.ensureDisplayed(reviewPage.getFourthStar()).click();
        reviewPage.getCommentReview().clear();
        reviewPage.getCommentReview().sendKeys(E2EConstants.REVIEW_COMMENT1);

        reviewPage.getSubmitBtn().click();

        utils.waitFor(1000);

        utils.ensureDisplayed(reviewPage.getReviewButton()).click();
        WebElement fourthStar = utils.ensureDisplayed(reviewPage.getFourthStar());
        WebElement fifthStar = reviewPage.getFifthStar();
        assertTrue(fourthStar.getAttribute("class").contains("pi-star"));
        assertTrue(fifthStar.getAttribute("class").contains("pi-star-o"));
        assertEquals(E2EConstants.REVIEW_COMMENT1, reviewPage.getCommentReview().getAttribute("value"));
    }
}
