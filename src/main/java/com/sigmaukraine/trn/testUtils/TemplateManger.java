package com.sigmaukraine.trn.testUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * This class works with templates, that contain placeholders, that need to be replaced
 */
public class TemplateManger {
    /**
     * Replaces placeholders
     * @param templateBody - template content as String
     * @param placeholders - map with placeholders
     * This method gets a map of placeholders and iterates over it, searching for matches.
     * Map's key is placeholder's name without '$' sign (it's added by default inside the method),
     * value contains the value that has to replace the key.
     * @return - returns String with replaced placeholders.
     */
    public static String replacePlaceholders(String templateBody, Map<String, String> placeholders){
        for (Map.Entry<String, String> placeholder : placeholders.entrySet()){
            templateBody = templateBody.replace("$" + placeholder.getKey(), placeholder.getValue());
        }
        return templateBody;
    }

    /**
     * Same method for one placeholder
     */
    public static String replacePlaceholders(String templateBody, String placeholder, String value){
        Map<String, String> placeholders = new HashMap<String, String>();
        placeholders.put(placeholder, value);
        return replacePlaceholders(templateBody, placeholders);
    }
}
