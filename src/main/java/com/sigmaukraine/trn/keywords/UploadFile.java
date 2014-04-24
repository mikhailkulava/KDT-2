package com.sigmaukraine.trn.keywords;

import com.sigmaukraine.trn.testUtils.SftpManager;

import java.util.Map;

/**
 * This keyword uploads file to server
 */
public class UploadFile {
    /**
     * Keyword execution method
     * @param parametersAndValues - stores parameters and values required
     */
    public static void execute(Map<String, String> parametersAndValues){
        SftpManager sftpManager = new SftpManager();
        sftpManager.establishConnection();
        sftpManager.uploadFile(parametersAndValues.get("From"), parametersAndValues.get("To"));
        sftpManager.closeConnection();
    }
}
