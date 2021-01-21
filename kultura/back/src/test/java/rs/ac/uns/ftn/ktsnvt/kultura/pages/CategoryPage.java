package rs.ac.uns.ftn.ktsnvt.kultura.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class CategoryPage {
    private final WebDriver driver;

    @FindBy(id = "newCategory")
    private WebElement newCategoryBtn;

    @FindBy(id = "newSubcategory")
    private WebElement newSubcategoryBtn;

    @FindBy(id = "subcategoryName")
    private WebElement subcategoryName;

    @FindBy(id = "addCategory")
    private WebElement saveCategoryBtn;

    @FindBy(id = "name")
    private WebElement name;

    @FindBy(className = "p-confirm-dialog-accept")
    private WebElement confirmationService;

    public CategoryPage(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement deleteBtnCategory(String categoryName) {
        return driver.findElement(By.id("delete" + categoryName));
    }

    public WebElement editBtnCategory(String categoryName) {
        return driver.findElement(By.id("edit" + categoryName));
    }

    public WebElement deleteBtnSubcategory(String subcategoryName) {
        return driver.findElement(By.id("delete" + subcategoryName));
    }

    public WebElement editBtnSubcategory(String subcategoryName) {
        return driver.findElement(By.id("edit" + subcategoryName));
    }

    public WebElement subcategoriesBtnCategory(String categoryName) {
        return driver.findElement(By.id("subcategories" + categoryName));
    }

    public WebElement getElementById(String id) {
        return driver.findElement(By.id(id));
    }

    public WebElement getElementByClass(String className) {
        return driver.findElement(By.className(className));
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
