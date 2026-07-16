import java.io.Serializable;

public class Tiger extends Animal implements Walk, Serializable {
    private static final long serialVersionUID = 1L;

    private int numberOfStripes;
    private int speed;
    private int soundLevelOfRoar;

    public Tiger() {
        super("Tiger");
    }

    public int getNumberOfStripes() { return numberOfStripes; }
    public void setNumberOfStripes(int numberOfStripes) { this.numberOfStripes = numberOfStripes; }

    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }

    public int getSoundLevelOfRoar() { return soundLevelOfRoar; }
    public void setSoundLevelOfRoar(int soundLevelOfRoar) { this.soundLevelOfRoar = soundLevelOfRoar; }

    // Overloaded helper methods for ZooTest compatibility
    public int getSoundLevel() { return soundLevelOfRoar; }
    public void setSoundLevel(int soundLevel) { this.soundLevelOfRoar = soundLevel; }

    @Override
    public void eatingCompleted() {
        System.out.println("Tiger: I have eaten meat");
    }

    @Override
    public void walking() {
        System.out.println("Tiger: I am walking at the speed of " + speed + " mph");
    }

    @Override
    public String toString() {
        return "Tiger{" +
                "numberOfStripes=" + numberOfStripes +
                ", speed=" + speed +
                ", soundLevelOfRoar=" + soundLevelOfRoar +
                "}";
    }
}