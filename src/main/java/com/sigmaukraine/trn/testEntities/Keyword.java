package com.sigmaukraine.trn.testEntities;

import com.sigmaukraine.trn.keywords.*;
import com.sigmaukraine.trn.testUtils.WebDriverManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains parameters and values for each keyword, and execution method
 */
public class Keyword {
    private String keywordName;
    private Map<String, String> parametersAndValues = new HashMap<String, String>();

    Keyword(List<String[]> keywordContent){
        keywordName = keywordContent.get(0)[1];
        for (String[] currentLine : keywordContent) {
            String paramName = currentLine.length < 3 ? "" : currentLine[2];
            String paramValue = currentLine.length < 4 ? "" : currentLine[3];
            parametersAndValues.put(paramName, paramValue);
        }
    }

    /**
     * Keyword execution method. If keyword name matches condition, then keyword execution method is called.
     */
    public void execute(){
        if(keywordName.equals("simulator_config")){
            SimulatorConfig.execute(parametersAndValues);
        }
        else if (keywordName.equals("playerInfo_get")){
            PlayerInfoGet.execute(parametersAndValues);
        }
        else if(keywordName.equals("remoteDirRemove")){
            RemoteDirRemove.execute(parametersAndValues);
        }
        else if(keywordName.equals("localDirRemove")){
            LocalDirRemove.execute(parametersAndValues);
        }
        else if(keywordName.equals("uploadFile")){
            UploadFile.execute(parametersAndValues);
        }
        else if (keywordName.equals("vk_login")){
            VkLogin.execute(parametersAndValues);
        }
        else if (keywordName.equals("vk_navigatePage")){
            VkNavigatePage.execute(parametersAndValues);
        }
        else if (keywordName.equals("vk_allPostsLike")){
            VkAllPostsLike.execute();
        }
        else if (keywordName.equals("vk_allVideosLike")){
            VkAllVideosLike.execute();
        }
        else if(keywordName.equals("vk_allPhotosLike")){
            VkAllPhotosLike.execute();
        }
        else if (keywordName.equals("driver_close")){
            WebDriverManager.closeWebDriver();
        }
        else if (keywordName.equals("updatePlayerSession")){
            UpdatePlayerSession.execute(parametersAndValues);
        }
    }

    /**
     * @return - keyword name getter
     */
    public String getKeywordName() {
        return keywordName;
    }

     /**
     * @return - parameters and values getter
     */
    public Map<String, String> getParametersAndValues() {
        return parametersAndValues;
    }
}
