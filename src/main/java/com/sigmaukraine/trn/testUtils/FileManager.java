package com.sigmaukraine.trn.testUtils;

import com.sigmaukraine.trn.report.Log;
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
 * This class provides simple file actions, such as getting txt- and csv-files contents, creating a file with certain content,
 * and format string to nice XML format.
 */
public class FileManager {
    /**
     * @param srcFile - input path to source txt-file.
     * @return - returns file content as String
     */
    public static String getTxtFileContent(String srcFile){
        String fileContent = "";
        try{
            Log.info("Opening file: " + srcFile);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(srcFile));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line).append("\n");
            }
            fileContent = stringBuilder.toString();
        } catch (FileNotFoundException e){
            Log.error("File not found!", e);
        } catch (IOException e){
            Log.error("IO exception occurred!", e);
        }
        Log.info("File content received!");
        return fileContent;
    }

    /**
     * @param srcCsvFile - input path to source csv-file
     * @return - returns csv file content as List of String array, where each list element is line and each array element is cell.
     */
    public static List<String[]> getCsvFileContent(String srcCsvFile){
        List<String[]> csvFileContent = new LinkedList<String[]>();
        BufferedReader bufferedReader;
        String line;
        String separator = TestConfig.CONFIG_PROPERTIES.getProperty("csvSeparator");
        try {
            bufferedReader = new BufferedReader(new FileReader(srcCsvFile));
            while ((line = bufferedReader.readLine()) != null) {
                String[] currentLine = line.split(separator);
                csvFileContent.add(currentLine);
            }
        } catch (IOException e) {
            Log.error("IO exception occurred while parsing CSV file!", e);
        }
        return csvFileContent;
    }

    /**
     * This method creates text file
     * @param destFile - path and file name
     * @param content - file's content.
     */
    public static void createFile(String destFile, String content){
        try {
            File file = new File(destFile);
            file.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            Log.error("IO exception occurred!", e);
        }
    }

    /**
     * This methods converts String to pretty XML view
     */
    public static String formatStringToXML(String xmlString){
        try {
            InputSource src = new InputSource(new StringReader(xmlString));
            Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src).getDocumentElement();
            Boolean keepDeclaration = xmlString.startsWith("<?xml");
            DOMImplementationRegistry domImplementationRegistry = DOMImplementationRegistry.newInstance();
            DOMImplementationLS domImplementationLS = (DOMImplementationLS) domImplementationRegistry.getDOMImplementation("LS");
            LSSerializer writer = domImplementationLS.createLSSerializer();
            writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE); // Set this to true if the output needs to be beautified.
            writer.getDomConfig().setParameter("xml-declaration", keepDeclaration); // Set this to true if the declaration is needed to be outputted.
            return writer.writeToString(document);
        } catch (Exception e) {
            Log.error("Exception occurred!", e);
        }
        return xmlString;
    }


}
