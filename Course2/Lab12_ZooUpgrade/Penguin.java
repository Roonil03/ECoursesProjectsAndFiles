import java.io.Serializable;

public class Penguin extends Animal implements Walk, Swim, Serializable {
    private static final long serialVersionUID = 1L;

    private boolean isSwimming;
    private int walkSpeed;
    private int swimSpeed;

    public Penguin() {
        super("Penguin");
    }

    public boolean getIsSwimming() { return isSwimming; }
    public void setIsSwimming(boolean isSwimming) { this.isSwimming = isSwimming; }

    // Overloaded helper methods for ZooTest compatibility
    public boolean isSwimming() { return isSwimming; }
    public boolean getSwimming() { return isSwimming; }
    public void setSwimming(boolean isSwimming) { this.isSwimming = isSwimming; }

    public int getWalkSpeed() { return walkSpeed; }
    public void setWalkSpeed(int walkSpeed) { this.walkSpeed = walkSpeed; }

    public int getSwimSpeed() { return swimSpeed; }
    public void setSwimSpeed(int swimSpeed) { this.swimSpeed = swimSpeed; }

    @Override
    public void eatingFood() {
        System.out.println("Penguin: I am eating delicious fish");
    }

    @Override
    public void eatingCompleted() {
        System.out.println("I have eaten fish");
    }

    @Override
    public void walking() {
        System.out.println("Penguin: I am walking at the speed of " + walkSpeed + " mph");
    }

    @Override
    public void swimming() {
        System.out.println("Penguin: I am swimming at the speed of " + swimSpeed + " nautical miles per hour");
    }

    @Override
    public String toString() {
        return "Penguin{" +
                "isSwimming=" + isSwimming +
                ", walkSpeed=" + walkSpeed +
                ", swimSpeed=" + swimSpeed +
                "}";
    }
}