import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        boolean isEngineOn = false;
        String gear = "P";
        int speed   = 0;
        int choice  = 0;
        Scanner keyBoard   = new Scanner(System.in);

        //////////////////////////////////////////////////////
        while (choice !=5) {
            System.out.println("------ Car Dashboard ------");
            System.out.println("Speed: " + speed);
            System.out.println("Engine running: " + isEngineOn);
            System.out.println("Gear: " + gear);
            System.out.println("------ End Car Dashboard ------");
            System.out.println();
            System.out.println("Menu:");
            System.out.println("1. Turn on/off the engine");
            System.out.println("2. Change gear (P, D, R)");
            System.out.println("3. Accelerate");
            System.out.println("4. Brake");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            choice = keyBoard.nextInt();

            System.out.print("Menu item selected : " + choice);
            System.out.println();
            //////////////////////////////////////////////////////

            switch (choice) {
                case 1:
                    isEngineOn = !isEngineOn;
                    break;
                case 2:
                    System.out.print("Enter gear (P, D, R): ");
                    gear = keyBoard.next();
                    break;
                case 3:
                    if (isEngineOn && !gear.equals("P")) {
                        speed += 10; // speed = speed+10
                    } else {
                        System.out.println("Cannot accelerate while engine is off or in Park (P) gear.");
                    }
                    break;
                case 4:
                    if (isEngineOn && speed > 0) {
                        speed -= 5; // speed = speed-5
                    } else {
                        System.out.println("Cannot apply braking while engine is off or car not moving.");
                    }
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        System.out.println();
        System.out.println("Turning off engine and exiting the car. Goodbye!");
    }
}