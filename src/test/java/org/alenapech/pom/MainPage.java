package org.alenapech.pom;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.alenapech.pom.elements.DummyTableRow;
import org.openqa.selenium.support.FindBy;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {

    @FindBy(css = "nav li.mdc-menu-surface--anchor a")
    private SelenideElement usernameLinkNavBar;

    @FindBy(id = "create-btn")
    private SelenideElement createDummyButton;

    @FindBy(xpath = "//form//span[contains(text(), 'Login')]/following-sibling::input")
    private SelenideElement dummyLoginField;

    private final SelenideElement dummyFirstNameField = $x("//form//span[contains(text(), 'Fist Name')]/following-sibling::input");

    private final SelenideElement submitButtonOnModalWindow = $("form div.submit button");

    private final SelenideElement closeCreateDummyIcon = $x("//span[text()='Creating Dummy']" +
            "//ancestor::div[contains(@class, 'form-modal-header')]//button");

    private final ElementsCollection rowsInDummyTable = $$x("//table[@aria-label='Dummies list']/tbody/tr");

    private final SelenideElement dummyCredentialsModalWindowTitle = $x("//div[@class='mdc-dialog__surface' and @role='alertdialog']//h2[@id='simple-title']");

    private final SelenideElement dummyCredentialsModalWindowContent = $x("//div[@class='mdc-dialog__content']");

    private final SelenideElement profileLinkNavBar = $("nav li.mdc-menu-surface--anchor div li");

    public SelenideElement waitAndGetDummyTitleByText(String title) {
        return $x(String.format("//table[@aria-label='Dummies list']/tbody//td[text()='%s']", title)).shouldBe(visible);
    }

    public void createDummy(String dummyLogin) {
        createDummyButton.shouldBe(visible).click();
        dummyLoginField.shouldBe(visible).setValue(dummyLogin);
        dummyFirstNameField.shouldBe(visible).setValue(dummyLogin);
        submitButtonOnModalWindow.shouldBe(visible).click();
        waitAndGetDummyTitleByText(dummyLogin);
    }

    public void editDummy(String newDummyFirstName) {
        Selenide.sleep(500);
        dummyFirstNameField.shouldBe(visible).setValue(newDummyFirstName);
        Selenide.sleep(500);
        submitButtonOnModalWindow.shouldBe(visible).click();
        waitAndGetDummyTitleByText(newDummyFirstName);
    }

    public void closeCreateDummyModalWindow() {
        closeCreateDummyIcon.shouldBe(visible).click();
        closeCreateDummyIcon.shouldNotBe(visible);
    }

    public String getUsernameLabelText() {
        return usernameLinkNavBar.shouldBe(visible).getText().replace("\n", " ");
    }

    public void clickTrashIconOnDummyWithTitle(String title) {
        getRowByTitle(title).clickTrashIcon();
    }

    public void clickRestoreFromTrashIconOnDummyWithTitle(String title) {
        getRowByTitle(title).clickRestoreFromTrashIcon();
    }

    public String getStatusOfDummyWithTitle(String title) {
        return getRowByTitle(title).getStatus();
    }

    private DummyTableRow getRowByTitle(String title) {
        return rowsInDummyTable.shouldHave(sizeGreaterThan(0))
                .asFixedIterable()
                .stream()
                .map(DummyTableRow::new)
                .filter(row -> title.equals(row.getTitle()))
                .findFirst().orElseThrow();
    }

    private DummyTableRow getRowById(String id) {
        return rowsInDummyTable.shouldHave(sizeGreaterThan(0))
                .asFixedIterable()
                .stream()
                .map(DummyTableRow::new)
                .filter(row -> id.equals(row.getId()))
                .findFirst().orElseThrow();
    }

    public String getIdOfDummyWithTitle(String title) {
        return getRowByTitle(title).getId();
    }

    public void clickEditIconOnDummyWithTitle(String title) {
        getRowByTitle(title).clickEditButton();
    }

    public String getTitleOfDummyWithId(String id) {
        return getRowById(id).getTitle();
    }

    public void clickKeyIconOnDummyWithTitle(String title) {
        getRowByTitle(title).clickKeyButton();
    }

    public String getTitleOfDummyCredentialModalWindow() {
        return dummyCredentialsModalWindowTitle.shouldBe(visible).getText();
    }

    public String getContentOfDummyCredentialModalWindow() {
        return dummyCredentialsModalWindowContent.shouldBe(visible).getText().replace("\n", " ");
    }

    public void clickUsernameLabel() {
        usernameLinkNavBar.shouldBe(visible).click();
    }

    public void clickProfileLink() {
        profileLinkNavBar.shouldBe(visible).click();
    }
}
