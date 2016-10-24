package mastermind.ai;

import mastermind.game.GameMaster;
import mastermind.game.MiscUtil;
import mastermind.game.State;

import java.util.ArrayList;
import java.util.List;

public class Player {
    protected final State goalState;
    protected int colorSize, positionSize, time, preStateIdx;
    protected List<State> stateList;
    protected int[][] stateSpaceMat;

    public Player(State goalState) {
        this.goalState = goalState;
        this.colorSize = goalState.colorSizes.length;
        this.positionSize = goalState.elements.length;
        this.time = 0;
        this.preStateIdx = 0;
        this.stateList = new ArrayList<>();
        this.stateSpaceMat = MiscUtil.initStateSpaceMatrix(this.colorSize, this.positionSize);
    }

    protected State searchNaively() {
        if (this.stateList.size() == 0) {
            int[] elements = new int[this.positionSize];
            for (int i = 0; i < elements.length; i++) {
                elements[i] = 0;
            }
            return new State(elements, this.colorSize);
        }

        this.preStateIdx = this.stateList.size() - 1;
        State preState =  this.stateList.get(this.preStateIdx);
        int[] elements = new int[preState.elements.length];
        boolean carryFlag = true;
        for (int i = elements.length - 1; i >= 0; i--) {
            int n = (carryFlag) ? preState.elements[i] + 1 : preState.elements[i];
            carryFlag = n >= this.colorSize;
            if (carryFlag) {
                n = 0;
            }
            elements[i] = n;
        }
        return new State(elements, this.positionSize);
    }

    protected State search() {
        return searchNaively();
    }

    public void play() {
        while (true) {
            State currentState = search();
            currentState.setPegs(GameMaster.evaluate(currentState, this.goalState));
            this.time++;
            this.stateList.add(currentState);
            System.out.println("Round " + String.valueOf(this.time) + "\t" + currentState.toString());
            if (GameMaster.checkIfGoalState(currentState, this.positionSize)) {
                break;
            }
        }
    }
}
