package org.learnquest;

public class Laptop {
    private int id;
    private String brand;

    public Laptop() {
        super();
    }

    public Laptop(int id, String brand) {
        this.id = id;
        this.brand = brand;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
}
