package org.example.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class UploadXmlParser {

    public void parse(InputStream inputStream) {
        try {
            // Parse XML file using DOM parser
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(inputStream);
            document.getDocumentElement().normalize();
            processXmlDocument(document);
        } catch (Exception e) {
            throw new RuntimeException("Error processing XML file: " + e.getMessage());
        }
    }

    private void processXmlDocument(Document document) {
        Element rootElement = document.getDocumentElement();
        String tableName = getTableName(rootElement);
        List<String> columnNames = getColumnNames(rootElement);
        List<List<String>> rows = getRowValues(rootElement);
        System.out.println("Table Name: " + tableName);
        System.out.println("Columns: " + columnNames);
        System.out.println("Rows: " + rows);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://localhost:3306/test_db";
            String username = "root";
            String password = "toor";
            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                createTable(connection, tableName, columnNames);
                insertData(connection, tableName, rows);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertData(Connection connection, String tableName, List<List<String>> rows) throws Exception {
        for (List<String> row : rows) {
            StringBuilder insertSqlQuery = new StringBuilder("INSERT INTO ")
                    .append(tableName)
                    .append(" VALUES(");
            for (int i = 0; i < row.size(); i++) {
                insertSqlQuery.append("\"" + row.get(i) + "\"");
                if (i != row.size() - 1) {
                    insertSqlQuery.append(", ");
                }
            }
            insertSqlQuery.append(")");
            Statement insertStatement = connection.createStatement();
            System.out.println(insertSqlQuery.toString());
            insertStatement.execute(insertSqlQuery.toString());
        }
    }

    private String getTableName(Element root) {
        return root.getElementsByTagName("name").item(0).getTextContent();
    }

    private List<String> getColumnNames(Element root) {
        List<String> columnNames = new ArrayList<>();
        NodeList nodeList = ((Element) root.getElementsByTagName("columns").item(0)).getElementsByTagName("column");
        for (int i = 0; i < nodeList.getLength(); i++) {
            columnNames.add(((Element) nodeList.item(i)).getElementsByTagName("name").item(0).getTextContent());
        }
        return columnNames;
    }

    private List<List<String>> getRowValues(Element root) {
        List<List<String>> rows = new ArrayList<>();
        NodeList nodeList = ((Element) root.getElementsByTagName("rows").item(0)).getElementsByTagName("row");
        for (int i = 0; i < nodeList.getLength(); i++) {
            List<String> rowValues = new ArrayList<>();
            NodeList valuesList = ((Element) nodeList.item(i)).getElementsByTagName("value");
            for (int j = 0; j < valuesList.getLength(); j++) {
                rowValues.add(valuesList.item(j).getTextContent());
            }
            rows.add(rowValues);
        }
        return rows;
    }

    private void createTable(Connection connection, String tableName, List<String> columnNames) throws Exception {
        StringBuilder createTableQuery = new StringBuilder("CREATE TABLE ")
                .append(tableName)
                .append("(");
        for (int i = 0; i < columnNames.size(); i++) {
            createTableQuery.append(columnNames.get(i))
                    .append(" ")
                    .append("VARCHAR(255)");
            if (i != columnNames.size() - 1) {
                createTableQuery.append(",");
            }
        }
        createTableQuery.append(")");
        Statement statement = connection.createStatement();
        statement.execute(createTableQuery.toString());
    }
}
