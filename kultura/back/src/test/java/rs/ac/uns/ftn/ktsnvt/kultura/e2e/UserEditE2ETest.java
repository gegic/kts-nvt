package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.UserDto;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.LoginPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.UserEditPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.VerificationPage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static rs.ac.uns.ftn.ktsnvt.kultura.e2e.E2EConstants.ADMIN_PASSWORD;
import static rs.ac.uns.ftn.ktsnvt.kultura.e2e.E2EConstants.EMAIL_BAD_FORMAT;

public class UserEditE2ETest {

    private static E2EUtils utils;

    private static WebDriver driver;

    private static UserEditPage userEditPage;

    private static LoginPage loginPage;

    private static VerificationPage verificationPage;

    private static String userPassword = "admin123";

    private static String userEmail = "user@mail.com";

    private static boolean loggedIn = false;

    @BeforeClass
    public static void setUp() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        utils = new E2EUtils(driver);
        userPassword = "admin123";
        driver.manage().window().maximize();
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }

    public static void login() throws InterruptedException {
        if (loggedIn) {
            return;
        }
        LoginPage loginPage = PageFactory.initElements(driver, LoginPage.class);
        driver.get("https://localhost:4200/login");

        utils.ensureDisplayed(loginPage.getEmail()).sendKeys(userEmail);
        loginPage.getNextBtn().click();
        utils.ensureDisplayed(loginPage.getPassword()).sendKeys(userPassword);
        loginPage.getLoginBtn().click();
        utils.ensureLoggedIn();
        loggedIn = true;
    }

    @Test
    public void ChangeNameTestSuccess() throws InterruptedException {

        login();

        driver.get("https://localhost:4200/user-edit");
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);

        utils.ensureDisplayed(userEditPage.getName()).sendKeys(E2EConstants.NAME);

        utils.ensureDisplayed(userEditPage.getSubmitName()).click();

        utils.waitFor(1000);

        String toast = utils.ensureDisplayed("update-successful").getText();

        assertEquals("Success\nFirst name updated", toast);
        assertEquals(E2EConstants.NAME, userEditPage.getNameVal().getText());
    }

    @Test
    public void ChangeNameTestFail() throws InterruptedException {
        login();

        driver.get("https://localhost:4200/user-edit");

        utils.ensureDisplayed(userEditPage.getName()).sendKeys("");

        utils.ensureDisplayed(userEditPage.getSubmitName()).click();

        utils.waitFor(1000);

        String toast = utils.ensureDisplayed("name-failed").getText();

        assertEquals("Not updated\nFirst name must not be empty.", toast);
    }

    @Test
    public void ChangeLastNameTestSuccess() throws InterruptedException {
        login();

        driver.get("https://localhost:4200/user-edit");
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);

        utils.ensureDisplayed(userEditPage.getLastName()).sendKeys(E2EConstants.LASTNAME);

        utils.ensureDisplayed(userEditPage.getSubmitLastName()).click();

        utils.waitFor(1000);

        String toast = utils.ensureDisplayed("update-successful").getText();

        assertEquals("Success\nLast name updated", toast);
        assertEquals(E2EConstants.LASTNAME, userEditPage.getLastNameVal().getText());

    }

    @Test
    public void ChangeLastNameTestFail() throws InterruptedException {
        login();

        driver.get("https://localhost:4200/user-edit");

        utils.ensureDisplayed(userEditPage.getLastName()).sendKeys("");

        utils.ensureDisplayed(userEditPage.getSubmitLastName()).click();

        utils.waitFor(1000);

        String toast = utils.ensureDisplayed("last-name-failed").getText();

        assertEquals("Not updated\nLast name must not be empty.", toast);
    }

    @Test
    public void ChangePasswordTestSuccess() throws InterruptedException {
        if (loggedIn) {
            driver.get("https://localhost:4200/user-edit");
            userEditPage = PageFactory.initElements(driver, UserEditPage.class);

            utils.ensureDisplayed("avatar").click();

            utils.ensureDisplayed("logout-btn").click();

            assertEquals("https://localhost:4200/login", driver.getCurrentUrl());

            loggedIn = false;
        }
        userEmail = "user3@mail.com";
        login();

        driver.get("https://localhost:4200/user-edit");
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);

        utils.ensureDisplayed(userEditPage.getPasswordTab()).click();

        utils.ensureDisplayed(userEditPage.getPassword()).sendKeys(E2EConstants.NEW_PASSWORD);
        userEditPage.getConfirmPassword().sendKeys(E2EConstants.NEW_PASSWORD);
        userEditPage.getSubmitPassword().click();

        String toastLogin = utils.ensureDisplayed("sign-out-toast").getText();
        String toastSuccess = utils.ensureDisplayed("update-successful").getText();

        assertEquals("Success\npassword updated", toastSuccess);
        assertEquals("Logged out\nPlease log in with the new password", toastLogin);

        userPassword = E2EConstants.NEW_PASSWORD;
        userEmail = "user@mail.com";
        loggedIn = false;
    }

    @Test
    public void ChangePasswordTestFail() throws InterruptedException {
        login();
        driver.get("https://localhost:4200/user-edit");
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        utils.ensureDisplayed(userEditPage.getPasswordTab()).click();

        utils.ensureDisplayed(userEditPage.getPassword()).sendKeys("");
        userEditPage.getSubmitPassword().click();

        String toast = utils.ensureDisplayed("password-valid-error").getText();

        assertEquals("Not updated\nNew password must not be empty.", toast);
        driver.get("https://localhost:4200/user-edit");
        utils.ensureDisplayed(userEditPage.getPasswordTab()).click();

        utils.ensureDisplayed(userEditPage.getPassword()).sendKeys(E2EConstants.NEW_PASSWORD_BAD_FORMAT0);
        userEditPage.getSubmitPassword().click();

        toast = utils.ensureDisplayed("password-valid-error").getText();
        assertEquals("Not updated\nNew password length must be between 8 and 50 characters.", toast);

        driver.get("https://localhost:4200/user-edit");
        utils.ensureDisplayed(userEditPage.getPasswordTab()).click();

        utils.ensureDisplayed(userEditPage.getPassword()).sendKeys(E2EConstants.NEW_PASSWORD_BAD_FORMAT0_1);
        userEditPage.getSubmitPassword().click();
        toast = utils.ensureDisplayed("password-valid-error").getText();
        assertEquals("Not updated\nNew password length must be between 8 and 50 characters.", toast);
        driver.get("https://localhost:4200/user-edit");
        utils.ensureDisplayed(userEditPage.getPasswordTab()).click();

        userEditPage.getPassword().sendKeys(E2EConstants.NEW_PASSWORD_BAD_FORMAT1);
        userEditPage.getSubmitPassword().click();


        toast = utils.ensureDisplayed("password-valid-error").getText();
        assertEquals("Not updated\nPassword must contain capital letter.", toast);

        driver.get("https://localhost:4200/user-edit");
        utils.ensureDisplayed(userEditPage.getPasswordTab()).click();

        userEditPage.getPassword().sendKeys(E2EConstants.NEW_PASSWORD_BAD_FORMAT2);
        userEditPage.getSubmitPassword().click();


        toast = utils.ensureDisplayed("password-valid-error").getText();
        assertEquals("Not updated\nPassword must contain digit.", toast);
        driver.get("https://localhost:4200/user-edit");
        utils.ensureDisplayed(userEditPage.getPasswordTab()).click();

        userEditPage.getPassword().sendKeys(E2EConstants.NEW_PASSWORD);
        userEditPage.getConfirmPassword().sendKeys(E2EConstants.NEW_PASSWORD_BAD_FORMAT0);
        userEditPage.getSubmitPassword().click();

        toast = utils.ensureDisplayed("password-error").getText();
        assertEquals("Not updated\nPasswords must be same.", toast);

        userEditPage.getPassword().clear();
    }

    @Test
    public void ChangeEmailTestFail() throws InterruptedException {
        login();
        driver.get("https://localhost:4200/user-edit");
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);

        utils.ensureDisplayed(userEditPage.getEmail()).sendKeys("");

        utils.ensureDisplayed(userEditPage.getSubmitEmail()).click();

        utils.waitFor(1000);

        String toast = utils.ensureDisplayed("email-failed").getText();

        assertEquals("Not updated\nE-mail must not be empty.", toast);


        utils.ensureDisplayed(userEditPage.getEmail()).sendKeys(EMAIL_BAD_FORMAT);

        utils.ensureDisplayed(userEditPage.getSubmitEmail()).click();

        utils.waitFor(1000);

        toast = utils.ensureDisplayed("email-valid-failed").getText();

        assertEquals("Not updated\nE-mail is not valid", toast);
    }

    @Test
    public void ChangeEmailTestSuccess() throws InterruptedException {
        if (loggedIn) {
            driver.get("https://localhost:4200/user-edit");
            userEditPage = PageFactory.initElements(driver, UserEditPage.class);

            utils.ensureDisplayed("avatar").click();

            utils.ensureDisplayed("logout-btn").click();

            assertEquals("https://localhost:4200/login", driver.getCurrentUrl());

            loggedIn = false;
        }
        userEmail = "user2@mail.com";

        login();
        driver.get("https://localhost:4200/user-edit");
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);

        utils.ensureDisplayed(userEditPage.getEmail()).sendKeys(E2EConstants.EMAIL);

        utils.ensureDisplayed(userEditPage.getSubmitEmail()).click();

        utils.waitFor(1000);

        String toast = utils.ensureDisplayed("update-successful").getText();

        assertEquals("Success\nemail updated", toast);

        userEmail = "user@mail.com";
        loggedIn = false;
    }
}
