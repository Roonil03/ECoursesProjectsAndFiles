// TODO 11: Create Teacher class with attributes and constructor
public class Teacher extends Employee {
    String subject;
    String qualification;

    public Teacher(String name, Date dob, Date dateOfAppointment, String qualification, String subject, int salary) {
        super(name, dob, dateOfAppointment, salary);
        this.qualification = qualification;
        this.subject = subject;
    }

    // TODO 13: Implement getSalary() and setSalary() methods
    @Override
    public int getSalary() {
        return salary;
    }

    @Override
    public void setSalary(int salary) {
        this.salary = salary;
    }

    // TODO 12: Override the getDetails() method
    @Override
    public void getDetails() {
        System.out.println("Teacher Name: " + name);
        System.out.println("Date of Birth: " + dob.getDate());
        System.out.println("Date of Appointment: " + dateOfAppointment.getDate());
        System.out.println("Subject: " + subject);
        System.out.println("Qualification: " + qualification);
        System.out.println("Salary: " + salary);
        System.out.println("----------------------------");
    }
}
