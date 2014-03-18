package com.sigmaukraine.trn.testUtils;

import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mkulava on 06.03.14.
 */
public class FileManager {

    public static String getTxtFileContent(String srcFile){
        String fileContent = "";
        try{
            LogManager.info("Opening file: " + srcFile);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(srcFile));
            StringBuilder stringBuilder = new StringBuilder();
            String line = "";
            while((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line + "\n");
            }
            fileContent = stringBuilder.toString();
        } catch (FileNotFoundException e){
            LogManager.warning(e.getLocalizedMessage());
        } catch (IOException e){
            LogManager.warning(e.getLocalizedMessage());
        }
        LogManager.info("File content received!");
        return fileContent;
    }

    public static List<String[]> getCsvFileContent(String srcCsvFile){
        List<String[]> csvFileContent = new LinkedList<String[]>();
        BufferedReader bufferedReader = null;
        String line = "";
        String separator = TestConfig.CONFIG_PROPERTIES.getProperty("csvSeparator");
        try {
            LogManager.info("Parsing csv file: " + srcCsvFile);
            bufferedReader = new BufferedReader(new FileReader(srcCsvFile));
            while ((line = bufferedReader.readLine()) != null) {
                String[] currentLine = line.split(separator);
                csvFileContent.add(currentLine);
            }
        } catch (IOException e) {
            LogManager.warning(e.getLocalizedMessage());
        } finally {
            LogManager.info("CSV file was parsed successfully!");
        }

        return csvFileContent;
    }

    public static void createFile(String destFile, String content){
        try {
            File file = new File(destFile);
            file.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            LogManager.warning(e.getLocalizedMessage());
        }
    }

    public static String formatStringToXML(String xmlString){
        try {
            InputSource src = new InputSource(new StringReader(xmlString));
            Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src).getDocumentElement();
            Boolean keepDeclaration = Boolean.valueOf(xmlString.startsWith("<?xml"));
            DOMImplementationRegistry domImplementationRegistry = DOMImplementationRegistry.newInstance();
            DOMImplementationLS domImplementationLS = (DOMImplementationLS) domImplementationRegistry.getDOMImplementation("LS");
            LSSerializer writer = domImplementationLS.createLSSerializer();
            writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE); // Set this to true if the output needs to be beautified.
            writer.getDomConfig().setParameter("xml-declaration", keepDeclaration); // Set this to true if the declaration is needed to be outputted.
            return writer.writeToString(document);
        } catch (Exception e) {
            LogManager.warning(e.getLocalizedMessage());
        }
        return xmlString;
    }


}
