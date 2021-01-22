package rs.ac.uns.ftn.ktsnvt.kultura.pages;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Getter
public class CulturalOfferingDetailsPage {

    @FindBy(id = "delete-btn")
    WebElement deleteBtn;

    @FindBy(className = "confirm-deletion")
    WebElement confirmDeletionBtn;

    @FindBy(id = "subscribe-btn")
    WebElement subscribeBtn;

    @FindBy(id = "unsubscribe-btn")
    WebElement unsubscribeBtn;

    private final WebDriver driver;


    public CulturalOfferingDetailsPage(WebDriver driver) {
        this.driver = driver;
    }

}
