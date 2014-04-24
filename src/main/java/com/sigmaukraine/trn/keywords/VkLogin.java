package com.sigmaukraine.trn.keywords;

import com.sigmaukraine.trn.report.Log;
import com.sigmaukraine.trn.testUtils.TestConfig;
import com.sigmaukraine.trn.testUtils.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import java.util.Map;

/**
 * This keywrod navigates to VK base URL and performs login procedure
 */
public class VkLogin {
    final static WebDriver driver = WebDriverManager.getDriver();
    /**
     * Keyword execution method
     */
    public static void execute(Map<String, String> parametersAndValues){
        //get BASE_URL from property file
        final String BASE_URL = TestConfig.VK_PROPERTIES.getProperty("vkBaseURL");
        //initialize implicitly wait timeouts
        WebDriverManager.init();
        //navigate to URL
        driver.get(BASE_URL);
        Log.info("Logging in");
        //login procedure
        driver.findElement(By.xpath(TestConfig.VK_XPATH.getProperty("emailField"))).sendKeys(parametersAndValues.get("login") + Keys.TAB
                           + parametersAndValues.get("password") + Keys.ENTER);
        WebDriverManager.waitFor(Integer.parseInt(TestConfig.VK_PROPERTIES.getProperty("pageLoadWait")));
        //check if login was successful
        if(WebDriverManager.isElementPresent(By.xpath(TestConfig.VK_XPATH.getProperty("myProfileLink")))){
            Log.info("Logged in successfully!",
                    "Login: " + parametersAndValues.get("login") +
                            "\nPassword: " + parametersAndValues.get("password"), WebDriverManager.takeSnapshot());
        }
        else{
            Log.error("Failed to log in!", "Failed to login with following credentials: " +
                    parametersAndValues.get("login") + "\n" + parametersAndValues.get("password"), WebDriverManager.takeSnapshot());
            WebDriverManager.closeWebDriver();
        }
    }
}
