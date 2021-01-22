package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class E2EUtils {

    private final WebDriver driver;

    public E2EUtils(WebDriver driver) {
        this.driver = driver;
    }

    public void waitFor(int timeout) throws InterruptedException {
        synchronized (driver) {
            driver.wait(timeout);
        }
    }

    public WebElement ensureLoggedIn() throws InterruptedException {
        return this.ensureDisplayed("avatar");
    }

    public void ensureNotDisplayed(String id) {
        (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.invisibilityOfElementLocated(By.id(id)));
    }

    public WebElement ensureDisplayed(String id) throws InterruptedException {
        return this.ensureDisplayed(id, 400);
    }

    public WebElement ensureDisplayed(String id, long timeout) throws InterruptedException {
        WebElement webElement = new WebDriverWait(driver, 30)
                .until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        synchronized (driver) {
            driver.wait(timeout);
        }
        return webElement;
    }

    public WebElement ensureDisplayed(WebElement element) throws InterruptedException {
        return this.ensureDisplayed(element, 400);
    }
    public WebElement ensureDisplayed(WebElement element, long timeout) throws InterruptedException {
        WebElement webElement = new WebDriverWait(driver, 30).until(ExpectedConditions.visibilityOf(element));
        synchronized (driver) {
            driver.wait(timeout);
        }
        return webElement;
    }

    public WebElement ensureEnabled(WebElement element) throws InterruptedException {
        return this.ensureEnabled(element, 400);
    }
    public WebElement ensureEnabled(WebElement element, long timeout) throws InterruptedException {
        WebElement webElement = new WebDriverWait(driver, 30)
                .until(ExpectedConditions.elementToBeClickable(element));
        synchronized (driver) {
            driver.wait(timeout);
        }
        return webElement;
    }
}
