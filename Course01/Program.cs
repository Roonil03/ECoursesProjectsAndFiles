using System;
using System.Collections.Generic;

namespace StudentGradeManagementSystem
{
    /// <summary>
    /// Represents a subject and its corresponding numerical score.
    /// </summary>
    public class SubjectGrade
    {
        public string SubjectName { get; set; }
        public double Score { get; set; }

        public SubjectGrade(string subjectName, double score)
        {
            SubjectName = subjectName;
            Score = score;
        }
    }

    /// <summary>
    /// Represents a student profile with an ID, Name, and assigned grades.
    /// </summary>
    public class Student
    {
        public string Id { get; set; }
        public string Name { get; set; }
        public List<SubjectGrade> Grades { get; set; }

        public Student(string id, string name)
        {
            Id = id;
            Name = name;
            Grades = new List<SubjectGrade>();
        }
    }

    class Program
    {
        static void Main(string[] args)
        {
            // Initialize Student List
            List<Student> students = new List<Student>();
            bool isRunning = true;

            // Seed sample data for immediate verification
            SeedSampleData(students);

            // While App Running: Display Menu
            while (isRunning)
            {
                Console.Clear();
                Console.WriteLine("=================================================");
                Console.WriteLine("       STUDENT GRADE MANAGEMENT SYSTEM           ");
                Console.WriteLine("=================================================");
                Console.WriteLine("1. Add New Student");
                Console.WriteLine("2. Assign Grades to a Student");
                Console.WriteLine("3. Calculate Average for a Student");
                Console.WriteLine("4. Display All Student Records");
                Console.WriteLine("5. Exit Program");
                Console.WriteLine("=================================================");
                Console.Write("Enter your choice (1-5): ");

                string choice = Console.ReadLine()?.Trim();

                switch (choice)
                {
                    case "1":
                        AddStudent(students);
                        break;
                    case "2":
                        AddGrade(students);
                        break;
                    case "3":
                        CalculateAverageAndDisplay(students);
                        break;
                    case "4":
                        DisplayRecords(students);
                        break;
                    case "5":
                        Console.WriteLine("\nExiting program. Thank you for using Student Grade Management System!");
                        isRunning = false;
                        break;
                    default:
                        Console.ForegroundColor = ConsoleColor.Red;
                        Console.WriteLine("\nInvalid menu choice! Please select an option between 1 and 5.");
                        Console.ResetColor();
                        Pause();
                        break;
                }
            }
        }

        /// <summary>
        /// Choice 1 Method: AddStudent
        /// Prompts for ID & Name and validates that ID does not already exist.
        /// </summary>
        static void AddStudent(List<Student> students)
        {
            Console.Clear();
            Console.WriteLine("--- Add New Student ---");

            Console.Write("Enter Student ID: ");
            string id = Console.ReadLine()?.Trim();

            if (string.IsNullOrWhiteSpace(id))
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine("Error: Student ID cannot be empty.");
                Console.ResetColor();
                Pause();
                return;
            }

            // If ID Already Exists?
            Student existingStudent = FindStudentById(students, id);
            if (existingStudent != null)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine($"Error: A student with ID '{id}' already exists ({existingStudent.Name}).");
                Console.ResetColor();
                Pause();
                return;
            }
            else
            {
                Console.Write("Enter Student Full Name: ");
                string name = Console.ReadLine()?.Trim();

                if (string.IsNullOrWhiteSpace(name))
                {
                    Console.ForegroundColor = ConsoleColor.Red;
                    Console.WriteLine("Error: Student Name cannot be empty.");
                    Console.ResetColor();
                    Pause();
                    return;
                }

                // Save Name & ID to List
                students.Add(new Student(id, name));
                Console.ForegroundColor = ConsoleColor.Green;
                Console.WriteLine($"\nSuccess: Student profile '{name}' (ID: {id}) has been saved!");
                Console.ResetColor();
                Pause();
            }
        }

        /// <summary>
        /// Choice 2 Method: AddGrade
        /// Checks if student exists and validates that score is between 0 and 100.
        /// </summary>
        static void AddGrade(List<Student> students)
        {
            Console.Clear();
            Console.WriteLine("--- Assign Grade to Student ---");

            if (students.Count == 0)
            {
                Console.ForegroundColor = ConsoleColor.Yellow;
                Console.WriteLine("No students found. Please add a student profile first.");
                Console.ResetColor();
                Pause();
                return;
            }

            Console.Write("Enter Student ID: ");
            string id = Console.ReadLine()?.Trim();

            // If Student Exists?
            Student targetStudent = FindStudentById(students, id);
            if (targetStudent == null)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine($"Error: Student with ID '{id}' was not found.");
                Console.ResetColor();
                Pause();
                return;
            }
            else
            {
                Console.WriteLine($"Student Found: {targetStudent.Name} (ID: {targetStudent.Id})");
                Console.Write("Enter Subject Name: ");
                string subject = Console.ReadLine()?.Trim();

                if (string.IsNullOrWhiteSpace(subject))
                {
                    Console.ForegroundColor = ConsoleColor.Red;
                    Console.WriteLine("Error: Subject name cannot be empty.");
                    Console.ResetColor();
                    Pause();
                    return;
                }

                Console.Write("Enter Numeric Grade (0 - 100): ");
                string gradeInput = Console.ReadLine()?.Trim();

                if (double.TryParse(gradeInput, out double score))
                {
                    // If Score between 0 and 100?
                    if (score < 0 || score > 100)
                    {
                        Console.ForegroundColor = ConsoleColor.Red;
                        Console.WriteLine("Error: Invalid grade! Grade must be between 0 and 100.");
                        Console.ResetColor();
                    }
                    else
                    {
                        // Append Grade to Student Record
                        targetStudent.Grades.Add(new SubjectGrade(subject, score));
                        Console.ForegroundColor = ConsoleColor.Green;
                        Console.WriteLine($"\nSuccess: Grade {score:F2} for {subject} added to {targetStudent.Name}'s record.");
                        Console.ResetColor();
                    }
                }
                else
                {
                    Console.ForegroundColor = ConsoleColor.Red;
                    Console.WriteLine("Error: Invalid input! Please enter a valid numerical grade.");
                    Console.ResetColor();
                }

                Pause();
            }
        }

        /// <summary>
        /// Choice 3 Method: CalculateAverage logic wrapper
        /// Finds student and prints their calculated average using a loop.
        /// </summary>
        static void CalculateAverageAndDisplay(List<Student> students)
        {
            Console.Clear();
            Console.WriteLine("--- Calculate Average for Student ---");

            if (students.Count == 0)
            {
                Console.ForegroundColor = ConsoleColor.Yellow;
                Console.WriteLine("No students enrolled yet.");
                Console.ResetColor();
                Pause();
                return;
            }

            Console.Write("Enter Student ID: ");
            string id = Console.ReadLine()?.Trim();

            Student student = FindStudentById(students, id);
            if (student == null)
            {
                Console.ForegroundColor = ConsoleColor.Red;
                Console.WriteLine($"Error: Student with ID '{id}' was not found.");
                Console.ResetColor();
            }
            else if (student.Grades.Count == 0)
            {
                Console.ForegroundColor = ConsoleColor.Yellow;
                Console.WriteLine($"Student '{student.Name}' has no assigned grades yet.");
                Console.ResetColor();
            }
            else
            {
                // Method: CalculateAverage
                double average = CalculateAverage(student);
                Console.ForegroundColor = ConsoleColor.Cyan;
                Console.WriteLine($"\nStudent: {student.Name} (ID: {student.Id})");
                Console.WriteLine($"Total Subjects: {student.Grades.Count}");
                Console.WriteLine($"Calculated Average Grade: {average:F2} / 100.00 ({DetermineLetterGrade(average)})");
                Console.ResetColor();
            }

            Pause();
        }

        /// <summary>
        /// Method: CalculateAverage
        /// For Each Grade: Sum and Divide.
        /// </summary>
        static double CalculateAverage(Student student)
        {
            if (student.Grades.Count == 0)
            {
                return 0.0;
            }

            double totalScore = 0.0;

            // For Each Grade: Sum and Divide
            for (int i = 0; i < student.Grades.Count; i++)
            {
                totalScore += student.Grades[i].Score;
            }

            return totalScore / student.Grades.Count;
        }

        /// <summary>
        /// Choice 4 Method: DisplayRecords
        /// Foreach Loop: Print Name, ID, Grades, and Averages.
        /// </summary>
        static void DisplayRecords(List<Student> students)
        {
            Console.Clear();
            Console.WriteLine("=================================================");
            Console.WriteLine("             COMPREHENSIVE STUDENT RECORDS       ");
            Console.WriteLine("=================================================");

            if (students.Count == 0)
            {
                Console.ForegroundColor = ConsoleColor.Yellow;
                Console.WriteLine("No student records available.");
                Console.ResetColor();
                Pause();
                return;
            }

            // Foreach Loop: Print Name, ID, Grades, and Averages
            foreach (Student stu in students)
            {
                Console.WriteLine($"\nID: {stu.Id} | Name: {stu.Name}");
                Console.WriteLine("-------------------------------------------------");

                if (stu.Grades.Count == 0)
                {
                    Console.WriteLine("   No grades assigned yet.");
                }
                else
                {
                    foreach (SubjectGrade sg in stu.Grades)
                    {
                        Console.WriteLine($"   Subject: {sg.SubjectName,-22} Grade: {sg.Score,6:F2}");
                    }

                    double average = CalculateAverage(stu);
                    Console.WriteLine("   .............................................");
                    Console.WriteLine($"   Calculated Average: {average:F2} / 100.00   |   Letter Grade: {DetermineLetterGrade(average)}");
                }
                Console.WriteLine("=================================================");
            }

            Pause();
        }

        /// <summary>
        /// Custom method to determine letter grade based on numeric average.
        /// </summary>
        static string DetermineLetterGrade(double average)
        {
            if (average >= 90.0) return "A";
            else if (average >= 80.0) return "B";
            else if (average >= 70.0) return "C";
            else if (average >= 60.0) return "D";
            else return "F";
        }

        /// <summary>
        /// Finds a student by ID using a loop.
        /// </summary>
        static Student FindStudentById(List<Student> students, string id)
        {
            for (int i = 0; i < students.Count; i++)
            {
                if (string.Equals(students[i].Id, id, StringComparison.OrdinalIgnoreCase))
                {
                    return students[i];
                }
            }
            return null;
        }

        /// <summary>
        /// Helper method to pause execution until key press.
        /// </summary>
        static void Pause()
        {
            Console.WriteLine("\nPress any key to return to the Main Menu...");
            Console.ReadKey(true);
        }

        /// <summary>
        /// Seeds sample data for immediate testing.
        /// </summary>
        static void SeedSampleData(List<Student> students)
        {
            Student s1 = new Student("S101", "Alice Smith");
            s1.Grades.Add(new SubjectGrade("Mathematics", 92.5));
            s1.Grades.Add(new SubjectGrade("Physics", 88.0));
            s1.Grades.Add(new SubjectGrade("Computer Science", 95.0));

            Student s2 = new Student("S102", "Bob Jones");
            s2.Grades.Add(new SubjectGrade("Mathematics", 74.0));
            s2.Grades.Add(new SubjectGrade("Physics", 68.5));

            students.Add(s1);
            students.Add(s2);
        }
    }
}
