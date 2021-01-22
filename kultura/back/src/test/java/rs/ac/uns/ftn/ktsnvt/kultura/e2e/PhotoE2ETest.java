package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.core.io.ClassPathResource;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.LoginPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.PhotosPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.PostsPage;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class PhotoE2ETest {
    public static final String BASE_URL = "http://localhost:4200";
    private static E2EUtils utils;
    private static WebDriver driver;
    private static PhotosPage photosPage;

    @BeforeClass
    public static void setUp() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        utils = new E2EUtils(driver);

        driver.manage().window().maximize();
        LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);

        driver.get("http://localhost:4200/login");

        loginPage.ensureDisplayed(loginPage.getEmail()).sendKeys("moderator@mail.com");
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
    public void addPhoto() throws InterruptedException, IOException {

        String path = new ClassPathResource("test_photo.png").getFile().getAbsolutePath();

        driver.get("http://localhost:4200/cultural-offering/1/photos");

        photosPage = PageFactory.initElements(driver, PhotosPage.class);

        utils.ensureDisplayed(photosPage.getAddPhotoBtn()).click();

        utils.ensureDisplayed(photosPage.getChooseBtn());

        photosPage.getPhotoInput().sendKeys(path);

        utils.ensureEnabled(photosPage.getUploadBtn()).click();

        String toast = utils.ensureDisplayed("upload-success").getText();

        assertEquals("Photo uploaded\nThe photo was uploaded successfully.", toast);
    }

    @Test
    public void deletePhoto() throws InterruptedException, IOException {
        String path = new ClassPathResource("test_photo.png").getFile().getAbsolutePath();

        driver.get("http://localhost:4200/cultural-offering/1/photos");

        photosPage = PageFactory.initElements(driver, PhotosPage.class);

        utils.ensureDisplayed(photosPage.getFirstPhoto()).click();

        utils.ensureDisplayed(photosPage.getDeletePhotoBtn()).click();

        utils.ensureDisplayed(photosPage.getConfirmDeletionBtn()).click();

        String toast = utils.ensureDisplayed("deletion-successful").getText();

        assertEquals("Photo deleted\nThe photo was deleted successfully.", toast);

    }
}
