package rs.ac.uns.ftn.ktsnvt.kultura.pages;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Getter
public class ListViewPage {

    @FindBy(id = "search-input")
    WebElement searchInput;

    @FindBy(id = "filter-btn")
    WebElement filterButton;

    @FindBy(id = "reset-filter-btn")
    WebElement resetFilterBtn;

    @FindBy(css = ".p-slider-range:first-child")
    WebElement leftHandle;

    @FindBy(css = ".p-slider-range:nth-child(2)")
    WebElement rightHandle;

    @FindBy(id = "only-reviews-cbx")
    WebElement onlyReviewsCbx;

    @FindBy(id = "category")
    WebElement categorySelect;

    @FindBy(id = "subcategory")
    WebElement subcategorySelect;

    @FindBy(css = "#category .ng-option")
    WebElement firstCategory;

    @FindBy(css = "#subcategory .ng-option")
    WebElement firstSubcategory;

    @FindBy(className = "category-subcategory")
    List<WebElement> categoriesSubcategories;

    @FindBy(id = "save-filter-btn")
    WebElement saveFilterBtn;

    @FindBy(id = "delete-num0")
    WebElement firstDeleteBtn;

    @FindBy(className = "confirm-deletion")
    WebElement confirmDeletionBtn;

    @FindBy(className = "offering-name")
    List<WebElement> offeringNames;

    @FindBy(className = "review")
    List<WebElement> reviews;


    private final WebDriver driver;

    public ListViewPage(WebDriver driver) {
        this.driver = driver;
    }

}
