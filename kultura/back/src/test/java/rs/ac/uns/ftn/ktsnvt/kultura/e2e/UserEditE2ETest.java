package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.UserDto;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.LoginPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.UserEditPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.VerificationPage;
import rs.ac.uns.ftn.ktsnvt.kultura.service.UserService;

import static org.junit.Assert.assertEquals;
import static rs.ac.uns.ftn.ktsnvt.kultura.e2e.E2EConstants.ADMIN_PASSWORD;

public class UserEditE2ETest {


    private static WebDriver driver;

    private static UserEditPage userEditPage;

    private static LoginPage loginPage;

    private static VerificationPage verificationPage;

    private static void justWait() throws InterruptedException {
        synchronized (driver) {
            driver.wait(1000);
        }
    }

    private static void justWait(int timeout) throws InterruptedException {
        synchronized (driver) {
            driver.wait(timeout);
        }
    }


    @BeforeClass
    public static void setUp() throws InterruptedException {

        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        driver.manage().window().maximize();
        login(E2EConstants.ADMIN_EMAIL, ADMIN_PASSWORD);

    }

    private static void login(String email, String password) throws InterruptedException {
        loginPage = PageFactory.initElements(driver, LoginPage.class);

        driver.get("http://localhost:4200/login");

        justWait(300);

        loginPage.getEmail().sendKeys(email);
        loginPage.getNextBtn().click();
        justWait(300);
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        loginPage.getPassword().sendKeys(password);
        loginPage.getLoginBtn().click();
        justWait(300);
    }

    @AfterClass
    public static void tearDown() {
        driver.quit();
    }


    private static void verify() throws InterruptedException {
        driver.get("http://localhost:4200/verify/"+E2EConstants.ADMIN_ID);
        justWait(300);
        verificationPage = PageFactory.initElements(driver, VerificationPage.class);
        verificationPage.getVerifyBtn().click();
        justWait(300);
    }

    private static void resetPassword() throws InterruptedException {
        login(E2EConstants.ADMIN_EMAIL, E2EConstants.NEW_PASSWORD);

        justWait(300);

        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage.getPasswordTab().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        userEditPage.getPassword().sendKeys(ADMIN_PASSWORD);
        userEditPage.getConfirmPassword().sendKeys(ADMIN_PASSWORD);
        userEditPage.getSubmitPassword().click();
        justWait();
    }

    private static void resetEmail() throws InterruptedException {
        verify();

        justWait(300);
        login(E2EConstants.EMAIL, ADMIN_PASSWORD);

        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        userEditPage.getEmail().sendKeys(E2EConstants.ADMIN_EMAIL);
        userEditPage.getSubmitEmail().click();
        ensureIsDisplayedToast();
        verify();
        login(E2EConstants.ADMIN_EMAIL, ADMIN_PASSWORD);

    }

    public static String ensureIsDisplayedToast() {
        return (new WebDriverWait(driver, 30))
                .until(ExpectedConditions.presenceOfElementLocated(By.className("p-toast-detail")))
                .getText();
    }

    @Test
    public void ChangeNameTestSuccess() throws InterruptedException {
        justWait(1000);

        driver.get("http://localhost:4200/user-edit");
        justWait(300);
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait(300);
        userEditPage.getName().sendKeys(E2EConstants.NAME);
        justWait(300);
        userEditPage.getSubmitName().click();
        justWait(300);
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait(300);
        assertEquals(E2EConstants.NAME, userEditPage.getNameVal().getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
        assertEquals("First name updated", driver.findElement(By.className("p-toast-detail")).getText());
    }

    @Test
    public void ChangeNameTestFail() throws InterruptedException {
        justWait(1000);

        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        userEditPage.getName().sendKeys("");
        userEditPage.getSubmitName().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        assertEquals("First name must not be empty.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
    }

    @Test
    public void ChangeLastNameTestSuccess() throws InterruptedException {
        justWait(1000);

        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        userEditPage.getLastName().sendKeys(E2EConstants.LASTNAME);
        userEditPage.getSubmitLastName().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        assertEquals(E2EConstants.LASTNAME, userEditPage.getLastNameVal().getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
        assertEquals("Last name updated", driver.findElement(By.className("p-toast-detail")).getText());

    }

    @Test
    public void ChangeLastNameTestFail() throws InterruptedException {
        justWait(1000);

        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        userEditPage.getLastName().sendKeys("");
        userEditPage.getSubmitLastName().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        assertEquals("Last name must not be empty.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
    }

    @Test
    public void ChangePasswordTestSuccess() throws InterruptedException {
        justWait(1000);

        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage.getPasswordTab().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        userEditPage.getPassword().sendKeys(E2EConstants.NEW_PASSWORD);
        userEditPage.getConfirmPassword().sendKeys(E2EConstants.NEW_PASSWORD);
        userEditPage.getSubmitPassword().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        assertEquals("http://localhost:4200/login", driver.getCurrentUrl());

        resetPassword();
    }

    @Test
    public void ChangePasswordTestFail() throws InterruptedException {
        UserDto userDto = new UserDto();
        userDto.setId(E2EConstants.ADMIN_ID);
        userDto.setPassword(ADMIN_PASSWORD);
        justWait(1000);

        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage.getPasswordTab().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        userEditPage.getPassword().sendKeys("");
        userEditPage.getSubmitPassword().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);


        assertEquals("New password must not be empty.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());



        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        userEditPage.getPassword().sendKeys(E2EConstants.NEW_PASSWORD_BAD_FORMAT0);
        userEditPage.getSubmitPassword().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);

        assertEquals("New password length must be between 8 and 50 characters.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
        userEditPage.getPassword().clear();



        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        userEditPage.getPassword().sendKeys(E2EConstants.NEW_PASSWORD_BAD_FORMAT0_1);
        userEditPage.getSubmitPassword().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);

        assertEquals("New password length must be between 8 and 50 characters.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
        userEditPage.getPassword().clear();



        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        userEditPage.getPassword().sendKeys(E2EConstants.NEW_PASSWORD_BAD_FORMAT1);
        userEditPage.getSubmitPassword().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);


        assertEquals("Password must contain capital letter.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
        userEditPage.getPassword().clear();



        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        userEditPage.getPassword().sendKeys(E2EConstants.NEW_PASSWORD_BAD_FORMAT2);
        userEditPage.getSubmitPassword().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);


        assertEquals("Password must contain digit.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
        userEditPage.getPassword().clear();



        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        userEditPage.getPassword().sendKeys(E2EConstants.NEW_PASSWORD);
        userEditPage.getConfirmPassword().sendKeys(E2EConstants.NEW_PASSWORD_BAD_FORMAT0);
        userEditPage.getSubmitPassword().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);


        assertEquals("Passwords must be same.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
        userEditPage.getPassword().clear();


    }

    @Test
    public void ChangeEmailTestFail() throws InterruptedException {
        UserDto userDto = new UserDto();
        userDto.setId(E2EConstants.ADMIN_ID);
        userDto.setEmail(E2EConstants.ADMIN_EMAIL);
        justWait(1000);

        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        userEditPage.getEmail().clear();
        userEditPage.getEmail().sendKeys("");
        justWait();
        userEditPage.getSubmitEmail().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();

        assertEquals("E-mail must not be empty.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());


        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        userEditPage.getEmail().sendKeys(E2EConstants.EMAIL_BAD_FORMAT);
        userEditPage.getSubmitEmail().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();


        assertEquals("E-mail is not valid.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());

    }

    @Test
    public void ChangeEmailTestSuccess() throws InterruptedException {
        justWait(1000);


        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        userEditPage.getEmail().sendKeys(E2EConstants.EMAIL);
        userEditPage.getSubmitEmail().click();
        ensureIsDisplayedToast();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();

        assertEquals("http://localhost:4200/login", driver.getCurrentUrl());

        resetEmail();


    }


}
