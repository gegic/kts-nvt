package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.CategoryPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.LoginPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.ModeratorPage;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CategoryE2ETest {
    public static final String BASE_URL = "https://localhost:4200";
    private static WebDriver driver;
    private static LoginPage loginPage;
    private static CategoryPage categoryPage;

    @Before
    public void setUp() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        driver.manage().window().maximize();
        loginPage = PageFactory.initElements(driver, LoginPage.class);

        driver.get("https://localhost:4200/login");

        justWait(1000);

        loginPage.getEmail().sendKeys("admin@mail.com");
        loginPage.getNextBtn().click();
        justWait(1000);
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        justWait(1000);
        loginPage.getPassword().sendKeys("admin123");
        loginPage.getLoginBtn().click();
        justWait(1000);
    }

    @After
    public void tearDown() throws InterruptedException {
        driver.quit();
    }

    private static void justWait(int timeout) throws InterruptedException {
        synchronized (driver) {
            driver.wait(timeout);
        }
    }
    
    public void deleteSubcategory(String categoryName, String subcategoryName) throws InterruptedException {
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        addCategory(categoryName);
        
        justWait(1000);
        categoryPage.ensureIsDisplayed("p" + categoryName);
        
        justWait(1000);
        categoryPage.subcategoriesBtnCategory(categoryName).click();
        justWait(1000);
        categoryPage.deleteBtnSubcategory(subcategoryName).click();
        justWait(1000);
        categoryPage.getConfirmationService().click();
        justWait(4000);
    }
    
    public void addSubcategory(String categoryName, String subcategoryName) throws InterruptedException {
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);
        
        categoryPage.getElementById("subcategories" + categoryName).click();
        justWait(1000);
        
        categoryPage.getNewSubcategoryBtn().click();
        justWait(1000);
        
        categoryPage.getSubcategoryName().sendKeys(subcategoryName);
        justWait(1000);
        
        categoryPage.getElementById("saveSubcategory").click();
        justWait(1000);
    }

    public void deleteCategory(String name) throws InterruptedException {
        driver.get("https://localhost:4200/categories");

        justWait(1000);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
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
        driver.get("https://localhost:4200/categories");
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);

        categoryPage.getNewCategoryBtn().click();
        justWait(1000);
        categoryPage.getName().sendKeys(name);

        categoryPage.getSaveCategoryBtn().click();
        justWait(1000);
    }

    @Test
    public void addAndDeleteCategory() throws InterruptedException {
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String name = "TestCategory" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);
        categoryPage.getNewCategoryBtn().click();
        justWait(1000);
        categoryPage.getName().sendKeys(name);
        justWait(1000);
        categoryPage.getSaveCategoryBtn().click();
        justWait(1000);
        deleteCategory(name);
    }

    @Test
    public void editCategory() throws InterruptedException {
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        String name = "TestCategory" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);

        categoryPage.getNewCategoryBtn().click();
        justWait(1000);
        categoryPage.getName().sendKeys(name);

        justWait(1000);
        categoryPage.getSaveCategoryBtn().click();
        justWait(1000);
        
        categoryPage.ensureIsDisplayed("p" + name);
        categoryPage.editBtnCategory(name).click();
        justWait(1000);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);
        categoryPage.getName().sendKeys("2");
        justWait(1000);
        categoryPage.getElementById("addCategory").click();
        justWait(1000);
        categoryPage.ensureIsDisplayed("p"+name+"2");
        justWait(1000);
        deleteCategory(name+"2");
    }

    @Test
    public void addCategoryExists() throws InterruptedException {
        addCategory("asdsadsadadasdasdsasa");
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String name = "asdsadsadadasdasdsasa";
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        justWait(1000);

        categoryPage.getNewCategoryBtn().click();
        justWait(1000);
        categoryPage.getName().sendKeys(name);

        justWait(1000);
        categoryPage.getSaveCategoryBtn().click();
        justWait(1000);

        String toast = categoryPage.ensureIsDisplayedToast();

        assertEquals("Already exists\nA category with this name already exists", toast);
        
        justWait(1000);
        deleteCategory("asdsadsadadasdasdsasa");
    }

    @Test
    public void addCategoryEmptyName() throws InterruptedException {
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String name = "";
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        justWait(1000);

        categoryPage.getNewCategoryBtn().click();
        justWait(1000);
        categoryPage.getName().sendKeys(name);

        justWait(1000);
        categoryPage.getSaveCategoryBtn().click();
        justWait(1000);

        String toast = categoryPage.ensureIsDisplayedToast();

        assertEquals("Required\n" +
                "Name is required.", toast);
    }

    @Test
    public void editCategoryNameExists() throws InterruptedException {
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String name = "TestCategory" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);

        categoryPage.getNewCategoryBtn().click();
        justWait(1000);
        categoryPage.getName().sendKeys(name);

        categoryPage.getSaveCategoryBtn().click();
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        justWait(1000);
        categoryPage.ensureIsDisplayed("p" + name);
        categoryPage.editBtnCategory(name).click();
        justWait(1000);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);
        categoryPage.getName().clear();
        justWait(1000);
        categoryPage.getName().sendKeys("Sed");
        justWait(1000);
        categoryPage.getElementById("addCategory").click();
        justWait(1000);

        String toast = categoryPage.ensureIsDisplayedToast();

        assertEquals("Already exists\nA category with this name already exists", toast);
        deleteCategory(name);
    }

    @Test
    public void deleteCategoryWithSubcategories() throws InterruptedException {
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        justWait(1000);
        String name = "Institucijjja"+ ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        String subcategory = "Subcategory1"+ ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        addCategory(name);
        justWait(1000);
        addSubcategory(name, subcategory);
        justWait(1000);
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        justWait(1000);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);

        categoryPage.deleteBtnCategory(name).click();
        justWait(1000);
        categoryPage.getConfirmationService().click();
        String toast = categoryPage.ensureIsDisplayedToast();
        justWait(1000);

        assertEquals("Deletion unsuccessful\nThis category has subcategories associated" +
                " with it. Firstly delete all its subcategories in order to be able to delete it.", toast);
        
        deleteSubcategory(name, subcategory);
        justWait(1000);
        deleteCategory(name);
    }

    @Test
    public void addCategoryAndSubcategories() throws InterruptedException {
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String name = "TestCategory" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);

        categoryPage.getNewCategoryBtn().click();
        justWait(1000);
        categoryPage.getName().sendKeys(name);

        justWait(1000);
        categoryPage.getSaveCategoryBtn().click();
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        justWait(1000);
        categoryPage.ensureIsDisplayed("p" + name);

        categoryPage.subcategoriesBtnCategory(name).click();
        justWait(1000);

        categoryPage.getNewSubcategoryBtn().click();
        justWait(1000);

        categoryPage.getSubcategoryName().sendKeys("Subcatdsfsdegory14");
        justWait(1000);

        categoryPage.getElementById("saveSubcategory").click();
        justWait(1000);

        String toast = categoryPage.ensureIsDisplayedToast();
        justWait(1000);

        assertEquals("Saved\nSubcategory was successfully saved.", toast);

        //clear up everything
        justWait(4000);
        categoryPage.ensureIsDisplayed("deleteSubcatdsfsdegory14");
        //categoryPage.deleteBtnSubcategory(name).click();
//        WebElement ele = driver.findElement(By.id("delete"+name));
//        JavascriptExecutor executor = (JavascriptExecutor)driver;
//        executor.executeScript("arguments[0].click();", ele);
        //iz nekog razloga mora ovako, dijalog koji iskoci ga zbuni i nece da klikne...
        categoryPage.getElementById("deleteSubcatdsfsdegory14").sendKeys(Keys.RETURN);
        justWait(1000);
        categoryPage.getConfirmationService().click();
        justWait(1000);
        deleteCategory(name);
    }

    @Test
    public void addExistingSubcategory() throws InterruptedException {
        String randomCategory = "okrwejfkwen213";
        String randomSubcategory = "ojjewb1432";
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        justWait(1000);
        addCategory(randomCategory);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);
        addSubcategory(randomCategory, randomSubcategory);
        justWait(1000);
    
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);
    
        categoryPage.getElementById("subcategories" + randomCategory).click();
        justWait(1000);
    
        categoryPage.getNewSubcategoryBtn().click();
        justWait(1000);
    
        categoryPage.getSubcategoryName().sendKeys(randomSubcategory);
        justWait(1000);
    
        categoryPage.getElementById("saveSubcategory").click();
        justWait(1000);

        String toast = categoryPage.ensureIsDisplayedToast();
        justWait(1000);

        assertEquals("Already exists\n" +
                "A subcategory with this name already exists", toast);
        deleteSubcategory(randomCategory, randomSubcategory);
        deleteCategory(randomCategory);
    }

    @Test
    public void addSubcategoryEmptyName() throws InterruptedException {
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String name = "";
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        justWait(1000);
        addCategory("Institucijaaa");
        justWait(1000);
        
        categoryPage.getElementById("subcategoriesInstitucijaaa").click();
        justWait(1000);

        categoryPage.getNewSubcategoryBtn().click();
        justWait(1000);

        categoryPage.getSubcategoryName().sendKeys(name);
        justWait(1000);

        categoryPage.getElementById("saveSubcategory").click();
        justWait(1000);

        String toast = categoryPage.ensureIsDisplayedToast();
        justWait(1000);

        assertEquals("Required\nName is required.", toast);
        deleteCategory("Institucijaaa");
    }

    @Test
    public void deleteSubcategory() throws InterruptedException {
        justWait(1000);
        driver.get("https://localhost:4200/categories");
        String categoryName = "TestCategory" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        addCategory(categoryName);

        justWait(1000);
        categoryPage.ensureIsDisplayed("p" + categoryName);

        justWait(1000);
        categoryPage.subcategoriesBtnCategory(categoryName).click();
        justWait(1000);

        categoryPage.getNewSubcategoryBtn().click();
        justWait(1000);

        categoryPage.getSubcategoryName().sendKeys("Subcategory1");
        justWait(1000);

        categoryPage.getElementById("saveSubcategory").click();
        justWait(1000);

        justWait(1000);
        categoryPage.deleteBtnSubcategory("Subcategory1").click();
        justWait(1000);
        categoryPage.getConfirmationService().click();
//        String toast = categoryPage.ensureIsDisplayedCustom("subcategory-deleted-message");
//        categoryPage.ensureIsNonDisplayed("deleteSubcategory1");
        justWait(4000);
        String subcategoriesNumberText = categoryPage.getElementById("subcategoriesNumberText").getText();

        justWait(1000);
        assertEquals(subcategoriesNumberText,
                "In total there are 0 subcategories for "+categoryName+".");
        
        deleteCategory(categoryName);
    }

    @Test
    public void editSubcategory() throws InterruptedException {
        justWait(1000);
        String categoryName = "TestCategory" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        String subcategoryName = "TestSubcategory" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        addCategory(categoryName);
        justWait(1000);
        
        categoryPage.ensureIsDisplayed("p" + categoryName);

        categoryPage.subcategoriesBtnCategory(categoryName).click();
        justWait(1000);

        categoryPage.getNewSubcategoryBtn().click();
        justWait(1000);

        categoryPage.getSubcategoryName().sendKeys("Subcategory1");
        justWait(1000);

        categoryPage.getElementById("saveSubcategory").click();
        justWait(1000);

        justWait(1000);
        categoryPage.editBtnSubcategory("Subcategory1").click();
        justWait(1000);
        categoryPage.getSubcategoryName().clear();
        justWait(1000);

        categoryPage.getSubcategoryName().sendKeys(subcategoryName);
        justWait(1000);

        categoryPage.getElementById("saveSubcategory").click();
        justWait(1000);

        String toast = categoryPage.ensureIsDisplayedToast();
        justWait(1000);

        assertEquals("Saved\nSubcategory was successfully saved.", toast);
        
        deleteSubcategory(categoryName, subcategoryName);
        deleteCategory(categoryName);
    }

//    @Test
//    public void editSubcategoryEmptyName() throws InterruptedException {
//        justWait(1000);
//        String categoryName = "TestCategory" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
//        String subcategoryName = "TestSubcategory" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
//        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
//        addCategory(categoryName);
//        justWait(1000);
//
//        JavascriptExecutor jse = (JavascriptExecutor) driver;
//        jse.executeScript("window.scrollTo(0, document.body.scrollHeight)");
//
//        justWait(1000);
//        categoryPage.ensureIsDisplayed("p" + categoryName);
//
//        categoryPage.subcategoriesBtnCategory(categoryName).click();
//        justWait(1000);
//
//        categoryPage.getNewSubcategoryBtn().click();
//        justWait(1000);
//
//        categoryPage.getSubcategoryName().sendKeys("Subcategory1");
//        justWait(1000);
//
//        categoryPage.getElementById("saveSubcategory").sendKeys(Keys.RETURN);
//        justWait(2000);
//
//        justWait(1000);
//        categoryPage.editBtnSubcategory("Subcategory1").click();
//        justWait(1000);
//
//        //ostavi prazno ime
//        categoryPage.getSubcategoryName().clear();
//        justWait(1000);
//        categoryPage.getSubcategoryName().sendKeys("");
//        justWait(1000);
//
//        categoryPage.getElementById("saveSubcategory").click();
//        justWait(2000);
//
//        String toast = categoryPage.ensureIsDisplayedToast();
//        justWait(1000);
//
//        assertEquals("Required\nName is required.", toast);
//    }

    @Test
    public void editSubcategoryNameExists() throws InterruptedException {
        justWait(1000);
        String categoryName = "TestCategory" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        String subcategoryName = "TestSubcategoryaa" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
        String subcategoryName2 = "TestSubcategoryaa2" + ThreadLocalRandom.current().nextInt(0, 9999 + 1);
    
        categoryPage = PageFactory.initElements(driver, CategoryPage.class);
        addCategory(categoryName);
        justWait(1000);

        categoryPage.ensureIsDisplayed("p" + categoryName);

        categoryPage.subcategoriesBtnCategory(categoryName).click();
        justWait(1000);

        categoryPage.getNewSubcategoryBtn().click();
        justWait(1000);

        categoryPage.getSubcategoryName().sendKeys(subcategoryName);
        justWait(1000);

        categoryPage.getElementById("saveSubcategory").click();
        justWait(1000);
        categoryPage.getNewSubcategoryBtn().click();
        justWait(1000);
    
        categoryPage.getSubcategoryName().sendKeys(subcategoryName2);
        justWait(1000);
    
        categoryPage.getElementById("saveSubcategory").click();
        justWait(1000);
    
    
        categoryPage.editBtnSubcategory(subcategoryName).click();
        justWait(1000);
        categoryPage.getSubcategoryName().clear();
        justWait(1000);

        categoryPage.getSubcategoryName().sendKeys(subcategoryName2);
        justWait(1000);

        categoryPage.getElementById("saveSubcategory").click();
        justWait(1000);

        String toast = categoryPage.ensureIsDisplayedToast();
        justWait(1000);

    assertEquals("Already exists\n" + "A subcategory with this name already exists", toast);
        deleteSubcategory(categoryName, subcategoryName2);
        deleteSubcategory(categoryName, subcategoryName);
        deleteCategory(categoryName);
    }


}
