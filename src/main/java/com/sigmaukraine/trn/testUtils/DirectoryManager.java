package com.sigmaukraine.trn.testUtils;

import com.sigmaukraine.trn.report.Log;

import java.io.File;

/**
 * This class provides simple directory actions, as deleting and creating directories.
 */
public class DirectoryManager {
    /**
     * Create directory method.
     * @param dirPath - path where directory has to be created (including directory to be created name)
     * @return - returns created directory path
     */
    public static String createDir(String dirPath){
        File file = new File(dirPath);
        if(!file.exists()){
           file.mkdirs();
           Log.info("Creating directory", "creating directory: " + file);
        }
        else{
           Log.info("Directory " + dirPath + " already exists");
        }
        return dirPath;
    }

    /**
     * Delete directory method.
     * @param dirToDelete - path of a directory to be deleted.
     */
    public static void removeDir(File dirToDelete){
        if (!dirToDelete.exists()){
            return;
        }
        if (dirToDelete.isDirectory()) {
            for (File file : dirToDelete.listFiles()) {
                Log.info("Removing directory", "removing directory: " + file);
                removeDir(file);
            }
        }
        dirToDelete.delete();
    }
}





