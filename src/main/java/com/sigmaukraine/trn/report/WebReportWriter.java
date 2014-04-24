package com.sigmaukraine.trn.report;

import com.sigmaukraine.trn.testUtils.TestConfig;
import com.sigmaukraine.trn.testUtils.Utils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * Report is the singletone instance, so it's usage is pretty easy:
 * <code>
 *      Report.getReport.info(...);
 * </code>
 * 
 * Uses: 
 * 1) config/report/report.zip
 * 2) in test.properties: report.dir, report.zipSnapshots
 * 3) in log4j.properties: properties for commonLogger
 */
public class WebReportWriter implements ReportWriter {
    private static final Log log = LogFactory.getLog(WebReportWriter.class);

    public static final String SUFFIX_KEY = "suffix";
    public static final String REPORT_DIR_KEY = "reportDir";
    public static final String REPORT_DIR_ROOT_KEY = "reportRoot";
    public static final String REPORT_THREAD_LOCAL_KEY = "report.thread.local";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
    
    private static ReportThreadLocal report = new ReportThreadLocal();

    private File reportDir;
    private File indexFile;
    private long indexFileLength;
    private String description = "";
    private File reportFile;
    private long reportFileLength;
    private List<Logger> customLoggers;


    private int logId = 0;
    private Stack<String> currentLogCallStack = new Stack<String>();
    private String currentLogName = null;
    private int msgId = 0;
    private int maxPriorityReported = Priority.INFO_INT;
    private int pageId = 0;
    private Stack<Long> timeStack = new Stack<Long>();
    
    private boolean isInit = false;
    
    private static String reportJarDir = TestConfig.CONFIG_PROPERTIES.getProperty("reportJarDir");

    private WebReportWriter(Properties p) {

        String suffix = (p==null) ? "" : p.getProperty(SUFFIX_KEY, "");
        if (suffix.length() > 0)
            suffix = "_" + suffix;

        String reportDirname = (p == null) 
                ? Config.getString(REPORT_DIR_KEY)
                : p.getProperty(REPORT_DIR_KEY, Config.getString(REPORT_DIR_KEY));
        if (reportDirname.length()==0) {
            reportDirname = "report" + suffix + "_" + new SimpleDateFormat("yyMMdd_HHmmss_SSS").format(new Date());
        } else {
            reportDirname += suffix;
        }
        String threadName = Thread.currentThread().getName();
        if (!"main".equals(threadName) && ReportThreadLocal.isReportThreadLocal) {
            reportDirname += "_" + threadName;
        }
        String reportRootDir = (p == null) 
                ? Config.getString(REPORT_DIR_ROOT_KEY) 
                : p.getProperty(REPORT_DIR_ROOT_KEY, Config.getString(REPORT_DIR_ROOT_KEY));
        if (reportRootDir.length()==0) {
            reportRootDir = TestConfig.CONFIG_PROPERTIES.getProperty("results") + File.separator +
                            Utils.getCurrentTimeDate(TestConfig.CONFIG_PROPERTIES.getProperty("reportDateTimeFormat"));
        }
        reportDir = new File(reportRootDir, reportDirname);
        customLoggers = new ArrayList<Logger>();
        customLoggers.add(Logger.getLogger("reportResultLogger"));
    }
    
    private void prepareReportTemplate() {
        if (reportDir.exists()) {
            try {
                FileUtils.cleanDirectory(reportDir);
            } catch (IOException e) {
                log.warn("Failed to clean existing report dir: " + reportDir.getPath());
            }
        }
        File pagesDir = new File(reportDir, "pages");
        pagesDir.mkdirs();
        try {
            URLConnection connection = this.getClass().getClassLoader().getResource(reportJarDir).openConnection();
            if (connection instanceof JarURLConnection) {
                JarFile archive = ((JarURLConnection)connection).getJarFile();
                Enumeration<? extends JarEntry> e = archive.entries();
                while (e.hasMoreElements()) {
                    JarEntry entry = e.nextElement();
                    String entryName = entry.getName();
                    if (!entryName.startsWith(reportJarDir) || entryName.equals(reportJarDir)) continue;
                    File file = new File(reportDir, entry.getName().substring(reportJarDir.length()));
                    if (entry.isDirectory() && !file.exists()) {
                        file.mkdirs();
                    } else {
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdirs();
                        }
                        InputStream is = null;
                        OutputStream os = null;
                        try {
                            is = archive.getInputStream(entry);
                            os = new FileOutputStream(file);
                            byte[] buffer = new byte[8192];
                            int read;
                            while ((read=is.read(buffer))!=-1) {
                                os.write(buffer, 0, read);
                            }
                        } finally {
                            Utils.close(is, os);
                        }
                    }
                }
            } else {
                //Use folder with class. Need for use in IDE, when current project link to each other when sources has compiled, but jar file hasn`t created.  
                FileUtils.copyDirectory(new File(connection.getURL().toURI()), reportDir);
            }
        } catch (IOException e) {
            log.error("Unable to find report files", e);
            return;
        } catch (URISyntaxException e) {
            log.error("Unable to find report files", e);
            return;
        }

        indexFile = new File(reportDir, "list.html");
        if (! indexFile.exists()) {
            Utils.writeStringToFile(indexFile, 
                "<head>\n" +
                "  <base target=\"report\"/>\n" +
                "  <title>Reports list</title>\n" +
                "  <script type=\"text/javascript\" src=\"template/list.js\"></script>\n" +
                "  <link rel=\"stylesheet\" type=\"text/css\" href=\"template/list.css\">\n" +
                "</head>\n");
        }
    }
    
    /**
     * Initialize Report with properties. In general you shouldn't call this method directly, Scenario does it for you.
     * @param p - properties collection
     */
    public static void init(Properties p) {
        WebReportWriter.report.set(new WebReportWriter(p));
    }
    
    /**
     * Get the report instance with the lazy initialization
     * @return a report instance
     */
    public static WebReportWriter getWebReportWriter() {
        WebReportWriter report = WebReportWriter.report.get();
        if (report==null) {
            report = new WebReportWriter(null);
            WebReportWriter.report.set(report);
        }
        return report;
    }
    
    /**
     * Opens a new log that indicates of the new scenario. Every log is stored in the separate XML file.
     * @param logName - The name that log will use.
     */
    public synchronized void openLog(final String logName) {
        openLog(logName, "");
    }
    /**
     * Opens a new log that indicates of the new scenario. Every log is stored in the separate XML file.
     * @param logName - The name that log will use.
     * @param description - The description for scenario.
     */
    public synchronized void openLog(final String logName, String description) {
        if(!isInit) {
            prepareReportTemplate();
            isInit = true;
        }
        this.description = description;
        String  filename = "report" + String.format("%04d", logId ++) + ".xml";
        currentLogName = logName;
        currentLogCallStack.clear();
        timeStack.clear();
        reportFile = new File(reportDir, filename);
        //TODO Do?
        reportFile.delete();
        appendReportFile("<?xml version=\"1.0\" encoding=\"windows-1251\"?>\n" +
                "<?xml-stylesheet type=\"text/xsl\" href=\"template/tree.xsl\"?>\n" +
                "<LOG date=\""+dateFormat.format(new Date())+"\" t=\""+timeFormat.format(new Date())+"\">\n" +
                "<logName><![CDATA["+logName+"]]></logName>\n");
        reportFileLength = reportFile.length();
        timeStack.push(System.currentTimeMillis());
        msgId = 0;
        maxPriorityReported = Priority.INFO_INT;
        indexFileLength = indexFile.length();
    }

    /**
     * Closes the currently opened log if any.
     */
    public void closeLog() {
        //do nothing, log already closed
    }
    
    /**
     * Append tail of log
     */
    private void appendTail() {
        if(reportFile==null) return;
        reportFileLength = reportFile.length();
        for (String ignored : currentLogCallStack) {
            appendReportFile(formatMessageClose());
        }
        appendReportFile(formatMessageTime(timeStack.lastElement(),System.currentTimeMillis())+"</LOG>");
        FileChannel indexFileChannel = null;
        try {
            indexFileChannel = new RandomAccessFile(indexFile, "rw").getChannel();
            byte[] indexBytes = ("<div class=\"" + Level.toLevel(maxPriorityReported).toString().toLowerCase()+"\">" +
                    "<a href=\"" + reportFile.getName() + "\" " + (description.length()==0?"":"TITLE=\"" + description.replaceAll("\"","'") + "\" ") +
                    " onclick=\"highlight(this);\">" + currentLogName + "</a>" +
                    "</div>").getBytes();
            indexFileChannel.write(ByteBuffer.wrap(indexBytes),indexFileLength);
            indexFileChannel.truncate(indexFileLength + indexBytes.length);
        } catch (IOException e) {
            log.error("Unable to write to index file", e);
        } finally {
            Utils.close(indexFileChannel);
        }
    }

    /**
     * Opens the section inside a log. A section is the node in the tree, that has subnodes, snapshot and a message in the details. 
     * @param sectionName - Section name
     * @param message - a message in the details
     * @param page - a takeSnapshot source
     */
    public synchronized void openSection(String sectionName, String message, SourceProvider page) {
        currentLogCallStack.push(sectionName);
        appendReportFile(formatMessageOpen(sectionName, null, message, page),reportFileLength);
        for (Logger logger:customLoggers) {
            logger.info(currentLogCallStack.toString());
        }
        timeStack.push(System.currentTimeMillis());
        appendTail();
    }
    
    /**
     * Closes the currently opened section if any
     */
    public synchronized void closeSection() {
        if (currentLogCallStack.isEmpty()) {
            return;
        }
        currentLogCallStack.pop();
        appendReportFile(formatMessageTime(timeStack.pop(),System.currentTimeMillis())+formatMessageClose(),reportFileLength);
        appendTail();
    }
    
    public synchronized void message(String title, Level level, String message, SourceProvider page) {
        maxPriorityReported = Math.max(maxPriorityReported, level.toInt());
        appendReportFile(formatMessageOpen(title, level, message, page)+formatMessageClose(),reportFileLength);

        String msg = currentLogCallStack.toString() +
            " Title: " + (title == null ? "" : title ) + ", Message: "+message==null?"":message;

        for (Logger customerLogger:customLoggers) {
            customerLogger.log(level, msg);
        }
        appendTail();
    }
    
    private File createSnapshot(SourceProvider page) {
        String pageFilename = "takeSnapshot"+String.format("%04d", pageId++);
        File pageFileHTML = new File(new File(reportDir, "pages"), pageFilename + "."+page.getExtension());
        Utils.writeStringToFile(pageFileHTML, page.getSource());
        return pageFileHTML;
    }
    
    private String formatMessageOpen(String title, Level level, String message, SourceProvider page) {
        StringBuilder result = new StringBuilder();
        result.append("<MSG id=\"").append(msgId++).append("\"");
        result.append(" t=\"").append(timeFormat.format(new Date())).append("\"");
        if (level!=null) {
            result.append(" status=\"").append(level.toString().toLowerCase()).append("\"");
        }
        if (page!=null) {
            File snapShotFile = createSnapshot(page);
    	    result.append(" snapshot=\"").append(snapShotFile.getName()).append("\"");
        }
        result.append(">");
        result.append("<title><![CDATA[").append(title!=null&&title.length()>0?title:"[message]").append("]]></title>");
        if (message!=null && message.length()>0) {
            result.append("<message><![CDATA[").append(message).append("]]></message>");
        }
        return result.toString();
    }
    private String formatMessageClose() {
        return "</MSG>\n";
    }
    private String formatMessageTime(long startTime, long finishTime) {
        return "<TIME t=\""+(finishTime-startTime)/1000+"\" />";
    }

    /**
     * Add a custom logger to the report, so the output will be redirected there too.
     * @param logger - a custom logger
     */
    public void addLogger(Logger logger) {
        customLoggers.add(logger);
    }
    
    private void appendReportFile(String data) {
        appendReportFile(data, -1);
    }
    
    private void appendReportFile(String data, long position) {
        if (reportFile==null) return;
        FileChannel fileChannel = null;
        byte[] dataBytes = data.getBytes();
        try {
            fileChannel = new RandomAccessFile(reportFile, "rw").getChannel();
            if(position>0) {
                fileChannel.write(ByteBuffer.wrap(dataBytes),position);
                fileChannel.truncate(position+dataBytes.length);
            } else {
                fileChannel.write(ByteBuffer.wrap(dataBytes),fileChannel.size());
            }
        } catch (IOException e) {
            log.error("Unable to write to " + reportFile.getName(), e);
        } finally {
            try {
                if(fileChannel!=null)fileChannel.close();
            } catch (IOException e) {
                log.error("Unable to close " + reportFile.getName(), e);
            }
        }
    }

    public File getReportDir() {
        return reportDir;
    }


    public List<Logger> getCustomLoggers() {
        return customLoggers;
    }

    private static class ReportThreadLocal extends ThreadLocal<WebReportWriter> {
        private static boolean isReportThreadLocal = Boolean.parseBoolean(Config.getString(REPORT_THREAD_LOCAL_KEY));
        private WebReportWriter report;
        @Override
        public WebReportWriter get() {
            if(isReportThreadLocal) {
                return super.get();
            } else {
                return report;
            }
        }

        @Override
        public void set(WebReportWriter value) {
            if(isReportThreadLocal) {
                super.set(value);
            } else {
                report = value;
            }
        }
    }
}

