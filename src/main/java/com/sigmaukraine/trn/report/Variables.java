package com.sigmaukraine.trn.report;

import com.sigmaukraine.trn.testUtils.Randomizer;
import com.sigmaukraine.trn.testUtils.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Variables class is used to load various properties files, and is similar to {@link Config} class,
 * but the Variables have a somewhat different macros.
 * 
 * Variables are stored, like the properties, in the <br/> 
 * <code>
 * Key=Value<br/>
 * </code>
 * But unlike the properties, they can not have $ macro expansion. The macros that variables have are the following: <br/>
 * <code>
 * date(integer)<br/> is replaced with the current or any other date with an offset from the current time, depending on the integer parameter
 * random(integer)<br/> is replaced with the random value starting from the integer parameter
 * </code>
 *
 */
public class Variables extends HashMap<String, Object>{

    private static final long serialVersionUID = 9064597943226944575L;
    public static final String CONFIG_VAR_FORMAT_ALIAS_KEY_PREFIX = "var.format.alias";
    public static final String CONFIG_VAR_DEFAULT_DATE_MASK_KEY = "var.format.date.mask";
    public static final String CONFIG_VAR_DATE_FORMAT_TIMEZONE_KEY = "var.format.date.timezone";

    private static final Log log = LogFactory.getLog(Variables.class);

    protected static final Map<String, String> FORMAT_ALIASES = Config
            .getStringsByPrefix(CONFIG_VAR_FORMAT_ALIAS_KEY_PREFIX);
    protected static final String DEFAULT_DATE_FORMAT = Config.getString(
            CONFIG_VAR_DEFAULT_DATE_MASK_KEY, "yyyy-MM-dd HH:mm:ss");
    protected static final TimeZone DATE_FORMAT_TIMEZONE;
    static {
        String timeZoneId = Config.getString(CONFIG_VAR_DATE_FORMAT_TIMEZONE_KEY);
        DATE_FORMAT_TIMEZONE = timeZoneId.length() == 0 ? TimeZone.getDefault(): TimeZone.getTimeZone(timeZoneId);
    }

    protected static final Pattern PARAMETERIZATION_PATTERN = Pattern.compile("#(.*?)#");
    protected static final Pattern PARAMETERIZATION_VARS_PATTERN = Pattern.compile("\\$\\{(.*?)\\}");

    public Variables() {
        super();
    }

    /**
     * Create the variables instance from the other variables instance. It's properties are stored in the new variables instance.
     * @param variables - other variables instance.
     */
    public Variables(Variables variables) {
        super(variables);
    }

    public void setValue(String name, Object value) {
       put(name, value);
    }
    
    @Override
    public Object put(String name, Object value) {
        if (value == null) {
            return remove(name);
        } else {
            return super.put(name, value);
        }
    }
    /**
     * Load the properties file for the further processing
     * @param propertiesFile - the properties file
     */
    public void loadFromFile(File propertiesFile) {
        loadFromFile(propertiesFile, System.currentTimeMillis());
    }

    /**
     * Load the properties from the file with the time base, that is added to the dates in the properties file.
     * @param propertiesFile - a file with the properties
     * @param baseTime - a base of the time
     */
    public void loadFromFile(File propertiesFile, long baseTime) {
        if (!propertiesFile.exists()) {
            Report.getReport().error("Properties file not found: " + propertiesFile.getAbsolutePath());
            return;
        }
        Properties p = Utils.readPropertiesFile(propertiesFile);
        Set<Object> keys = p.keySet();
        for (Object objectKey : keys) {
            if (!(objectKey instanceof String))
                continue;
            String key = (String) objectKey;
            String expression = p.getProperty(key, "");

            expression = setParametersInVars(expression);

            if (expression.toUpperCase().startsWith("RANDOM")) {
                long argument = extractNumericArgument(expression, 10);
                if (argument > Integer.MAX_VALUE) {
                    Report.getReport().error("Too large value in argument for variable: " + key);
                }
                String value = Randomizer.generateId((int) argument);
                setValue(key, value);
            } else if (expression.toUpperCase().startsWith("DATE")) {
                long argument = extractNumericArgument(expression, 0);
                long dateValue = baseTime + 1000l * argument;
                setValue(key, dateValue);
            } else {
                setValue(key, expression);
            }
        }
    }

    private String setParametersInVars(String expression) {
        Matcher m = PARAMETERIZATION_VARS_PATTERN.matcher(expression);
        StringBuffer resultLine = new StringBuffer();
        while (m.find()) {
            String key = m.group(1);
            String replacement= Config.getString(key,null);
            if (replacement==null) {
                replacement = key;
                Report.getReport().error("Substitution was not made: " + replacement,new Throwable());
                
            }
            replacement = replacement.replaceAll("([\\$\\\\])","\\\\$1");
            m.appendReplacement(resultLine, replacement);
        }
        m.appendTail(resultLine);
        expression = resultLine.toString();
        return expression;
    }

    private long extractNumericArgument(String s, long defaultValue) {
        long argument = defaultValue;
        int openBracketIdx = s.indexOf('(');
        int closeBracketIdx = s.indexOf(')');
        if (openBracketIdx > -1 && closeBracketIdx > openBracketIdx) {
            String fullArgumentString = s.substring(openBracketIdx + 1,closeBracketIdx).trim();

            if (fullArgumentString.length() > 0
                    && fullArgumentString.charAt(0) == '+') {
                fullArgumentString = fullArgumentString.substring(1);
            }

            long multipleFactor = 1;
            if (fullArgumentString.length() > 0) {
                char suffix = fullArgumentString.charAt(fullArgumentString.length() - 1);
                switch (suffix) {
                case 'd':
                    multipleFactor *= 24;
                    // no break
                case 'h':
                    multipleFactor *= 60;
                    // no break
                case 'm':
                    multipleFactor *= 60;
                    // no break
                case 's':
                    // multipleFactor*=1;
                    fullArgumentString = fullArgumentString.substring(0,fullArgumentString.length() - 1);
                    break;
                default:
                    break;
                }
            }
            fullArgumentString = fullArgumentString.trim();
            if (fullArgumentString.length() > 0) {
                try {
                    argument = Long.valueOf(fullArgumentString) * multipleFactor;
                } catch (NumberFormatException e) {
                    Report.getReport().error("Incorrect argument in expression", e);
                    
                }
            }
        }
        return argument;
    }

    /**
     * Get the property value using the key. If the property doesn't exist, error message is written into the log.
     * @param key - a key name of the property
     * @return - a value of the property
     */
    public String getStringValue(String key) {
        // special case: ## -> #
        if (key.length() == 0) return "#";

        // extract format from key=storedKey[|format]
        String[] parts = key.split("\\|", 2);
        String storedKey = parts.length > 0 ? parts[0] : "";
        String format = parts.length > 1 ? parts[1] : "";

        Object value = get(storedKey);
        if (value == null) {
            Report.getReport().error("Variable is not defined: " + key);
            return null; // TODO ""/null?
        }

        if (value instanceof Long) {
            // format aliases for date
            if (format != null && format.length() > 0) {
                String newFormat = FORMAT_ALIASES.get(CONFIG_VAR_FORMAT_ALIAS_KEY_PREFIX + '.' + format);
                if (newFormat != null && newFormat.length() > 0) {
                    format = newFormat;
                }
            }
            if (format == null || format.length() == 0) {
                format = DEFAULT_DATE_FORMAT;
            }

            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
                sdf.setTimeZone(DATE_FORMAT_TIMEZONE);
                return sdf.format(value);
            } catch (IllegalArgumentException e) {
                Report.getReport().error("Error while formatting date with pattern: " + format,e);
                
            }
        }
        return value.toString();
    }
    
    /**
     * Replace variables in the string. Names of the variables are defined as 
     * <code>#variable#</code> and are replaced according their values in the properties file.
     * If the variable is not defined, an error message is written into the log and the variable is left as is 
     * @param s - a string with variables
     * @return - a replaced string
     */
    public String replaceVarsInString(String s) {
        Matcher m = PARAMETERIZATION_PATTERN.matcher(s);
        StringBuffer resultLine = new StringBuffer();
        while (m.find()) {
            String macrosKey = m.group(1);
            String replacement = getStringValue(macrosKey);
            if (replacement == null) {
                replacement = m.group(0);
                Report.getReport().error("Substitution was not made: " + replacement);
            }
            replacement = replacement.replace("\\", "\\\\\\\\").replace("$","\\$");
            m.appendReplacement(resultLine, replacement);
        }
        m.appendTail(resultLine);
        return resultLine.toString();
    }

    @Override
    public String toString() {
        List<String> keys = new ArrayList<String>(keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(key).append(" = ").append(getStringValue(key)).append('\n');
        }
        return sb.toString();
    }

    public void debug() {
        log.debug(toString());
    } 
}
