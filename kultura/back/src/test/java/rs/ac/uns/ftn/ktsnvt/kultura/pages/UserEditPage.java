package rs.ac.uns.ftn.ktsnvt.kultura.pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class UserEditPage {

    private WebDriver driver;

    @FindBy(id = "name")
    private WebElement name;

    @FindBy(id = "lastName")
    private WebElement lastName;

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(id = "confirmPassword")
    private WebElement confirmPassword;

    @FindBy(id = "p-tabpanel-1-label")
    private WebElement passwordTab;

    @FindBy(id = "submitName")
    private WebElement submitName;

    @FindBy(id = "submitLastName")
    private WebElement submitLastName;

    @FindBy(id = "submitEmail")
    private WebElement submitEmail;

    @FindBy(id = "submitPassword")
    private WebElement submitPassword;

    @FindBy(id = "name-val")
    private WebElement nameVal;

    @FindBy(id = "lastName-val")
    private WebElement lastNameVal;

    @FindBy(id = "email-val")
    private WebElement emailVal;

    public UserEditPage() {
    }

    public UserEditPage(WebDriver driver) {
        this.driver = driver;
    }

    public String ensureIsDisplayedToast() {
        return (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.id("toast-container"))).getText();
    }

}
