package com.sigmaukraine.trn.testUtils;

import com.sigmaukraine.trn.testUtils.PropertyManager;

import java.util.Properties;

/**
 * Created by mkulava on 12.03.14.
 */
public class TestConfig {
    //CONFIG_PROPERTIES
    private final static String CONFIG_PROPERTIES_FILE = "config\\config.properties";
    public final static Properties CONFIG_PROPERTIES = PropertyManager.getProperties(CONFIG_PROPERTIES_FILE);

    public static String SCENARIOS_DIR = CONFIG_PROPERTIES.getProperty("scenariosDir");
    public static String REPORT_DIR = CONFIG_PROPERTIES.getProperty("reportDir");

    //SERVER_PROPERTIES
    private final static String SERVER_PROPERTIES_FILE = CONFIG_PROPERTIES.getProperty("server.properties");
    public final static Properties SERVER_PROPERTIES = PropertyManager.getProperties(SERVER_PROPERTIES_FILE);
}
