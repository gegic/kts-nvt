package rs.ac.uns.ftn.ktsnvt.kultura.pages;

import lombok.Data;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Getter
public class LoginPage {

    private WebDriver driver;

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "password")
    private WebElement password;

    @FindBy(id = "submit")
    private WebElement nextBtn;

    @FindBy(id = "login")
    private WebElement loginBtn;

    public LoginPage() {
    }

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }

    public String ensureIsDisplayedToast() {
        return (new WebDriverWait(driver, 30)).until(ExpectedConditions.presenceOfElementLocated(By.id("toast-container"))).getText();
    }

}
