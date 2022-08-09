package com.example.application;

import com.vaadin.flow.component.notification.Notification;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBasesController {
    public String dbPath = "jdbc:mysql:";
    public String dbUser = "root";
    public String dbPass = "1234321";

    public static String modelName = "DataBases";

//    public Server hsqlServer = null;
//    public Connection hsqlConnection = nulappTabsl;

    public List<String> classesList;

//    public static final String SQL_CREATE_USERS_TABLE = "CREATE TABLE USERS (LOGIN VARCHAR(15), PASS VARCHAR(15), DESCRIPTION VARCHAR(50), CODE VARCHAR(10))";
//    public static final String SQL_USERS_CREATE_UNIQUE_INDEX = "create unique index USERS_LOGIN_uindex on USERS (LOGIN)";
    public static final String SQL_CREATE_ADMIN_USER = "INSERT INTO USERS ( LOGIN, PASS, DESCRIPTION, CODE ) VALUES ( 'admin', '', 'Administrator user', 'admin1' )";
//    public static final String SQL_CREATE_ARTICOLE_TABLE = "CREATE TABLE ARTICOLE (ID BIGINT, DESCRIPTION VARCHAR(100), CODE VARCHAR(10), DENUMIREACOMPLETA VARCHAR(500), PARENT_REF BIGINT, ISFOLDER TINYINT, SERVICIU TINYINT, UM_REF BIGINT, CODDEBAREPRINCIPAL VARCHAR(30), CORARTICOL VARCHAR(10), COTATVA_REF BIGINT)";

    private String xmlPath = "config.xml";
    public File xmlFile = new File(xmlPath);

    public java.sql.Connection hsqlConnection = null;

    public Connection getHsqlConnection() {
        return hsqlConnection;
    }

    private java.sql.Connection hsqlConnect(String dbPath, String dbUser, String dbPassword) {
        java.sql.Connection conn = null;
        try {
            //Registering the HSQLDB JDBC driver
//			Class.forName("org.hsqldb.jdbc.JDBCDriver");
//			Class.forName("com.mysql.jdbc.Driver");
            //Creating the connection with HSQLDB
//			conn = DriverManager.getConnection(dbPath, dbUser, dbPassword);
            // Connection connection = <your java.sql.Connection>
//			conn = DriverManager.getConnection(dbPath, "root", "1234321");

            conn = DriverManager.getConnection(dbPath + "/3dm", "root", "1234321");
//			conn.setAutoCommit(false);
        } catch (Exception e){
            try {
//				ResultSet resultSet = conn.getMetaData().getCatalogs();
//				iterate each catalog in the ResultSet
//			boolean databaseTransExists = false;
//			while (resultSet.next()) {
                // Get the database name, which is at position 1
//				String databaseName = resultSet.getString(1);
//				if (databaseName.equals("trans")) {
//					databaseTransExists = true;
//				}
//			}
//			if (!databaseTransExists) {
                conn = DriverManager.getConnection(dbPath, "root", "1234321");
//				conn.setAutoCommit(false);
                String query = "CREATE database 3dm";
                //Executing the query
                Statement stmt = conn.createStatement();
                stmt.execute(query);
                System.out.println("Database trans created");
                conn.close();
//			}

                conn = DriverManager.getConnection(dbPath + "/3dm", "root", "1234321");
//				conn.setAutoCommit(false);


            } catch (Exception e1) {
                e1.printStackTrace(System.out);
            }
        }

        return conn;
    }

    public void connectToDatabase(String addDBPath){
        if (hsqlConnection != null){
            return;
        }

        if (hsqlConnection != null) {
            try {
                hsqlConnection.close();
            } catch (SQLException e) {

            }
        }

        try {
            hsqlConnection = hsqlConnect("jdbc:mysql://localhost:3306", "root", "1234321");
        } catch (Exception e){
            e.printStackTrace();
        }

        boolean hsqlConnectionIsValid = false;
        try {
            if (hsqlConnection != null) {
                hsqlConnectionIsValid = hsqlConnection.isValid(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (hsqlConnection == null){
            Notification.show("Could not connect to database");
        }

//        getSession().setAttribute("hsqlConnection", hsqlConnection);
//        getSession().setAttribute("fullDBPath", fullDBPath);
//        getSession().setAttribute("addDBName", addDBName);

    }

    public synchronized ResultSet hsqlQuery(String expression) throws SQLException {

        Statement st = null;
        ResultSet rs = null;

        st = hsqlConnection.createStatement();         // statement objects can be reused with

        rs = st.executeQuery(expression);    // run the query

        return rs;
    }

    public synchronized void hsqlQuery2(String expression) throws SQLException {

        Statement st = null;
        ResultSet rs = null;

        st = hsqlConnection.createStatement();         // statement objects can be reused with

        st.execute(expression);    // run the query
    }

    public DataBasesController() {
        connectToDatabase("jdbc:mysql://localhost:3306");
        createTables();
    }

    void createTables(){
        boolean picFolderExists = false;
        try {
            String sql = "SELECT * \n" +
                    "FROM information_schema.tables\n" +
                    "WHERE table_schema = '3dm' \n" +
                    "    AND table_name = 'picFolder'\n" +
                    "LIMIT 1;";
            ResultSet rs = hsqlQuery(sql);
            if (rs.next()) {
                picFolderExists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!picFolderExists){
            try {
                String sql = "CREATE TABLE picFolder (\n" +
                        "    id BIGINT NOT NULL AUTO_INCREMENT,\n" +
                        "    fileName TEXT,\n" +
                        "    filePath TEXT,\n" +
                        "    red INT,\n" +
                        "    green INT,\n" +
                        "    blue INT,\n" +
                        "    PRIMARY KEY (id)\n" +
                        ");";
                hsqlQuery2(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        boolean projectFilesExists = false;
        try {
            String sql = "SELECT * \n" +
                    "FROM information_schema.tables\n" +
                    "WHERE table_schema = '3dm' \n" +
                    "    AND table_name = 'projectFiles'\n" +
                    "LIMIT 1;";
            ResultSet rs = hsqlQuery(sql);
            if (rs.next()) {
                projectFilesExists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!projectFilesExists){
            try {
                String sql = "CREATE TABLE projectFiles (\n" +
                        "    id BIGINT NOT NULL AUTO_INCREMENT,\n" +
                        "    name TEXT,\n" +
                        "    fileName TEXT,\n" +
                        "    filePath TEXT,\n" +
                        "    red INT,\n" +
                        "    green INT,\n" +
                        "    blue INT,\n" +
                        "    project INT,\n" +
                        "    PRIMARY KEY (id)\n" +
                        ");";
                hsqlQuery2(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        boolean picTagsExists = false;
        try {
            String sql = "SELECT * \n" +
                    "FROM information_schema.tables\n" +
                    "WHERE table_schema = '3dm' \n" +
                    "    AND table_name = 'picTags'\n" +
                    "LIMIT 1;";
            ResultSet rs = hsqlQuery(sql);
            if (rs.next()) {
                picTagsExists = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!picTagsExists){
            try {
                String sql = "CREATE TABLE picTags (\n" +
                        "    id BIGINT NOT NULL AUTO_INCREMENT,\n" +
                        "    name VarChar(500),\n" +
                        "    picFolder INT,\n" +
                        "    PRIMARY KEY (id)\n" +
                        ");";
                hsqlQuery2(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
