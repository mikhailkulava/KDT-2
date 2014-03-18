package com.sigmaukraine.trn.keywords;

import com.sigmaukraine.trn.testUtils.SftpManager;
import com.sigmaukraine.trn.testUtils.LogManager;

import java.util.Map;

/**
 * Created by mkulava on 07.03.14.
 */
public class RemoteDirRemove {

    public static void execute(Map<String, String> parametersAndValues){
        SftpManager sftpManager = new SftpManager();
        sftpManager.establishConnection();
        try{
            sftpManager.removeRemoteDir(parametersAndValues.get("dirPath"));
        } catch (Exception e){
            LogManager.warning(e.getLocalizedMessage());
        }
        sftpManager.closeConnection();
    }

}
