package org.alenapech.pom.elements;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.SelenideWait;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.NoSuchElementException;

import static com.codeborne.selenide.Condition.visible;

public class DummyTableRow {

    private final SelenideElement root;

    public DummyTableRow(SelenideElement root) {
        this.root = root;
    }

    public String getId() {
        return root.$x("./td[1]").shouldBe(visible).getText();
    }

    public String getTitle() {
        return root.$x("./td[2]").shouldBe(visible).getText();
    }

    public String getStatus() {
        return root.$x("./td[4]").shouldBe(visible).getText();
    }

    public void clickTrashIcon() {
        root.$x("./td/button[text()='delete']").shouldBe(visible).click();
        (new SelenideWait(WebDriverRunner.getWebDriver(),  30, 5))
                .ignoring(NoSuchElementException.class)
                .until(webDriver -> root.$x("./td/button[text()='restore_from_trash']").shouldBe(visible));
    }

    public void clickRestoreFromTrashIcon() {
        root.$x("./td/button[text()='restore_from_trash']").shouldBe(visible).click();
        (new SelenideWait(WebDriverRunner.getWebDriver(),  30, 5))
                .ignoring(NoSuchElementException.class)
                .until(webDriver -> root.$x("./td/button[text()='delete']").shouldBe(visible));
    }

    public void clickEditButton() {
        root.$x("./td/button[text()='edit']").shouldBe(visible).click();
    }

    public void clickKeyButton() {
        root.$x("./td/button[text()='key']").shouldBe(visible).click();
    }
}
