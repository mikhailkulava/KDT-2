package com.sigmaukraine.trn.testUtils;

import com.sigmaukraine.trn.report.Log;
import com.sigmaukraine.trn.report.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

/**
 * This class implements simple web driver actions.
 * Static web-driver instance allows to interact between keywords,
 * while executing scenario
 */
public class WebDriverManager{
    private static WebDriver driver = new FirefoxDriver();

    /**
     * Set implicitly wait timeout. Can be changed from config-file.
     */
    public static void init(){
        driver.manage().timeouts().implicitlyWait(Integer.parseInt(TestConfig.CONFIG_PROPERTIES.getProperty("implicitlyWait")), TimeUnit.MILLISECONDS);
    }

    /**
     * Navigates to URL.
     * @param url = target URL.
     * Taking snapshots can be enabled(disabled) through configuration file.
     */
    public static void goToURL(String url){
        driver.get(url);
        Log.info("Opening page: " + url);
        if (TestConfig.TAKE_SNAPSHOTS){
            Log.info("Page opened successfully!", "snapshot taken", takeSnapshot());
        }
        else{
            Log.info("Page opened successfully!");
        }
    }

    public static void waitFor(int mSeconds){
        try {
            Thread.sleep(mSeconds);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static void closeWebDriver(){
        driver.close();
        driver.quit();
        Log.info("WebDriver closed!");
    }

    public static boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static Page takeSnapshot() {
            return new Page(WebDriverManager.getDriver());
    }

    public static WebDriver getDriver() {
        return driver;
    }
}
