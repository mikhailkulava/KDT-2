package com.sigmaukraine.trn.testEntities;

import com.sigmaukraine.trn.testUtils.LogManager;

import java.util.*;

/**
 * This class contains list of com.sigmaukraine.trn.keywords for current test case
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
                keyword.setParentTestCase(this);
                keywordList.add(keyword);
                keywordContent.clear();
            }
            keywordContent.add(currentLine);
            if(!listIterator.hasNext()){
                keywordContent.add(currentLine);
                Keyword keyword = new Keyword(keywordContent);
                keyword.setParentTestCase(this);
                keywordList.add(keyword);
                keywordContent.clear();
            }
        }
    }

    public void print(){
        for (Keyword keyword : keywordList){
            LogManager.info("     " + keyword.getKeywordName());
            keyword.print();
        }
    }

    public void execute(){
        LogManager.info("Executing test case: \"" + testCaseName.toUpperCase() +"\"");
        for(Keyword keyword : keywordList){
            LogManager.info("");
            keyword.execute();
        }
    }

    public List<Keyword> getKeywordList() {
        return keywordList;
    }

    public String getTestCaseName(){
        return testCaseName;
    }

    public void setParentTestSuite(TestSuite parentTestSuite) {
        this.parentTestSuite = parentTestSuite;
    }

    public TestSuite getParentTestSuite() {
        return parentTestSuite;
    }

}
