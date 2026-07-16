package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HBoxController {
        @FXML
    private Label cityLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private Button nextButton;

    @FXML
    private Button prevButton;

    @FXML
    private Label zipcodeLabel;
    private List<String[]> personData;  // List to store fetched records
    private int currentIndex = 0;

    // Initialize method
    @FXML
    public void initialize() {
        personData = fetchPersonData();  // Fetch data from the database
        if (!personData.isEmpty()) {
            displayRecord(currentIndex);
        }
       // updateButtonState();
    }

    // Fetch records from the MySQL 'person' table
    private List<String[]> fetchPersonData() {
        List<String[]> data = new ArrayList<>();
        String query = "SELECT name, city, zipcode FROM person";  // SQL Query

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            // Iterate through the result set and store each record
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String city = resultSet.getString("city");
                String zipcode = resultSet.getString("zipcode");
                data.add(new String[]{name, city, zipcode});
            }

        } catch (Exception e) {
            e.printStackTrace();  // Handle exception (log or show error message)
        }

        return data;
    }

    // Display the record at the current index
    private void displayRecord(int index) {
        String[] person = personData.get(index);
        nameLabel.setText("Name: " + person[0]);
        cityLabel.setText("City: " + person[1]);
        zipcodeLabel.setText("Zipcode: " + person[2]);
    }

     @FXML
    void nextRecord(ActionEvent event) {
        if (currentIndex < personData.size() - 1) {
            currentIndex++;
            displayRecord(currentIndex);
            //updateButtonState();
        }
            
    }




    @FXML
    void prevRecord(ActionEvent event) {

    }

    
    @FXML
    void switchGridScene(ActionEvent event) throws IOException {
        App.setRoot("gridscene");

    }
    
}
