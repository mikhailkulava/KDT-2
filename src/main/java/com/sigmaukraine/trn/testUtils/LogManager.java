package com.sigmaukraine.trn.testUtils;

import com.sigmaukraine.trn.testEntities.TestCase;
import com.sigmaukraine.trn.testEntities.TestSuite;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by mkulava on 07.03.14.
 */
public class LogManager {
    private static Logger log = Logger.getLogger("main");
    static FileHandler testCaseFileHandler;
    static FileHandler testSuiteFileHandler;

    public static void info(String message){
        log.info(message);
    }

    public static void warning(String message){
        log.warning(message);
    }

    public static void openTestSuite(TestSuite testSuite){
        LogManager.info("");
        LogManager.info("******************************************" + testSuite.getScenarioName() + "******************************************");
        LogManager.info("");
        String scenarioReportDir = DirectoryManager.createDir(TestConfig.REPORT_DIR + File.separator +
                testSuite.getScenarioName() +
                "_" + getCurrentTimeDate(TestConfig.CONFIG_PROPERTIES.getProperty("reportDirDateTimeFormat")));
        testSuite.setScenarioReportDir(scenarioReportDir);
         testSuiteFileHandler = addFileHandler(scenarioReportDir + File.separator + "com.sigmaukraine.trn.Main.log");

    }

    public static void closeTestSuite(){
        closeFileHandler(testSuiteFileHandler);
        LogManager.info("");
        LogManager.info("-----------------------------------------------------TEST SUITE END-----------------------------------------------------");
        LogManager.info("");
    }

    public static void openTestCase(TestCase testCase){
        LogManager.info("");
        LogManager.info("------------------------------------------" + testCase.getTestCaseName() + "-----------------------------------------");
        LogManager.info("");

        String testCaseReportDir = DirectoryManager.createDir( testCase.getParentTestSuite().getScenarioReportDir() + File.separator + testCase.getTestCaseName() +
                                                              "_" + getCurrentTimeDate(TestConfig.CONFIG_PROPERTIES.getProperty("reportDirDateTimeFormat")));
        testCaseFileHandler = addFileHandler(testCaseReportDir + File.separator + testCase.getTestCaseName() + ".log");

    }

    public static void closeTestCase(){
        closeFileHandler(testCaseFileHandler);
    }

    //This methods saves log to a file
    public static FileHandler addFileHandler(String destFile){
        FileHandler fileHandler = null;
        try {
            // This block configure the logger with handler and formatter
            fileHandler = new FileHandler(destFile);
            log.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (SecurityException e) {
            LogManager.warning(e.getLocalizedMessage());
        } catch (IOException e) {
            LogManager.warning(e.getLocalizedMessage());
        }
        return fileHandler;
    }

    private static void closeFileHandler(FileHandler fileHandler){
        fileHandler.close();
        log.removeHandler(fileHandler);
    }

    private static String getCurrentTimeDate(String dateTimeFormat){
        String currentTimeDate;
        DateFormat dateFormat = new SimpleDateFormat(dateTimeFormat);
        //get current date time with Date()
        Date date = new Date();
        return currentTimeDate = dateFormat.format(date).toString();
    }
}


