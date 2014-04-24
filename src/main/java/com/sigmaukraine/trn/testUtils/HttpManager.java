package com.sigmaukraine.trn.testUtils;

import com.sigmaukraine.trn.report.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

/**
 * This class provides simple HTTP actions, such as sending SOAP request and XSD validation
 */
public class HttpManager {

    /**
     * This method sends SOAP request and returns response body as String
     * @param request - request body
     * @param endPointURI - endpoind
     */
    public static String sendSoapRequest(String request, String endPointURI){
        String responseBody = "";
        String gmsURL ="http://" + TestConfig.SERVER_PROPERTIES.getProperty("GMSURL");
        String gmsPort = TestConfig.SERVER_PROPERTIES.getProperty("GMSPort");
        try {
            HttpClient httpclient = HttpClientBuilder.create().build();
            StringEntity strEntity = new StringEntity(request);
            Log.info("Sending SOAP request", "Endpoint: " + gmsURL + ":" + gmsPort + endPointURI);
            HttpPost post = new HttpPost(gmsURL + ":" + gmsPort + endPointURI);
            post.addHeader("Content-type", "text/xml");
            post.setEntity(strEntity);
            // Execute request
            HttpResponse response = httpclient.execute(post);
            HttpEntity respEntity = response.getEntity();
            responseBody = EntityUtils.toString(respEntity);
            responseBody = FileManager.formatStringToXML(responseBody);
            Log.info("Response received: \n\n", responseBody);
        } catch (IOException e) {
            Log.error("IO exception occurred!", e);
        }
        return responseBody;
    }

    /**
     * This method validates XML document agains XSD -schema
     * @param xsdPath - path to XSD-schema
     * @param xmlBody - body of XML-document that needs to be validated
     * @return - returns boolean (true or false)
     */
    public static boolean validateXMLSchema(String xsdPath, String xmlBody){
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new StringReader(xmlBody)));
        } catch (IOException e){
            Log.error("IO exception occurred!", e);
            return false;
        } catch (SAXException e){
            Log.error("SAX exception occurred!", e);
            return false;
        }
        return true;
    }
}
