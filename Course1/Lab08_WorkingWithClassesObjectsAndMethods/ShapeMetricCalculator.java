public class ShapeMetricCalculator {
    // method to calculate the area of a rectangle object
    public static double calculateRectangleArea(Rectangle rectangle) {
            return rectangle.length * rectangle.width;
    }

    // method to calculate the area of a circle object
    public static double calculateCircleArea(Circle circle) {
        return circle.radius * circle.radius * Math.PI;
    }
}
