package com.sigmaukraine.trn.testUtils;

import com.sigmaukraine.trn.report.Log;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements required db actions (establish connection, execute queries, save results to map)
 */
public class DbManager {
    /**
     * This method establishes connection to database and returns connection.
     * Private - as it is used in public methods as rows() and executeQuery()
     */
    private Connection establishConnection(){
        try {
            Log.info("Registering JDBC driver");
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            Log.error("Couldn't locate required JDBC driver!", e);
        } finally {
            Log.info("Driver registered successfully!");
        }
        Connection connection = null;
        try {
            String dbURL = TestConfig.SERVER_PROPERTIES.getProperty("databaseurl");
            String dbPort = TestConfig.SERVER_PROPERTIES.getProperty("databaseport");
            String dbName = TestConfig.SERVER_PROPERTIES.getProperty("database");
            String dbUsername = TestConfig.SERVER_PROPERTIES.getProperty("databaseuser");
            String dbPassword = TestConfig.SERVER_PROPERTIES.getProperty("databasepassword");
            Log.info("Establishing connection to database: \"" + dbName + "\"",
                     "\nData base URL: " + dbURL + "\n" +
                     "Port: " + dbPort + "\n" +
                     "Login: " + dbUsername);
            connection = DriverManager.getConnection(
                    "jdbc:oracle:thin:@" + dbURL + ":" + dbPort + "/" + dbName,
                    dbUsername,
                    dbPassword);
        } catch (SQLException e) {
            Log.error("SQL exception occurred!", e);
        } finally {
            Log.info("Connected successfully!");
        }
        return connection;
    }

    /**
     * @param query - input query to execute
     * @return Map, where key = column name, value - cell value
     */
    public Map<String, String> rows(String query){
        Map<String, String> queryResult = new HashMap<String, String>();
        Connection currentConnection = establishConnection();
        Log.info("Executing query: ", query);

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
            Log.error("SQL exception occurred!", e);
        }
        return queryResult;
    }

    /**
     * @param query - input query to be executed, doesn't return anything, is used for INSERT, UPDATE, DELETE queries.
     */
    public void executeQuery(String query){
        Connection currentConnection = establishConnection();
        Log.info("Executing query: ", query);
        try{
            currentConnection.setAutoCommit(true);
            Statement stmt = currentConnection.createStatement();
            stmt.executeQuery(query);
        } catch (SQLException e){
            Log.error("SQL exception occurred", e);
        }
    }

}
