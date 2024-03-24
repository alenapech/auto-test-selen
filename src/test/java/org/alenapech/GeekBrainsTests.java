package org.alenapech;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.alenapech.pom.LoginPage;
import org.alenapech.pom.MainPage;
import org.alenapech.pom.ProfilePage;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeekBrainsTests {

    private static String USERNAME;
    private static String PASSWORD;
    private LoginPage loginPage;
    private MainPage mainPage;

    private static final String SCREENSHOT_PATH = "src\\test\\resources";

    @BeforeAll
    static void beforeAll() {
        USERNAME = System.getProperty("geekbrains.username", System.getenv("geekbrains.username"));;
        PASSWORD = System.getProperty("geekbrains.password", System.getenv("geekbrains.password"));
    }

    @BeforeEach
    void setUp() {
        Selenide.open("https://test-stand.gb.ru/login");
        loginPage = Selenide.page(LoginPage.class);
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
        mainPage = Selenide.page(MainPage.class);
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
        mainPage = Selenide.page(MainPage.class);
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
        mainPage = Selenide.page(MainPage.class);
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

    @Test
    public void testFullNameOnProfilePage() {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = Selenide.page(MainPage.class);
        mainPage.clickUsernameLabel();
        mainPage.clickProfileLink();
        ProfilePage profilePage = Selenide.page(ProfilePage.class);
        assertEquals("GB202305 ee7820", profilePage.getFullNameFromAdditionalInfo());
        assertEquals("GB202305 ee7820", profilePage.getFullNameFromAdditionalInfo());
        saveScreenshotWithMillis("full-name-on-profile-page");
    }

    private void saveScreenshotWithMillis(String name) {
        saveScreenshot(name + System.currentTimeMillis());
    }

    private void saveScreenshot(String name) {
        Selenide.screenshot(SCREENSHOT_PATH + name + ".png");
    }

    @AfterEach
    void tearDown() {
        WebDriverRunner.closeWebDriver();
    }
}
