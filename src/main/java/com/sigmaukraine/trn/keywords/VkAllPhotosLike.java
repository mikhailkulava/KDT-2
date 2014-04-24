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
 * This keyword likes all wall user's photos, until "captcha" will interrupt scenario
 */
public class VkAllPhotosLike {
     private static final WebDriver driver = WebDriverManager.getDriver();
    /**
     * Keyword execution method
     */
    public static void execute(){
        //scroll down, to make profile albums visible
        ((JavascriptExecutor)driver).executeScript("scroll(0,500);");
        //open albums
        driver.findElement(By.xpath(TestConfig.VK_XPATH.getProperty("profileAlbums"))).click();
        //show all photos
        driver.findElement(By.xpath(TestConfig.VK_XPATH.getProperty("showAllPhotos"))).click();
        //wait page to load
        WebDriverManager.waitFor(Integer.parseInt(TestConfig.VK_PROPERTIES.getProperty("pageLoadWait")));
        //scroll down while all photos will become visible
        WebElement lastElementBeforeScroll = driver.findElement(By.xpath(TestConfig.VK_XPATH.getProperty("lastPhotoVisible")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", lastElementBeforeScroll);
        WebElement lastElementAfterScroll = driver.findElement(By.xpath(TestConfig.VK_XPATH.getProperty("lastPhotoVisible")));
        //keep scrolling while last visible photo before scroll == last visible photo after scroll
        while (!lastElementBeforeScroll.getAttribute("id").equals(lastElementAfterScroll.getAttribute("id"))){
            lastElementBeforeScroll = driver.findElement(By.xpath(TestConfig.VK_XPATH.getProperty("lastPhotoVisible")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", lastElementBeforeScroll);
            lastElementAfterScroll = driver.findElement(By.xpath(TestConfig.VK_XPATH.getProperty("lastPhotoVisible")));
        }
        //define like counter
        int likeCounter = 0;
        //add all photos to list of web elements
        List<WebElement> allPhotos = driver.findElements(By.xpath(TestConfig.VK_XPATH.getProperty("allPhotos")));
        List<String> photoLinks = new LinkedList<String>();
        //get all photos links in correct format, without "?***"
        for(WebElement singlePhoto : allPhotos){
            int lastIndex = singlePhoto.getAttribute("href").lastIndexOf("?");
            photoLinks.add(singlePhoto.getAttribute("href").substring(0, lastIndex));
        }
        //navigate to each url in a loop
        for(String photoURL : photoLinks){
            WebDriverManager.goToURL(photoURL);
            WebDriverManager.waitFor(Integer.parseInt(TestConfig.VK_PROPERTIES.getProperty("pageLoadWait")));
            //check if like wasn't clicked by current user before
            if(WebDriverManager.isElementPresent(By.xpath(TestConfig.VK_XPATH.getProperty("photoLikeUnliked")))){
                driver.findElement(By.xpath(TestConfig.VK_XPATH.getProperty("photoLikeUnliked"))).click();
                likeCounter++;
                Log.info("Photo liked! [Like# " + likeCounter + "]");
            }
            //check if captcha appeared
            if(WebDriverManager.isElementPresent(By.xpath(TestConfig.VK_XPATH.getProperty("captcha")))){
                Log.error("vk_allPhotosLike FAILED", "Captcha appeared!<br>Videos liked: " + likeCounter, WebDriverManager.takeSnapshot());
                break;
            }
        }
    }
}
