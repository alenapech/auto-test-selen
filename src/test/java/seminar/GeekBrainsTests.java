package seminar;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeekBrainsTests {

    private WebDriver driver;
    private WebDriverWait wait;
    private static String USERNAME;
    private static String PASSWORD;

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
    }

    private void login() {
        driver.get("https://test-stand.gb.ru/login");
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("form#login input[type='text']"))).sendKeys(USERNAME);
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("form#login input[type='password']"))).sendKeys(PASSWORD);

        WebElement loginButton = driver.findElement(By.cssSelector("form#login button"));
        loginButton.click();
        wait.until(ExpectedConditions.invisibilityOf(loginButton));

        WebElement usernameLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(USERNAME)));
        assertEquals(String.format("Hello, %s", USERNAME)
                , usernameLink.getText().replace("\n", " ").trim());
    }

    @Test
    void loginTest() {
        login();
    }

    @Test
    void dummyCreationTest() {
        login();
        WebElement addButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@id='create-btn']")));
        addButton.click();

        String login = String.valueOf(System.currentTimeMillis());
        String firstName = String.format("First Name %s", login);
        String lastName = String.format("Last Name %s", login);


        wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[@id='upsert-item']//span[contains(text(), 'Fist Name')]/../input[@type='text']"))) //here we have misprint on the form
                .sendKeys(firstName);
        wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[@id='upsert-item']//span[contains(text(), 'Last Name')]/../input[@type='text']")))
                .sendKeys(lastName);
        wait.until(ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//*[@id='upsert-item']//span[contains(text(), 'Login')]/../input[@type='text']")))
                .sendKeys(login);

        WebElement loginButton = driver.findElement(By.xpath("//*[@id='upsert-item']//span[contains(text(), 'Save')]//..//..//button"));
        loginButton.click();

        WebElement createdName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath(String.format("//table[@aria-label='Dummies list']/tbody/tr/td[contains(text(), '%s')]", login))));
        assertEquals(String.format("%s %s", lastName, firstName), createdName.getText().replace("\n", " ").trim());

        saveScreenshot(login);
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
