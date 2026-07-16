package C3M4L1.library;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class UserInteractionLogger {

    private static final String LOG_FILE = "src/resources/data/user_interactions.log";

    public void logSearch(String searchTerm) {
        log("Search for: " + searchTerm);
    }

    public void logSort(String sortCriteria) {
        log("Sorted by: " + sortCriteria);
    }

    public void logViewAllBooks() {
        log("Viewed all books");
    }

    public void log(String message) {
        // TODO 17: Open the log file in append mode using a FileWriter
        // TODO 18: Combine timestamp with message
        // TODO 19: Handle exceptions with a try-catch block
        try (FileWriter fw = new FileWriter(LOG_FILE, true)) {
            fw.write(LocalDateTime.now() + " - " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}