package com.sigmaukraine.trn.testUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements required db actions (establish connection, execute queries, save results to map)
 */
public class DbManager {

    private Connection establishConnection(){
        try {
            LogManager.info("Registering JDBC driver");
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            LogManager.warning("Couldn't locate required JDBC driver!" + e.getLocalizedMessage());
        } finally {
            LogManager.info("Driver registered successfully!");
        }
        Connection connection = null;
        try {
            String dbURL = TestConfig.SERVER_PROPERTIES.getProperty("databaseurl");
            String dbPort = TestConfig.SERVER_PROPERTIES.getProperty("databaseport");
            String dbName = TestConfig.SERVER_PROPERTIES.getProperty("database");
            String dbUsername = TestConfig.SERVER_PROPERTIES.getProperty("databaseuser");
            String dbPassword = TestConfig.SERVER_PROPERTIES.getProperty("databasepassword");
            LogManager.info("Establishing connection to database: \"" + dbName + "\"");
            LogManager.info("Data base URL: " + dbURL);
            LogManager.info("Port: " + dbPort);
            LogManager.info("Login: " + dbUsername);
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@" + dbURL + ":" + dbPort + "/" + dbName,
                    dbUsername,
                    dbPassword);
        } catch (SQLException e) {
            LogManager.warning(e.getLocalizedMessage());
        } finally {
            LogManager.info("Connected successfully!");
        }
        return connection;
    }

    public String createQueryFromTemplate(String srcTemplateFile, Map<String, String> placeholders){
        LogManager.info("Getting query template: " + srcTemplateFile);
        String query = FileManager.getTxtFileContent(srcTemplateFile);
        for(Map.Entry<String, String> entry : placeholders.entrySet()){
            query = query.replace("$" + entry.getKey(), entry.getValue());
        }
        LogManager.info("Query created successfully:\n" + query);
        return query;
    }

    public Map<String, String> rows(String query){
        Map<String, String> queryResult = new HashMap<String, String>();
        Connection currentConnection = establishConnection();
        LogManager.info("Executing query");
        try {
            Statement stmt = currentConnection.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData headers = rs.getMetaData();
            while(rs.next()){
                for(int i = 1; i <= headers.getColumnCount(); i++){
                    queryResult.put(headers.getColumnName(i), rs.getString(i));
                    }
                }
        } catch (SQLException e){
            LogManager.warning(e.getLocalizedMessage());
        }
        return queryResult;
    }

}
