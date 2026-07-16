package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatabaseHandler {

    // TODO1: Define the database URL in a private static final String variable
    private static final String url = "jdbc:mysql://localhost:3306/404booksnotfoundlibrary"; // Change this to your database URL

    // TODO2: Define the database username in a private static final String variable
    private static final String username = "root"; // Change this to your MySQL username

    // TODO3: Define the database password in a private static final String variable
    private static final String password = "password"; // Change this to your MySQL password

    // TODO4: Define a public static method named getDatabaseConnection that returns a Connection object
    public static Connection getDatabaseConnection() throws SQLException {
        // TODO5: Use the DriverManager.getConnection method to establish a connection to the database
            // a. Pass the url, username, and password variables as arguments to the getConnection method
            // b. Add the return keyword to return the Connection object
        return DriverManager.getConnection(url, username, password);

        // TODO6: Add throws SQLException to the method signature to forward the exception to the calling method
    }

    // TODO7: Define a public static method named fetchBooks that returns an ObservableList of Book objects
    public static ObservableList<Book> fetchBooks() throws SQLException {
        // TODO8: Define an ObservableList of Book objects named books and initialize it with FXCollections.observableArrayList()
        ObservableList<Book> books = FXCollections.observableArrayList();

        // TODO9: Define a String variable named query and assign the SQL query to select all columns from the book table
        String query = "SELECT * FROM book";

        // TODO10: Call the getDatabaseConnection method and assign the returned object to a Connection variable named libraryConnection
        Connection libraryConnection = getDatabaseConnection();

        // TODO11: Add throws SQLException to the method signature

        // TODO12: Call the createStatement method on the libraryConnection and assign the returned object to a Statement variable named queryStatement
        Statement queryStatement = libraryConnection.createStatement();

        // TODO13: Call the executeQuery method on the queryStatement object with the query variable as an argument and assign the returned object to a ResultSet variable named booksResultSet
        ResultSet booksResultSet = queryStatement.executeQuery(query);

        // TODO14: Use a while loop to iterate through the resultSet
        while(booksResultSet.next()) {

            // TODO15: Store the value of the id column in the resultSet in an int variable named id
            int id = booksResultSet.getInt("id");

            // TODO16: Store the value of the title column in the resultSet in a String variable named title
            String title = booksResultSet.getString("title");

            // TODO17: Store the value of the author column in the resultSet in a String variable named author
            String author = booksResultSet.getString("author");

            // TODO18: Store the value of the publication year column in the resultSet in an int variable named publicationYear
            int publicationYear = booksResultSet.getInt("publication_year");

            // TODO19: Store the value of the genre column in the resultSet in a String variable named genre
            String genre = booksResultSet.getString("genre");
            
            // TODO20: Create a new Book object with the id, title, author, publicationYear, and genre variables as arguments
            Book book = new Book(id, title, author, publicationYear, genre);

            // TODO21: Add the book object to the books observable list
            books.add(book);
        }

        // TODO22: Return the books observable list
        return books;
    }
}
