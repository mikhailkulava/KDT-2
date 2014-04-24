package com.sigmaukraine.trn.testUtils;

import com.sigmaukraine.trn.report.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * This class implements simple actions with property files
 */
public class PropertyManager {
    /**
     * Changes property value in source property file
     * @param propertyFilePath - property file path
     * @param propertyName - target property name
     * @param propertyNewValue - target property's new value
     */
    public static void setPropertyValue(String propertyFilePath, String propertyName, String propertyNewValue){
        OutputStream output = null;
        try {
            Properties fileProperties = getProperties(propertyFilePath);
            output = new FileOutputStream(propertyFilePath);
            fileProperties.setProperty(propertyName,propertyNewValue);
            Log.info("Changing property value", "Setting new value [" + propertyNewValue + "] for: " + "\"" + propertyName + "\"");
            fileProperties.store(output, null);
        } catch (IOException e){
            Log.error("IO exception occurred", e);
        } finally {
            try{
                output.close();
            } catch (IOException e){
                Log.error("IO exception occurred", e);
            }
        }
    }

    /**
     * Gets property from property file.
     * @param propertyFilePath - source property file path.
     * @return - returns Properties
     */
    public static Properties getProperties(String propertyFilePath){
        Properties properties = new Properties();
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(propertyFilePath);
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e){
            Log.error("IO exception occurred!", e);
        }
        return properties;
    }
}
