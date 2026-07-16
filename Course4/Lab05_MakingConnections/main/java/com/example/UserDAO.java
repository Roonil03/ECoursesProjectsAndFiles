package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.DatabaseUtility;


public class UserDAO {

    public static final String URL = "jdbc:mysql://localhost:3306/clicknbuy";

    public static final String USER_NAME = "root";
    public static final String PASSWORD = "password";

    public int addUser(User newUser) throws ClassNotFoundException, SQLException{
        
        Connection clickNBuyConnection = DatabaseUtility.getDbConnection(URL, USER_NAME, PASSWORD);

        String sqlQuery = "INSERT INTO clicknbuy.user (username, user_email, first_name, last_name, phone_number, reward_points) VALUES (?,?,?,?,?,?)";

        PreparedStatement insertPreparedStatement = clickNBuyConnection.prepareStatement(sqlQuery);
        insertPreparedStatement.setString(1, newUser.getUserName());
        insertPreparedStatement.setString(2, newUser.getUserEmail());
        insertPreparedStatement.setString(3, newUser.getFirstName());
        insertPreparedStatement.setString(4, newUser.getLastName());
        insertPreparedStatement.setString(5, newUser.getPhoneNumber());
        insertPreparedStatement.setInt(6, newUser.getRewardPoints());

        int rowsUpdated = insertPreparedStatement.executeUpdate();
        return rowsUpdated; 

    }

    
   
}
