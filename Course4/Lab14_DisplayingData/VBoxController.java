package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class VBoxController {

    // TODO 9: @FXML annotations matching SceneBuilder UI components [source: 27]
    @FXML
    private Label nameLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label zipcodeLabel;

    // TODO 14: Declare personData List object and int variable currentIndex set to 0 [source: 27]
    private List<String[]> personData;  
    private int currentIndex = 0;

    // TODO 21: Define initialize() method called automatically [source: 27]
    @FXML
    public void initialize() {
        // TODO 22: Call the fetchPersonData() method first [source: 27]
        personData = fetchPersonData();  
        if (!personData.isEmpty()) {
            // TODO 23: Call displayRecord() method to show contents at currentIndex [source: 27]
            displayRecord(currentIndex);
        }
    }

    // TODO 15: Define fetchPersonData() method to read all records into an ArrayList [source: 27]
    private List<String[]> fetchPersonData() {
        List<String[]> data = new ArrayList<>();
        
        // TODO 16: Construct an SQL query string to select name, city, zipcode from person [source: 27]
        String query = "SELECT name, city, zipcode FROM person";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                // TODO 17: Store name, city and zipcode in string variables via resultSet [source: 27]
                String name = resultSet.getString("name");
                String city = resultSet.getString("city");
                String zipcode = resultSet.getString("zipcode");
                data.add(new String[]{name, city, zipcode});
            }
        } catch (Exception e) {
            e.printStackTrace();  
        }
        return data;
    }

    // TODO 18: Define displayRecord() method [source: 27]
    private void displayRecord(int index) {
        // TODO 19: Fetch information at index and set nameLabel [source: 27]
        String[] person = personData.get(index);
        nameLabel.setText("Name: " + person[0]);
        // TODO 20: Similarly set cityLabel and zipcodeLabel [source: 27]
        cityLabel.setText("City: " + person[1]);
        zipcodeLabel.setText("Zipcode: " + person[2]);
    }

    @FXML
    void nextRecord(ActionEvent event) {
        if (currentIndex < personData.size() - 1) {
            // TODO 24: Increment currentIndex and call displayRecord() method [source: 27]
            currentIndex++;
            displayRecord(currentIndex);
        }
    }

    @FXML
    void prevRecord(ActionEvent event) {
        if (currentIndex > 0) {
            currentIndex--;
            displayRecord(currentIndex);
        }
    }

    @FXML
    void switchHScene(ActionEvent event) throws IOException {
        // TODO 25: Set the hboxscene.fxml as the root of the application window [source: 27]
        App.setRoot("hboxscene");
    }
}