package com.training.salesforce;

import java.time.Duration;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Epic("Salesforce Authentication")
@Feature("Login")
public final class LoginTests {
    private WebDriver driver;
    private LoginPage loginPage;
    private boolean liveExecutionEnabled;

    @BeforeTest(alwaysRun = true)
    public void setUp() {
        liveExecutionEnabled = Boolean.parseBoolean(System.getenv("RUN_LIVE_SALESFORCE_TESTS"));
    }

    @BeforeMethod(alwaysRun = true)
    public void startBrowser() {
        if (!liveExecutionEnabled) {
            return;
        }
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--window-size=1440,1000");
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ZERO);
            loginPage = new LoginPage(driver);
            loginPage.open();
        } catch (IllegalStateException | WebDriverException exception) {
            try {
                closeDriver();
            } catch (IllegalStateException cleanupException) {
                exception.addSuppressed(cleanupException);
            }
            throw new IllegalStateException("Salesforce login test setup failed", exception);
        }
    }

    @Test
    @Story("Valid registered credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void validCredentialsShouldLeaveTheLoginPage() {
        requireLiveExecution();
        String username = requiredEnvironmentVariable("SALESFORCE_VALID_USERNAME");
        String password = requiredEnvironmentVariable("SALESFORCE_VALID_PASSWORD");
        String successUrlFragment = requiredEnvironmentVariable("SALESFORCE_EXPECTED_SUCCESS_URL_FRAGMENT");

        loginPage.login(username, password);
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(currentDriver -> currentDriver.getCurrentUrl().contains(successUrlFragment));

        if (loginPage.isUsernameFieldVisible()
                || !loginPage.currentUrl().contains(successUrlFragment)) {
            throw new AssertionError("The configured authenticated URL was not reached after valid login");
        }
    }

    @Test
    @Story("Invalid credentials")
    @Severity(SeverityLevel.NORMAL)
    public void invalidCredentialsShouldNotLeaveTheLoginPage() {
        requireLiveExecution();
        String username = requiredEnvironmentVariable("SALESFORCE_INVALID_USERNAME");
        String password = requiredEnvironmentVariable("SALESFORCE_INVALID_PASSWORD");
        String expectedErrorText = requiredEnvironmentVariable("SALESFORCE_INVALID_LOGIN_ERROR_TEXT");

        loginPage.open();
        loginPage.login(username, password);
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(currentDriver -> loginPage.isTextVisible(expectedErrorText));

        if (!loginPage.isTextVisible(expectedErrorText) || !loginPage.isUsernameFieldVisible()) {
            throw new AssertionError("The login form is no longer visible after submitting invalid credentials");
        }
    }

    @AfterMethod(alwaysRun = true)
    public void stopBrowser() {
        closeDriver();
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() {
        closeDriver();
    }

    private void requireLiveExecution() {
        if (!liveExecutionEnabled) {
            throw new SkipException("Set RUN_LIVE_SALESFORCE_TESTS=true to enable live browser tests");
        }
        if (driver == null || loginPage == null) {
            throw new SkipException("A Salesforce browser session is not available");
        }
    }

    private String requiredEnvironmentVariable(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new SkipException("Set " + name + " to enable this test");
        }
        return value;
    }

    private void closeDriver() {
        if (driver != null) {
            try {
                driver.quit();
            } catch (WebDriverException exception) {
                throw new IllegalStateException("Unable to close the browser session", exception);
            } finally {
                driver = null;
                loginPage = null;
            }
        }
    }
}
