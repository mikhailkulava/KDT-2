package com.sigmaukraine.trn;

import com.sigmaukraine.trn.testEntities.TestSuite;

import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        List<TestSuite> testScenarios = new LinkedList<TestSuite>();

        testScenarios.add(new TestSuite("Pre-requisites KDT Scenario 1"));
        testScenarios.add(new TestSuite("KDT Scenario 1"));
        testScenarios.add(new TestSuite("KDT Scenario 2"));

        for (TestSuite testSuite : testScenarios){
            testSuite.execute();
        }
    }
}


























