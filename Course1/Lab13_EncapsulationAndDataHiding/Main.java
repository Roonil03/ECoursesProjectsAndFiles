public class Main {
    public static void main(String[] args) {

        /** TODO 2 Solution: Create an object of the SideKick class named "sideKickObject" **/
        SideKick sideKickObject = new SideKick();
        /** TODO 2 Solution End **/

        /** TODO 3: Try accessing the property "modeOfOperation" using the dot operator
         *          like:
         *          System.out.println("Mode of operation is: " + sideKickObject.modeOfOperation);
         *
         **/
        //System.out.println("Mode of operation is: " + sideKickObject.modeOfOperation);

        /** TODO 3 Solution End **/

        /** TODO 4 Solution: Comment out the line created by TODO 3 by putting "//" above    **/


        /** TODO 6 Solution: Display the current value of the property "modeOfOperation"  of the object
         *         "sideKickObject" within a println() using the getter method of the object
         *         you created in TODO 5
         **/
        System.out.println("The value of modeOfOperation is: " + sideKickObject.getModeOfOperation());

        /** TODO 6 Solution End **/

        /** TODO 9 Solution: Set the value of the property "modeOfOperation" of the object
         *        "sideKickObject", using the setter you created in TODO 8, to a
         *        a value of 3
         **/
        sideKickObject.setModeOfOperation(3);

        /** TODO 9 Solution End **/

        /** TODO 10 Solution: Call the method takeAction() of the object "sideKickObject" **/
        sideKickObject.takeAction();
        /** TODO 10 Solution End **/


        /** TODO 12: Set the value of the property "modeOfOperation" of the object
         *          "sideKickObject to a value of 1
         *           using the setter method you created in TODO 7 and TODO 8.
         *           After that is done, call the method takeAction() using object
         *           "sideKickObject" and the dot operator.
         **/
        sideKickObject.setModeOfOperation(1);
        sideKickObject.takeAction();

        /** TODO 12 Solution End **/


        /** TODO 15: Set the value of the property "modeOfOperation" of the object
         *          "sideKickObject to a value of 2
         *           using the setter method you created in TODO 7 and TODO 8.
         *           After that is done, call the method takeAction() using object
         *           "sideKickObject" and the dot operator.
         **/
        sideKickObject.setModeOfOperation(2);
        sideKickObject.takeAction();
        /** TODO 15 Solution End **/
    }
}