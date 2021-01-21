package rs.ac.uns.ftn.ktsnvt.kultura.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class ReviewPage {

  private final WebDriver driver;

  @FindBy(id = "review-button")
  private WebElement reviewButton;

  @FindBy(className = "ng-star-inserted")
  private WebElement star;

  @FindBy(id = "review-comment")
  private WebElement commentReview;

  @FindBy(id = "review-submit")
  private WebElement submitBtn;



  public ReviewPage(WebDriver driver) {
    this.driver = driver;
  }

  public String ensureIsDisplayedToast() {
    return (new WebDriverWait(driver, 30))
        .until(ExpectedConditions.presenceOfElementLocated(By.id("toast-container")))
        .getText();
  }
}
