package com.sigmaukraine.trn.testUtils;

import com.jcraft.jsch.*;
import com.sigmaukraine.trn.report.Log;


/**
 * This manager provides remote file actions, such as upload, download file and creating remote directories if file path, doesn't exist
 */
public class SftpManager {
    private Session session = null;
    private Channel channel = null;
    private ChannelSftp sftpChannel = null;

    /**
     * Establishing connection with SFTP server, connection credentials are stored in: config/server.properties
     */
    public void establishConnection(){
        try{
            //setting connection credentials
            String host = TestConfig.SERVER_PROPERTIES.getProperty("sftpurl");
            String login = TestConfig.SERVER_PROPERTIES.getProperty("sftpuser");
            String password = TestConfig.SERVER_PROPERTIES.getProperty("sftppassword");
            int port = Integer.parseInt(TestConfig.SERVER_PROPERTIES.getProperty("sftpport"));
            Log.info("Establishing connection with SFTP server",
                     "\nHost: " + host + "\n" +
                     "Port: " + port + "\n" +
                     "Login: " + login + "\n");


            JSch jsch = new JSch();
            session = jsch.getSession(login, host, port);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();
            channel = session.openChannel("sftp");
            channel.connect();
            sftpChannel = (ChannelSftp)channel;
        } catch (JSchException e) {
            Log.error("Failed to establish connection!", e);
        } finally {
            Log.info("Connected successfully!");
        }
    }

    /**
     * This method downloads file from SFTP server
     * @param srcFile - path to the remote file to be downloaded
     * @param destFolder - download to path
     */
    public void downloadFile(String srcFile, String destFolder){
        DirectoryManager.createDir(destFolder);
        try{
            sftpChannel.get(srcFile, destFolder);
        } catch (SftpException e) {
            Log.error("Failed to download file!", e);
        } finally {
            Log.info("File was downloaded successfully! ", "file: " +
                    srcFile + "was downloaded to: " + destFolder);
        }

    }

    /**
     * Upload file to SFTP server
     * @param srcFile - path file at the local machine
     * @param destFolder - destination folder path
     */
    public void uploadFile(String srcFile, String destFolder){
        try{
            createRemoteDir(destFolder);
            Log.info("Uploading file", "uploading file: " + srcFile + " to: " + destFolder);
            sftpChannel.put(srcFile, destFolder);
        } catch (Exception e) {
            Log.error("Failed to upload file! ", e);
        } finally {
            Log.info("File was uploaded successfully!");
        }
    }

    /**
     * This method creates remote directory if it doesn't exist
     * @param path - remote directory path, including desired directory name
     */
    public void createRemoteDir(String path) {
        String[] folders = path.split("/");
        if(path.startsWith("/")){
            folders[0] = "/";
        }
        for ( String folder : folders ) {
            if ( folder.length() > 0 ) {
                try {
                    sftpChannel.cd(folder);
                }
                catch ( SftpException e ) {
                    try{
                        Log.info("Directory: \"" + folder + "\" created");
                        sftpChannel.mkdir( folder );
                        sftpChannel.cd( folder );
                    }
                    catch (SftpException e1){
                        Log.error("Sftp exception occurred!", e);
                    }
                }
            }
        }

      }

    /**
     * This method removes remote directory
     * @param path - path to remote directory
     */
    public void removeRemoteDir(String path){
            try{
                Log.info("Removing directory", "Removing directory: \"" + path + "\"");
                sftpChannel.rm(path + "*");
                sftpChannel.rmdir(path);
            } catch (SftpException e){
                Log.error("Sftp exception occurred!", e);
            } finally {
                Log.info("Directory removed successfully");
            }
    }

    public void closeConnection(){
        Log.info("Closing connection");
        sftpChannel.disconnect();
        channel.disconnect();
        session.disconnect();
        Log.info("Connection closed");
    }
}
