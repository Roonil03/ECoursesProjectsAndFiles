// TODO 4: Design Student class by extending Person
public class Student extends Person {
    // TODO 5: Declare two instance attributes: subject and teacher
    String subject;
    Teacher teacher;

    // TODO 6: Define a constructor for the Student class
    public Student(String name, Date dob, Teacher teacher, String subject) {
        super(name, dob);
        this.teacher = teacher;
        this.subject = subject;
    }
    // TODO 7: Override the getDetails() method
    @Override
    public void getDetails() {
        System.out.println("Student Name: " + name);
        System.out.println("Date of Birth: " + dob.getDate());
        System.out.println("Subject: " + subject);
        System.out.println("Teacher Name: " + teacher.name);
        System.out.println("Teacher Subject: " + teacher.subject);
        System.out.println("Teacher Qualification: " + teacher.qualification);
        System.out.println("Teacher Salary: " + teacher.getSalary());
        System.out.println("----------------------------");
    }
}
