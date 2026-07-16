package com.lesson.lab;

import com.lesson.lab.controller.SeatBooking;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Take input using Scanner class
        Scanner keyboard = new Scanner(System.in);
        // Create an object of SeatBooking class
        SeatBooking bookingManager = new SeatBooking();

        while (true) {
            System.out.println("\n1. Add Booking\n2. Cancel Booking\n3. Update Booking\n4. Display Bookings\n5. Exit");
            System.out.print("Choose an option: ");
            // Choose an option
            int choice = -1;
            try {
                choice = keyboard.nextInt();
                keyboard.nextLine(); // Consume newline character
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                keyboard.nextLine(); // Clear invalid input
                continue; // Skip the rest of the loop iteration
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter Seat Number: ");
                    String seatNumber = keyboard.next();
                    bookingManager.addNewBooking(seatNumber);
                    break;
                case 2:
                    System.out.print("Enter Seat Number to cancel: ");
                    seatNumber = keyboard.next();
                    bookingManager.cancelBooking(seatNumber);
                    break;
                case 3:
                    System.out.print("Enter Seat Number to update: ");
                    String oldSeatNumber = keyboard.next();
                    System.out.print("Enter new Seat Number: ");
                    String newSeatNumber = keyboard.next();
                    bookingManager.updateBooking(oldSeatNumber, newSeatNumber);
                    break;
                case 4:
                    bookingManager.displayBookings();
                    break;
                case 5:
                    System.out.println("Exiting program.");
                    keyboard.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}


