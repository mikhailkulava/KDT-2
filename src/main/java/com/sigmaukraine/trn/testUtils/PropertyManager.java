package com.sigmaukraine.trn.testUtils;

import com.sigmaukraine.trn.testUtils.LogManager;

import java.io.*;
import java.util.Properties;

/**
 * Created by mkulava on 31.01.14.
 */
public class PropertyManager {

    public static void setPropertyValue(String propertyFilePath, String propertyName, String propertyNewValue){
        OutputStream output = null;
        try {
            Properties fileProperties = getProperties(propertyFilePath);
            output = new FileOutputStream(propertyFilePath);
            fileProperties.setProperty(propertyName,propertyNewValue);
            LogManager.info("Setting new value [" + propertyNewValue + "] for: " + "\"" + propertyName + "\"");
            fileProperties.store(output, null);
        } catch (IOException e){
            LogManager.warning(e.getLocalizedMessage());
        } finally {
            try{
                output.close();
            } catch (IOException e){
                LogManager.warning(e.getLocalizedMessage());
            }
        }
    }

    public static Properties getProperties(String propertyFilePath){
        Properties properties = new Properties();
        FileInputStream inputStream = null;
        try {
            LogManager.info("Getting properties from: " + propertyFilePath);
            inputStream = new FileInputStream(propertyFilePath);
            properties.load(inputStream);
        } catch (IOException e){
            LogManager.warning(e.getLocalizedMessage());
        } finally {
            try{
                inputStream.close();
            } catch (IOException e){
                LogManager.warning(e.getLocalizedMessage());
            }
        }
        return properties;
    }
}
