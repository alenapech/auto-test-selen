package org.alenapech.pom;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private final WebDriverWait wait;

    @FindBy(css="form#login input[type='text']")
    private WebElement usernameField;

    @FindBy(css="form#login input[type='password']")
    private WebElement passwordField;

    @FindBy(css="form#login button")
    private WebElement loginButton;

    @FindBy(xpath = "//div[contains(@class, 'error-block')]/p[1]")
    private WebElement invalidCredentialsMessage;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        PageFactory.initElements(driver, this);
        this.wait = wait;
    }

    public void login(String username, String password) {
        typeUsernameInField(username);
        typePasswordInField(password);
        clickLoginButton();
    }

    public void invalidCredentialsLogin() {
        clickLoginButton();
    }

    public String getInvalidCredentialsLoginText() {
        return wait.until(ExpectedConditions.visibilityOf(invalidCredentialsMessage))
                .getText();
    }

    public void clickLoginButton() {
        wait.until(ExpectedConditions.visibilityOf(loginButton)).click();
    }

    public void typePasswordInField(String password) {
        wait.until(ExpectedConditions.visibilityOf(passwordField)).sendKeys(password);
    }

    public void typeUsernameInField(String username) {
        wait.until(ExpectedConditions.visibilityOf(usernameField)).sendKeys(username);
    }
}
