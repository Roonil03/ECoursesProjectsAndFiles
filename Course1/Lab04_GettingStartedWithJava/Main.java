public class Main {
    public static void main(String[] args) {
        //TODO 1: Declare and initialize variable for First Name
        String firstName = "Sandy";

        //TODO 1: Declare and initialize variable for Last Name
        String lastName = "Jones";

        //TODO 1: Declare and initialize variable for Movie Title
        String movieTitle = "The Source Code";

        //TODO 1: Declare and initialize variable for Number of tickets
        int numberOfTickets = 8;

        //TODO 1: Declare and initialize variable for Price per ticket
        double pricePerTicket = 10.57;

        System.out.println("First Name: " + firstName);
        System.out.println("Last Name: " + lastName);
        System.out.println("Movie Title: " + movieTitle);
        System.out.println("Number of Tickets: " + numberOfTickets);
        System.out.println("Price per ticket in USD: " + pricePerTicket);

        //TODO 2: Calculate the total price based on number of tickets and price per ticket. Print the output of the total price
        double totalPrice = numberOfTickets * pricePerTicket;
        System.out.println("Total Price of 8 tickets: " + totalPrice);

        //TODO 3: Concatenate first name and last name into new variable username with no space in between
        // first and last name and convert them to lower case
        String userName = (firstName + lastName).toLowerCase();
        System.out.println("Username: " + userName);

        // TODO 4: Convert the movie title to upper case and assign to variable movieName
        String movieName = movieTitle.toUpperCase();
        System.out.println("Updated movie name: " + movieName);

        // TODO 5: Write multiple print statements to match your output as per the Final Expected Output
        System.out.println("Congratulations!! You have successfully booked the tickets\n");
        System.out.println("Username: " + userName);
        System.out.println("Movie: " + movieName);
        System.out.println("Number of Tickets: " + numberOfTickets);
        System.out.println("Price per ticket: $" + pricePerTicket);
        System.out.println("Total price of " + numberOfTickets + " tickets: $" + totalPrice);
        System.out.println("Thank you for choosing us for booking your movie tickets\n");
        System.out.println("Enjoy your movie!!!");
    }
}
