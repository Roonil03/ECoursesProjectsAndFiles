// TODO 1: Declare the abstract Person class

public abstract class Person {
    protected String name;
    protected Date dob;

    public Person(String name, Date dob) {
        this.name = name;
        this.dob = dob;
    }

    public abstract void getDetails();
}
