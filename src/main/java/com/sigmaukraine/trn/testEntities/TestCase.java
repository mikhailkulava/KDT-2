package com.sigmaukraine.trn.testEntities;

import com.sigmaukraine.trn.report.Log;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * This class contains list of keywords for current test case, execution method for each test case
 */
public class TestCase {
    private String testCaseName;
    private List<Keyword> keywordList = new LinkedList<Keyword>();
    private TestSuite parentTestSuite;

    TestCase(List<String[]> testCaseContent){
        this.testCaseName = testCaseContent.get(0)[0];
        ListIterator<String[]> listIterator = testCaseContent.listIterator();
        List<String[]> keywordContent = new LinkedList<String[]>();

        while(listIterator.hasNext()){
            String[] currentLine = listIterator.next();
            if(!currentLine[1].isEmpty() && keywordContent.size() != 0){
                Keyword keyword = new Keyword(keywordContent);
                keywordList.add(keyword);
                keywordContent.clear();
            }
            keywordContent.add(currentLine);
            if(!listIterator.hasNext()){
                keywordContent.add(currentLine);
                Keyword keyword = new Keyword(keywordContent);
                keywordList.add(keyword);
                keywordContent.clear();
            }
        }
    }

    /**
     * Test case execution method, calls keyword execution method for each keyword
     */
    public void execute(){
        for(Keyword keyword : keywordList){
            Log.openKeyword(keyword);
            keyword.execute();
        }
    }

    /**
     * Keyword list getter
     */
    public List<Keyword> getKeywordList() {
        return keywordList;
    }

    /**
     * Test case name getter
     */
    public String getTestCaseName(){
        return testCaseName;
    }

    /**
     * Parent test suite setter
     */
    public void setParentTestSuite(TestSuite parentTestSuite) {
        this.parentTestSuite = parentTestSuite;
    }

    /**
     * Parent test suite getter
     */
    public TestSuite getParentTestSuite() {
        return parentTestSuite;
    }

}
