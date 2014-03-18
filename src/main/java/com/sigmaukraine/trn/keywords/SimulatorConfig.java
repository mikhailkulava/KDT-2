package com.sigmaukraine.trn.keywords;

import com.sigmaukraine.trn.testUtils.PropertyManager;
import com.sigmaukraine.trn.testUtils.SftpManager;

import java.util.Map;

/**
 * Created by mkulava on 07.03.14.
 */
public class SimulatorConfig {

    public static void execute(Map<String, String> parametersAndValues){
        SftpManager sftpManager = new SftpManager();
        sftpManager.establishConnection();

        sftpManager.downloadFile(parametersAndValues.get("remotePath") +
                parametersAndValues.get("fileName"),
                parametersAndValues.get("localPath"));

        PropertyManager.setPropertyValue(parametersAndValues.get("localPath") +
                parametersAndValues.get("fileName"),
                parametersAndValues.get("propertyName"),
                parametersAndValues.get("propertyValue"));

        sftpManager.uploadFile(parametersAndValues.get("localPath") +
                parametersAndValues.get("fileName"),
                parametersAndValues.get("remotePath"));

        sftpManager.closeConnection();
    }

}
