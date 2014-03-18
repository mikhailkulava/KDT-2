package com.sigmaukraine.trn.keywords;

import com.sigmaukraine.trn.testUtils.DirectoryManager;

import java.io.File;
import java.util.Map;

/**
 * Created by mkulava on 07.03.14.
 */
public class LocalDirRemove {

     public static void execute(Map<String, String> parametersAndValues){
        DirectoryManager.removeDir(new File(parametersAndValues.get("dirPath")));
    }
}
