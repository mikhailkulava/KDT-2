package com.sigmaukraine.trn.keywords;

import com.sigmaukraine.trn.playerDetails.PlayerDetails;
import com.sigmaukraine.trn.playerDetails.PlayerDetailsConverter;
import com.sigmaukraine.trn.report.Log;
import com.sigmaukraine.trn.testUtils.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This keyword gets player's information (by @param - playerID) from database and SOAP response. Also provides XSD validation of the response.
 **/
public class PlayerInfoGet {
    /**
     * Keyword execution method
     * @param parametersAndValues - stores parameters required for keyword execution
     */
    public static void execute(Map<String, String> parametersAndValues){
        //actual result map
        Map<String, String > actualResultMap = new HashMap<String, String>();

        //getting expected result map
        DbManager dbManager = new DbManager();
        String query = TemplateManger.replacePlaceholders(
                                                          FileManager.getTxtFileContent(TestConfig.CONFIG_PROPERTIES.getProperty("getPlayerDetailsSql")),
                                                          parametersAndValues);
        Map<String, String> expectedResultMap = dbManager.rows(query);

        String currentPlayerSession = dbManager.rows(
                                                        TemplateManger.replacePlaceholders(
                                                                                            FileManager.getTxtFileContent(
                                                                                            TestConfig.CONFIG_PROPERTIES.getProperty("getSessionByPlayerID")),
                                                        parametersAndValues)).get ("EXTERNAL_CLIENT_SESSION_ID");

        //creating request from a template
        parametersAndValues.put("playerSessionUID", currentPlayerSession);
        String request = TemplateManger.replacePlaceholders(
                                                            FileManager.getTxtFileContent(TestConfig.CONFIG_PROPERTIES.getProperty("getPlayerDetailsSoap")),
                                                            parametersAndValues);
        Log.info("Request ready: ", request);
        String response = HttpManager.sendSoapRequest(request,
                parametersAndValues.get("endpointURI"));

        try {
            //parsing response
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder =  builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(new ByteArrayInputStream(response.getBytes()));
            XPath xPath =  XPathFactory.newInstance().newXPath();
            Node currentNode;
            String xPathTemplate = PropertyManager.getProperties(TestConfig.CONFIG_PROPERTIES.getProperty("getPlayerDetailsXpath")).getProperty("getPlayerDetails");

            //getting actual result map
            for(Map.Entry<String, String> entry : expectedResultMap.entrySet()){
                String replaceKey = entry.getKey();
                if(replaceKey.equals("IdentificationManualVerified")){
                    replaceKey = "IdentificationManualVerifiedUntil";
                }
                //readyExpression = xPathTemplate.replace("$key", replaceKey);
                String readyExpression = TemplateManger.replacePlaceholders(xPathTemplate, "key", replaceKey);
                currentNode = (Node)xPath.compile(readyExpression).evaluate(xmlDocument, XPathConstants.NODE);
                actualResultMap.put(entry.getKey(), currentNode.getTextContent());
            }

            //xsd validation
            Log.info("Validating response against XSD-schema");
            Log.info("[VALIDATION] XML response valid: " + Boolean.toString(
                    HttpManager.validateXMLSchema(
                            TestConfig.CONFIG_PROPERTIES.getProperty("getPlayerDetailsXSD"), response)
            ).toUpperCase());
        } catch (FileNotFoundException e) {
            Log.error("File not found!", e);
        } catch (SAXException e) {
            Log.error("SAXException occurred!", e);
        } catch (IOException e) {
            Log.error("IO exception occurred!", e);
        } catch (ParserConfigurationException e) {
            Log.error("ParserConfigurationException exception occurred!", e);
        } catch (XPathException e){
            Log.error("XPathException exception occurred!", e);
        }

        //comparing player details from db and soap response
        PlayerDetails sqlPlayerDetails = PlayerDetailsConverter.convert(expectedResultMap);
        PlayerDetails soapPlayerDetails = PlayerDetailsConverter.convert(actualResultMap);
        Log.info("Comparing player details from DB and SOAP response");
        if(sqlPlayerDetails.equals(soapPlayerDetails)){
            Log.info("ASSERTION PASSED: Actual result == Expected result");
        }
        else {
            Log.error("ASSERTION FAILED: Actual result != Expected result");
        }
    }
}
