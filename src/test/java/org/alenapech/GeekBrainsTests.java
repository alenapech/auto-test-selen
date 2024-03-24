package org.alenapech;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.alenapech.pom.LoginPage;
import org.alenapech.pom.MainPage;
import org.alenapech.pom.ProfilePage;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

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
        Configuration.remote = "http://localhost:4444/wd/hub";
        Configuration.browser = "chrome";
        Configuration.browserVersion = "122";
        Map<String, Object> options = new HashMap<>();
        options.put("enableVNC", true);
        options.put("enableLog", true);
        Configuration.browserCapabilities.setCapability("selenoid:options", options);
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
        assertEquals("GB202305 ee7820", profilePage.getFullNameFromAvatarSection());
        saveScreenshotWithMillis("full-name-on-profile-page");
    }

    @Test
    public void testAvatarOnEditingPopupOnProfilePage() {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = Selenide.page(MainPage.class);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        mainPage.clickUsernameLabel();
        mainPage.clickProfileLink();
        ProfilePage profilePage = Selenide.page(ProfilePage.class);
        profilePage.clickEditIconInAvatarSection();
        assertEquals("", profilePage.getAvatarInputValueOnSettingPopup());
        String filePath = "src\\test\\resources\\avatar.png";
        profilePage.uploadPictureFileToAvatarField(filePath);
        assertEquals(filePath.substring(filePath.lastIndexOf("\\") + 1),
                profilePage.getAvatarInputValueOnSettingPopup());
        saveScreenshotWithMillis("test-avatar-on-profile-page");
    }

    @Test
    public void testDateOfBirthOnProfilePage() {
        loginPage.login(USERNAME, PASSWORD);
        mainPage = Selenide.page(MainPage.class);
        assertTrue(mainPage.getUsernameLabelText().contains(USERNAME));
        mainPage.clickUsernameLabel();
        mainPage.clickProfileLink();
        ProfilePage profilePage = Selenide.page(ProfilePage.class);
        profilePage.clickEditIconInAvatarSection();
        LocalDate birthday = LocalDate.parse("2007-12-11");
        String phone = "11111111111";
        profilePage.editBirthdate(birthday);
        profilePage.editPhone(phone); // phone is mandatory, so it should be filled to store birthdate
        profilePage.submitProfileChanges();
        assertEquals(birthday.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), profilePage.getBirthdateOnAdditionalInfo());
        assertEquals(phone, profilePage.getPhoneOnAdditionalInfo());
        saveScreenshotWithMillis("test-date-of-birth-on-profile-page");
    }

    private void saveScreenshotWithMillis(String name) {
        saveScreenshot(name + System.currentTimeMillis());
    }

    private void saveScreenshot(String name) {
        Selenide.screenshot(SCREENSHOT_PATH + name);
    }

    @AfterEach
    void tearDown() {
        WebDriverRunner.closeWebDriver();
    }
}
