package mastermind.game;

public class State {
    public final int[] elements;
    public final int[] colorSizes;

    public State(int[] elements, int colorSize) {
        this.elements = new int[elements.length];
        this.colorSizes = new int[colorSize];
        for (int i = 0; i < elements.length; i++) {
            this.elements[i] = elements[i];
        }
        for (int i = 0; i < this.elements.length; i++) {
            this.colorSizes[this.elements[i]]++;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.elements.length; i++) {
            char c = (char) ('a' + this.elements[i]);
            String str = (sb.length() == 0) ? String.valueOf(c) : "," + String.valueOf(c);
            sb.append(str);
        }
        return sb.toString();
    }
}
