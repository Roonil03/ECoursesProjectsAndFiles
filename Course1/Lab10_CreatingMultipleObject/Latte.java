// TODO 14: Inherit from Coffee using the extends keyword
public class Latte extends Coffee {
    // TODO 15: Declare two attributes to store the milk type and syrup flavor (String)
    String milkType;
    String syrupFlavor;

    // TODO 16: Constructor to initialize all attributes
    public Latte(String name, String roast, double price, String milkType, String syrupFlavor) {
        // TODO 17: Use super to call the Coffee constructor
        super(name, roast, price);

        // TODO 18: Initialize milkType and syrupFlavor using this
        this.milkType = milkType;
        this.syrupFlavor = syrupFlavor;
    }

    // TODO 19: Define a method to print the details of milk type and syrup flavor along with the total bill
    public void printLatteDetails() {
        System.out.println("Your latte has " + milkType + " milk and " + syrupFlavor + " flavor.");
        System.out.println("Your total bill is " + price + "$.");
    }
}
