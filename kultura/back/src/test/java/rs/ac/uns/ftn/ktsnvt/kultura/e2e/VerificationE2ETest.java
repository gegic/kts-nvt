package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.VerificationPage;

import static org.junit.Assert.assertEquals;

public class VerificationE2ETest {

  private WebDriver driver;

  private VerificationPage verificationPage;

  @Before
  public void setUp() {

    System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
    driver = new ChromeDriver();

    driver.manage().window().maximize();
    verificationPage = PageFactory.initElements(driver, VerificationPage.class);
  }

  @After
  public void tearDown() {
    driver.quit();
  }

  @Test
  public void VerificationSuccess() throws InterruptedException {

    driver.get("http://localhost:4200/verify/18");
    justWait();
    verificationPage.getVerifyBtn().click();
    justWait();
    verificationPage.ensureIsNotVisibleVerifiedP();
    assertEquals("http://localhost:4200/verify/18", driver.getCurrentUrl());
  }

  private void justWait() throws InterruptedException {
    synchronized (driver) {
      driver.wait(500);
    }
  }
}
