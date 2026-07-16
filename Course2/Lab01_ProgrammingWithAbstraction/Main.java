import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Calculating Area of Rectangle: ");
        System.out.print("Enter Width:");
        float w = in.nextFloat();
        System.out.print("Enter height: ");
        float h = in.nextFloat();

        Rectangle rct = new Rectangle(w, h);
        rct.draw();
        System.out.println("Area: "+rct.calculateArea());

        System.out.println("Calculating perimeter of rectangle: ");
        System.out.println("Perimeter: "+rct.calculatePerimeter());


        System.out.println("Calculating Area of Circle: ");
        System.out.print("Enter radius:");
        float r = in.nextFloat();

        Circle cr = new Circle(r);
        cr.draw();
        System.out.println("Area: "+cr.calculateArea());

        System.out.println("Calculating perimeter of a Circle: ");
        System.out.println("Perimeter: "+cr.calculatePerimeter());
    }


}
