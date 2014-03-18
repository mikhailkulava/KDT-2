package com.sigmaukraine.trn.testUtils;

import com.jcraft.jsch.*;


/**
 * Created by mkulava on 31.01.14.
 */
public class SftpManager {
    private Session session = null;
    private Channel channel = null;
    private ChannelSftp sftpChannel = null;

    //establishing connection method
    public void establishConnection(){
        LogManager.info("Establishing connection with SFTP server");
        try{
            //setting connection credentials
            String host = TestConfig.SERVER_PROPERTIES.getProperty("sftpurl");
            String login = TestConfig.SERVER_PROPERTIES.getProperty("sftpuser");
            String password = TestConfig.SERVER_PROPERTIES.getProperty("sftppassword");
            int port = Integer.parseInt(TestConfig.SERVER_PROPERTIES.getProperty("sftpport"));
            LogManager.info("Host: " + host);
            LogManager.info("Port: " + port);
            LogManager.info("Login: " + login);

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
            LogManager.warning("Failed to establish connection!" + e.getLocalizedMessage());
        } finally {
            LogManager.info("Connected successfully!");
        }
    }

    //download file method
    public void downloadFile(String srcFile, String destFolder){
        DirectoryManager.createDir(destFolder);
        try{
            sftpChannel.get(srcFile, destFolder);
        } catch (SftpException e) {
            LogManager.warning("Failed to download file!" + e.getLocalizedMessage());
        } finally {
            LogManager.info("File: " + srcFile + " was downloaded to: " + destFolder);
        }

    }

    //upload file method
    public void uploadFile(String srcFile, String destFolder){
        try{
            createRemoteDir(destFolder);
            LogManager.info("uploading file: " + srcFile + " to: " + destFolder);
            sftpChannel.put(srcFile, destFolder);
        } catch (Exception e) {
            LogManager.warning("Failed to upload file! " + e.getLocalizedMessage());
        } finally {
            LogManager.info("File was uploaded successfully!");
        }
    }

    //create remote directory if doesn't exist
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
                        LogManager.info("Directory: \"" + folder + "\" created");
                        sftpChannel.mkdir( folder );
                        sftpChannel.cd( folder );
                    }
                    catch (SftpException e1){
                        LogManager.warning(e.getLocalizedMessage());
                    }
                }
            }
        }

      }

    //delete directory method
    public void removeRemoteDir(String path){
            try{
                LogManager.info("Removing directory: \"" + path + "\"");
                sftpChannel.rm(path + "*");
                sftpChannel.rmdir(path);
            } catch (SftpException e){
               LogManager.warning(e.getLocalizedMessage());
            } finally {
                LogManager.info("Directory removed successfully");
            }
    }

    public void closeConnection(){
        LogManager.info("Closing connection!");
        sftpChannel.disconnect();
        channel.disconnect();
        session.disconnect();
    }
}
