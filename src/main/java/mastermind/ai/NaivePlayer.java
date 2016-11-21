package mastermind.ai;

import mastermind.game.State;

public class NaivePlayer extends Player{
    public static final String TYPE = "np";

    public NaivePlayer(State goalState) {
        super(goalState);
    }

    @Override
    protected State search() {
        if (this.stateList.size() == 0) {
            int[] elements = new int[this.positionSize];
            for (int i = 0; i < elements.length; i++) {
                elements[i] = 0;
            }
            return new State(elements, this.colorSize);
        }

        State preState =  this.stateList.get(this.preStateIdx);
        int[] elements = new int[this.positionSize];
        boolean carryFlag = true;
        for (int i = elements.length - 1; i >= 0; i--) {
            int n = (carryFlag) ? preState.elements[i] + 1 : preState.elements[i];
            carryFlag = n >= this.colorSize;
            if (carryFlag) {
                n = 0;
            }
            elements[i] = n;
        }
        return new State(elements, this.colorSize);
    }
}
