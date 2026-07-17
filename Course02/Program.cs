using System;

namespace LibraryManagementSystem
{
    class Program
    {
        static string[] books = new string[5];
        static bool[] isCheckedOut = new bool[5];
        static int borrowedCount = 0;
        const int MaxBorrowLimit = 3;

        static void Main(string[] args)
        {
            books[0] = "Clean Code";
            books[1] = "Design Patterns";
            books[2] = "Refactoring";

            bool running = true;
            while (running)
            {
                Console.Clear();
                Console.WriteLine("==============================================");
                Console.WriteLine("         LIBRARY MANAGEMENT SYSTEM            ");
                Console.WriteLine("==============================================");
                Console.WriteLine("1. Add Book to Catalog");
                Console.WriteLine("2. Remove Book from Catalog");
                Console.WriteLine("3. View Catalog");
                Console.WriteLine("4. Search Book");
                Console.WriteLine("5. Check Out Book (Borrow)");
                Console.WriteLine("6. Check In Book (Return)");
                Console.WriteLine("7. Exit");
                Console.WriteLine("==============================================");
                Console.Write("Enter your choice (1-7): ");

                string choice = Console.ReadLine()?.Trim();
                switch (choice)
                {
                    case "1":
                        AddBook();
                        break;
                    case "2":
                        RemoveBook();
                        break;
                    case "3":
                        ViewCatalog();
                        break;
                    case "4":
                        SearchBook();
                        break;
                    case "5":
                        CheckOutBook();
                        break;
                    case "6":
                        CheckInBook();
                        break;
                    case "7":
                        Console.WriteLine("\nExiting Library Management System. Goodbye!");
                        running = false;
                        break;
                    default:
                        ShowError("Invalid choice! Please enter a number between 1 and 7.");
                        break;
                }
            }
        }

        static void AddBook()
        {
            Console.Clear();
            Console.WriteLine("--- Add Book ---");
            int slot = -1;
            for (int i = 0; i < books.Length; i++)
            {
                if (string.IsNullOrEmpty(books[i]))
                {
                    slot = i;
                    break;
                }
            }

            if (slot == -1)
            {
                ShowError("Catalog is full! Cannot add more than 5 books.");
                return;
            }

            Console.Write("Enter book title: ");
            string title = Console.ReadLine()?.Trim();
            if (string.IsNullOrEmpty(title))
            {
                ShowError("Book title cannot be empty.");
                return;
            }

            for (int i = 0; i < books.Length; i++)
            {
                if (!string.IsNullOrEmpty(books[i]) && string.Equals(books[i], title, StringComparison.OrdinalIgnoreCase))
                {
                    ShowError("This book already exists in the catalog.");
                    return;
                }
            }

            books[slot] = title;
            isCheckedOut[slot] = false;
            ShowSuccess($"Book '{title}' added to slot {slot + 1}.");
        }

        static void RemoveBook()
        {
            Console.Clear();
            Console.WriteLine("--- Remove Book ---");
            Console.Write("Enter title of book to remove: ");
            string title = Console.ReadLine()?.Trim();
            if (string.IsNullOrEmpty(title))
            {
                ShowError("Book title cannot be empty.");
                return;
            }

            for (int i = 0; i < books.Length; i++)
            {
                if (!string.IsNullOrEmpty(books[i]) && string.Equals(books[i], title, StringComparison.OrdinalIgnoreCase))
                {
                    if (isCheckedOut[i])
                    {
                        borrowedCount--;
                        isCheckedOut[i] = false;
                    }
                    string removedTitle = books[i];
                    books[i] = null;
                    ShowSuccess($"Book '{removedTitle}' removed from the catalog.");
                    return;
                }
            }
            ShowError($"Book '{title}' not found in the catalog.");
        }

        static void ViewCatalog()
        {
            Console.Clear();
            Console.WriteLine("--- Library Catalog ---");
            bool isEmpty = true;
            for (int i = 0; i < books.Length; i++)
            {
                if (!string.IsNullOrEmpty(books[i]))
                {
                    string status = isCheckedOut[i] ? "[Checked Out]" : "[Available]";
                    Console.WriteLine($"Slot {i + 1}: {books[i]} {status}");
                    isEmpty = false;
                }
                else
                {
                    Console.WriteLine($"Slot {i + 1}: [Empty Slot]");
                }
            }

            if (isEmpty)
            {
                Console.WriteLine("\nCatalog is completely empty.");
            }
            Console.WriteLine($"\nBooks currently borrowed: {borrowedCount} / {MaxBorrowLimit}");
            Pause();
        }

        static void SearchBook()
        {
            Console.Clear();
            Console.WriteLine("--- Search Book ---");
            Console.Write("Enter search query (exact or partial title): ");
            string query = Console.ReadLine()?.Trim();
            if (string.IsNullOrEmpty(query))
            {
                ShowError("Search query cannot be empty.");
                return;
            }

            bool found = false;
            for (int i = 0; i < books.Length; i++)
            {
                if (!string.IsNullOrEmpty(books[i]) && books[i].Contains(query, StringComparison.OrdinalIgnoreCase))
                {
                    string status = isCheckedOut[i] ? "[Checked Out]" : "[Available]";
                    Console.WriteLine($"Match found -> Slot {i + 1}: {books[i]} {status}");
                    found = true;
                }
            }

            if (!found)
            {
                ShowError($"No books found matching '{query}'.");
            }
            else
            {
                Pause();
            }
        }

        static void CheckOutBook()
        {
            Console.Clear();
            Console.WriteLine("--- Check Out Book (Borrow) ---");
            if (borrowedCount >= MaxBorrowLimit)
            {
                ShowError($"Borrowing limit reached! You cannot borrow more than {MaxBorrowLimit} books at a time.");
                return;
            }

            Console.Write("Enter title of book to check out: ");
            string title = Console.ReadLine()?.Trim();
            if (string.IsNullOrEmpty(title))
            {
                ShowError("Book title cannot be empty.");
                return;
            }

            for (int i = 0; i < books.Length; i++)
            {
                if (!string.IsNullOrEmpty(books[i]) && string.Equals(books[i], title, StringComparison.OrdinalIgnoreCase))
                {
                    if (isCheckedOut[i])
                    {
                        ShowError($"Book '{books[i]}' is already checked out.");
                        return;
                    }
                    isCheckedOut[i] = true;
                    borrowedCount++;
                    ShowSuccess($"You checked out '{books[i]}'. Borrowed count: {borrowedCount}/{MaxBorrowLimit}.");
                    return;
                }
            }
            ShowError($"Book '{title}' not found in the catalog.");
        }

        static void CheckInBook()
        {
            Console.Clear();
            Console.WriteLine("--- Check In Book (Return) ---");
            Console.Write("Enter title of book to check in: ");
            string title = Console.ReadLine()?.Trim();
            if (string.IsNullOrEmpty(title))
            {
                ShowError("Book title cannot be empty.");
                return;
            }

            for (int i = 0; i < books.Length; i++)
            {
                if (!string.IsNullOrEmpty(books[i]) && string.Equals(books[i], title, StringComparison.OrdinalIgnoreCase))
                {
                    if (!isCheckedOut[i])
                    {
                        ShowError($"Book '{books[i]}' is already in the library ([Available]).");
                        return;
                    }
                    isCheckedOut[i] = false;
                    borrowedCount--;
                    ShowSuccess($"You checked in '{books[i]}'. Borrowed count: {borrowedCount}/{MaxBorrowLimit}.");
                    return;
                }
            }
            ShowError($"Book '{title}' not found in the catalog.");
        }

        static void ShowError(string message)
        {
            Console.ForegroundColor = ConsoleColor.Red;
            Console.WriteLine($"\nError: {message}");
            Console.ResetColor();
            Pause();
        }

        static void ShowSuccess(string message)
        {
            Console.ForegroundColor = ConsoleColor.Green;
            Console.WriteLine($"\nSuccess: {message}");
            Console.ResetColor();
            Pause();
        }

        static void Pause()
        {
            Console.WriteLine("\nPress any key to return to the Main Menu...");
            Console.ReadKey(true);
        }
    }
}
