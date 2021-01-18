package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.LoginPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.UserEditPage;

import static org.junit.Assert.assertEquals;

public class UserEditE2ETest {


  private static final String NAME = "Hercul";
  private static final String LASTNAME = "Poirot";
  private static final String EMAIL = "hp@mail.com";

  private WebDriver driver;

  private UserEditPage userEditPage;

  private LoginPage loginPage;

  @Before
  public void setUp() {

    System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
    driver = new ChromeDriver();

    driver.manage().window().maximize();
    userEditPage = PageFactory.initElements(driver, UserEditPage.class);
    loginPage = PageFactory.initElements(driver, LoginPage.class);
  }

  @After
  public void tearDown() {
    driver.quit();
  }


  @Test
  public void ChangeNameTestSuccess() throws InterruptedException {

    driver.get("http://localhost:4200/login");

    justWait();

    loginPage.getEmail().sendKeys("admin@mail.com");
    loginPage.getNextBtn().click();
    justWait();
    loginPage = PageFactory.initElements(driver, LoginPage.class);
    loginPage.getPassword().sendKeys("admin123");
    loginPage.getLoginBtn().click();
    justWait();

    driver.get("http://localhost:4200/user-edit");
    justWait();
    userEditPage = PageFactory.initElements(driver, UserEditPage.class);
    userEditPage.getName().sendKeys(NAME);
    userEditPage.getSubmitName().click();
    justWait();
    userEditPage = PageFactory.initElements(driver, UserEditPage.class);
    justWait();
    assertEquals(NAME,userEditPage.getNameVal().getText());
    assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
  }


  @Test
  public void ChangeLastNameTestSuccess() throws InterruptedException {

    driver.get("http://localhost:4200/login");

    justWait();

    loginPage.getEmail().sendKeys("admin@mail.com");
    loginPage.getNextBtn().click();
    justWait();
    loginPage = PageFactory.initElements(driver, LoginPage.class);
    loginPage.getPassword().sendKeys("admin123");
    loginPage.getLoginBtn().click();
    justWait();

    driver.get("http://localhost:4200/user-edit");
    justWait();
    userEditPage = PageFactory.initElements(driver, UserEditPage.class);
    userEditPage.getLastName().sendKeys(LASTNAME);
    userEditPage.getSubmitLastName().click();
    justWait();
    userEditPage = PageFactory.initElements(driver, UserEditPage.class);
    justWait();
    assertEquals(LASTNAME,userEditPage.getLastNameVal().getText());
    assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
  }

  @Test
  public void ChangeEmailTestSuccess() throws InterruptedException {

    driver.get("http://localhost:4200/login");

    justWait();

    loginPage.getEmail().sendKeys("admin@mail.com");
    loginPage.getNextBtn().click();
    justWait();
    loginPage = PageFactory.initElements(driver, LoginPage.class);
    loginPage.getPassword().sendKeys("admin123");
    loginPage.getLoginBtn().click();
    justWait();

    driver.get("http://localhost:4200/user-edit");
    justWait();
    userEditPage = PageFactory.initElements(driver, UserEditPage.class);
    userEditPage.getEmail().sendKeys(EMAIL);
    userEditPage.getSubmitEmail().click();
    justWait();
    userEditPage = PageFactory.initElements(driver, UserEditPage.class);
    justWait();
    assertEquals(EMAIL,userEditPage.getEmailVal().getText());
    assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
  }

  @Test
  public void ChangePasswordTestSuccess() throws InterruptedException {
    // TODO zavrsiti test
    driver.get("http://localhost:4200/login");

    justWait();

    loginPage.getEmail().sendKeys("admin@mail.com");
    loginPage.getNextBtn().click();
    justWait();
    loginPage = PageFactory.initElements(driver, LoginPage.class);
    loginPage.getPassword().sendKeys("admin123");
    loginPage.getLoginBtn().click();
    justWait();

    driver.get("http://localhost:4200/user-edit");
    justWait();
    userEditPage = PageFactory.initElements(driver, UserEditPage.class);
    userEditPage.getEmail().sendKeys(EMAIL);
    userEditPage.getSubmitEmail().click();
    justWait();
    userEditPage = PageFactory.initElements(driver, UserEditPage.class);
    justWait();
    assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
  }

  private void justWait() throws InterruptedException {
    synchronized (driver) {
      driver.wait(1000);
    }
  }
}
