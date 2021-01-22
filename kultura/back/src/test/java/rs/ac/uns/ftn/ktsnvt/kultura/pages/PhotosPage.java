package rs.ac.uns.ftn.ktsnvt.kultura.pages;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class PhotosPage {
    private final WebDriver driver;

    @FindBy(id = "add-photo-btn")
    private WebElement addPhotoBtn;

    @FindBy(css = "#file-upload input")
    private WebElement photoInput;

    @FindBy(css = "#file-upload .p-fileupload-choose")
    private WebElement chooseBtn;

    @FindBy(css = "#file-upload p-button[ng-reflect-label=\"Upload\"] button")
    private WebElement uploadBtn;

    @FindBy(id = "photo-num0")
    private WebElement firstPhoto;

    @FindBy(id = "delete-photo")
    private WebElement deletePhotoBtn;

    @FindBy(className = "confirm-delete")
    private WebElement confirmDeletionBtn;

    public PhotosPage(WebDriver driver) {
        this.driver = driver;
    }
}
