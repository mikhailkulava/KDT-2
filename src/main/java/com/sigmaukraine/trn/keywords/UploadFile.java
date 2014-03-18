package com.sigmaukraine.trn.keywords;

import com.sigmaukraine.trn.testUtils.SftpManager;

import java.util.Map;

/**
 * Created by mkulava on 14.03.14.
 */
public class UploadFile {
    public static void execute(Map<String, String> parametersAndValues){
        SftpManager sftpManager = new SftpManager();
        sftpManager.establishConnection();
        sftpManager.uploadFile(parametersAndValues.get("From"), parametersAndValues.get("To"));
        sftpManager.closeConnection();
    }
}
