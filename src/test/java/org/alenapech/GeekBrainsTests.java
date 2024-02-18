package org.alenapech;

import org.alenapech.pom.LoginPage;
import org.alenapech.pom.MainPage;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeekBrainsTests {

    private WebDriver driver;
    private WebDriverWait wait;
    private static String USERNAME;
    private static String PASSWORD;
    private LoginPage loginPage;
    private MainPage mainPage;

    private static final String SCREENSHOT_PATH = "src\\test\\resources";

    @BeforeAll
    static void beforeAll() {
        System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
        USERNAME = System.getProperty("geekbrains.username", System.getenv("geekbrains.username"));;
        PASSWORD = System.getProperty("geekbrains.password", System.getenv("geekbrains.password"));
    }

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get("https://test-stand.gb.ru/login");
        loginPage = new LoginPage(driver, wait);
    }

    @Test
    public void testGeekBrainsStandInvalidCredentialsLogin() {
        loginPage.invalidCredentialsLogin();
        assertEquals("Invalid credentials.", loginPage.getInvalidCredentialsLoginText());
        saveScreenshotWithMillis("invalid-creds");
    }

    @Test
    public void testCheckDummyCredentialsOnMainPage() {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        String dummyTestLogin = "dummy" + System.currentTimeMillis();
        mainPage.createDummy(dummyTestLogin);
        mainPage.closeCreateDummyModalWindow();
        assertEquals("active", mainPage.getStatusOfDummyWithTitle(dummyTestLogin));
        mainPage.clickKeyIconOnDummyWithTitle(dummyTestLogin);
        assertEquals("Dummy credentials", mainPage.getTitleOfDummyCredentialModalWindow());
        assertTrue(mainPage.getContentOfDummyCredentialModalWindow().contains(String.format("Login: %s PW: ", dummyTestLogin)));
        saveScreenshot(dummyTestLogin);
    }

    @Test
    public void testEditingDummyOnMainPage() {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        String dummyTestLogin = "dummy" + System.currentTimeMillis();
        mainPage.createDummy(dummyTestLogin);
        mainPage.closeCreateDummyModalWindow();
        assertEquals("active", mainPage.getStatusOfDummyWithTitle(dummyTestLogin));
        String dummyId = mainPage.getIdOfDummyWithTitle(dummyTestLogin);
        mainPage.clickEditIconOnDummyWithTitle(dummyTestLogin);
        String newDummyFirstName = dummyTestLogin + "-changed";
        mainPage.editDummy(newDummyFirstName);
        assertEquals(newDummyFirstName, mainPage.getTitleOfDummyWithId(dummyId));
        saveScreenshot(dummyTestLogin);
    }

    @Test
    public void testAddingDummyOnMainPage() {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = new MainPage(driver, wait);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        String dummyTestLogin = "dummy" + System.currentTimeMillis();
        mainPage.createDummy(dummyTestLogin);
        mainPage.closeCreateDummyModalWindow();
        assertEquals("active", mainPage.getStatusOfDummyWithTitle(dummyTestLogin));
        mainPage.clickTrashIconOnDummyWithTitle(dummyTestLogin);
        assertEquals("inactive", mainPage.getStatusOfDummyWithTitle(dummyTestLogin));
        mainPage.clickRestoreFromTrashIconOnDummyWithTitle(dummyTestLogin);
        assertEquals("active", mainPage.getStatusOfDummyWithTitle(dummyTestLogin));
        saveScreenshot(dummyTestLogin);
    }

    private void saveScreenshotWithMillis(String name) {
        saveScreenshot(name + System.currentTimeMillis());
    }

    private void saveScreenshot(String name) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot, new File(SCREENSHOT_PATH + name + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
