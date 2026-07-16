package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    // TODO 12: declare URL, USER and PASSWORD variables [source: 25]
    private static final String URL = "jdbc:mysql://localhost:3306/mynewdatabase"; 
    private static final String USER = "root"; 
    private static final String PASSWORD = "password"; 

    // Define a static method getConnection() that returns MySQL Connection object [source: 25]
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}