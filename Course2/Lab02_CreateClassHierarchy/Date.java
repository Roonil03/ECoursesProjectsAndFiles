// TODO 2: Declare the Date class with date, month, year
public class Date {
    private int date;
    private int month;
    private int year;

    public Date(int date, int month, int year) {
        this.date = date;
        this.month = month;
        this.year = year;
    }
    // TODO 3: Include the getDate() method
    public String getDate() {
        return date + "/" + month + "/" + year;
    }
}
