public class Rectangle {
    // member variables to store the length and width of a rectangle
    double length;
    double width;

    // No-argument constructor with default values
    public Rectangle() {
        this.length = 1.0;
        this.width = 1.0;
    }

    // Parameterized constructor
    public Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }
}
