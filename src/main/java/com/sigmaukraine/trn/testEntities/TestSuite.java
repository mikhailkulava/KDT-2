package com.sigmaukraine.trn.testEntities;

import com.sigmaukraine.trn.report.Log;
import com.sigmaukraine.trn.testUtils.FileManager;
import com.sigmaukraine.trn.testUtils.TestConfig;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * This class parses test scenario, creates test cases and forwards content for each test case
 * to the test case level. Also contains execution method for test suite.
 */
public class TestSuite {
    private String scenarioName;
    private List<TestCase> testCaseList = new LinkedList<TestCase>();
    private List<String []> testSuiteContent;

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

    /**
     * Test suite execution method calls Test Case execution method, for each test case in @testCaseList
     */
    public void execute(){
        Log.openTestSuite(this);
        for(TestCase testCase : testCaseList){
            Log.openTestCase(testCase);
            testCase.execute();
        }
        Log.closeTestSuite();
    }

    /**
     * Returns test scenario as string
     */
    public String getTestSuiteContent (){
        String testSuiteContent = "";
        String tabulation = "  ";
        for (TestCase testCase : testCaseList){
            testSuiteContent = testSuiteContent + (testCase.getTestCaseName()) + "\n";
            for(Keyword keyword : testCase.getKeywordList()){
                testSuiteContent = testSuiteContent + tabulation +  (keyword.getKeywordName()) + "\n";
                for (Map.Entry<String, String> parametersAndValues : keyword.getParametersAndValues().entrySet()){
                    testSuiteContent = testSuiteContent + tabulation + tabulation + parametersAndValues.getKey() + ":" +
                                       tabulation + parametersAndValues.getValue() + "\n";
                }
            }
        }
        return testSuiteContent;
    }

    /**
     * scenario name getter
     */
    public String getScenarioName() {
        return scenarioName;
    }
}
