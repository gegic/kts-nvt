package rs.ac.uns.ftn.ktsnvt.kultura.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class VerificationPage {

  private final WebDriver driver;

  @FindBy(id = "verify")
  private WebElement verifyBtn;

  public VerificationPage(WebDriver driver) {
    this.driver = driver;
  }

  public void ensureIsNotVisibleVerifiedP() {
    (new WebDriverWait(driver, 10))
        .until(ExpectedConditions.visibilityOfElementLocated(By.id("verified")));
  }
}
