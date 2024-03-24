package org.alenapech.pom;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.SetValueOptions;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.SetValueOptions.withDate;

public class ProfilePage {

    @FindBy(xpath = "//h3/following-sibling::div" +
            "//div[contains(text(), 'Full name')]/following-sibling::div")
    private SelenideElement fullNameInAdditionalInfo;

    @FindBy(css = "div.mdc-card h2")
    private SelenideElement fullNameInAvatarSection;

    @FindBy(css = "div.mdc-card div.mdc-card__action-icons button[title='More options']")
    private SelenideElement editIconInAvatarSection;

    @FindBy(xpath = "//form[@id='update-item']//span[contains(text(), 'New Avatar')]/following-sibling::input")
    private SelenideElement newAvatarFileOnEditingPopup;

    @FindBy(xpath = "//form[@id='update-item']//span[contains(text(), 'Birthdate')]/..//input[@type='date']")
    private SelenideElement birthdateOnEditingPopup;

    @FindBy(xpath = "//form[@id='update-item']//span[contains(text(), 'Phone')]/following-sibling::input")
    private SelenideElement phoneOnEditingPopup;

    @FindBy(xpath = "//div[text()='Date of birth']/../div[contains(@class, 'content')]")
    private SelenideElement birthdateOnAdditionalInfo;

    @FindBy(xpath = "//div[text()='Phone']/../div[contains(@class, 'content')]")
    private SelenideElement phoneOnAdditionalInfo;

    private final SelenideElement submitButtonOnModalWindow = $("form div.submit button");

    public String getBirthdateOnAdditionalInfo() {
        return birthdateOnAdditionalInfo.shouldBe(visible).getText();
    }

    public String getPhoneOnAdditionalInfo() {
        return phoneOnAdditionalInfo.shouldBe(visible).getText();
    }

    public void clickEditIconInAvatarSection() {
        editIconInAvatarSection.shouldBe(visible).click();
    }

    public void editBirthdate(LocalDate value) {
        submitButtonOnModalWindow.shouldBe(visible);
        birthdateOnEditingPopup.shouldBe(visible).setValue(withDate(value));
    }

    public void editPhone(String value) {
        submitButtonOnModalWindow.shouldBe(visible);
        phoneOnEditingPopup.shouldBe(visible).setValue(value);
    }

    public void submitProfileChanges() {
        submitButtonOnModalWindow.shouldBe(visible).click();
    }

    public void uploadPictureFileToAvatarField(String filePath) {
        newAvatarFileOnEditingPopup.shouldBe(visible).uploadFile(new File(filePath));
    }

    public String getAvatarInputValueOnSettingPopup() {
        String inputValue = newAvatarFileOnEditingPopup.getValue();
        return Objects.requireNonNull(inputValue)
                .substring(inputValue.lastIndexOf("\\") + 1);
    }

    public String getFullNameFromAdditionalInfo() {
        return fullNameInAdditionalInfo.shouldBe(visible).text();
    }

    public String getFullNameFromAvatarSection() {
        return fullNameInAvatarSection.shouldBe(visible).text();
    }

}
