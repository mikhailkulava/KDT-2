package com.sigmaukraine.trn.keywords;

import com.sigmaukraine.trn.report.Log;
import com.sigmaukraine.trn.testUtils.TemplateManger;
import com.sigmaukraine.trn.testUtils.TestConfig;
import com.sigmaukraine.trn.testUtils.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * This keyword likes all posts on the wall
 */
public class VkAllPostsLike {
    private static final WebDriver driver = WebDriverManager.getDriver();
    /**
     * Keyword execution method
     */
    public static void execute(){
        //Open wall module
        driver.findElement(By.xpath(TestConfig.VK_XPATH.getProperty("openWall"))).click();
        //define like counter
        Integer likeCounter = 0;
        WebDriverManager.waitFor(Integer.parseInt(TestConfig.VK_PROPERTIES.getProperty("pageLoadWait")));
        //find all elements, which have like that wasn't clicked by current user
        for(WebElement webElement : driver.findElements(By.xpath(TestConfig.VK_XPATH.getProperty("unlikedPosts"))))
        {
            webElement.click();
            likeCounter++;
            Log.info("Post liked! [Like# " + likeCounter + "]");
            WebDriverManager.waitFor(Integer.parseInt(TestConfig.VK_PROPERTIES.getProperty("likeWait")));
        }
        //define page index
        int pageIndex = 2;
        /*get xpath template, placeholder @id will be replaced all the time
        in a loop below and check the condition, if next page exists than navigate to next page*/
        String pageIndexXpathTemplate = TestConfig.VK_XPATH.getProperty("wallPageIndex");
        String nextPageIndexXpath = TemplateManger.replacePlaceholders(pageIndexXpathTemplate, "id", Integer.toString(pageIndex));
        //if next page exists, navigate to next page
        while (WebDriverManager.isElementPresent(By.xpath(nextPageIndexXpath))){
            //check if captcha appeared
            if(WebDriverManager.isElementPresent(By.xpath(TestConfig.VK_XPATH.getProperty("captcha")))){
                Log.error("vk_allPostsLike FAILED", "Captcha appeared!<br>Posts liked: " + likeCounter, WebDriverManager.takeSnapshot());
                break;
            }
            WebElement nextPage = driver.findElement(By.xpath(nextPageIndexXpath));
            nextPage.click();
            //liking all posts, that haven't been liked yet
            WebDriverManager.waitFor(Integer.parseInt(TestConfig.VK_PROPERTIES.getProperty("pageLoadWait")));
            try{
                for(WebElement webElement : driver.findElements(By.xpath(TestConfig.VK_XPATH.getProperty("unlikedPosts")))){
                    webElement.click();
                    likeCounter++;
                    WebDriverManager.waitFor(Integer.parseInt(TestConfig.VK_PROPERTIES.getProperty("likeWait")));
                    Log.info("Post liked! [Like# " + likeCounter + "]");
                }
                pageIndex++;
                nextPageIndexXpath = TemplateManger.replacePlaceholders(pageIndexXpathTemplate, "$id", Integer.toString(pageIndex));
            } catch (StaleElementReferenceException e){
                Log.error("Posts liked: " + likeCounter + "\n", e.getLocalizedMessage(), WebDriverManager.takeSnapshot());
            }
        }
        Log.info("Posts liked: " + likeCounter);
    }
}
