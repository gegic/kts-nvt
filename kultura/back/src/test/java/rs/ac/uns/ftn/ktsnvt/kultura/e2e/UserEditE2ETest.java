package rs.ac.uns.ftn.ktsnvt.kultura.e2e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rs.ac.uns.ftn.ktsnvt.kultura.dto.UserDto;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.LoginPage;
import rs.ac.uns.ftn.ktsnvt.kultura.pages.UserEditPage;
import rs.ac.uns.ftn.ktsnvt.kultura.service.AuthorityService;
import rs.ac.uns.ftn.ktsnvt.kultura.service.UserService;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserEditE2ETest {


    @Autowired
    UserService userService;


    private WebDriver driver;

    private UserEditPage userEditPage;

    private LoginPage loginPage;

    @Before
    public void setUp() {

        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();

        driver.manage().window().maximize();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        loginPage = PageFactory.initElements(driver, LoginPage.class);
    }

    @After
    public void tearDown() {
        driver.quit();
    }


    @Test
    public void ChangeNameTestSuccess() throws InterruptedException {

        driver.get("http://localhost:4200/login");

        justWait();

        loginPage.getEmail().sendKeys(E2EConstants.ADMIN_EMAIL);
        loginPage.getNextBtn().click();
        justWait();
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        loginPage.getPassword().sendKeys(E2EConstants.ADMIN_PASSWORD);
        loginPage.getLoginBtn().click();
        justWait();

        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        userEditPage.getName().sendKeys(E2EConstants.NAME);
        userEditPage.getSubmitName().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        assertEquals(E2EConstants.NAME, userEditPage.getNameVal().getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
    }

    @Test
    public void ChangeNameTestFail() throws InterruptedException {

        driver.get("http://localhost:4200/login");

        justWait();

        loginPage.getEmail().sendKeys(E2EConstants.ADMIN_EMAIL);
        loginPage.getNextBtn().click();
        justWait();
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        loginPage.getPassword().sendKeys(E2EConstants.ADMIN_PASSWORD);
        loginPage.getLoginBtn().click();
        justWait();

        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        userEditPage.getName().sendKeys("");
        userEditPage.getSubmitName().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        assertEquals("First name must not be empty.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
    }

    @Test
    public void ChangeLastNameTestSuccess() throws InterruptedException {

        driver.get("http://localhost:4200/login");

        justWait();

        loginPage.getEmail().sendKeys(E2EConstants.ADMIN_EMAIL);
        loginPage.getNextBtn().click();
        justWait();
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        loginPage.getPassword().sendKeys(E2EConstants.ADMIN_PASSWORD);
        loginPage.getLoginBtn().click();
        justWait();

        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        userEditPage.getLastName().sendKeys(E2EConstants.LASTNAME);
        userEditPage.getSubmitLastName().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        assertEquals(E2EConstants.LASTNAME, userEditPage.getLastNameVal().getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
    }

    @Test
    public void ChangeLastNameTestFail() throws InterruptedException {

        driver.get("http://localhost:4200/login");

        justWait();

        loginPage.getEmail().sendKeys(E2EConstants.ADMIN_EMAIL);
        loginPage.getNextBtn().click();
        justWait();
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        loginPage.getPassword().sendKeys(E2EConstants.ADMIN_PASSWORD);
        loginPage.getLoginBtn().click();
        justWait();

        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        userEditPage.getLastName().sendKeys("");
        userEditPage.getSubmitLastName().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        assertEquals("Last name must not be empty.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
    }

    @Test
    public void ChangePasswordTestSuccess() throws InterruptedException {
        // TODO zavrsiti test
        driver.get("http://localhost:4200/login");

        justWait();

        loginPage.getEmail().sendKeys(E2EConstants.ADMIN_EMAIL);
        loginPage.getNextBtn().click();
        justWait();
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        loginPage.getPassword().sendKeys(E2EConstants.ADMIN_PASSWORD);
        loginPage.getLoginBtn().click();
        justWait();

        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage.getPasswordTab().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        userEditPage.getPassword().sendKeys(E2EConstants.NEW_PASSWORD);
        userEditPage.getConfirmPassword().sendKeys(E2EConstants.NEW_PASSWORD);
        userEditPage.getSubmitPassword().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        assertEquals("http://localhost:4200/login", driver.getCurrentUrl());


        UserDto userDto = new UserDto();
        userDto.setId(E2EConstants.ADMIN_ID);
        userDto.setPassword(E2EConstants.ADMIN_PASSWORD);
        userService.update(userDto);
    }

    @Test
    public void ChangePasswordTestFail() throws InterruptedException {
        UserDto userDto = new UserDto();
        userDto.setId(E2EConstants.ADMIN_ID);
        userDto.setPassword(E2EConstants.ADMIN_PASSWORD);


        driver.get("http://localhost:4200/login");

        justWait();

        loginPage.getEmail().sendKeys(E2EConstants.ADMIN_EMAIL);
        loginPage.getNextBtn().click();
        justWait();
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        loginPage.getPassword().sendKeys(E2EConstants.ADMIN_PASSWORD);
        loginPage.getLoginBtn().click();
        justWait();

        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage.getPasswordTab().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        userEditPage.getPassword().sendKeys("");
        userEditPage.getSubmitPassword().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        assertEquals("New password must not be empty.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());

        userService.update(userDto);


        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        userEditPage.getPassword().sendKeys(E2EConstants.NEW_PASSWORD_BAD_FORMAT0);
        userEditPage.getSubmitPassword().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        assertEquals("New password length must be between 8 and 50 characters.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
        userEditPage.getPassword().clear();

        userService.update(userDto);


        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        userEditPage.getPassword().sendKeys(E2EConstants.NEW_PASSWORD_BAD_FORMAT0_1);
        userEditPage.getSubmitPassword().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        assertEquals("New password length must be between 8 and 50 characters.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
        userEditPage.getPassword().clear();

        userService.update(userDto);


        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        userEditPage.getPassword().sendKeys(E2EConstants.NEW_PASSWORD_BAD_FORMAT1);
        userEditPage.getSubmitPassword().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        assertEquals("Password must contain capital letter.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
        userEditPage.getPassword().clear();

        userService.update(userDto);


        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        userEditPage.getPassword().sendKeys(E2EConstants.NEW_PASSWORD_BAD_FORMAT2);
        userEditPage.getSubmitPassword().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        assertEquals("Password must contain digit.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
        userEditPage.getPassword().clear();


        userService.update(userDto);

        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        userEditPage.getPassword().sendKeys(E2EConstants.NEW_PASSWORD);
        userEditPage.getConfirmPassword().sendKeys(E2EConstants.NEW_PASSWORD_BAD_FORMAT0);
        userEditPage.getSubmitPassword().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        assertEquals("Passwords must be same.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());
        userEditPage.getPassword().clear();


        userService.update(userDto);
    }

    @Test
    public void ChangeEmailTestFail() throws InterruptedException {
        UserDto userDto = new UserDto();
        userDto.setId(E2EConstants.ADMIN_ID);
        userDto.setEmail(E2EConstants.ADMIN_EMAIL);

        driver.get("http://localhost:4200/login");

        justWait();

        loginPage.getEmail().sendKeys(E2EConstants.ADMIN_EMAIL);
        loginPage.getNextBtn().click();
        justWait();
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        loginPage.getPassword().sendKeys(E2EConstants.ADMIN_PASSWORD);
        loginPage.getLoginBtn().click();
        justWait();

        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        userEditPage.getEmail().clear();
        userEditPage.getEmail().sendKeys("");
        justWait();
        userEditPage.getSubmitEmail().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        assertEquals("E-mail must not be empty.", driver.findElement(By.className("p-toast-detail")).getText());
        justWait();
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());

        userService.update(userDto);
        userService.verify(E2EConstants.ADMIN_ID);


        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        userEditPage.getEmail().sendKeys(E2EConstants.EMAIL_BAD_FORMAT);
        userEditPage.getSubmitEmail().click();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        assertEquals("E-mail is not valid.", driver.findElement(By.className("p-toast-detail")).getText());
        assertEquals("http://localhost:4200/user-edit", driver.getCurrentUrl());

        userService.update(userDto);
        userService.verify(E2EConstants.ADMIN_ID);
    }

    @Test
    public void ChangeEmailTestSuccess() throws InterruptedException {
        UserDto userDto = new UserDto();
        userDto.setId(E2EConstants.ADMIN_ID);
        userDto.setEmail(E2EConstants.ADMIN_EMAIL);

        driver.get("http://localhost:4200/login");

        justWait();

        loginPage.getEmail().sendKeys(E2EConstants.ADMIN_EMAIL);
        loginPage.getNextBtn().click();
        justWait();
        loginPage = PageFactory.initElements(driver, LoginPage.class);
        loginPage.getPassword().sendKeys(E2EConstants.ADMIN_PASSWORD);
        loginPage.getLoginBtn().click();
        justWait();

        driver.get("http://localhost:4200/user-edit");
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        userEditPage.getEmail().sendKeys(E2EConstants.EMAIL);
        userEditPage.getSubmitEmail().click();
        justWait();
        justWait();
        userEditPage = PageFactory.initElements(driver, UserEditPage.class);
        justWait();
        assertEquals("http://localhost:4200/login", driver.getCurrentUrl());

        userService.update(userDto);
        userService.verify(E2EConstants.ADMIN_ID);
    }


    private void justWait() throws InterruptedException {
        synchronized (driver) {
            driver.wait(1000);
        }
    }
}
