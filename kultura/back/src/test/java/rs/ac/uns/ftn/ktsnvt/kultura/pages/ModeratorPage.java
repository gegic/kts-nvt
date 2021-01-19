package rs.ac.uns.ftn.ktsnvt.kultura.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class ModeratorPage {

  private final WebDriver driver;

  @FindBy(id = "email")
  private WebElement emailInput;

  @FindBy(id = "firstName")
  private WebElement firstName;

  @FindBy(id = "lastName")
  private WebElement lastNameInput;

  @FindBy(id = "password")
  private WebElement passwordInput;

  @FindBy(id = "repeatPassword")
  private WebElement repeatPasswordInput;

  @FindBy(id = "addModerator")
  private WebElement addModeratorBtn;

  @FindBy(className = "p-confirm-dialog-accept")
  private WebElement confirmationService;

  public ModeratorPage(WebDriver driver) {
    this.driver = driver;
  }

  public WebElement deleteBtnModerator(String email) {
    return driver.findElement(By.id("delete" + email));
  }

  public WebElement editBtnModerator(String email) {
    return driver.findElement(By.id("edit" + email));
  }

  public WebElement getElementById(String id) {
    return driver.findElement(By.id(id));
  }

  public void ensureIsNonDisplayed(String id) {
    (new WebDriverWait(driver, 30))
        .until(ExpectedConditions.invisibilityOfElementLocated(By.id(id)));
  }

  public void ensureIsDisplayed(String id) {
    (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
  }

  public String ensureIsDisplayedToast() {
    return (new WebDriverWait(driver, 30))
        .until(ExpectedConditions.presenceOfElementLocated(By.id("toast-container")))
        .getText();
  }
}
