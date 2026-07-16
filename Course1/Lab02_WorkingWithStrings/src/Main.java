public class Main {

    public static void main(String[] args) {
        String bookTitle = "The Adventures of Captain Fantastic and the Magical Unicorn";
        int titleLength = //TODO: Complete the code statement to find out the length of bookTitle
        System.out.println("Length of the book title: " + titleLength);

        //Declare and initialise the length of shortened book title
        int maxLength = 20;


        // Shortened the book title
        String shortBookTitle = // TODO: Write the code to shorten the bookTitle to 20 characters
        System.out.println("Original title: " + bookTitle);
        System.out.println("Shortened title: " + shortBookTitle);

        // Declare and initialize the searchWord
        String searchWord = "Captain";

        // Check if the searchWord is present in book title
        boolean containsWord = //TODO: Write the code snippet to check if searchWord is present in bookTitle.
        System.out.println("Does the title contain the word \"" + searchWord + "\"? " + containsWord);
    }
}
