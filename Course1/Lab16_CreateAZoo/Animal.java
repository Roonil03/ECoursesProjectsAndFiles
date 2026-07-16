public abstract class Animal implements Eat {
    private String nameOfAnimal;
    private int weight;
    private int height;
    private int age;

    public Animal() {
        this.nameOfAnimal = "Unknown Animal";
    }

    public Animal(String nameOfAnimal) {
        this.nameOfAnimal = nameOfAnimal;
    }

    public String getNameOfAnimal() { return nameOfAnimal; }
    public void setNameOfAnimal(String nameOfAnimal) { this.nameOfAnimal = nameOfAnimal; }

    public int getWeight() { return weight; }
    public void setWeight(int weight) { this.weight = weight; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    @Override
    public void eatingFood() {
        System.out.println("Animal is eating food.");
    }
}