package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.LoginPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.ModeratorPage;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

public class ModeratorE2ETest {
  public static final String BASE_URL = "https://localhost:4200";
  private static WebDriver driver;
  private static LoginPage loginPage;
  private static ModeratorPage moderatorPage;

  @BeforeClass
  public static void setUp() throws InterruptedException {
    System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
    driver = new ChromeDriver();

    driver.manage().window().maximize();
    loginPage = PageFactory.initElements(driver, LoginPage.class);

    driver.get("https://localhost:4200/login");

    justWait(300);

    loginPage.getEmail().sendKeys("admin@mail.com");
    loginPage.getNextBtn().click();
    justWait(300);
    loginPage = PageFactory.initElements(driver, LoginPage.class);
    justWait(300);
    loginPage.getPassword().sendKeys("admin123");
    loginPage.getLoginBtn().click();
    justWait(300);
  }

  @AfterClass
  public static void tearDown() throws InterruptedException {
    driver.quit();
  }

  private static void justWait(int timeout) throws InterruptedException {
    synchronized (driver) {
      driver.wait(timeout);
    }
  }

  public void deleteModerator(String mail) throws InterruptedException {
    driver.get("https://localhost:4200/moderators");

    justWait(1000);
    moderatorPage = PageFactory.initElements(driver, ModeratorPage.class);
    justWait(1000);
    moderatorPage.deleteBtnModerator(mail).click();
    justWait(1000);
    moderatorPage.getConfirmationService().click();
    String toast = moderatorPage.ensureIsDisplayedToast();
    justWait(1000);
    moderatorPage.ensureIsNonDisplayed("delete" + mail);
    assertEquals(toast, "Deleted successfully\n" + "The moderator was deleted successfully");
  }

  @Test
  public void editModerator() throws InterruptedException {
    justWait(1000);
    driver.get("https://localhost:4200/add-moderator");
    String email =
        "test_moderator" + ThreadLocalRandom.current().nextInt(0, 9999 + 1) + "@mail.com";
    moderatorPage = PageFactory.initElements(driver, ModeratorPage.class);
    justWait(1000);

    moderatorPage.getEmailInput().sendKeys(email);

    moderatorPage.getFirstName().sendKeys("Petter");
    moderatorPage.getLastNameInput().sendKeys("Hans");

    moderatorPage.getPasswordInput().sendKeys("MitarMiric123");
    moderatorPage.getRepeatPasswordInput().sendKeys("MitarMiric123");

    moderatorPage.getAddModeratorBtn().click();
    justWait(2000);
    moderatorPage = PageFactory.initElements(driver, ModeratorPage.class);
    assertEquals("https://localhost:4200/moderators", driver.getCurrentUrl());
    justWait(1000);
    moderatorPage.ensureIsDisplayed("p" + email);
    moderatorPage.ensureIsDisplayed("pPetterHans");
    moderatorPage.editBtnModerator(email).click();
    justWait(1000);
    moderatorPage = PageFactory.initElements(driver, ModeratorPage.class);
    justWait(1000);
    moderatorPage.getFirstName().sendKeys("s");
    moderatorPage.getLastNameInput().sendKeys("s");
    moderatorPage.getElementById("updateModerator").click();
    justWait(5000);
    moderatorPage.ensureIsDisplayed("pPettersHanss");
    justWait(5000);
    deleteModerator(email);
  }

  @Test
  public void addAndDeleteModerator() throws InterruptedException {
    justWait(1000);
    driver.get("https://localhost:4200/add-moderator");
    String email =
        "test_moderator" + ThreadLocalRandom.current().nextInt(0, 9999 + 1) + "@mail.com";
    moderatorPage = PageFactory.initElements(driver, ModeratorPage.class);
    justWait(1000);

    moderatorPage.getEmailInput().sendKeys(email);

    moderatorPage.getFirstName().sendKeys("Petter");
    moderatorPage.getLastNameInput().sendKeys("Hans");

    moderatorPage.getPasswordInput().sendKeys("MitarMiric123");
    moderatorPage.getRepeatPasswordInput().sendKeys("MitarMiric123");

    moderatorPage.getAddModeratorBtn().click();
    justWait(3000);
    assertEquals("https://localhost:4200/moderators", driver.getCurrentUrl());
    justWait(1000);
    deleteModerator(email);
  }

  @Test
  public void addModeratorWrongEmail() throws InterruptedException {
    justWait(1000);
    driver.get("https://localhost:4200/add-moderator");
    String email = "test_moderator" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
    moderatorPage = PageFactory.initElements(driver, ModeratorPage.class);
    justWait(1000);

    moderatorPage.getEmailInput().sendKeys(email);

    moderatorPage.getFirstName().sendKeys("Petter");
    moderatorPage.getLastNameInput().sendKeys("Hans");

    moderatorPage.getPasswordInput().sendKeys("MitarMiric123");
    moderatorPage.getRepeatPasswordInput().sendKeys("MitarMiric123");

    moderatorPage.getAddModeratorBtn().click();
    justWait(300);
    String toast = moderatorPage.ensureIsDisplayedToast();

    assertEquals("Enter the mail in the correct format", toast);
    assertEquals(BASE_URL + "/add-moderator", driver.getCurrentUrl());
    justWait(2000);
  }

  @Test
  public void addModeratorWrongFirstName() throws InterruptedException {
    justWait(1000);
    driver.get("https://localhost:4200/add-moderator");
    String email =
        "test_moderator" + ThreadLocalRandom.current().nextInt(0, 9999 + 1) + "@mail.com";
    moderatorPage = PageFactory.initElements(driver, ModeratorPage.class);
    justWait(1000);

    moderatorPage.getEmailInput().sendKeys(email);

    moderatorPage.getFirstName().sendKeys("");
    moderatorPage.getLastNameInput().sendKeys("Hans");

    moderatorPage.getPasswordInput().sendKeys("MitarMiric123");
    moderatorPage.getRepeatPasswordInput().sendKeys("MitarMiric123");

    moderatorPage.getAddModeratorBtn().click();
    justWait(300);
    String toast = moderatorPage.ensureIsDisplayedToast();

    assertEquals("First name cannot be empty and must start with a capital letter", toast);
    assertEquals(BASE_URL + "/add-moderator", driver.getCurrentUrl());
    justWait(2000);
  }

  @Test
  public void addModeratorWrongLastName() throws InterruptedException {
    justWait(1000);
    driver.get("https://localhost:4200/add-moderator");
    String email =
        "test_moderator" + ThreadLocalRandom.current().nextInt(0, 9999 + 1) + "@mail.com";
    moderatorPage = PageFactory.initElements(driver, ModeratorPage.class);
    justWait(1000);

    moderatorPage.getEmailInput().sendKeys(email);

    moderatorPage.getFirstName().sendKeys("Petter");
    moderatorPage.getLastNameInput().sendKeys("");

    moderatorPage.getPasswordInput().sendKeys("MitarMiric123");
    moderatorPage.getRepeatPasswordInput().sendKeys("MitarMiric123");

    moderatorPage.getAddModeratorBtn().click();
    justWait(300);
    String toast = moderatorPage.ensureIsDisplayedToast();

    assertEquals("Last name cannot be empty and must start with a capital letter", toast);
    assertEquals(BASE_URL + "/add-moderator", driver.getCurrentUrl());
    justWait(2000);
  }

  @Test
  public void addModeratorTooSmallPassword() throws InterruptedException {
    justWait(1000);
    driver.get("https://localhost:4200/add-moderator");
    String email =
        "test_moderator" + ThreadLocalRandom.current().nextInt(0, 9999 + 1) + "@mail.com";
    moderatorPage = PageFactory.initElements(driver, ModeratorPage.class);
    justWait(1000);

    moderatorPage.getEmailInput().sendKeys(email);

    moderatorPage.getFirstName().sendKeys("Petter");
    moderatorPage.getLastNameInput().sendKeys("Hans");

    moderatorPage.getPasswordInput().sendKeys("Ma1");
    moderatorPage.getRepeatPasswordInput().sendKeys("Ma1");

    moderatorPage.getAddModeratorBtn().click();
    justWait(300);
    String toast = moderatorPage.ensureIsDisplayedToast();

    assertEquals(
        "Password has to contain at least one uppercase, one lowercase letter and one digit. It has to be at least 8 characters long",
        toast);
    assertEquals(BASE_URL + "/add-moderator", driver.getCurrentUrl());
    justWait(2000);
  }

  @Test
  public void addModeratorPasswordDontHaveUppercase() throws InterruptedException {
    justWait(1000);
    driver.get("https://localhost:4200/add-moderator");
    String email =
        "test_moderator" + ThreadLocalRandom.current().nextInt(0, 9999 + 1) + "@mail.com";
    moderatorPage = PageFactory.initElements(driver, ModeratorPage.class);
    justWait(1000);

    moderatorPage.getEmailInput().sendKeys(email);

    moderatorPage.getFirstName().sendKeys("Petter");
    moderatorPage.getLastNameInput().sendKeys("Hans");

    moderatorPage.getPasswordInput().sendKeys("mitarmiric123");
    moderatorPage.getRepeatPasswordInput().sendKeys("mitarmiric123");

    moderatorPage.getAddModeratorBtn().click();
    justWait(300);
    String toast = moderatorPage.ensureIsDisplayedToast();

    assertEquals(
        "Password has to contain at least one uppercase, one lowercase letter and one digit. It has to be at least 8 characters long",
        toast);
    assertEquals(BASE_URL + "/add-moderator", driver.getCurrentUrl());
    justWait(2000);
  }

  @Test
  public void addModeratorPasswordDontHaveDigit() throws InterruptedException {
    justWait(1000);
    driver.get("https://localhost:4200/add-moderator");
    String email =
        "test_moderator" + ThreadLocalRandom.current().nextInt(0, 9999 + 1) + "@mail.com";
    moderatorPage = PageFactory.initElements(driver, ModeratorPage.class);
    justWait(1000);

    moderatorPage.getEmailInput().sendKeys(email);

    moderatorPage.getFirstName().sendKeys("Petter");
    moderatorPage.getLastNameInput().sendKeys("Hans");

    moderatorPage.getPasswordInput().sendKeys("Mitarmiric");
    moderatorPage.getRepeatPasswordInput().sendKeys("Mitarmiric");

    moderatorPage.getAddModeratorBtn().click();
    justWait(300);
    String toast = moderatorPage.ensureIsDisplayedToast();

    assertEquals(
        "Password has to contain at least one uppercase, one lowercase letter and one digit. It has to be at least 8 characters long",
        toast);
    assertEquals(BASE_URL + "/add-moderator", driver.getCurrentUrl());
    justWait(2000);
  }

  @Test
  public void addModeratorWrongDintSamePasswords() throws InterruptedException {
    justWait(1000);
    driver.get("https://localhost:4200/add-moderator");
    String email =
        "test_moderator" + ThreadLocalRandom.current().nextInt(0, 9999 + 1) + "@mail.com";
    moderatorPage = PageFactory.initElements(driver, ModeratorPage.class);
    justWait(1000);

    moderatorPage.getEmailInput().sendKeys(email);

    moderatorPage.getFirstName().sendKeys("Petter");
    moderatorPage.getLastNameInput().sendKeys("Hans");

    moderatorPage.getPasswordInput().sendKeys("Mitarmiric123");
    moderatorPage.getRepeatPasswordInput().sendKeys("Mitarmiric1234");

    moderatorPage.getAddModeratorBtn().click();
    justWait(300);
    String toast = moderatorPage.ensureIsDisplayedToast();

    assertEquals("Repeated password has to match the original password.", toast);
    assertEquals(BASE_URL + "/add-moderator", driver.getCurrentUrl());
    justWait(2000);
  }

  @Test
  public void addModeratorWrongMailAlreadyExist() throws InterruptedException {
    justWait(1000);
    driver.get("https://localhost:4200/add-moderator");
    String email = "admin@mail.com";
    moderatorPage = PageFactory.initElements(driver, ModeratorPage.class);
    justWait(1000);

    moderatorPage.getEmailInput().sendKeys(email);

    moderatorPage.getFirstName().sendKeys("Petter");
    moderatorPage.getLastNameInput().sendKeys("Hans");

    moderatorPage.getPasswordInput().sendKeys("Mitarmiric123");
    moderatorPage.getRepeatPasswordInput().sendKeys("Mitarmiric123");

    moderatorPage.getAddModeratorBtn().click();
    justWait(300);
    String toast = moderatorPage.ensureIsDisplayedToast();

    assertEquals(
        "Moderator couldn't be added\n"
            + "This moderator couln't be added due to the fact the email already exists in the database",
        toast);
    assertEquals(BASE_URL + "/add-moderator", driver.getCurrentUrl());
    justWait(2000);
  }
}
