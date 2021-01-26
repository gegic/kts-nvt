package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.core.io.ClassPathResource;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.*;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CulturalOfferingsE2ETest {
  public static final String BASE_URL = "http://localhost:4200";
  private static E2EUtils utils;
  private static WebDriver driver;
  private static AddCulturalOfferingPage addCulturalOfferingPage;

  @BeforeClass
  public static void setUp() throws InterruptedException {
    System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
    driver = new ChromeDriver();
    utils = new E2EUtils(driver);

    driver.manage().window().maximize();
    LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
    driver.get("http://localhost:4200/login");

    utils.ensureDisplayed(loginPage.getEmail()).sendKeys("moderator@mail.com");
    loginPage.getNextBtn().click();

    utils.ensureDisplayed(loginPage.getPassword()).sendKeys("admin123");

    loginPage.getLoginBtn().click();

    utils.ensureLoggedIn();
  }

  @AfterClass
  public static void tearDown() {
    driver.quit();
  }

  @Test
  public void addOffering() throws InterruptedException, IOException {
    String photoPath = new ClassPathResource("test_photo.png").getFile().getAbsolutePath();

    driver.get("http://localhost:4200/create-offering");
    addCulturalOfferingPage = PageFactory.initElements(driver, AddCulturalOfferingPage.class);

    utils.ensureDisplayed(addCulturalOfferingPage.getOfferingNameInput()).sendKeys("Festival nauke");

    utils.ensureDisplayed(addCulturalOfferingPage.getCategorySelect()).click();

    utils.ensureDisplayed(addCulturalOfferingPage.getFirstCategory()).click();

    utils.waitFor(2000);

    addCulturalOfferingPage.getSubcategorySelect().click();

    utils.ensureDisplayed(addCulturalOfferingPage.getFirstSubcategory()).click();

    addCulturalOfferingPage.getBriefInfoArea().sendKeys("Festival nauke u Tutinu");

    addCulturalOfferingPage.getFileUpload().sendKeys(photoPath);

    utils.ensureDisplayed(addCulturalOfferingPage.getThumbnail());

    addCulturalOfferingPage.getAddressAutocompleteInput().sendKeys("Gimnazija Tutin");

    utils.ensureDisplayed(addCulturalOfferingPage.getFirstAddress()).click();

    addCulturalOfferingPage.getCreateBtn().click();

    String toast = utils.ensureDisplayed("offering-added-toast").getText();

    assertEquals("Successfully added\nThe cultural offering was added successfully", toast);
  }

  @Test
  public void addOfferingNoPhoto() throws InterruptedException, IOException {

    driver.get("http://localhost:4200/create-offering");
    addCulturalOfferingPage = PageFactory.initElements(driver, AddCulturalOfferingPage.class);

    utils.ensureDisplayed(addCulturalOfferingPage.getOfferingNameInput()).sendKeys("Festival nauke");

    utils.ensureDisplayed(addCulturalOfferingPage.getCategorySelect()).click();

    utils.ensureDisplayed(addCulturalOfferingPage.getFirstCategory()).click();

    utils.waitFor(2000);

    addCulturalOfferingPage.getSubcategorySelect().click();

    utils.ensureDisplayed(addCulturalOfferingPage.getFirstSubcategory()).click();

    addCulturalOfferingPage.getBriefInfoArea().sendKeys("Festival nauke u Tutinu");

    addCulturalOfferingPage.getAddressAutocompleteInput().sendKeys("Gimnazija Tutin");

    utils.ensureDisplayed(addCulturalOfferingPage.getFirstAddress()).click();

    addCulturalOfferingPage.getCreateBtn().click();

    String toast = utils.ensureDisplayed("photo-error").getText();

    assertEquals("Photo is missing\nA cultural offering has to have a main photo.", toast);
  }

  @Test
  public void addOfferingNoName() throws InterruptedException, IOException {
    String photoPath = new ClassPathResource("test_photo.png").getFile().getAbsolutePath();

    driver.get("http://localhost:4200/create-offering");
    addCulturalOfferingPage = PageFactory.initElements(driver, AddCulturalOfferingPage.class);

    utils.ensureDisplayed(addCulturalOfferingPage.getCategorySelect()).click();

    utils.ensureDisplayed(addCulturalOfferingPage.getFirstCategory()).click();

    utils.waitFor(2000);

    addCulturalOfferingPage.getSubcategorySelect().click();

    utils.ensureDisplayed(addCulturalOfferingPage.getFirstSubcategory()).click();

    addCulturalOfferingPage.getBriefInfoArea().sendKeys("Festival nauke u Tutinu");

    addCulturalOfferingPage.getFileUpload().sendKeys(photoPath);

    utils.ensureDisplayed(addCulturalOfferingPage.getThumbnail());

    addCulturalOfferingPage.getAddressAutocompleteInput().sendKeys("Gimnazija Tutin");

    utils.ensureDisplayed(addCulturalOfferingPage.getFirstAddress()).click();

    addCulturalOfferingPage.getCreateBtn().click();

    String toast = utils.ensureDisplayed("name-error").getText();

    assertEquals("Offering wasn't saved\nName is required and can contain only letters, digits and spaces", toast);
  }

  @Test
  public void addOfferingNoCategoryAndSubcategory() throws InterruptedException, IOException {
    String photoPath = new ClassPathResource("test_photo.png").getFile().getAbsolutePath();

    driver.get("http://localhost:4200/create-offering");
    addCulturalOfferingPage = PageFactory.initElements(driver, AddCulturalOfferingPage.class);

    utils.ensureDisplayed(addCulturalOfferingPage.getOfferingNameInput()).sendKeys("Festival nauke");

    addCulturalOfferingPage.getBriefInfoArea().sendKeys("Festival nauke u Tutinu");

    addCulturalOfferingPage.getFileUpload().sendKeys(photoPath);

    utils.ensureDisplayed(addCulturalOfferingPage.getThumbnail());

    addCulturalOfferingPage.getAddressAutocompleteInput().sendKeys("Gimnazija Tutin");

    utils.ensureDisplayed(addCulturalOfferingPage.getFirstAddress()).click();

    addCulturalOfferingPage.getCreateBtn().click();

    String toast = utils.ensureDisplayed("category-error").getText();

    assertEquals("Offering wasn't saved\nCategory is required.", toast);
  }

  @Test
  public void addOfferingNoBriefInfo() throws InterruptedException, IOException {
    String photoPath = new ClassPathResource("test_photo.png").getFile().getAbsolutePath();

    driver.get("http://localhost:4200/create-offering");
    addCulturalOfferingPage = PageFactory.initElements(driver, AddCulturalOfferingPage.class);

    utils.ensureDisplayed(addCulturalOfferingPage.getOfferingNameInput()).sendKeys("Festival nauke");

    utils.ensureDisplayed(addCulturalOfferingPage.getCategorySelect()).click();

    utils.ensureDisplayed(addCulturalOfferingPage.getFirstCategory()).click();

    utils.waitFor(2000);

    addCulturalOfferingPage.getSubcategorySelect().click();

    utils.ensureDisplayed(addCulturalOfferingPage.getFirstSubcategory()).click();

    addCulturalOfferingPage.getFileUpload().sendKeys(photoPath);

    utils.ensureDisplayed(addCulturalOfferingPage.getThumbnail());

    addCulturalOfferingPage.getAddressAutocompleteInput().sendKeys("Gimnazija Tutin");

    utils.ensureDisplayed(addCulturalOfferingPage.getFirstAddress()).click();

    addCulturalOfferingPage.getCreateBtn().click();

    String toast = utils.ensureDisplayed("brief-info-error").getText();

    assertEquals("Offering wasn't saved\nBrief info is required and it can contain a maximum of 200 characters.", toast);

  }

  @Test
  public void addOfferingNoAddress() throws InterruptedException, IOException {
    String photoPath = new ClassPathResource("test_photo.png").getFile().getAbsolutePath();

    driver.get("http://localhost:4200/create-offering");
    addCulturalOfferingPage = PageFactory.initElements(driver, AddCulturalOfferingPage.class);

    utils.ensureDisplayed(addCulturalOfferingPage.getOfferingNameInput()).sendKeys("Festival nauke");

    utils.ensureDisplayed(addCulturalOfferingPage.getCategorySelect()).click();

    utils.ensureDisplayed(addCulturalOfferingPage.getFirstCategory()).click();

    utils.waitFor(2000);

    addCulturalOfferingPage.getSubcategorySelect().click();

    utils.ensureDisplayed(addCulturalOfferingPage.getFirstSubcategory()).click();

    addCulturalOfferingPage.getBriefInfoArea().sendKeys("Festival nauke u Tutinu");

    addCulturalOfferingPage.getFileUpload().sendKeys(photoPath);

    utils.ensureDisplayed(addCulturalOfferingPage.getThumbnail());

    addCulturalOfferingPage.getCreateBtn().click();

    String toast = utils.ensureDisplayed("address-error").getText();

    assertEquals("Offering wasn't saved\nAddress is required", toast);
  }

  @Test
  public void addOfferingNoSubcategory() throws InterruptedException, IOException {
    String photoPath = new ClassPathResource("test_photo.png").getFile().getAbsolutePath();

    driver.get("http://localhost:4200/create-offering");
    addCulturalOfferingPage = PageFactory.initElements(driver, AddCulturalOfferingPage.class);

    utils.ensureDisplayed(addCulturalOfferingPage.getOfferingNameInput()).sendKeys("Festival nauke");

    utils.ensureDisplayed(addCulturalOfferingPage.getCategorySelect()).click();

    utils.ensureDisplayed(addCulturalOfferingPage.getFirstCategory()).click();

    addCulturalOfferingPage.getBriefInfoArea().sendKeys("Festival nauke u Tutinu");

    addCulturalOfferingPage.getFileUpload().sendKeys(photoPath);

    utils.ensureDisplayed(addCulturalOfferingPage.getThumbnail());

    addCulturalOfferingPage.getAddressAutocompleteInput().sendKeys("Gimnazija Tutin");

    utils.ensureDisplayed(addCulturalOfferingPage.getFirstAddress()).click();

    addCulturalOfferingPage.getCreateBtn().click();

    String toast = utils.ensureDisplayed("subcategory-error").getText();

    assertEquals("Offering wasn't saved\nSubcategory is required", toast);
  }

  @Test
  public void deleteCulturalOfferingFromDetailsView() throws InterruptedException {
    driver.get("http://localhost:4200/cultural-offering/6");

    CulturalOfferingDetailsPage detailsPage = PageFactory.initElements(driver, CulturalOfferingDetailsPage.class);

    utils.ensureDisplayed(detailsPage.getDeleteBtn()).click();

    utils.ensureDisplayed(detailsPage.getConfirmDeletionBtn()).click();

    String toast = utils.ensureDisplayed("deletion-successful").getText();

    assertEquals("Deleted successfully\nThe cultural offering was deleted successfully", toast);
  }

  @Test
  public void editCulturalOfferingChangeName() throws InterruptedException {
    driver.get("http://localhost:4200/edit-offering/2");
    addCulturalOfferingPage = PageFactory.initElements(driver, AddCulturalOfferingPage.class);

    WebElement nameInput = utils.ensureDisplayed(addCulturalOfferingPage.getOfferingNameInput());
    nameInput.sendKeys(Keys.CONTROL + "a");
    nameInput.sendKeys(Keys.DELETE);

    nameInput.sendKeys("New name");

    addCulturalOfferingPage.getCreateBtn().click();

    String toast = utils.ensureDisplayed("offering-edited-toast").getText();

    assertEquals("Successfully edited\nThe cultural offering was edited successfully", toast);
  }

  @Test
  public void editCulturalOfferingChangePhoto() throws InterruptedException, IOException {
    String photoPath = new ClassPathResource("test_photo.png").getFile().getAbsolutePath();

    driver.get("http://localhost:4200/edit-offering/2");
    addCulturalOfferingPage = PageFactory.initElements(driver, AddCulturalOfferingPage.class);

    utils.ensureDisplayed(addCulturalOfferingPage.getThumbnail());

    addCulturalOfferingPage.getFileUpload().sendKeys(photoPath);

    utils.ensureDisplayed(addCulturalOfferingPage.getThumbnail());

    addCulturalOfferingPage.getCreateBtn().click();

    String toast = utils.ensureDisplayed("offering-edited-toast").getText();

    assertEquals("Successfully edited\nThe cultural offering was edited successfully", toast);
  }

  @Test
  public void editCulturalOfferingNoName() throws InterruptedException {
    driver.get("http://localhost:4200/edit-offering/2");
    addCulturalOfferingPage = PageFactory.initElements(driver, AddCulturalOfferingPage.class);

    WebElement nameInput = utils.ensureDisplayed(addCulturalOfferingPage.getOfferingNameInput());
    nameInput.sendKeys(Keys.CONTROL + "a");
    nameInput.sendKeys(Keys.DELETE);

    addCulturalOfferingPage.getCreateBtn().click();

    String toast = utils.ensureDisplayed("name-error").getText();

    assertEquals("Offering wasn't saved\nName is required and can contain only letters, digits and spaces", toast);
  }

  @Test
  public void deleteCulturalOfferingFromListView() throws InterruptedException {
    driver.get("http://localhost:4200/list-view");

    ListViewPage listViewPage = PageFactory.initElements(driver, ListViewPage.class);

    utils.ensureDisplayed(listViewPage.getThirdDeleteButton()).click();

    utils.ensureDisplayed(listViewPage.getConfirmDeletionBtn()).click();

    String toast = utils.ensureDisplayed("deletion-successful").getText();
    assertEquals("Deleted successfully\nThe cultural offering was deleted successfully", toast);
  }
}
