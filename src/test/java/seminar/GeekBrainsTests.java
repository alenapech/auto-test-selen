package seminar;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeekBrainsTests {

    private WebDriver driver;
    private WebDriverWait wait;
    private static String USERNAME;
    private static String PASSWORD;

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

    @Test
    void loginTest() {
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

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
