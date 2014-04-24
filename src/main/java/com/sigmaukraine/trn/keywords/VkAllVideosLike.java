package com.sigmaukraine.trn.keywords;

import com.sigmaukraine.trn.report.Log;
import com.sigmaukraine.trn.testUtils.TestConfig;
import com.sigmaukraine.trn.testUtils.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.LinkedList;
import java.util.List;

/**
 * This keyword likes all user's videos
 */
public class VkAllVideosLike {
    private static final WebDriver driver = WebDriverManager.getDriver();
    /**
     * Keyword execution method
     */
    public static void execute(){
        //define like counter
        int likeCounter = 0;
        //scroll down, to make profile videos visible
        ((JavascriptExecutor)driver).executeScript("scroll(0,500);");
        //open profile videos
        driver.findElement(By.xpath(TestConfig.VK_XPATH.getProperty("profileVideos"))).click();
        //if "more videos" link is active, then click it to display more videos, untill all videos shown
        while (!WebDriverManager.isElementPresent(By.xpath(TestConfig.VK_XPATH.getProperty("isMoreVideos")))){
            driver.findElement(By.xpath(TestConfig.VK_XPATH.getProperty("getMoreVideos"))).click();
            driver.findElements(By.xpath(TestConfig.VK_XPATH.getProperty("allVideos")));
        }
        //get all videos links, by attribute @href
        List<String> videoLinks = new LinkedList<String>();
        for (WebElement webElement : driver.findElements(By.xpath(TestConfig.VK_XPATH.getProperty("allVideos")))){
            videoLinks.add(webElement.getAttribute("href"));
        }
        //navigate to each video page
        for (String videoLink : videoLinks){
            WebDriverManager.goToURL(videoLink);
            //check if videos is not liked by current user yet
            if(WebDriverManager.isElementPresent(By.xpath(TestConfig.VK_XPATH.getProperty("videoLikeUnliked")))){
                //if yes - like it
                driver.findElement(By.xpath(TestConfig.VK_XPATH.getProperty("videoLikeUnliked"))).click();
                //check if like was accepted, increment like counter
                if(!WebDriverManager.isElementPresent(By.xpath(TestConfig.VK_XPATH.getProperty("videoLikeUnliked")))){
                    likeCounter++;
                    Log.info("Video liked! [Like# " + likeCounter + "]");
                }
            }
            //check if captcha appeared
            if(WebDriverManager.isElementPresent(By.xpath(TestConfig.VK_XPATH.getProperty("captcha")))){
                Log.error("vk_allVideosLike FAILED", "Captcha appeared!\nVideos liked: " + likeCounter, WebDriverManager.takeSnapshot());
                break;
            }

        }
        Log.info("Videos liked : " + likeCounter);
    }
}
