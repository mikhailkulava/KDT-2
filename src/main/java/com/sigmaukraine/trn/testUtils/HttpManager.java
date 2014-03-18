package com.sigmaukraine.trn.testUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.util.Map;

/**
 * Created by mkulava on 11.02.14.
 */
public class HttpManager {

    //this method parses request file and inputs needed properties
    public static String createRequestFromTemplate(String xmlFilePath, Map<String, String> placeholders){
        LogManager.info("Getting request template: " + xmlFilePath);
        String requestBody = FileManager.getTxtFileContent(xmlFilePath);
        LogManager.info("Replacing placeholders");
        for(Map.Entry<String, String> entry : placeholders.entrySet()){
            requestBody = requestBody.replace("$" + entry.getKey(), entry.getValue());
        }
        LogManager.info("Request body created successfully!");
        LogManager.info("Request body: \n\n" + requestBody);
        return requestBody;
    }

    //this method sens request and saves response to a file
    public static String sendSoapRequest(String request, String endPointURI){
        String responseBody = "";
        String gmsURL ="http://" + TestConfig.SERVER_PROPERTIES.getProperty("GMSURL");
        String gmsPort = TestConfig.SERVER_PROPERTIES.getProperty("GMSPort");
        try {
            HttpClient httpclient = HttpClientBuilder.create().build();
            StringEntity strEntity = new StringEntity(request);
            LogManager.info("Sending request to: " + gmsURL + ":" + gmsPort + endPointURI);
            HttpPost post = new HttpPost(gmsURL + ":" + gmsPort + endPointURI);
            post.addHeader("Content-type", "text/xml");
            post.setEntity(strEntity);
            // Execute request
            HttpResponse response = httpclient.execute(post);
            HttpEntity respEntity = response.getEntity();
            responseBody = EntityUtils.toString(respEntity);
            responseBody = FileManager.formatStringToXML(responseBody);
            LogManager.info("Response received: \n\n" + responseBody);
        } catch (IOException e) {
            LogManager.warning(e.getLocalizedMessage());
        }
        return responseBody;
    }
}
