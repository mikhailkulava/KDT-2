package com.sigmaukraine.trn.keywords;

import com.sigmaukraine.trn.report.Log;
import com.sigmaukraine.trn.testUtils.SftpManager;

import java.util.Map;

/**
 * This keyword remove remote directory by @param - dirPath
 */
public class RemoteDirRemove {
    /**
     * Keyword execution method
     * @param parametersAndValues - stores parameters and values required
     */
    public static void execute(Map<String, String> parametersAndValues){
        SftpManager sftpManager = new SftpManager();
        sftpManager.establishConnection();
        try{
            sftpManager.removeRemoteDir(parametersAndValues.get("dirPath"));
        } catch (Exception e){
            Log.error("Exception occurred!", e.getLocalizedMessage());
        }
        sftpManager.closeConnection();
    }

}
