package mastermind.game;

public class Pegs {
    public final int[] rwe;

    public Pegs(int[] rwe) {
        this.rwe = new int[3];
        for (int i = 0; i < 3; i++) {
            this.rwe[i] = rwe[i];
        }
    }

    public String toString() {
        return "Red: " + String.valueOf(this.rwe[0]) + ", White: " + String.valueOf(this.rwe[1]) + ", Empty: " + this.rwe[2];
    }
}
