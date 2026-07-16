import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class IdeaWriter {

    public static void writeIdea(String filePath, String fileContents) {

        // TODO 7: Create a File object with the given filePath
        File file = new File(filePath);

        // Ensure parent directories exist
        file.getParentFile().mkdirs();

        // TODO 8: Convert the fileContents String into a byte array
        byte[] fileContentsAsBytes = fileContents.getBytes();

        // TODO 9 & 10: Pass File object to FileOutputStream and write bytes
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(fileContentsAsBytes);
            System.out.println("Idea saved successfully to " + filePath);
        } catch (FileNotFoundException e) {
            System.err.println("File not found! Please check the path and try again!");
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
