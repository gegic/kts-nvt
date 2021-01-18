package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.LoginPage;

import static org.junit.Assert.assertEquals;

public class LoginE2ETest {

  private WebDriver driver;

  private LoginPage loginPage;

  @Before
  public void setUp() {

    System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
    driver = new ChromeDriver();

    driver.manage().window().maximize();
    loginPage = PageFactory.initElements(driver, LoginPage.class);
  }

  @After
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void AdminLogInTestSuccess() throws InterruptedException {

    driver.get("http://localhost:4200/login");

    justWait();

    loginPage.getEmail().sendKeys("admin@mail.com");
    loginPage.getNextBtn().click();
    justWait();
    loginPage = PageFactory.initElements(driver, LoginPage.class);
    justWait();
    loginPage.getPassword().sendKeys("admin123");
    loginPage.getLoginBtn().click();
    justWait();
    assertEquals("http://localhost:4200/admin-panel", driver.getCurrentUrl());
  }

  @Test
  public void ModeratorLogInTestSuccess() throws InterruptedException {

    driver.get("http://localhost:4200/login");

    justWait();

    loginPage.getEmail().sendKeys("moderator@mail.com");
    loginPage.getNextBtn().click();
    justWait();
    loginPage = PageFactory.initElements(driver, LoginPage.class);
    justWait();
    loginPage.getPassword().sendKeys("admin123");
    loginPage.getLoginBtn().click();
    justWait();
    assertEquals("http://localhost:4200/", driver.getCurrentUrl());
  }

  @Test
  public void UserLogInTestSuccess() throws InterruptedException {

    driver.get("http://localhost:4200/login");

    justWait();

    loginPage.getEmail().sendKeys("user@mail.com");
    loginPage.getNextBtn().click();
    justWait();
    loginPage = PageFactory.initElements(driver, LoginPage.class);
    justWait();
    loginPage.getPassword().sendKeys("admin123");
    loginPage.getLoginBtn().click();
    justWait();
    assertEquals("http://localhost:4200/", driver.getCurrentUrl());
  }

  @Test
  public void LogInTestWrongEmail() throws InterruptedException {

    driver.get("http://localhost:4200/login");

    justWait();

    loginPage.getEmail().sendKeys("aadmin@gmail.com");

    loginPage.getNextBtn().click();

    String toast = loginPage.ensureIsDisplayedToast();

    assertEquals("Email not found\n" + "A user with this email doesn't exist.", toast);
    assertEquals("http://localhost:4200/login", driver.getCurrentUrl());
  }

  @Test
  public void LogInTestWrongPassword() throws InterruptedException {

    driver.get("http://localhost:4200/login");
    justWait();
    loginPage.getEmail().sendKeys("moderator@mail.com");
    loginPage.getNextBtn().click();
    justWait();
    loginPage = PageFactory.initElements(driver, LoginPage.class);
    justWait();
    loginPage.getPassword().sendKeys("admin1233");
    loginPage.getLoginBtn().click();
    justWait();

    String toast = loginPage.ensureIsDisplayedToast();

    assertEquals("Your password is incorrect", toast);
    assertEquals("http://localhost:4200/login/password", driver.getCurrentUrl());
  }

  private void justWait() throws InterruptedException {
    synchronized (driver) {
      driver.wait(1000);
    }
  }
}
