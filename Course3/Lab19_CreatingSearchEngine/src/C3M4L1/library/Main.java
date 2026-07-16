package C3M4L1.library;

import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // TODO 4, TODO 8, TODO 12, TODO 16, TODO 23: Complete initialization & serialization orchestrations
        Library library = new Library();
        LibrarySerializer serializer = new LibrarySerializer();
        UserInteractionLogger logger = new UserInteractionLogger();

        logger.log("Program started and menu displayed.");

        // Load operations
        List<Book> books = serializer.loadLibrary("src/resources/data/library.ser");
        if (books != null) {
            library.setBooks(books);
            System.out.println("Library loaded successfully from src/resources/data/library.ser");
        } else {
            System.out.println("Loading data from books.txt...");
            library.loadBooks("src/resources/data/books.txt");
        }

        // Active control session
        LibraryMenu menu = new LibraryMenu(library);
        menu.displayMenu();

        // Save operations upon exit
        serializer.saveLibrary(library.getBooks(), "src/resources/data/library.ser");
        System.out.println("Library saved successfully to src/resources/data/library.ser");
    }
}