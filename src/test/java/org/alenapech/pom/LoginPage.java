package org.alenapech.pom;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class LoginPage {


    private final SelenideElement usernameField = $("form#login input[type='text']");

    private final SelenideElement passwordField = $("form#login input[type='password']");

    private final SelenideElement loginButton = $("form#login button");

    private final SelenideElement invalidCredentialsMessage = $x("//div[contains(@class, 'error-block')]/p[1]");

    public void login(String username, String password) {
        typeUsernameInField(username);
        typePasswordInField(password);
        clickLoginButton();
    }

    public void invalidCredentialsLogin() {
        clickLoginButton();
    }

    public String getInvalidCredentialsLoginText() {
        return invalidCredentialsMessage.shouldBe(visible).getText();
    }

    public void clickLoginButton() {
        loginButton.shouldBe(visible).click();
    }

    public void typePasswordInField(String password) {
        passwordField.shouldBe(visible).sendKeys(password);
    }

    public void typeUsernameInField(String username) {
        usernameField.shouldBe(visible).sendKeys(username);
    }
}
