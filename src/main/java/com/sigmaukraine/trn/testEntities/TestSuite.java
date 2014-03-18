package com.sigmaukraine.trn.testEntities;

import com.sigmaukraine.trn.testEntities.TestCase;
import com.sigmaukraine.trn.testUtils.FileManager;
import com.sigmaukraine.trn.testUtils.LogManager;
import com.sigmaukraine.trn.testUtils.TestConfig;

import java.io.File;
import java.util.*;

/**
 * Created by mkulava on 04.02.14.
 */
public class TestSuite {
    private String scenarioName;
    private List<TestCase> testCaseList = new LinkedList<TestCase>();
    private List<String []> testSuiteContent;
    private String scenarioReportDir;


    public TestSuite(String testScenarioName){
        scenarioName = testScenarioName;
        String scenarioPath = TestConfig.SCENARIOS_DIR + File.separator + scenarioName + ".csv";
        testSuiteContent = FileManager.getCsvFileContent(scenarioPath);
        testSuiteContent = testSuiteContent.subList(1,testSuiteContent.size());

        ListIterator<String[]> listIterator = testSuiteContent.listIterator();
        List<String[]> testCaseContent = new LinkedList<String[]>();
        while(listIterator.hasNext()){
            String[] currentLine = listIterator.next();
            if(!currentLine[0].isEmpty() && testCaseContent.size() != 0){
                TestCase testCase = new TestCase(testCaseContent);
                testCase.setParentTestSuite(this);
                testCaseList.add(testCase);
                testCaseContent.clear();
            }
            testCaseContent.add(currentLine);
            if(currentLine[0].isEmpty() && !listIterator.hasNext()){
                TestCase testCase = new TestCase(testCaseContent);
                testCase.setParentTestSuite(this);
                testCaseList.add(testCase);
            }
        }
    }

    public void execute(){
        LogManager.openTestSuite(this);
        print();
        for(TestCase testCase : testCaseList){
            LogManager.openTestCase(testCase);
            testCase.execute();
            LogManager.closeTestCase();
        }
        LogManager.closeTestSuite();
    }
    public void print(){
        LogManager.info("Test suite \"" + scenarioName + "\" content: ");
        for(TestCase testCase : testCaseList){
            LogManager.info(testCase.getTestCaseName());
            testCase.print();
        }
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public List<TestCase> getTestCaseList() {
        return testCaseList;
    }

    public String getScenarioReportDir() {
        return scenarioReportDir;
    }

    public void setScenarioReportDir(String scenarioReportDir) {
        this.scenarioReportDir = scenarioReportDir;
    }
}
