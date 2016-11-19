package mastermind.ai;

import mastermind.game.Config;
import mastermind.game.GameMaster;
import mastermind.game.MiscUtil;
import mastermind.game.State;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    public static final String TYPE = "abstract";
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

    protected abstract State search();

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
