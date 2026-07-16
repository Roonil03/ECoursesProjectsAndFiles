package com.lesson.lab.controller;

import com.lesson.lab.model.Seat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SeatBooking {
    // List to store bookings
    private List<Seat> bookedSeatsList;
    // Constructor
    public SeatBooking() {
        // Initialize the list of booked seats
        this.bookedSeatsList = new ArrayList<>();
    }
    // Method to add a new seat booking
    public void addNewBooking(String seatNumber) {
        for (Seat seat : bookedSeatsList) {
            if (seat.getSeatNumber().equals(seatNumber) && seat.isBooked() && !seat.isCanceled()) {
                System.out.println("Seat " + seatNumber + " is already booked.");
                return;
            }
        }
        Seat newSeat = new Seat(seatNumber);
        newSeat.setBooked(true);
        newSeat.setBookingDate(new Date()); // Set the current date as the booking date
        bookedSeatsList.add(newSeat);
        System.out.println("Seat " + seatNumber + " has been successfully booked!");
     }
    // Method to cancel a booking
    public void cancelBooking(String seatNumber) {
        for (Seat seat : bookedSeatsList) {
            if (seat.getSeatNumber().equals(seatNumber) && !seat.isCanceled()) {
                seat.setCanceled(true);
                seat.setBooked(false);
                System.out.println("Booking for seat " + seat.getSeatNumber() + " has been successfully canceled!");
                return;
            }
        }
        System.out.println("No booking found for seat number: " + seatNumber);
    }
    // Method to update a booking seat number
    public void updateBooking(String oldSeatNumber, String newSeatNumber) {
        for (Seat seat : bookedSeatsList) {
            if (seat.getSeatNumber().equals(oldSeatNumber) && !seat.isCanceled()) {
                seat.setSeatNumber(newSeatNumber);
                System.out.println("Booking updated successfully to new seat number: " + newSeatNumber);
                return;
            }
        }
        System.out.println("No booking found for seat number: " + oldSeatNumber);
    }
    // Method to display all bookings
    public void displayBookings() {
        if (bookedSeatsList.isEmpty()) {
            System.out.println("No bookings made yet.");
        } else {
            for (Seat seat : bookedSeatsList) {
                if (!seat.isCanceled() && seat.isBooked()) {
                    System.out.println("Seat Number: " + seat.getSeatNumber() + ", Booking Date: " + seat.getBookingDate());
                }
            }
        }
    }
}
