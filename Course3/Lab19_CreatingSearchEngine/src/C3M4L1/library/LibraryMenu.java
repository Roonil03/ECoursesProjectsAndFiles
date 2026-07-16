package C3M4L1.library;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class LibraryMenu {
    private Library library;
    private UserInteractionLogger logger = new UserInteractionLogger();
    private LibrarySerializer serializer = new LibrarySerializer();

    public LibraryMenu(Library library) {
        this.library = library;

        List<Book> books = serializer.loadLibrary("src/resources/data/library.ser");
        if (books != null) {
            library.setBooks(books);
            System.out.println("Library loaded successfully.");
        } else {
            System.out.println("No saved library found. Loading default books.");
            library.loadBooks("src/resources/data/books.txt");
        }
    }

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        
        // TODO 13: Use a while(true) loop to keep menu active
        while (true) {
            // TODO 14: Present the menu options
            System.out.println("\n--- Library Menu ---");
            System.out.println("1. Viewing all books.");
            System.out.println("2. Sorting books by title.");
            System.out.println("3. Sorting books by author.");
            System.out.println("4. Sorting books by publication year.");
            System.out.println("5. Searching for a book by keyword.");
            System.out.println("6. Exiting the program.");
            System.out.print("Enter choice (1-6): ");
            
            // TODO 15 & TODO 20: Use a Scanner and switch statement, integrate logging
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    library.viewAllBooks();
                    logger.logViewAllBooks();
                    break;
                case 2:
                    SortUtil.bubbleSort(library.getBooks(), Comparator.comparing(Book::getTitle));
                    System.out.println("Books sorted by title:");
                    library.viewAllBooks();
                    logger.logSort("title");
                    break;
                case 3:
                    SortUtil.insertionSort(library.getBooks(), Comparator.comparing(Book::getAuthor));
                    System.out.println("Books sorted by author:");
                    library.viewAllBooks();
                    logger.logSort("author");
                    break;
                case 4:
                    SortUtil.quickSort(library.getBooks(), Comparator.comparing(Book::getPublicationYear), 0, library.getBooks().size() - 1);
                    System.out.println("Books sorted by publication year:");
                    library.viewAllBooks();
                    logger.logSort("publication year");
                    break;
                case 5:
                    System.out.print("Enter keyword (title, author, or year): ");
                    String keyword = scanner.nextLine();
                    Book foundBook = library.searchBookByKeyword(keyword);
                    if (foundBook != null) {
                        System.out.println("Book found: " + foundBook);
                    } else {
                        System.out.println("Book not found.");
                    }
                    logger.logSearch(keyword);
                    break;
                case 6:
                    logger.log("Program exited via menu.");
                    return; // Exits displayMenu loop so Main can handle final serialization
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}