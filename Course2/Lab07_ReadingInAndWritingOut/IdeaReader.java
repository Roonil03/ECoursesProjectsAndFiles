import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class IdeaReader {

    public static String readIdea(String filePath) {
        String fileContents = "";

        // TODO 1: Create a File object using the filePath parameter
        File file = new File(filePath);

        // TODO 2: Pass the file object to the FileInputStream in the try block
        try (FileInputStream inputStream = new FileInputStream(file)) {

            // TODO 3: Read all bytes from the file and store in a byte array
            byte[] fileContentsAsBytes = inputStream.readAllBytes();

            // TODO 4: Convert the byte array to a String
            fileContents = new String(fileContentsAsBytes);

            // Print the file contents
            System.out.println("\n--- Idea Content ---");
            System.out.println(fileContents);
            System.out.println("--------------------\n");

        } catch (FileNotFoundException e) {
            // TODO 5: Print an error message if the file is not found
            System.err.println("File not found! Please check the path and try again!");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return fileContents;
    }
}
