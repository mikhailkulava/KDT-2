package com.sigmaukraine.trn.keywords;

import com.sigmaukraine.trn.testUtils.TestConfig;
import com.sigmaukraine.trn.playerDetails.PlayerDetails;
import com.sigmaukraine.trn.playerDetails.PlayerDetailsConverter;
import com.sigmaukraine.trn.testUtils.DbManager;
import com.sigmaukraine.trn.testUtils.HttpManager;
import com.sigmaukraine.trn.testUtils.LogManager;
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
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mkulava on 07.03.14.
 */
public class PlayerInfoGet {
    public static void execute(Map<String, String> parametersAndValues){
        //actual result map
        Map<String, String > actualResultMap = new HashMap<String, String>();

        //getting expected result map
        DbManager dbManager = new DbManager();
        String query = dbManager.createQueryFromTemplate(TestConfig.CONFIG_PROPERTIES.getProperty("getPlayerDetailsSql"), parametersAndValues);
        Map<String, String> expectedResultMap = dbManager.rows(query);

        //creating request from a template
        String request = HttpManager.createRequestFromTemplate(TestConfig.CONFIG_PROPERTIES.getProperty("getPlayerDetailsSoap"), parametersAndValues);
        String response = HttpManager.sendSoapRequest(request,
                parametersAndValues.get("endpointURI"));
        try {
            //getting xPath template
            BufferedReader bufferedReader = new BufferedReader(new FileReader(TestConfig.CONFIG_PROPERTIES.getProperty("getPlayerDetailsXpath")));
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder =  builderFactory.newDocumentBuilder();
            Document xmlDocument = builder.parse(new ByteArrayInputStream(response.getBytes()));
            XPath xPath =  XPathFactory.newInstance().newXPath();
            Node currentNode = null;
            String xPathTemplate = bufferedReader.readLine();
            bufferedReader.close();

            //getting actual result map
            for(Map.Entry<String, String> entry : expectedResultMap.entrySet()){
                String replaceKey = entry.getKey();
                if(replaceKey.equals("IdentificationManualVerified")){
                    replaceKey = "IdentificationManualVerifiedUntil";
                }
                String readyExpression = xPathTemplate.replace("$key", replaceKey);
                currentNode = (Node)xPath.compile(readyExpression).evaluate(xmlDocument, XPathConstants.NODE);
                actualResultMap.put(entry.getKey(), currentNode.getTextContent());
            }
        } catch (FileNotFoundException e) {
            LogManager.warning(e.getLocalizedMessage());
        } catch (SAXException e) {
            LogManager.warning(e.getLocalizedMessage());
        } catch (IOException e) {
            LogManager.warning(e.getLocalizedMessage());
        } catch (ParserConfigurationException e) {
            LogManager.warning(e.getLocalizedMessage());
        } catch (XPathException e){
            LogManager.warning(e.getLocalizedMessage());
        }

        //comparing player details from db and soap response
        PlayerDetails sqlPlayerDetails = PlayerDetailsConverter.convert(expectedResultMap);
        PlayerDetails soapPlayerDetails = PlayerDetailsConverter.convert(actualResultMap);
        LogManager.info("Comparing player details from DB and Soap response");
        if(sqlPlayerDetails.equals(soapPlayerDetails)){
            LogManager.info("ASSERTION PASSED: Actual result == Expected result");
        }
        else {
            LogManager.info("ASSERTION FAILED: Actual result != Expected result");
        }
    }
}
