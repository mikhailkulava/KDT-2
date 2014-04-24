package com.sigmaukraine.trn.keywords;

import com.sigmaukraine.trn.testUtils.TestConfig;
import com.sigmaukraine.trn.testUtils.WebDriverManager;

import java.util.Map;

/**
 * This keywords navigates to takeSnapshot which is stored in @param - URI
 */
public class VkNavigatePage {
    /**
     * Keyword execution method
     */
    public static void execute(Map<String, String> parametersAndValues){
        final String BASE_URL = TestConfig.VK_PROPERTIES.getProperty("vkBaseURL");
        WebDriverManager.goToURL(BASE_URL + parametersAndValues.get("uri"));
    }
}
