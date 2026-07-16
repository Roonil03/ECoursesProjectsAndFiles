// TODO 8: Declare an abstract Employee class as extension of Person
public abstract class Employee extends Person {
    // TODO 9: Include attributes: dateOfAppointment and salary
    protected Date dateOfAppointment;
    protected int salary;

    public Employee(String name, Date dob, Date dateOfAppointment, int salary) {
        super(name, dob);
        this.dateOfAppointment = dateOfAppointment;
        this.salary = salary;
    }
    // TODO 10: Include abstract getter and setter for salary
    public abstract int getSalary();
    public abstract void setSalary(int salary);
}