package mastermind.ai;

import mastermind.game.Config;
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

    protected boolean invalidateDomRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= this.stateSpaceMat.length) {
            return false;
        }

        for (int i = 0 ; i < this.stateSpaceMat.length ; i++) {
            this.stateSpaceMat[rowIndex][i] = Config.FIXED_NON_ANSWER_VALUE;
        }
        return true;
    }

    protected boolean invalidateDomColumn(int columnIndex) {
        if (columnIndex < 0 || columnIndex >= this.stateSpaceMat[0].length) {
            return false;
        }

        for (int i = 0 ; i < this.stateSpaceMat[0].length ; i++) {
            this.stateSpaceMat[i][columnIndex] = Config.FIXED_NON_ANSWER_VALUE;
        }
        return true;
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
        return new State(elements, this.colorSize);
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
