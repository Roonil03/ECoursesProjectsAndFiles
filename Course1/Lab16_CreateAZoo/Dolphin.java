// TODO 2 & TODO 7: Extend Animal and implement Swim interface
public class Dolphin extends Animal implements Swim {
    private String color;
    private int swimmingSpeed;

    public Dolphin() {
        super("Dolphin");
    }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getSwimmingSpeed() { return swimmingSpeed; }
    public void setSwimmingSpeed(int swimmingSpeed) { this.swimmingSpeed = swimmingSpeed; }

    // TODO 3: Override eatingFood and implement eatingCompleted methods
    @Override
    public void eatingFood() {
        System.out.println("Dolphin: I am eating delicious fish");
    }

    @Override
    public void eatingCompleted() {
        System.out.println("I have eaten fish");
    }

    // TODO 7: Implement swimming method
    @Override
    public void swimming() {
        System.out.println("Dolphin: I am swimming at the speed of " + swimmingSpeed + " nautical miles per hour");
    }
}