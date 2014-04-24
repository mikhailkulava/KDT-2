package com.sigmaukraine.trn.report;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.*;

import com.sigmaukraine.trn.testUtils.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.XMLUnit;

/**
 * Class Config is used to load the test.properties file and get them inside test scenarios.
 * The test.properties should be located under the root of the test directory.
 * You can get the properties using the {@link #getString(String)}, {@link #getStringArray(String)}, {@link #getStringsByPrefix(String)} functions then.
 *
 * Properties are stored in a Key=Value format. The can be parameterized using $ macro. For example<br/>
 * <code>Key=Value<br/>
 * Key2=$Key value2<br/></code>
 * $Key will be replaced with it's value.
 *  
 */
public class Config {
    private static final Log log = LogFactory.getLog(Config.class);
    public static final String PROPERTIES_FILE = "test.properties";
    private static final Pattern PARAMETERIZATION_PATTERN = Pattern.compile("\\$\\{(.*?)\\}");
    private static final Properties config = readParameterizedPropertiesFile(new File(PROPERTIES_FILE));
    
    private static final DocumentBuilder builder;
    private static final TimeZone serverTimeZone;
    
    private static String tempDirPathname = "tmp";
    
    static {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //ignore dtd validation
            factory.setAttribute("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            factory.setAttribute("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            factory.setNamespaceAware(true);
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            log.fatal("ParserConfigurationException", e);
            throw new RuntimeException("ParserConfigurationException", e);
        }

        try {
            Class.forName("org.custommonkey.xmlunit.XMLUnit");
            XMLUnit.setCompareUnmatched(false);
            XMLUnit.setIgnoreWhitespace(true);
            XMLUnit.setNormalizeWhitespace(true);
            XMLUnit.setNormalize(true);
        } catch (ClassNotFoundException e) {
            // nothing to do
        }

        String timeZoneId = Config.getString("nc.timezone");
        serverTimeZone = timeZoneId.length()==0 ? TimeZone.getDefault() : TimeZone.getTimeZone(timeZoneId);
    }

    /**
     * Gets the property value from the loaded properties file before.
     * @param key - a key of the property.
     * @return a value if the property exists and an empty string otherwise.
     */
    public static String getString(String key) {
        return getString(key, "");
    }
    
    /**
     * Get the property value or provided default value in case of it's absense.
     * @param key - a key of the property
     * @param defaultValue - a value if the property with the given key doesn't exist
     * @return a value or the defaultValue instead
     */
    public static String getString(String key, String defaultValue) {
        return config.getProperty(key, defaultValue);
    }
    
    public static void setString(String key, String value) {
        config.setProperty(key.trim(), value.trim());
    }

    /**
     * Get the map of the string with the given prefix. 
     * @param prefix - a prefix up to dot "." in the names of the properties 
     * @return a map of the prefixes, that can be empty.
     */
    public static Map<String,String> getStringsByPrefix(String prefix) {
        Map<String,String> result = new HashMap<String,String>();
        Set<Object> keys = config.keySet();
        for (Object objectKey : keys) {
            if (!(objectKey instanceof String))
                continue;
            String key = (String)objectKey;
            if (key.equals(prefix) || key.startsWith(prefix+".")) {
                result.put(key, getString(key));
            }
        }
        return result;
    }

    /**
     * Get the string array of the property. Array in the property file should be separated with comma ",".
     * @param key - a key of the array property
     * @return - an array of the values, can be empty.
     */
    public static String[] getStringArray(String key) {
        return getString(key).split(",");
    }

    public static void setTempDirPathname(String tempDirPathname) {
        Config.tempDirPathname = tempDirPathname;
    }
    public static String getTempDirPathname() {
        return tempDirPathname;
    }

    public static String getConfigDirPathname() {
        return "config";
    }

    public static DocumentBuilder getDocumentBuilder() {
        return builder;
    }
    
    public static TimeZone getServerTimezone() {
        return serverTimeZone;
    }
    
    private static Properties readParameterizedPropertiesFile(File propertiesFile) {
        Properties p = new Properties();
        BufferedReader lines = null;
        if (!propertiesFile.exists()) {
            log.debug("'" + propertiesFile.getAbsolutePath() + "' file has not been found");
            return p;
        }
        try {
            lines = new BufferedReader(new InputStreamReader(new FileInputStream(propertiesFile),"UTF-8"));
            String line;
            while ((line=lines.readLine())!=null) {
                line = line.trim();
                if (line.startsWith("#") || line.length()==0) continue;
                Matcher m = PARAMETERIZATION_PATTERN.matcher(line);
                StringBuffer resultLine = new StringBuffer();
                while (m.find()) {
                    String key = m.group(1);
                    String replacement;
                    if (p.containsKey(key)) {
                        replacement = p.getProperty(key);
                    } else {
                        replacement = m.group(0);
                        log.error("Substitution was not made: "+replacement);
                    }
                    replacement = replacement.replace("\\","\\\\\\\\").replace("$", "\\$");
                    m.appendReplacement(resultLine, replacement);
                }
                m.appendTail(resultLine);
                line = resultLine.toString();
                //p.load(new StringReader(line)); //java6
                String[] keyAndValue = line.split("[=|:]",2);
                if (keyAndValue.length!=2) keyAndValue = line.split("[ |\t|\f]",2);
                if (keyAndValue.length<2) continue;
                p.put(keyAndValue[0].trim(),keyAndValue[1].trim());
            }
        } catch (IOException e) {
            log.fatal("Failed to read properties file: "+propertiesFile.getName(), e);
            throw new RuntimeException("IOException", e);
        } finally {
            Utils.close(lines);
        }
        return p;
    }
}
