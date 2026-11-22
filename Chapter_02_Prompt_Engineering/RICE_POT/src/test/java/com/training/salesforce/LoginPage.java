package com.training.salesforce;

import java.time.Duration;
import java.util.Objects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class LoginPage {
    private static final String LOGIN_URL = "https://login.salesforce.com/?locale=in";
    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(15);

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(xpath = "//input[@id='username']")
    private WebElement username;

    @FindBy(xpath = "//input[@id='password']")
    private WebElement password;

    @FindBy(xpath = "//input[@id='Login']")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        this.driver = Objects.requireNonNull(driver, "WebDriver must not be null");
        this.wait = new WebDriverWait(driver, WAIT_TIMEOUT);
        PageFactory.initElements(driver, this);
    }

    @Step("Open the Salesforce login page")
    public void open() {
        try {
            driver.get(LOGIN_URL);
            wait.until(ExpectedConditions.visibilityOf(username));
            wait.until(ExpectedConditions.visibilityOf(password));
            wait.until(ExpectedConditions.elementToBeClickable(loginButton));
        } catch (WebDriverException exception) {
            throw new IllegalStateException("Unable to load the Salesforce login form", exception);
        }
    }

    @Step("Submit login using the provided credentials")
    public void login(String user, String secret) {
        Objects.requireNonNull(user, "Username must not be null");
        Objects.requireNonNull(secret, "Password must not be null");
        try {
            wait.until(ExpectedConditions.visibilityOf(username)).clear();
            username.sendKeys(user);
            wait.until(ExpectedConditions.visibilityOf(password)).clear();
            password.sendKeys(secret);
            wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        } catch (WebDriverException exception) {
            throw new IllegalStateException("Unable to submit the Salesforce login form", exception);
        }
    }

    @Step("Check whether the username field is visible")
    public boolean isUsernameFieldVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(username)).isDisplayed();
        } catch (TimeoutException | StaleElementReferenceException exception) {
            return false;
        } catch (WebDriverException exception) {
            throw new IllegalStateException("Unable to determine login form visibility", exception);
        }
    }

    @Step("Read the current page URL")
    public String currentUrl() {
        try {
            return driver.getCurrentUrl();
        } catch (WebDriverException exception) {
            throw new IllegalStateException("Unable to read the current browser URL", exception);
        }
    }

    @Step("Check whether a configured message is visible")
    public boolean isTextVisible(String expectedText) {
        Objects.requireNonNull(expectedText, "Expected text must not be null");
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[normalize-space(.)=" + toXPathLiteral(expectedText) + "]"))).isDisplayed();
        } catch (TimeoutException | StaleElementReferenceException exception) {
            return false;
        } catch (WebDriverException exception) {
            throw new IllegalStateException("Unable to verify the configured page message", exception);
        }
    }

    private String toXPathLiteral(String value) {
        if (!value.contains("'")) {
            return "'" + value + "'";
        }
        if (!value.contains("\"")) {
            return "\"" + value + "\"";
        }
        String[] parts = value.split("'", -1);
        StringBuilder expression = new StringBuilder("concat(");
        for (int index = 0; index < parts.length; index++) {
            if (index > 0) {
                expression.append(", \"'\", ");
            }
            expression.append("'").append(parts[index]).append("'");
        }
        return expression.append(")").toString();
    }
}
