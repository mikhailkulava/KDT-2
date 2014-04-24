package com.sigmaukraine.trn.testUtils;

import com.sigmaukraine.trn.report.Report;
import com.sigmaukraine.trn.report.SourceProvider;
import com.sigmaukraine.trn.report.WebReportWriter;
import com.sigmaukraine.trn.testEntities.Keyword;
import com.sigmaukraine.trn.testEntities.TestCase;
import com.sigmaukraine.trn.testEntities.TestSuite;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import java.io.File;
import java.io.IOException;

/**
 * This class provides logging actions, such as:
 * <ul>
 *     <li>Structured web-report generation</li>
 *     <li>Console output</li>
 *     <li>Saving logs to file</li>
 * </ul>
 */
public class Log {
    private static Report report = Report.getReport();
    private static RollingFileAppender testSuiteFileAppender;
    private static RollingFileAppender testCaseFileAppender;
    private static String logsRootDir;
    private static int currentTestSuiteId = 1;
    private static int currentTestCaseId;
    private static int currentKeywordId;
    private static int currentTestStepId;
    private static Logger log = Logger.getLogger("logResultLogger");
    private static boolean isReportResultEnabled = Boolean.parseBoolean(TestConfig.CONFIG_PROPERTIES.getProperty("reportResult"));
    private static boolean isLogResultEnabled = Boolean.parseBoolean(TestConfig.CONFIG_PROPERTIES.getProperty("logResult"));

    /**
     * Writes INFO message to log file, console and web report
     * @param title - short message, at the web report is used for test steps short names
     */
    public static void info(String title){
        info(title, null);
        log.info(title);
    }

    /**
     * Writes INFO message to log file, console and web report
     * @param title - short message, at the web report is used for test steps short names
     * @param message - more detailed message, in log file displayed as single log message, at the web report - as test step description
     */
    public static void info(String title, String message){
        info(title, message, null);
        log.info(title);
        log.info(message);
    }

    /**
     * Writes INFO message to log file, console and web report
     * @param title - short message, at the web report is used for test steps short names
     * @param message - more detailed message, in log file displayed as single log message, at the web report - as test step description
     * @param page - page snapshot at the web report, only @title and @message displayed in the log file
     */
    public static void info(String title, String message, SourceProvider page){
        report.info(currentTestStepId + ") " + title, message, page);
        log.info(title);
        if (message != null) {
            log.info(message);
        }
        currentTestStepId++;
    }

    /**
     * Writes WARNING message to log file, console and web report
     * @param title - short message
     */
    public static void warn(String title){
        warn(title, null, null);
        log.warn(title);
    }

    /**
     * Writes WARNING message to log file, console and web report
     * @param title - short message
     * @param message - more detailed message, is displayed in description section at the web report, and as single message in log file or console
     */
    public static void warn(String title, String message){
        warn(title, message, null);
        log.warn(title);
        log.warn(message);
    }

    /**
     * Writes WARNING message to log file, console and web report
     * @param title - short message
     * @param message - more detailed message, is displayed in description section at the web report, and as single message in log file or console
     * @param page - page snapshot at the web report, only @title and @message displayed in the log file
     */
    public static void warn(String title, String message, SourceProvider page){
        report.warn(currentTestStepId + ") " + title, message, page);
        currentTestStepId++;
        log.warn(title);
        log.warn(message);
    }

    /**
     * Writes ERROR message to log file, console and web report
     * @param title - short message
     * At the web report, it's displayed as failed test step
     */
    public static void error(String title){
        error(title, title, null);
        log.error(title);
    }

    /**
     * Writes ERROR message to log file, console and web report
     * @param title - short message
     * At the web report, it's displayed as failed test step
     * @param message - stack trace is usually displayed here
     */
    public static void error(String title, String message){
        error(title, message, null);
        log.error(title);
        log.error(message);
    }

    /**
     * Writes ERROR message to log file, console and web report
     * @param title - short message
     * At the web report, it's displayed as failed test step
     * @param message - detailed error message
     * @param page - error page snapshot
     */
    public static void error(String title, String message, SourceProvider page){
        report.error(currentTestStepId + ") " + title, message, page);
        currentTestStepId++;
        log.error(title);
        log.error(message);

    }

    /**
     * Writes ERROR message to log file, console and web report
     * @param title - short message
     * @param t - contains exception
     */
    public static void error(String title, Throwable t){
        error(title, t, null);
        log.error(title);
    }

    /**
     * Writes ERROR message to log file, console and web report
     * @param title - short message
     * @param t - contains exception
     * @param page - error page snapshot
     */
    public static void error(String title, Throwable t, SourceProvider page){
        report.error(currentTestStepId + ") " + title, t, page);
        currentTestStepId++;
        log.error(title);
    }

    /**
     *Starts logging to Main.log and opens high-level section at the report.html file
     */
    public static void openTestSuite(TestSuite testSuite){
        if(isReportResultEnabled){
            currentTestCaseId = 1;
            WebReportWriter currentWebReportWriter = WebReportWriter.getWebReportWriter();
            logsRootDir = currentWebReportWriter.getReportDir().toString().replace("report", "log") + File.separator + testSuite.getScenarioName();
            report.openLog(currentTestSuiteId + " " + testSuite.getScenarioName(), testSuite.getTestSuiteContent());
        }
        else{
            logsRootDir = TestConfig.CONFIG_PROPERTIES.getProperty("results") + File.separator +
                          Utils.getCurrentTimeDate(TestConfig.CONFIG_PROPERTIES.getProperty("reportDateTimeFormat")) + File.separator +
                          "log" + File.separator +
                          testSuite.getScenarioName();
        }
        if(isLogResultEnabled){
            testSuiteFileAppender = addFileAppender(log, logsRootDir + File.separator, "Main.log");
        }
    }

    /**
     * Ends logging for Main.log and closes section for current test scenario
     */
    public static void closeTestSuite(){
        if(isLogResultEnabled){
            removeFileAppenders(log, testSuiteFileAppender);
        }
        if(isReportResultEnabled){
            report.closeLog();
            currentTestSuiteId++;
            currentTestCaseId = 1;
        }
    }

    /**
     * Starts separate logging for current test case
     * and creates test case section at the web report
     */
    public static void openTestCase(TestCase testCase){
        if(isReportResultEnabled){
            currentKeywordId = 1;
            report.openSection(currentTestSuiteId + "." + currentTestCaseId + " " + testCase.getTestCaseName());
        }
        if (isLogResultEnabled){
            testCaseFileAppender = addFileAppender(log, logsRootDir + File.separator, Utils.getCurrentTimeDate(TestConfig.CONFIG_PROPERTIES.getProperty("reportDateTimeFormat")) + "_" +
                                   testCase.getTestCaseName() + ".log");
        }
    }

    /**
     * Closes test Case log and closes test case section
     * at the web report
     */
    public static void closeTestCase(){
       if(isReportResultEnabled){
           report.closeSection();
           currentTestCaseId++;
       }
       if(isLogResultEnabled){
           removeFileAppenders(log, testCaseFileAppender);
       }
    }

    /**
     * Opens keyword section at the web report
     */
    public static void openKeyword(Keyword keyword){
        if(isReportResultEnabled){
            report.openSection(currentTestSuiteId + "." + currentTestCaseId + "." + currentKeywordId + " " + keyword.getKeywordName());
            currentTestStepId = 1;
        }
    }

    /**
     * Closes keyword section at the web report
     */
    public static void closeKeyword(){
        if(isReportResultEnabled){
            report.closeSection();
            currentKeywordId++;
        }
    }

    /**
     * Private method, used in openTestCase() and openTestSuite() methods
     * @param log - current logger instance
     * @param destPath - destination path to save log file
     * @param logFileName - log file name
     * @return - returns RollingFileAppender
     */
    private static RollingFileAppender addFileAppender (Logger log, String destPath, String logFileName ){
        PatternLayout layout = new PatternLayout("[%d{ISO8601}]%5p - %m%n");
        RollingFileAppender appender = null;
        try {
            appender = new RollingFileAppender(layout, destPath + logFileName);
            log.addAppender(appender);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appender;
    }

    /**
     * Removes file appender
     * @param log - - current logger instance
     * @param fileAppender - file appender to be removed and closed
     */
    private static void removeFileAppenders(Logger log, RollingFileAppender fileAppender){
        log.removeAppender(fileAppender);
        fileAppender.close();
    }
}
