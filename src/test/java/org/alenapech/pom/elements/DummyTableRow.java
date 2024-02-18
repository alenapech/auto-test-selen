package org.alenapech.pom.elements;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

public class DummyTableRow {

    private final WebElement root;

    public DummyTableRow(WebElement root) {
        this.root = root;
    }

    public String getId() {
        return root.findElement(By.xpath("./td[1]")).getText();
    }

    public String getTitle() {
        return root.findElement(By.xpath("./td[2]")).getText();
    }

    public String getStatus() {
        return root.findElement(By.xpath("./td[4]")).getText();
    }

    public void clickTrashIcon() {
        root.findElement(By.xpath("./td/button[text()='delete']")).click();
        (new FluentWait<>(root))
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class)
                .until(root -> root.findElement(By.xpath("./td/button[text()='restore_from_trash']")));
    }

    public void clickRestoreFromTrashIcon() {
        root.findElement(By.xpath("./td/button[text()='restore_from_trash']")).click();
        (new FluentWait<>(root))
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(NoSuchElementException.class)
                .until(root -> root.findElement(By.xpath("./td/button[text()='delete']")));
    }

    public void clickEditButton() {
        root.findElement(By.xpath("./td/button[text()='edit']")).click();
    }

    public void clickKeyButton() {
        root.findElement(By.xpath("./td/button[text()='key']")).click();
    }
}
