package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.test.annotation.Rollback;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.RegistrationPage;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;

public class RegistrationE2ETest {

  public static final String BASE_URL = "https://localhost:4200";
  private static String randomMail;
  private WebDriver driver;
  private RegistrationPage registrationPage;

  @BeforeClass
  public static void setUpClass() {
    System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
    randomMail = "newUser" + ThreadLocalRandom.current().nextInt(0, 9999 + 1) + "@mail.com";
  }

  @Before
  public void setUpTest() {
    driver = new ChromeDriver();
    driver.manage().window().maximize();
    registrationPage = PageFactory.initElements(driver, RegistrationPage.class);
  }

  @After
  public void tearDown() {
    driver.quit();
  }

  @Test
  @Rollback
  public void registrationSuccess() throws InterruptedException {
    driver.get(BASE_URL + "/register");

    justWait(300);

    registrationPage.getEmailInput().sendKeys(randomMail);
    registrationPage.getProceedBtn().click();

    justWait(300);
    registrationPage = PageFactory.initElements(driver, RegistrationPage.class);
    justWait(300);

    registrationPage.getFirstNameInput().sendKeys("Petter");
    registrationPage.getLastNameInput().sendKeys("Hans");
    registrationPage.getProceedBtn().click();
    justWait(300);

    registrationPage.getPasswordInput().sendKeys("MitarMiric123");
    registrationPage.getRepeatPasswordInput().sendKeys("MitarMiric123");
    justWait(300);

    registrationPage.getProceedBtn().click();
    justWait(7000);
    assertEquals(BASE_URL + "/register/success", driver.getCurrentUrl());
  }

  @Test
  public void registrationMailFormatFail() throws InterruptedException {
    driver.get(BASE_URL + "/register");

    justWait(300);

    registrationPage.getEmailInput().sendKeys("newUser");
    registrationPage.getProceedBtn().click();

    justWait(1000);
    String toast = registrationPage.ensureIsDisplayedToast();

    assertEquals("Email not valid\n" + "Email should be formatted properly.", toast);
    assertEquals(BASE_URL + "/register", driver.getCurrentUrl());
  }

  @Test
  public void registrationMailAlreadyExistsFail() throws InterruptedException {
    driver.get(BASE_URL + "/register");

    justWait(300);

    registrationPage.getEmailInput().sendKeys("admin@mail.com");
    registrationPage.getProceedBtn().click();

    justWait(1500);
    String toast = registrationPage.ensureIsDisplayedToast();

    assertEquals("Email already exists\n" + "An account with this email already exists.", toast);
    assertEquals(BASE_URL + "/register", driver.getCurrentUrl());
  }

  @Test
  public void registrationIncorrectNameFormat() throws InterruptedException {
    driver.get(BASE_URL + "/register");

    justWait(300);

    registrationPage.getEmailInput().sendKeys("testMail@test.test");
    registrationPage.getProceedBtn().click();

    justWait(300);
    registrationPage = PageFactory.initElements(driver, RegistrationPage.class);

    registrationPage.getFirstNameInput().sendKeys("sad");
    registrationPage.getLastNameInput().sendKeys("story");
    registrationPage.getProceedBtn().click();
    justWait(1000);
    String toast = registrationPage.ensureIsDisplayedToast();
    assertEquals(
        "Please enter only the first and the last name with capital first letters (Fields can't be empty).",
        toast);
    assertEquals(BASE_URL + "/register/name", driver.getCurrentUrl());
  }

  @Test
  public void registrationIncorrectEmptyName() throws InterruptedException {
    driver.get(BASE_URL + "/register");

    justWait(300);

    registrationPage.getEmailInput().sendKeys("testMail@test.test");
    registrationPage.getProceedBtn().click();

    justWait(300);
    registrationPage = PageFactory.initElements(driver, RegistrationPage.class);

    registrationPage.getProceedBtn().click();
    justWait(1000);

    String toast = registrationPage.ensureIsDisplayedToast();
    assertEquals(
        "Please enter only the first and the last name with capital first letters (Fields can't be empty).",
        toast);
    assertEquals(BASE_URL + "/register/name", driver.getCurrentUrl());
  }

  @Test
  public void registrationWrongFormatPasswordsTooSmall() throws InterruptedException {
    driver.get(BASE_URL + "/register");

    justWait(300);

    registrationPage.getEmailInput().sendKeys(randomMail);
    registrationPage.getProceedBtn().click();

    justWait(300);
    registrationPage = PageFactory.initElements(driver, RegistrationPage.class);
    justWait(300);

    registrationPage.getFirstNameInput().sendKeys("Petter");
    registrationPage.getLastNameInput().sendKeys("Hans");
    registrationPage.getProceedBtn().click();
    justWait(300);

    registrationPage.getPasswordInput().sendKeys("Asd");
    registrationPage.getRepeatPasswordInput().sendKeys("Asd");

    registrationPage.getProceedBtn().click();
    justWait(1000);
    String toast = registrationPage.ensureIsDisplayedToast();
    assertEquals(
        "Password has to contain at least one uppercase, one lowercase letter and one digit. It has to be at least 8 characters long",
        toast);
    justWait(300);
    assertEquals(BASE_URL + "/register/password", driver.getCurrentUrl());
  }

  @Test
  public void registrationWrongFormatPasswordWithoutUppercase() throws InterruptedException {
    driver.get(BASE_URL + "/register");

    justWait(300);

    registrationPage.getEmailInput().sendKeys(randomMail);
    registrationPage.getProceedBtn().click();

    justWait(300);
    registrationPage = PageFactory.initElements(driver, RegistrationPage.class);
    justWait(300);

    registrationPage.getFirstNameInput().sendKeys("Petter");
    registrationPage.getLastNameInput().sendKeys("Hans");
    registrationPage.getProceedBtn().click();
    justWait(300);

    registrationPage.getPasswordInput().sendKeys("aaaaasd1");
    registrationPage.getRepeatPasswordInput().sendKeys("aaaaasd1");

    registrationPage.getProceedBtn().click();
    justWait(1000);
    String toast = registrationPage.ensureIsDisplayedToast();
    assertEquals(
        "Password has to contain at least one uppercase, one lowercase letter and one digit. It has to be at least 8 characters long",
        toast);
    justWait(300);
    assertEquals(BASE_URL + "/register/password", driver.getCurrentUrl());
  }

  @Test
  public void registrationWrongFormatPasswordWithoutDigit() throws InterruptedException {
    driver.get(BASE_URL + "/register");

    justWait(300);

    registrationPage.getEmailInput().sendKeys(randomMail);
    registrationPage.getProceedBtn().click();

    justWait(300);
    registrationPage = PageFactory.initElements(driver, RegistrationPage.class);
    justWait(300);

    registrationPage.getFirstNameInput().sendKeys("Petter");
    registrationPage.getLastNameInput().sendKeys("Hans");
    registrationPage.getProceedBtn().click();
    justWait(300);

    registrationPage.getPasswordInput().sendKeys("aaaaasdAAA");
    registrationPage.getRepeatPasswordInput().sendKeys("aaaaasdAAA");

    registrationPage.getProceedBtn().click();
    justWait(1000);
    String toast = registrationPage.ensureIsDisplayedToast();
    assertEquals(
        "Password has to contain at least one uppercase, one lowercase letter and one digit. It has to be at least 8 characters long",
        toast);
    justWait(300);
    assertEquals(BASE_URL + "/register/password", driver.getCurrentUrl());
  }

  @Test
  public void registrationWrongDintSamePasswords() throws InterruptedException {
    driver.get(BASE_URL + "/register");

    justWait(300);

    registrationPage.getEmailInput().sendKeys(randomMail);
    registrationPage.getProceedBtn().click();

    justWait(300);
    registrationPage = PageFactory.initElements(driver, RegistrationPage.class);
    justWait(300);

    registrationPage.getFirstNameInput().sendKeys("Petter");
    registrationPage.getLastNameInput().sendKeys("Hans");
    registrationPage.getProceedBtn().click();
    justWait(300);

    registrationPage.getPasswordInput().sendKeys("MitarMiric123");
    registrationPage.getRepeatPasswordInput().sendKeys("MitarMiric1234");

    registrationPage.getProceedBtn().click();
    justWait(1000);
  
  
    String toast = registrationPage.ensureIsDisplayedToast();
    assertEquals("Repeated password has to match the original password.", toast);
    justWait(200);
    assertEquals(BASE_URL + "/register/password", driver.getCurrentUrl());
  }

  private void justWait(int timeout) throws InterruptedException {
    synchronized (driver) {
      driver.wait(timeout);
    }
  }
}
