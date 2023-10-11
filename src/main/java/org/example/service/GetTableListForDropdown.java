package org.example.service;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class GetTableListForDropdown {

    public List<String> getTableList() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://localhost:3306/test_db";
            String username = "root";
            String password = "toor";
            try {
                Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet resultSet = metaData.getTables(null, null, "%", new String[]{"TABLE"});
                List<String> names = new ArrayList<>();
                while (resultSet.next()) {
                    String tableName = resultSet.getString("TABLE_NAME");
                    names.add(tableName);
                }
                resultSet.close();
                connection.close();
                return names;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
