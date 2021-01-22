package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import rs.ac.uns.ftn.ktsnvt.kultura.mapper.Computed;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.LoginPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.ModeratorPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.PostsPage;
import rs.ac.uns.ftn.ktsnvt.kultura.service.PostService;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

public class PostE2ETest {
  public static final String BASE_URL = "http://localhost:4200";
  private static E2EUtils utils;
  private static WebDriver driver;
  private static PostsPage postsPage;

  @BeforeClass
  public static void setUp() throws InterruptedException {
    System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
    driver = new ChromeDriver();
    utils = new E2EUtils(driver);

    driver.manage().window().maximize();
    LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
    driver.get("http://localhost:4200/login");

    utils.ensureDisplayed(loginPage.getEmail()).sendKeys("moderator@mail.com");
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
  public void addPost() throws InterruptedException {
    String postContent = "Announcing something new :D";

    driver.get("http://localhost:4200/cultural-offering/1/posts");

    postsPage = PageFactory.initElements(driver, PostsPage.class);

    utils.ensureDisplayed(postsPage.getNewPostInput()).sendKeys(postContent);

    postsPage.getAnnounceBtn().click();

    utils.waitFor(3000);

    assertEquals(postContent, postsPage.getNewestPost().getText());
  }

  @Test
  public void deletePost() throws InterruptedException {
    driver.get("http://localhost:4200/cultural-offering/1/posts");

    postsPage = PageFactory.initElements(driver, PostsPage.class);

    utils.ensureDisplayed(postsPage.getNewestOptionsBtn()).click();

    utils.ensureDisplayed(postsPage.getNewestOpenDeleteBtn()).click();

    utils.ensureDisplayed(postsPage.getConfirmDeletionBtn()).click();

    utils.ensureNotDisplayed("confirm-delete");

    String toast = utils.ensureDisplayed("deleted-toast").getText();

    assertEquals("Post deleted\nThe post was deleted successfully.", toast);

  }

  @Test
  public void editPost() throws InterruptedException {
    driver.get("http://localhost:4200/cultural-offering/1/posts");

    postsPage = PageFactory.initElements(driver, PostsPage.class);

    utils.ensureDisplayed(postsPage.getNewestOptionsBtn()).click();

    utils.ensureDisplayed(postsPage.getNewestOpenEditBtn()).click();

    utils.ensureDisplayed(postsPage.getEditArea()).sendKeys("Editing this post");

    postsPage.getConfirmEditingBtn().click();

    String toast = utils.ensureDisplayed("edited-toast").getText();

    assertEquals("Post updated\nThe post was updated successfully.", toast);

  }

  @Test
  public void addEmptyPost() throws InterruptedException {
    driver.get("http://localhost:4200/cultural-offering/1/posts");

    postsPage = PageFactory.initElements(driver, PostsPage.class);
    utils.ensureDisplayed(postsPage.getAnnounceBtn()).click();

    String toast = utils.ensureDisplayed("add-empty-toast").getText();

    assertEquals("Empty announcement\nAnnouncement cannot be empty.", toast);
  }

  @Test
  public void editPostToEmpty() throws InterruptedException {
    driver.get("http://localhost:4200/cultural-offering/1/posts");

    postsPage = PageFactory.initElements(driver, PostsPage.class);

    utils.ensureDisplayed(postsPage.getNewestOptionsBtn()).click();

    utils.ensureDisplayed(postsPage.getNewestOpenEditBtn()).click();

    WebElement editArea = utils.ensureDisplayed(postsPage.getEditArea());

    editArea.sendKeys(Keys.CONTROL + "a");
    editArea.sendKeys(Keys.DELETE);

    postsPage.getConfirmEditingBtn().click();

    String toast = utils.ensureDisplayed("edit-empty-toast").getText();

    assertEquals("Empty announcement\nAnnouncement cannot be empty.", toast);
  }

}
