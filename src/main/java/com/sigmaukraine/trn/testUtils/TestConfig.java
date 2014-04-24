package com.sigmaukraine.trn.testUtils;

import java.util.Properties;

/**
 * This class stores PROPERTIES from all configuration files.
 * Quick acces to properties: TestConfig.{Properties name}.getProperty({key});
 */
public class TestConfig {
    //CONFIG_PROPERTIES
    private final static String CONFIG_PROPERTIES_FILE = "config\\config.properties";
    public final static Properties CONFIG_PROPERTIES = PropertyManager.getProperties(CONFIG_PROPERTIES_FILE);
    public static String SCENARIOS_DIR = CONFIG_PROPERTIES.getProperty("scenariosDir");

    //SERVER_PROPERTIES
    private final static String SERVER_PROPERTIES_FILE = CONFIG_PROPERTIES.getProperty("server.properties");
    public final static Properties SERVER_PROPERTIES = PropertyManager.getProperties(SERVER_PROPERTIES_FILE);

    //VK_PROPERTIES
    private static final String VK_PROPERTIES_FILE = CONFIG_PROPERTIES.getProperty("vk.properties");
    public final static Properties VK_PROPERTIES = PropertyManager.getProperties(VK_PROPERTIES_FILE);
    public final static Properties VK_XPATH = PropertyManager.getProperties(TestConfig.CONFIG_PROPERTIES.getProperty("vkXpath"));

    //WEB_DRIVER_PROPERTIES
    public final static boolean TAKE_SNAPSHOTS = Boolean.valueOf(CONFIG_PROPERTIES.getProperty("takeSnapshots"));

}
