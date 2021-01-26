package rs.ac.uns.ftn.ktsnvt.kultura.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import rs.ac.uns.ftn.ktsnvt.kultura.e2e.E2EConstants;

import java.util.Collection;
import java.util.List;

@Getter
public class ReviewPage {

  private final WebDriver driver;

  @FindBy(className = "review-button")
  private WebElement reviewButton;

  @FindBy(xpath = "//h4[contains(text(),'Rating')]/following-sibling::p-rating/div/span[contains(@class, 'p-rating-icon')][5]")
  private WebElement fourthStar;

  @FindBy(xpath = "//h4[contains(text(),'Rating')]/following-sibling::p-rating/div/span[contains(@class, 'p-rating-icon')][6]")
  private WebElement fifthStar;

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

  public void ensureIsDisplayed(String id) {
    (new WebDriverWait(driver, 30)).until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
  }
}
