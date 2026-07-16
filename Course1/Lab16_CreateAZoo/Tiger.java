// TODO 1 & TODO 5: Extend Animal and implement Walk interface
public class Tiger extends Animal implements Walk {
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

    // TODO 3: Implement eatingCompleted method
    @Override
    public void eatingCompleted() {
        System.out.println("Tiger: I have eaten meat");
    }

    // TODO 5: Implement walking method
    @Override
    public void walking() {
        System.out.println("Tiger: I am walking at the speed of " + speed + " mph");
    }
}