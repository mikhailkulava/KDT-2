package com.sigmaukraine.trn.keywords;

import com.sigmaukraine.trn.testUtils.DirectoryManager;

import java.io.File;
import java.util.Map;

/**
 * This keyword deletes local directory by @param - dirPath
 */
public class LocalDirRemove {
    /**
     * Keyword execution method
     * @param parametersAndValues - is a map that contains directory Path
     */
     public static void execute(Map<String, String> parametersAndValues){
        DirectoryManager.removeDir(new File(parametersAndValues.get("dirPath")));
    }
}
