// TODO 2: Inherit from Coffee using the extends keyword
public class Espresso extends Coffee {

    // TODO 3: Declare an attribute to store the number of shots (int)
    int numberOfShots;

    // TODO 4: Constructor to initialize all attributes
    public Espresso(String name, String roast, double price, int numberOfShots) {
        // TODO 5: Use super to call the Coffee constructor
        super(name, roast, price);

        // TODO 6: Initialize numberOfShots using this
        this.numberOfShots = numberOfShots;
    }

    // TODO 7: Define a method to print a message about the number of servings and total bill
    public void printEspressoDetails() {
        System.out.println("You asked for " + numberOfShots + " servings!");
        System.out.println("Every serving of Espresso costs " + price + "$. Your total bill is " + (price * numberOfShots) + "$.");
    }
}
