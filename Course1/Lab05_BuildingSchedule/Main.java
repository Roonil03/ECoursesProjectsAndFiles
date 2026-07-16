
/** TODO 1 Solution Start: Import the Scanner **/
import java.util.Scanner;

/** TODO 1 Solution End: Import the Scanner **/

public class Solution {
    public static void main(String args[]) {

        /** TODO 2 Solution Start: Create a Scanner variable named "keyboard" **/
        Scanner keyboard = new Scanner(System.in);

        /** TODO 3 Solution Start: A variable for getting the day of the week is given below,initialize it to 1 **/
        /** 1- is Monday, 2- is Tuesday and so on **/
        int dayOfTheWeek = 1;

        /** TODO 8 Solution Start: Go below the TODO 2 after the creation of Scanner variable  **/
        int subChoice;


        /** TODO 4 Solution Start: Display a menu to get the day of the week from the user **/
        System.out.println("************ MENU *****************");
        System.out.println("These are the choices for week of the day. \n Please enter only values from 1-7:\n");

        System.out.println("1. MONDAY");
        System.out.println("2. TUESDAY");
        System.out.println("3. WEDNESDAY");
        System.out.println("4. THURSDAY");
        System.out.println("5. FRIDAY");
        System.out.println("6. SATURDAY");
        System.out.println("7. SUNDAY");

        System.out.println("***********************************");

        /** TODO 5 Solution Start: Prompt Toni for input. Also let Toni know the valid choices like (1-7). **/
        System.out.println("Enter your choice of the day (1-7):");
        //get the input
        dayOfTheWeek = keyboard.nextInt();


        /** TODO 6 Solution Start: Create a switch with case values from 1-7 to handle the schedule **/
        switch(dayOfTheWeek) {

            case 1:
                System.out.println("Monday: It is back to work.....");

                /** TODO 9 Solution Start: Move below the display "Monday: It is back to work....." **/
                System.out.println("Did you have your breakfast?");

                System.out.println("************ SUB MENU *****************");
                System.out.println("1. Yes, I had my breakfast");
                System.out.println("2. No, I would like one ");
                System.out.println("****************************************");

                /** TODO 10 Solution Start: After the sub-menu inside case 1, prompt for input on choice **/
                System.out.println("Enter choice (1 or 2):");
                subChoice = keyboard.nextInt();

                /** TODO 11 Solution Start: Display appropriate response to Toni, based on value in **/
                switch (subChoice) {
                    case 1:
                        System.out.println("Good. Now you have a catchup meeting at 9 A.M. Prepare your notes. ");
                        break;
                    case 2:
                        System.out.println("Lets us divert on Highway 54 and have a Burger on the drive in");
                        break;
                    default :
                        //user has entered a value which is neither 1 nor 2
                        System.out.println("NO such choice available");
                }

                /** TODO 7 Solution Start: Introduce a "break" after each case code, except the last  **/
                break;

            case 2:
                System.out.println("Tuesday: It is coding time");

                /** TODO 7 Solution Start: Introduce a "break" after each case code, except the last  **/
                break;

            case 3:
                System.out.println("Wednesday: Design the program");

                break;

            case 4:
                System.out.println("Thursday: Start coding.....");

                /** TODO 7 Solution Start: Introduce a "break" after each case code, except the last  **/
                break;

            case 5:
                System.out.println("Friday: Run tests and check if the program is working as expected.......");

                /** TODO 7 Solution Start: Introduce a "break" after each case code, except the last  **/
                break;

            case 6:
                System.out.println("Saturday: Rest a bit and study a bit.......");

                /** TODO 7 Solution Start: Introduce a "break" after each case code, except the last  **/
                break;

            case 7:
                System.out.println("Sunday: I am going to chill .........");

                /** TODO 7 Solution Start: Introduce a "break" after each case code, except the last  **/
                break;

            default:
                //user entered a value not within 1-7
                System.out.println("Invalid week of DAY");

        } // end of the switch

    } //end of main method

} // end of main class

