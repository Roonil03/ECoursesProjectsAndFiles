public class Main {
    public static void main(String[] args) {
        // Rectangle object with specified length and width
        Rectangle rectangle = new Rectangle(5.0, 3.0);

        // Circle object with specified radius
        Circle circle = new Circle(4.0);

        // calculate the area of the rectangle, and store the result in a variable
        double areaOfRectangle = ShapeMetricCalculator.calculateRectangleArea(rectangle);

        // calculate the area of the circle, and store the result in a variable
        double areaOfCircle = ShapeMetricCalculator.calculateCircleArea(circle);

        // Print the results
        System.out.println("Area Of Rectangle: " + areaOfRectangle);
        System.out.println("Area Of Circle: " + areaOfCircle);
    }
}
