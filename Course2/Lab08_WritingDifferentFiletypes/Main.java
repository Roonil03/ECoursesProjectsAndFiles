import java.io.*;
import java.util.Scanner;

public class Main {

    // Task 2 - Write Person to file1.txt using FileWriter

    // TODO 4: Add a static method textFileWrite() that returns void
    public static void textFileWrite() {

        // TODO 5: Create Person object
        Person person = new Person("Alice", 25, 55.5);

        // TODO 6-7: Write to file using FileWriter in try-catch
        try {
            FileWriter writer = new FileWriter("file1.txt");
            writer.write(person.toString());
            writer.close();

            // TODO: Print message "File written with FileWriter..."
            System.out.println("File written with FileWriter...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Task 3 - Write user input to file2.txt using PrintWriter

    // TODO 8: Define a static method printWrite() that returns void
    public static void printWrite() {

        // TODO 9: Take input using Scanner
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter age: ");
        int age = sc.nextInt();
        System.out.print("Enter weight: ");
        double weight = sc.nextDouble();
        sc.nextLine(); // consume leftover newline

        // TODO 10: Create Person object
        Person person = new Person(name, age, weight);

        // TODO 11-12: Write to file using PrintWriter in try-catch
        try {
            PrintWriter pw = new PrintWriter("file2.txt");
            pw.println(person.toString());
            pw.close();

            // TODO: Print message "File written with PrintWriter..."
            System.out.println("File written with PrintWriter...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Task 4 - Write byte array to binary file3.dat using FileOutputStream

    // TODO 13: Define a static method writeBinaryNumbers() that returns void
    public static void writeBinaryNumbers() {

        // TODO 14: Declare byte array
        byte[] numbers = {10, 20, 30, 40, 50};

        // TODO 15-17: Write bytes using FileOutputStream in try-catch
        try {
            FileOutputStream fos = new FileOutputStream("file3.dat");
            for (byte num : numbers) {
                fos.write(num);
            }
            fos.close();

            // TODO: Print message "File written with FileOutputStream..."
            System.out.println("File written with FileOutputStream...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Task 5 - Write Person object to binary file4.dat using DataOutputStream

    // TODO 18: Define a static method writePersonBinary() that returns void
    public static void writePersonBinary() {

        // TODO 19: Read input using Scanner
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter age: ");
        int age = sc.nextInt();
        System.out.print("Enter weight: ");
        double weight = sc.nextDouble();
        sc.nextLine(); // consume leftover newline

        // TODO 20: Create Person object
        // (in this method we just use attributes directly, no Person object needed for writing)

        // TODO 21-23: Write attributes using DataOutputStream in try-catch
        try {
            DataOutputStream dos = new DataOutputStream(new FileOutputStream("file4.dat"));
            dos.writeUTF(name);
            dos.writeInt(age);
            dos.writeDouble(weight);
            dos.close();

            // TODO: Print message "File written with DataOutputStream..."
            System.out.println("File written with DataOutputStream...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // TODO 24: Call all static methods to test
        textFileWrite();
        writeBinaryNumbers();
        printWrite();
        writePersonBinary();
    }
}
