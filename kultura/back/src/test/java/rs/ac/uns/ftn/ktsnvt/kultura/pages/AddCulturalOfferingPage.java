package rs.ac.uns.ftn.ktsnvt.kultura.pages;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class AddCulturalOfferingPage {

    private final WebDriver driver;

    @FindBy(id = "offering-name")
    WebElement offeringNameInput;

    @FindBy(id = "category")
    WebElement categorySelect;

    @FindBy(id = "subcategory")
    WebElement subcategorySelect;

    @FindBy(css = "#category .ng-option")
    WebElement firstCategory;

    @FindBy(css = "#subcategory .ng-option")
    WebElement firstSubcategory;

    @FindBy(id = "brief-info")
    WebElement briefInfoArea;

    @FindBy(id = "file-upload")
    WebElement fileUpload;

    @FindBy(id = "thumbnail")
    WebElement thumbnail;

    @FindBy(css = "#address input[role=\"searchbox\"]")
    WebElement addressAutocompleteInput;

    @FindBy(css = "#address .p-autocomplete-item")
    WebElement firstAddress;

    @FindBy(id = "additional-info")
    WebElement additionalInfoArea;

    @FindBy(id = "create")
    WebElement createBtn;

    public AddCulturalOfferingPage(WebDriver driver) {
        this.driver = driver;
    }
}
