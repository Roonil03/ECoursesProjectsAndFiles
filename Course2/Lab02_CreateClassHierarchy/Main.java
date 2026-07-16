// TODO 14: Inside main(), create Date instances
public class Main {
    public static void main(String[] args) {
        // Create Date objects
        Date dobOfStudent = new Date(15, 8, 2005);
        Date dobOfTeacher = new Date(10, 2, 1980);
        Date dateOfAppointment = new Date(1, 6, 2010);

        // TODO 15: Declare Teacher object with initial salary 0
        // Create Teacher object
        Teacher teacher = new Teacher("Madhavan", dobOfTeacher, dateOfAppointment, "MTech", "Electronics", 0);

        // TODO 16: Declare Student object
        // Create Student object
        Student student = new Student("Belinda", dobOfStudent, teacher, "Electronics");

        // TODO 17: Call setSalary() and assign 50000
        // Set salary
        teacher.setSalary(50000);

        // TODO 18: Call getDetails() for teacher and student
        // Print details
        teacher.getDetails();
        student.getDetails();
    }
}
