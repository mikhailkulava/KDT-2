package com.sigmaukraine.trn.testUtils;

import java.io.*;

/**
 * Created by mkulava on 11.02.14.
 */
public class DirectoryManager {

    public static String createDir(String dirPath){
        File file = new File(dirPath);
        if(!file.exists()){
           file.mkdirs();
           LogManager.info("Creating directory: " + file);
        }
        else{
           LogManager.info("Directory " + dirPath + " already exists");
        }
        return dirPath;
    }

    public static void removeDir(File dirToDelete){
        if (!dirToDelete.exists()){
            return;
        }
        if (dirToDelete.isDirectory()) {
            for (File f : dirToDelete.listFiles()) {
                LogManager.info("Removing directory: " + f);
                removeDir(f);
            }
        }
        dirToDelete.delete();
    }
}





