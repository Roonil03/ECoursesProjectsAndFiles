import java.util.Scanner;

// DrivingSimulation class implements the Driving Simulation which includes various actions like turn the engine on/off, accelerate (i.e. increase the speed) and apply the brakes.
public class DrivingSimulationSolution{

    // Initial position of the gear is P (i.e. Park)
    public static String gear = "P";
    // Initial speed is 0
    public static int speed = 0;
    // Engine is initially off
    public static boolean isEngineOn = false;
    // Scanner for user input
    public static Scanner keyBoard = new Scanner(System.in);

    // Starts the car simulation
    public static void startSimulation() {
        boolean displayMenu = true;
        while (displayMenu) {
            displayDashboard();
            int choice = getUserChoice();
            processChoice(choice);
            System.out.println();
        }
    }

    // Display dashboard and menu
    public static void displayDashboard() {
        System.out.println("------ Car Dashboard ------");
        System.out.println("Speed: " + speed);
        System.out.println("Engine: " + (isEngineOn ? "On" : "Off"));
        System.out.println("Gear: " + gear);
        System.out.println("Menu:");
        System.out.println("1. Turn on/off the engine");
        System.out.println("2. Change gear (P, D, R)");
        System.out.println("3. Accelerate");
        System.out.println("4. Brake");
        System.out.println("5. Exit");
    }

    // Get user menu choice
    public static int getUserChoice() {
        System.out.print("Enter your choice from above menu: ");
        return keyBoard.nextInt();
    }

    // Process user choice
    public static void processChoice(int choice) {
        switch (choice) {
            case 1:
                toggleEngine();
                break;
            case 2:
                changeGear();
                break;
            case 3:
                accelerate();
                break;
            case 4:
                brake();
                break;
            case 5:
                exitSimulation();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    // Toggle engine on/off
    public static void toggleEngine() {
        isEngineOn = !isEngineOn;
        System.out.println("Engine is now " + (isEngineOn ? "On" : "Off"));
    }

    // Change gear
    public static void changeGear() {
        System.out.print("Enter gear (P, D, R): ");
        String inputGear = keyBoard.next().toUpperCase();
        if (inputGear.equals("P") || inputGear.equals("D") || inputGear.equals("R")) {
            gear = inputGear;
            System.out.println("Gear changed to " + gear);
        } else {
            System.out.println("Invalid gear! Please enter P, D, or R.");
        }
    }

    // Accelerate car
    public static void accelerate() {
        if (isEngineOn && !gear.equals("P")) {
            speed += 10;
            System.out.println("Car accelerated. Current speed: " + speed);
        } else {
            System.out.println("Cannot accelerate while engine is off or in Park (P) gear.");
        }
    }

    // Apply brakes
    public static void brake() {
        if (isEngineOn && speed > 5) {
            speed -= 5;
            System.out.println("Brake applied. Current speed: " + speed);
        } else {
            System.out.println("Cannot apply braking while engine is off or at no speed.");
        }
    }

    // Exit simulation
    public static void exitSimulation() {
        System.out.println("Exiting the car simulation. Goodbye!");
        System.exit(0);
    }

    // Main method
    public static void main(String[] args) {
        startSimulation();
    }
}
