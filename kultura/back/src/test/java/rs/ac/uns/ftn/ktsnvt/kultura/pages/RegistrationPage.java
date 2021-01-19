package rs.ac.uns.ftn.ktsnvt.kultura.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class RegistrationPage {

  private final WebDriver driver;
  @FindBy(id = "email")
  private WebElement emailInput;
  @FindBy(id = "proceed")
  private WebElement proceedBtn;
  @FindBy(id = "firstName")
  private WebElement firstNameInput;
  @FindBy(id = "lastName")
  private WebElement lastNameInput;
  @FindBy(id = "repeatPassword")
  private WebElement repeatPasswordInput;
  @FindBy(id = "password")
  private WebElement passwordInput;

  public RegistrationPage(WebDriver driver) {
    this.driver = driver;
  }

  public String ensureIsDisplayedToast() {
    return (new WebDriverWait(driver, 30))
        .until(ExpectedConditions.presenceOfElementLocated(By.id("toast-container")))
        .getText();
  }
}
