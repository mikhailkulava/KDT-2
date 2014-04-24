package com.sigmaukraine.trn.keywords;

import com.sigmaukraine.trn.testUtils.PropertyManager;
import com.sigmaukraine.trn.testUtils.SftpManager;

import java.util.Map;

/**
 * This keyword performs such actions:
 *      <ol>
 *      <li>Download file from SFTP server to the local machine</li>
 *      <li>Change file's properties</li>
 *      <li>Upload file back to the remote machine</li>
 *      </ol>
 */
public class SimulatorConfig {
    /**
     * Keyword execution method
     * @param parametersAndValues - stores parameters and values required
     */
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
