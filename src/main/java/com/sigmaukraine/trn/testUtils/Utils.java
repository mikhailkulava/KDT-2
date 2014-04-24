package com.sigmaukraine.trn.testUtils;

import com.sigmaukraine.trn.report.Config;
import com.sigmaukraine.trn.report.Report;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * This class contains
 */
public class Utils {

    protected static final Pattern XML_PROLOG_JUNK_PATTERN = Pattern.compile("^([\\W]+)<");

    /**
     * Reads the file and returns it's content as a string
     * Method is safe.
     * @param file - a file to be read 
     * @return a string content of the file
     */
    public static String readFileToString(File file) {
        try {
            return FileUtils.readFileToString(file,"UTF-8");
        } catch (IOException e) {
            Report.getReport().error("Failed to read file: " + file.getAbsolutePath(), e);
            
        } 
        return "";
    }
    
    /**
     * Read the file from the string path
     * Method is safe.
     * @param filePath - a path to the file
     * @return file content.
     */
    public static String readFileToString(String filePath) {
        return readFileToString(new File(filePath));
    }

    /**
     * Write the string into the file
     * @param file - an already created file 
     * @param data - the date to be written
     */
    public static void writeStringToFile(File file, String data) {
        try {
            FileUtils.writeStringToFile(file, data, "UTF-8");
        } catch (IOException e) {
            Report.getReport().error("Failed to write to file: "+file.getAbsolutePath(), e);
            
        }
    }
    
    /**
     * Write the string into the file using the file path
     * @param filePath - a path to the file
     * @param data - the data to be written
     */
    public static void writeStringToFile(String filePath, String data) {
        writeStringToFile(new File(filePath), data);
    }

    public static Document parseXml(String xmlString) {
        //Strip off any junk in the prolog (e.g. three-byte UTF-8 byte-order mark) 
        // to avoid "org.xml.sax.SAXParseException: Content is not allowed in prolog"
        Matcher junkMatcher = XML_PROLOG_JUNK_PATTERN.matcher(xmlString.trim());
        xmlString = junkMatcher.replaceFirst("<");
        
        DocumentBuilder builder = Config.getDocumentBuilder();
        if (xmlString.length()>0) {
            try {
                return builder.parse(new InputSource(new StringReader(xmlString)));
            } catch (SAXException e) {
                Report.getReport().error("Failed to parse XML", e);
                
            } catch (IOException e) {
                Report.getReport().error("Failed to parse XML", e);
                
            }
        }
        return builder.newDocument();
    }
    
    /**
     * Parses an XML file and returns it's DOM
     * @param xmlFile - an xml file to be parsed
     * @return a {@link Document} structure
     */
    public static Document parseXml(File xmlFile) {
        return parseXml(readFileToString(xmlFile));
    }
    
    public static String writeXmlToString(Node node) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");
            
            //initialize StreamResult with File object to save to file
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(node);
            transformer.transform(source, result);

            return result.getWriter().toString();
        } catch (TransformerConfigurationException e) {
            Report.getReport().error("Failed to output XML to String", e);
        } catch (TransformerFactoryConfigurationError e) {
            Report.getReport().error("Failed to output XML to String", e);
        } catch (TransformerException e) {
            Report.getReport().error("Failed to output XML to String", e);
        }
        return "";
    }

    /**
     * Evaluate an XPath expression in the specified context and return the result as a String
     * @param expression - xpath expression
     * @param node - node
     * @return The String that is the result of evaluating the expression and converting the result to a String
     */
    public static String evaluateXPath(String expression, Node node) {
        try {
            return XPathFactory.newInstance().newXPath().evaluate(expression, node);
        } catch (XPathExpressionException e) {
            Report.getReport().error("Failed to extract xpath value from xml: xpath=" + expression);
        }
        return "";
    }

    public static Properties readPropertiesFile(File propertiesFile) {
        Properties p = new Properties();
        InputStream is = null;
        try {
            is = new FileInputStream(propertiesFile);
            p.load(is);
        } catch (IOException e) {
            Report.getReport().error("Failed to read properties file: "+propertiesFile.getName(), e);
            
        } finally {
            Utils.close(is);
        }
        return p;
    }

    /**
     * Reads the properties from the InputStream. Useful for reading properties inside the jars.
     * @param propStream - a stream of the properties container
     * @return a new Properties instance
     */
    public static Properties readPropertiesFromStream(InputStream propStream) {
        Properties p = new Properties();
        try {
            p.load(propStream);
        } catch (IOException e) {
            Report.getReport().error("Failed to read properties from InputStream", e);
            
        }
        return p;
    }
    
    /**
     * Read resource in the classpath to string
     * @param resourcePath - source file path
     * @return - result string
     */
    public static String readResourceToString(ClassLoader classLoader, String resourcePath) {
        InputStream str = classLoader.getResourceAsStream(resourcePath);
        try {
            if (str != null) {
                BufferedInputStream data = new BufferedInputStream(str);
                byte[] buf = new byte[data.available()];
                data.read(buf);
                return new String(buf);
            }
        } catch (IOException e) {
            Report.getReport().error("Failed to read resource: " + resourcePath, e);
            
        } finally {
            close(str);
        }
        return "";
    }

    /**
     * Sleep for a given period of time. Test execution is paused in the meantime.
     * @param millis - a time in milliseconds
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Report.getReport().error("Exception caugth while sleeping", e);
        }
    }
    /**
     * Sleep for a given period of time.
     * @param seconds - a time in seconds
     */
    public static void sleepSeconds(int seconds) {
        sleep(1000 * seconds);
    }

    /**
     * Close any count of objects, that implement Closable
     * @param closeables - a set of objects to be closed
     */
    public static void close(Closeable... closeables) {
        if (closeables==null) 
            return;
        for (Closeable c : closeables) {
            try {
                if (c!=null) {c.close();}
            } catch (IOException e) {
                Report.getReport().error("Failed to close "+c.getClass().getSimpleName(), e);
                
            }
        }
    }

    private static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[8192];
        int read;
        while ((read=is.read(buffer))!=-1) {
            os.write(buffer, 0, read);
        }
    }
    
    /**
     * Unzips the given archive to the given directory.
     * @param zipFile -  an archive that needs to be unzipped
     * @param extractToDir - the directory, where the contents of the archive should be stored 
     */
    public static void unzip(File zipFile, File extractToDir) {
        try {
            ZipFile archive = new ZipFile(zipFile);
            Enumeration<? extends ZipEntry> e = archive.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();
                File file = new File(extractToDir, entry.getName());
                if (entry.isDirectory() && !file.exists()) {
                    file.mkdirs();
                } else {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    if (!file.isDirectory()) {
                        InputStream is = null;
                        OutputStream os = null;
                        try {
                            is = archive.getInputStream(entry);
                            os = new FileOutputStream(file);
                            copy(is, os);
                        } finally {
                            close(is, os);
                        }
                    }
                }
            }
        } catch (IOException e) {
            Report.getReport().error("Failed to unzip :"+zipFile+" to dir: "+extractToDir, e);
            
        } 
    }

    /**
     * Unzip files from stream. If extractToDir doesn't exist it will be created.
     * @param input - input zipped stream
     * @param extractToDir - a directory the files will be extracted to
     */
    public static void unzip(ZipInputStream input, File extractToDir) {
        ZipEntry entry = null;
        try {
            while ((entry = input.getNextEntry())!= null) {
                // create a new file we will be writing to
                File file = new File(extractToDir, entry.getName());
                if (entry.isDirectory() && !file.exists()) {
                    file.mkdirs();
                } else {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    if (!file.isDirectory()) {
                        OutputStream os = null;
                        try {
                            os = new FileOutputStream(file);
                            copy(input, os);
                        } finally {
                            close(os);
                        }
                    }
                }
            }
        } catch (IOException e) {
            if (entry != null) {
                Report.getReport().error("Failed to unzip :"+entry.getName()+" to dir: "+extractToDir, e);
                
            }
        } finally {
            close(input);
        }
    }

    /**
     * Archive the given file into the zip file. The given file can be directory, all it's contents will be zipped/
     * @param file - a file that has to be archived
     * @param zipFile - an archive file
     */
    public static void zip(File file, File zipFile) {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            if (file.isDirectory()) {
                zip(file.listFiles(), file, zos);
            } else if (file.isFile()) {
                zip(new File[]{file}, file.getParentFile(), zos);
            }
        } catch (IOException e) {
            Report.getReport().error("Failed to zip directory: "+file, e);
            
        } finally {
            close(zos);
        }
    }

    private static void zip(File[] files, File baseDir, ZipOutputStream zos) throws IOException {
        for (File file : files) {
            if (file.isDirectory()) {
                zip(file.listFiles(), baseDir, zos);
            } else {
                FileInputStream is = new FileInputStream(file);
                try {
                    ZipEntry entry = new ZipEntry(
                            file.getPath().substring(baseDir.getPath().length() + 1));
                    zos.putNextEntry(entry);
                    copy(is, zos);
                } finally {
                    close(is);
                }
            }
        }
    }

    /**
     * Gets the stack trace from the Trowable
     * @param t - an object that implements Throwable
     * @return the stack trace
     */
    public static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    /**
     * Normalizes string blank spaces.
     * @param   s String to normalize
     * @return  normalized string
     */
    public static String normalizeString(String s) {
        if (s == null) return "";
        return s.replace((char)0xA0, ' ').trim();
    }

    /**
     * Converts glob metachars to regexp ones. 
     * Selenium compliant implementation: see htmlutils.js, PatternMatcher.convertGlobMetaCharsToRegexpMetaChars
     * @param   glob String with glob metachars
     * @return  String with regexp metachars
     */
    public static String convertGlobMetaCharsToRegexpMetaChars(String glob) {
        return glob
            .replaceAll("([\\.\\^\\$\\+\\(\\)\\{\\}\\[\\]\\\\\\|])", "\\\\$1")
            .replaceAll("\\?", "(.|[\n\r])")
            .replaceAll("\\*", "(.|[\n\r])*");
    }

    /**
     * Prepares regular expression based on input String prefix. Default is "glob:"
     * @param   expression source String
     * @return  converted String
     */
    public static String getRegexp(String expression) {
        if (expression.startsWith("regexp:")) {
            return expression.replaceFirst("regexp:", "");
        } else {
            return "^" + convertGlobMetaCharsToRegexpMetaChars(expression) + "$";
        }
    }

    /**
     * Prepares pattern for regular expressions
     * @param   expression regular expression
     * @return  Pattern
     */
    public static Pattern preparePattern(String expression) {
        return Pattern.compile(getRegexp(expression));
    }

    /**
     *
     * @return - returns current time and date with required DateTime format
     */
    public static String getCurrentTimeDate(String dateTimeFormat){
        DateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
        //get current date time with Date()
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * Cleans results dir
     */
    public static void cleanResults(){
        DirectoryManager.removeDir(new File(TestConfig.CONFIG_PROPERTIES.getProperty("results")));
    }
        
}
