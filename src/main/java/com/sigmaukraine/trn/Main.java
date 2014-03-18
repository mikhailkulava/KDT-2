package com.sigmaukraine.trn;

import com.sigmaukraine.trn.testEntities.TestSuite;

import java.lang.String;

public class Main {
    public static void main(String[] args){
        TestSuite preRequisites = new TestSuite("Pre-requisites KDT Scenario 1");
        preRequisites.execute();

        TestSuite KDTscenario1 = new TestSuite("KDT Scenario 1");
        KDTscenario1.execute();
    }
}
