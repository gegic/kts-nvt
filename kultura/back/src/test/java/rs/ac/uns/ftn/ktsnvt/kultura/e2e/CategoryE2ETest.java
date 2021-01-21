package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.CategoryPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.LoginPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.ModeratorPage;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CategoryE2ETest {
    public static final String BASE_URL = "http://localhost:4200";
    private static WebDriver driver;
    private static LoginPage loginPage;
    private static CategoryPage categoryPage;

    @BeforeClass
    public static void setUp() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        driver.manage().window().maximize();
        loginPage = PageFactory.initElements(driver, LoginPage.class);

        driver.get("http://localhost:4200/login");

        justWait(2000);

        loginPage.getEmail().sendKeys("admin@mail.com");
        loginPage.getNextBtn().click();
        justWait(2000);
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        justWait(2000);
        loginPage.getPassword().sendKeys("admin123");
        loginPage.getLoginBtn().click();
        justWait(2000);
    }

    @AfterClass
    public static void tearDown() throws InterruptedException {
        driver.quit();
    }

    private static void justWait(int timeout) throws InterruptedException {
        synchronized (driver) {
            driver.wait(timeout);
        }
    }

    public void deleteCategory(String name) throws InterruptedException {
        driver.get("http://localhost:4200/categories");

        justWait(1000);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");

        justWait(1000);
        categoryPage.deleteBtnCategory(name).click();
        justWait(1000);
        categoryPage.getConfirmationService().click();
        String toast = categoryPage.ensureIsDisplayedToast();
        justWait(1000);
        categoryPage.ensureIsNonDisplayed("delete" + name);
        assertEquals(toast, "Deleted successfully\n" + "The category was deleted successfully");
    }

    public void addCategory(String name) throws InterruptedException {
        justWait(1000);
        driver.get("http://localhost:4200/categories");
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);

        categoryPage.getNewCategoryBtn().click();
        justWait(3000);
        categoryPage.getName().sendKeys(name);

        categoryPage.getSaveCategoryBtn().click();
        justWait(3000);
    }

    @Test
    public void addAndDeleteCategory() throws InterruptedException {
        justWait(1000);
        driver.get("http://localhost:4200/categories");
        String name = "TestCategory" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);

        categoryPage.getNewCategoryBtn().click();
        justWait(3000);
        categoryPage.getName().sendKeys(name);

        categoryPage.getSaveCategoryBtn().click();
        justWait(3000);
        deleteCategory(name);
    }

    @Test
    public void editCategory() throws InterruptedException {
        justWait(1000);
        driver.get("http://localhost:4200/categories");
        String name = "TestCategory" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);

        categoryPage.getNewCategoryBtn().click();
        justWait(3000);
        categoryPage.getName().sendKeys(name);

        categoryPage.getSaveCategoryBtn().click();
        justWait(3000);

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");

        justWait(1000);
        categoryPage.ensureIsDisplayed("p" + name);
        categoryPage.editBtnCategory(name).click();
        justWait(1000);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);
        categoryPage.getName().sendKeys("2");
        categoryPage.getElementById("addCategory").click();
        justWait(2000);
        jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        categoryPage.ensureIsDisplayed("p"+name+"2");
        justWait(1000);
        deleteCategory(name+"2");
    }

    @Test
    public void addCategoryExists() throws InterruptedException {
        justWait(1000);
        driver.get("http://localhost:4200/categories");
        String name = "Institucija";
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);

        categoryPage.getNewCategoryBtn().click();
        justWait(3000);
        categoryPage.getName().sendKeys(name);

        categoryPage.getSaveCategoryBtn().click();
        justWait(3000);

        String toast = categoryPage.ensureIsDisplayedToast();

        assertEquals("Already exists\nA category with this name already exists", toast);
    }

    @Test
    public void addCategoryEmptyName() throws InterruptedException {
        justWait(1000);
        driver.get("http://localhost:4200/categories");
        String name = "";
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);

        categoryPage.getNewCategoryBtn().click();
        justWait(3000);
        categoryPage.getName().sendKeys(name);

        categoryPage.getSaveCategoryBtn().click();
        justWait(3000);

        String toast = categoryPage.ensureIsDisplayedToast();

        assertEquals("Required\n" +
                "Name is required.", toast);
    }

    @Test
    public void editCategoryNameExists() throws InterruptedException {
        justWait(1000);
        driver.get("http://localhost:4200/categories");
        String name = "TestCategory" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);

        categoryPage.getNewCategoryBtn().click();
        justWait(3000);
        categoryPage.getName().sendKeys(name);

        categoryPage.getSaveCategoryBtn().click();
        justWait(3000);

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");

        justWait(1000);
        categoryPage.ensureIsDisplayed("p" + name);
        categoryPage.editBtnCategory(name).click();
        justWait(1000);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);
        categoryPage.getName().clear();
        categoryPage.getName().sendKeys("Institucija");
        categoryPage.getElementById("addCategory").click();
        justWait(2000);

        String toast = categoryPage.ensureIsDisplayedToast();

        assertEquals("Already exists\nA category with this name already exists", toast);
    }

    @Test
    public void deleteCategoryWithSubcategories() throws InterruptedException {
        driver.get("http://localhost:4200/categories");
        String name = "Institucija";

        justWait(1000);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);


        justWait(1000);
        categoryPage.deleteBtnCategory(name).click();
        justWait(1000);
        categoryPage.getConfirmationService().click();
        String toast = categoryPage.ensureIsDisplayedToast();
        justWait(1000);

        assertEquals("Deletion unsuccessful\nThis category has subcategories associated" +
                " with it. Firstly delete all its subcategories in order to be able to delete it.", toast);
    }

    @Test
    public void addCategoryAndSubcategories() throws InterruptedException {
        justWait(1000);
        driver.get("http://localhost:4200/categories");
        String name = "TestSubcategory" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);

        categoryPage.getNewCategoryBtn().click();
        justWait(3000);
        categoryPage.getName().sendKeys(name);

        categoryPage.getSaveCategoryBtn().click();
        justWait(3000);

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");

        justWait(1000);
        categoryPage.ensureIsDisplayed("p" + name);

        categoryPage.subcategoriesBtnCategory(name).click();
        justWait(1000);

        categoryPage.getNewSubcategoryBtn().click();
        justWait(1000);

        categoryPage.getSubcategoryName().sendKeys(name);
        justWait(1000);

        categoryPage.getElementById("saveSubcategory").click();
        justWait(2000);

        String toast = categoryPage.ensureIsDisplayedToast();
        justWait(1000);

        assertEquals("Saved\nSubcategory was successfully saved.", toast);
    }

    @Test
    public void addExistingSubcategory() throws InterruptedException {
        justWait(1000);
        driver.get("http://localhost:4200/categories");
        String name = "Muzej";
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);

        categoryPage.getElementById("subcategoriesInstitucija").click();
        justWait(1000);

        categoryPage.getNewSubcategoryBtn().click();
        justWait(1000);

        categoryPage.getSubcategoryName().sendKeys(name);
        justWait(1000);

        categoryPage.getElementById("saveSubcategory").click();
        justWait(2000);

        String toast = categoryPage.ensureIsDisplayedToast();
        justWait(1000);

        assertEquals("Already exists\n" +
                "A category with this name already exists", toast);
    }

    @Test
    public void addSubcategoryEmptyName() throws InterruptedException {
        justWait(1000);
        driver.get("http://localhost:4200/categories");
        String name = "";
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);

        categoryPage.getElementById("subcategoriesInstitucija").click();
        justWait(1000);

        categoryPage.getNewSubcategoryBtn().click();
        justWait(1000);

        categoryPage.getSubcategoryName().sendKeys(name);
        justWait(1000);

        categoryPage.getElementById("saveSubcategory").click();
        justWait(2000);

        String toast = categoryPage.ensureIsDisplayedToast();
        justWait(1000);

        assertEquals("Already exists\n" +
                "A subcategory with this name already exists", toast);
    }

    @Test
    public void deleteSubcategory() throws InterruptedException {
        justWait(1000);
        String categoryName = "TestCategory" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        addCategory(categoryName);
        justWait(1000);

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");

        justWait(1000);
        categoryPage.ensureIsDisplayed("p" + categoryName);

        categoryPage.subcategoriesBtnCategory(categoryName).click();
        justWait(1000);

        categoryPage.getNewSubcategoryBtn().click();
        justWait(1000);

        categoryPage.getSubcategoryName().sendKeys("Subcategory1");
        justWait(1000);

        categoryPage.getElementById("saveSubcategory").click();
        justWait(2000);

        justWait(1000);
        categoryPage.deleteBtnSubcategory("Subcategory1").click();
        justWait(1000);
        categoryPage.getConfirmationService().click();
        justWait(1000);
        String toast = categoryPage.ensureIsDisplayedToast();
        justWait(1000);
        categoryPage.ensureIsNonDisplayed("deleteSubcategory1");
        assertEquals(toast, "Deleted successfully\n" + "The subcategory was deleted successfully");
    }

    @Test
    public void editSubcategory() throws InterruptedException {
        justWait(1000);
        String categoryName = "TestCategory" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        String subcategoryName = "TestSubcategory" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        addCategory(categoryName);
        justWait(1000);

        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");

        justWait(1000);
        categoryPage.ensureIsDisplayed("p" + categoryName);

        categoryPage.subcategoriesBtnCategory(categoryName).click();
        justWait(1000);

        categoryPage.getNewSubcategoryBtn().click();
        justWait(1000);

        categoryPage.getSubcategoryName().sendKeys("Subcategory1");
        justWait(1000);

        categoryPage.getElementById("saveSubcategory").click();
        justWait(2000);

        justWait(1000);
        categoryPage.editBtnSubcategory("Subcategory1").click();
        justWait(1000);
        categoryPage.getSubcategoryName().clear();
        justWait(1000);

        categoryPage.getSubcategoryName().sendKeys(subcategoryName);
        justWait(1000);

        categoryPage.getElementById("saveSubcategory").click();
        justWait(2000);

        String toast = categoryPage.ensureIsDisplayedToast();
        justWait(1000);

        assertEquals("Saved\nSubcategory was successfully saved.", toast);
    }

}
