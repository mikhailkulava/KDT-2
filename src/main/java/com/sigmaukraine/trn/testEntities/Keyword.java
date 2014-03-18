package com.sigmaukraine.trn.testEntities;

import com.sigmaukraine.trn.keywords.LocalDirRemove;
import com.sigmaukraine.trn.keywords.PlayerInfoGet;
import com.sigmaukraine.trn.keywords.RemoteDirRemove;
import com.sigmaukraine.trn.keywords.UploadFile;
import com.sigmaukraine.trn.testUtils.LogManager;
import com.sigmaukraine.trn.keywords.SimulatorConfig;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * This class contains parameters and values for each keyword, and execution method
 */
public class Keyword {
    private String keywordName;
    private TestCase parentTestCase;
    private Map<String, String> parametersAndValues = new HashMap<String, String>();

    Keyword(List<String[]> keywordContent){
        keywordName = keywordContent.get(0)[1];
        ListIterator<String[]> listIterator = keywordContent.listIterator();
        while (listIterator.hasNext()){
            String[] currentLine = listIterator.next();
            parametersAndValues.put(currentLine[2],currentLine[3]);
        }
    }

    public void print(){
        for (Map.Entry<String, String> entry : parametersAndValues.entrySet()){
            LogManager.info("           " + entry.getKey() + " = " + entry.getValue());
        }
    }

    //executing current keyword method
    public void execute(){
        //executing keyword SimulatorConfig
        if(keywordName.equals("simulator_config")){
            LogManager.info("EXECUTING KEYWORD: " + keywordName);
            SimulatorConfig.execute(parametersAndValues);
        }
        //executing keyword PlayerInfoGet
        else if (keywordName.equals("playerInfo_get")){
            LogManager.info("EXECUTING KEYWORD: " + keywordName);
            PlayerInfoGet.execute(parametersAndValues);
        }
        //executing keyword RemoteDirRemove
        else if(keywordName.equals("remoteDirRemove")){
            LogManager.info("EXECUTING KEYWORD: " + keywordName);
            RemoteDirRemove.execute(parametersAndValues);
        }
        //executing keyword LocalDirRemove
        else if(keywordName.equals("localDirRemove")){
            LogManager.info("EXECUTING KEYWORD: " + keywordName);
            LocalDirRemove.execute(parametersAndValues);
        }
        else if(keywordName.equals("uploadFile")){
            LogManager.info("EXECUTING KEYWORD: " + keywordName);
            UploadFile.execute(parametersAndValues);
        }
    }

    public String getKeywordName() {
        return keywordName;
    }

    public void setParentTestCase(TestCase parentTestCase) {
        this.parentTestCase = parentTestCase;
    }
}
