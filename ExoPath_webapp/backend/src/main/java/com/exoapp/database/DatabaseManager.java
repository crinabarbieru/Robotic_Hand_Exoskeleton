package com.exoapp.database;

import java.sql.*;

public class DatabaseManager {
    private static String URL = "jdbc:mysql://localhost:3306/exoapp";
    private static String USER = "root";
    private static String PASSWORD = "123Crinel@";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static Connection getConnection(String URL, String USER, String PASSWORD) throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
