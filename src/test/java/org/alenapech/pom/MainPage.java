package org.alenapech.pom;

import org.alenapech.pom.elements.DummyTableRow;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class MainPage {

    private final WebDriverWait wait;

    @FindBy(css = "nav li.mdc-menu-surface--anchor a")
    private WebElement usernameLinkNavBar;

    @FindBy(id = "create-btn")
    private WebElement createDummyButton;

    @FindBy(xpath = "//form//span[contains(text(), 'Login')]/following-sibling::input")
    private WebElement dummyLoginField;

    @FindBy(xpath = "//form//span[contains(text(), 'Fist Name')]/following-sibling::input")
    private WebElement dummyFirstNameField;

    @FindBy(css = "form div.submit button")
    private WebElement submitButtonOnModalWindow;

    @FindBy(xpath = "//span[text()='Creating Dummy']" +
            "//ancestor::div[contains(@class, 'form-modal-header')]//button")
    private WebElement closeCreateDummyIcon;

    @FindBy(xpath = "//table[@aria-label='Dummies list']/tbody/tr")
    private List<WebElement> rowsInDummyTable;

    @FindBy(xpath = "//div[@class='mdc-dialog__surface' and @role='alertdialog']//h2[@id='simple-title']")
    private WebElement dummyCredentialsModalWindowTitle;

    @FindBy(xpath = "//div[@class='mdc-dialog__content']")
    private WebElement dummyCredentialsModalWindowContent;

    public MainPage(WebDriver driver, WebDriverWait wait) {
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public WebElement waitAndGetDummyTitleByText(String title) {
        String xpath = String.format("//table[@aria-label='Dummies list']/tbody//td[text()='%s']", title);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    public void createDummy(String dummyLogin) {
        wait.until(ExpectedConditions.visibilityOf(createDummyButton)).click();
        wait.until(ExpectedConditions.visibilityOf(dummyLoginField)).sendKeys(dummyLogin);
        wait.until(ExpectedConditions.visibilityOf(dummyFirstNameField)).sendKeys(dummyLogin);
        submitButtonOnModalWindow.click();
        waitAndGetDummyTitleByText(dummyLogin);
    }

    public void editDummy(String newDummyFirstName) {
        wait.until(ExpectedConditions.visibilityOf(dummyFirstNameField)).sendKeys(Keys.chord(Keys.CONTROL, "a"), newDummyFirstName);
        submitButtonOnModalWindow.click();
        waitAndGetDummyTitleByText(newDummyFirstName);
    }

    public void closeCreateDummyModalWindow() {
        closeCreateDummyIcon.click();
        wait.until(ExpectedConditions.invisibilityOf(closeCreateDummyIcon));
    }

    public String getUsernameLabelText() {
        return wait.until(ExpectedConditions.visibilityOf(usernameLinkNavBar))
                .getText().replace("\n", " ");
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
        return rowsInDummyTable.stream()
                .map(DummyTableRow::new)
                .filter(row -> title.equals(row.getTitle()))
                .findFirst().orElseThrow();
    }

    private DummyTableRow getRowById(String id) {
        return rowsInDummyTable.stream()
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
        return wait.until(ExpectedConditions.visibilityOf(dummyCredentialsModalWindowTitle)).getText();
    }

    public String getContentOfDummyCredentialModalWindow() {
        return wait.until(ExpectedConditions.visibilityOf(dummyCredentialsModalWindowContent))
                .getText().replace("\n", " ");
    }
}
