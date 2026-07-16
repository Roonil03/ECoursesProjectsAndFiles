-- Create Books table
CREATE TABLE Books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    available BOOLEAN NOT NULL
);

-- Create Borrowers table
CREATE TABLE Borrowers (
    borrower_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

-- Create Loans table
CREATE TABLE Loans (
    loan_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    borrower_id INT NOT NULL,
    loan_date DATE NOT NULL,
    return_date DATE NULL,
    FOREIGN KEY (book_id) REFERENCES Books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (borrower_id) REFERENCES Borrowers(borrower_id) ON DELETE CASCADE    
);

-- Populate Initial Data
INSERT INTO Books (title, author, available) 
VALUES 
('1984', 'George Orwell', true),
('To Kill a Mockingbird', 'Harper Lee', true),
('Pride and Prejudice', 'Jane Austen', false),
('The Great Gatsby', 'F. Scott Fitzgerald', true),
('Moby Dick', 'Herman Melville', true);

INSERT INTO Borrowers (name, email) 
VALUES 
('John Doe', 'john@example.com'),
('Jane Smith', 'jane@example.com'),
('Alice Johnson', 'alice@example.com'),
('Bob Brown', 'bob@example.com'),
('Charlie Davis', 'charlie@example.com');

INSERT INTO Loans (book_id, borrower_id, loan_date, return_date) 
VALUES 
(1, 1, '2024-09-01', '2024-09-15'),
(2, 2, '2024-09-02', NULL),
(3, 3, '2024-08-25', '2024-09-05'),
(4, 4, '2024-09-03', NULL),
(5, 5, '2024-09-04', '2024-09-12');