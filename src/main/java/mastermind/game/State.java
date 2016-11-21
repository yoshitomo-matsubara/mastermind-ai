package mastermind.game;

public class State {
    public final int[] elements;
    public final int[] colorSizes;
    public final int[] pegs;

    public State(int[] elements, int colorSize) {
        this.elements = new int[elements.length];
        this.colorSizes = new int[colorSize];
        this.pegs = new int[3];
        for (int i = 0; i < this.elements.length; i++) {
            this.elements[i] = elements[i];
            this.colorSizes[this.elements[i]]++;
        }
    }

    public void setPegs(int[] pegs) {
        for (int i = 0 ; i < this.pegs.length ; i++) {
            this.pegs[i] = pegs[i];
        }
    }

    public String toShortString(int[][] stateMatrix) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.elements.length; i++) {
            char c = (char) ('a' + this.elements[i]);
            String color = String.valueOf(c);
            if (stateMatrix != null && stateMatrix[this.elements[i]][i] == Config.FIXED_ANSWER_VALUE) {
                color = color.toUpperCase();
            }

            String str = (sb.length() == 0) ? color : "," + color;
            sb.append(str);
        }
        return sb.toString();
    }

    public String toString(int[][] stateMatrix) {
        String stateString = toShortString(stateMatrix);
        StringBuilder sb = new StringBuilder(stateString);
        sb.append("\t" + "Red: " + String.valueOf(this.pegs[0]) + ", White: " + String.valueOf(this.pegs[1]) + ", Empty: " + this.pegs[2]);
        return sb.toString();
    }

    public String toString() {
        return toString(null);
    }
}
