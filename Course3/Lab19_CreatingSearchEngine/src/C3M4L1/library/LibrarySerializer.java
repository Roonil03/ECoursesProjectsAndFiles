package C3M4L1.library;

import java.io.*;
import java.util.List;

public class LibrarySerializer {

    public void saveLibrary(List<Book> books, String fileName) {
        // TODO 21: Use writeObject method to serialize the list of books
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public List<Book> loadLibrary(String fileName) {
        File file = new File(fileName);
        if (!file.exists() || file.length() == 0) {
            return null;
        }

        // TODO 22: Use ObjectInputStream to read the list of books and cast properly
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (List<Book>) ois.readObject();
        } catch (EOFException e) {
            System.err.println("The file is empty or corrupted: " + fileName);
            return null;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}