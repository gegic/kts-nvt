package rs.ac.uns.ftn.ktsnvt.kultura.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class PostsPage {

  private final WebDriver driver;

  @FindBy(id = "new-post-input")
  private WebElement newPostInput;

  @FindBy(id = "announce-button")
  private WebElement announceBtn;

  @FindBy(id = "post-num0")
  private WebElement newestPost;

  @FindBy(id = "options-num0")
  private WebElement newestOptionsBtn;

  @FindBy(id = "edit-num0")
  private WebElement newestOpenEditBtn;

  @FindBy(id = "delete-num0")
  private WebElement newestOpenDeleteBtn;

  @FindBy(id = "confirm-edit")
  private WebElement confirmEditingBtn;

  @FindBy(className = "confirm-delete")
  private WebElement confirmDeletionBtn;

  @FindBy(id = "edit-area")
  private WebElement editArea;

  public PostsPage(WebDriver driver) {
    this.driver = driver;
  }

}
