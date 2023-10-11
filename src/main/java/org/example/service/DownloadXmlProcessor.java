package org.example.service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class DownloadXmlProcessor {

    public String generateXml(String tableName) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://localhost:3306/test_db";
            String username = "root";
            String password = "toor";
            List<String> columns = new ArrayList<>();
            List<List<String>> rows = new ArrayList<>();
            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                String query = "SELECT * FROM " + tableName + " LIMIT 100";
                ResultSet rs = connection.createStatement().executeQuery(query);
                ResultSetMetaData rsMeta = rs.getMetaData();
                int columnCount = rsMeta.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    columns.add(rsMeta.getColumnName(i));
                }
                while (rs.next()) {
                    List<String> row = new ArrayList<>();
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getString(i));
                    }
                    rows.add(row);
                }
            }
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("table");
            addName(doc, rootElement, tableName);
            addColumns(doc, rootElement, columns);
            addRows(doc, rootElement, rows);
            doc.appendChild(rootElement);
            String xmlString = convertDocumentToString(doc);
            return xmlString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addName(Document doc, Element element, String value) {
        Element dataElement = doc.createElement("name");
        dataElement.appendChild(doc.createTextNode(value));
        element.appendChild(dataElement);
    }

    private void addColumns(Document doc, Element element, List<String> columns) {
        Element columnsTag = doc.createElement("columns");
        for (String column : columns) {
            Element columnTag = doc.createElement("column");
            Element nameTag = doc.createElement("name");
            nameTag.appendChild(doc.createTextNode(column));
            columnTag.appendChild(nameTag);
            columnsTag.appendChild(columnTag);
        }
        element.appendChild(columnsTag);
    }

    private void addRows(Document doc, Element element, List<List<String>> rows) {
        Element rowsTag = doc.createElement("rows");
        for (List<String> row : rows) {
            Element rowTag = doc.createElement("row");
            for (String value : row) {
                Element valueTag = doc.createElement("value");
                valueTag.appendChild(doc.createTextNode(value));
                rowTag.appendChild(valueTag);
            }
            rowsTag.appendChild(rowTag);
        }
        element.appendChild(rowsTag);
    }

    private static String convertDocumentToString(Document doc) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
