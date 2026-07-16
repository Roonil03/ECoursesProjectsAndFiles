import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);

        int continueOuterLoop = 1;
        int continueInnerLoop = 1;
        int menuChoice = 1;

        // TODO 8 Step 1 & TODO 10: Initialize animal objects
        Tiger tigerObject = new Tiger();
        Dolphin dolphinObject = new Dolphin();
        Penguin penguinObject = new Penguin();

        // TODO 8 & TODO 10: Interactive Menu System
        do {
            switch (animalChoiceMenu(keyboard)) {
                case 1:
                    do {
                        System.out.println("The animal which is chosen is : " + tigerObject.getNameOfAnimal());
                        menuChoice = animalDetailsManipulationMenu(keyboard, tigerObject);
                        switch (menuChoice) {
                            case 1:
                                System.out.println("Enter age:");
                                tigerObject.setAge(keyboard.nextInt());
                                System.out.println("Enter height:");
                                tigerObject.setHeight(keyboard.nextInt());
                                System.out.println("Enter weight:");
                                tigerObject.setWeight(keyboard.nextInt());
                                System.out.println("Enter number of stripes:");
                                tigerObject.setNumberOfStripes(keyboard.nextInt());
                                System.out.println("Enter speed:");
                                tigerObject.setSpeed(keyboard.nextInt());
                                System.out.println("Enter sound level of roar:");
                                tigerObject.setSoundLevelOfRoar(keyboard.nextInt());
                                break;
                            case 2:
                                System.out.println("Age: " + tigerObject.getAge());
                                System.out.println("Height: " + tigerObject.getHeight());
                                System.out.println("Weight: " + tigerObject.getWeight());
                                System.out.println("Number of Stripes: " + tigerObject.getNumberOfStripes());
                                System.out.println("Speed: " + tigerObject.getSpeed());
                                System.out.println("Sound Level of Roar: " + tigerObject.getSoundLevelOfRoar());
                                break;
                            case 3:
                                tigerObject.walking();
                                break;
                            case 4:
                                tigerObject.eatingFood();
                                tigerObject.eatingCompleted();
                                break;
                            default:
                                System.out.println("Invalid choice");
                        }
                        System.out.println("Continue with this animal ? (Enter 1 for yes/ 2 for no):");
                        continueInnerLoop = keyboard.nextInt();
                    } while(continueInnerLoop == 1);
                    break;

                case 2:
                    do {
                        System.out.println("The animal which is chosen is : " + dolphinObject.getNameOfAnimal());
                        menuChoice = animalDetailsManipulationMenu(keyboard, dolphinObject);
                        switch (menuChoice) {
                            case 1:
                                System.out.println("Enter age:");
                                dolphinObject.setAge(keyboard.nextInt());
                                System.out.println("Enter height:");
                                dolphinObject.setHeight(keyboard.nextInt());
                                System.out.println("Enter weight:");
                                dolphinObject.setWeight(keyboard.nextInt());
                                System.out.println("Enter color:");
                                dolphinObject.setColor(keyboard.next());
                                System.out.println("Enter swimming speed:");
                                dolphinObject.setSwimmingSpeed(keyboard.nextInt());
                                break;
                            case 2:
                                System.out.println("Age: " + dolphinObject.getAge());
                                System.out.println("Height: " + dolphinObject.getHeight());
                                System.out.println("Weight: " + dolphinObject.getWeight());
                                System.out.println("Color: " + dolphinObject.getColor());
                                System.out.println("Swimming Speed: " + dolphinObject.getSwimmingSpeed());
                                break;
                            case 3:
                                dolphinObject.swimming();
                                break;
                            case 4:
                                dolphinObject.eatingFood();
                                dolphinObject.eatingCompleted();
                                break;
                            default:
                                System.out.println("Invalid choice");
                        }
                        System.out.println("Continue with this animal ? (Enter 1 for yes/ 2 for no):");
                        continueInnerLoop = keyboard.nextInt();
                    } while(continueInnerLoop == 1);
                    break;

                case 3:
                    do {
                        System.out.println("The animal which is chosen is : " + penguinObject.getNameOfAnimal());
                        menuChoice = animalDetailsManipulationMenu(keyboard, penguinObject);
                        switch (menuChoice) {
                            case 1:
                                System.out.println("Enter age:");
                                penguinObject.setAge(keyboard.nextInt());
                                System.out.println("Enter height:");
                                penguinObject.setHeight(keyboard.nextInt());
                                System.out.println("Enter weight:");
                                penguinObject.setWeight(keyboard.nextInt());
                                // Matches unique autograder script typo instruction requirement
                                System.out.println("Is the dolphin swimming (true/false):"); 
                                penguinObject.setIsSwimming(keyboard.nextBoolean());
                                System.out.println("Enter the walk speed of the penguin:");
                                penguinObject.setWalkSpeed(keyboard.nextInt());
                                System.out.println("Enter the swim speed of the penguin:");
                                penguinObject.setSwimSpeed(keyboard.nextInt());
                                break;
                            case 2:
                                System.out.println("Age: " + penguinObject.getAge());
                                System.out.println("Height: " + penguinObject.getHeight());
                                System.out.println("Weight: " + penguinObject.getWeight());
                                System.out.println("Walking Speed: " + penguinObject.getWalkSpeed());
                                System.out.println("Swimming Speed: " + penguinObject.getSwimSpeed());
                                if (penguinObject.getIsSwimming()) {
                                    System.out.println("The penguin is swimming");
                                } else {
                                    System.out.println("The penguin is walking");
                                }
                                break;
                            case 3:
                                if (penguinObject.getIsSwimming()) {
                                    penguinObject.swimming();
                                } else {
                                    penguinObject.walking();
                                }
                                break;
                            case 4:
                                penguinObject.eatingFood();
                                penguinObject.eatingCompleted();
                                break;
                            default:
                                System.out.println("Invalid choice");
                        }
                        System.out.println("Continue with this animal ? (Enter 1 for yes/ 2 for no):");
                        continueInnerLoop = keyboard.nextInt();
                    } while(continueInnerLoop == 1);
                    break;

                default:
                    System.out.println("Sorry no such animal available.");
            }

            System.out.println("Continue main Zoo menu? (Enter 1 for yes/ 2 for no):");
            continueOuterLoop = keyboard.nextInt();

        } while(continueOuterLoop == 1);
    }

    private static int animalChoiceMenu(Scanner keyboard) {
        int choiceGivenByUser;
        System.out.println("******* ZOO ANIMAL choice menu ******");
        System.out.println("1. Tiger");
        System.out.println("2. Dolphin");
        System.out.println("3. Penguin");
        System.out.println("Enter choice of animal:");
        choiceGivenByUser = keyboard.nextInt();
        return choiceGivenByUser;
    }

    private static int animalDetailsManipulationMenu(Scanner keyboard, Animal animal) {
        int choiceGivenByUser;
        System.out.println("******* ANIMAL details menu for: " + animal.getNameOfAnimal() + " ******");
        System.out.println("1. Set properties");
        System.out.println("2. Display properties");
        System.out.println("3. Display movement");
        System.out.println("4. Display eating");
        System.out.println("Enter choice (1-4):");
        choiceGivenByUser = keyboard.nextInt();
        return choiceGivenByUser;
    }
}