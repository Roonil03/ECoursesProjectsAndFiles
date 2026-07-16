package com.example;

import java.sql.SQLException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        User newUser = new User("betty.george" + System.currentTimeMillis(),
                        "betty.george" + System.currentTimeMillis() + "@email.com",
                        "Betty",
                        "George",
                        "+11536549870",
                        15000);

        UserDAO userDAO = new UserDAO();

        try {
            int rowsUpdated = userDAO.addUser(newUser);
            if(rowsUpdated == 1){
                System.out.println("New user added successfully");
            } else{
                System.out.println("Cannot add user");
            }
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }   
        
           
       
    }
}
