package mastermind.ai;

import mastermind.game.Config;
import mastermind.game.GameMaster;
import mastermind.game.MiscUtil;
import mastermind.game.State;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    public static final String TYPE = "abstract";
    public static final int UNSOLVED_VALUE = -1;
    protected final State goalState;
    protected int colorSize, positionSize, time, preStateIdx;
    protected List<State> stateList;
    protected int[] solutionColorSizes, solvedColorSizes, solvedElements;
    protected int[][] stateMatrix;

    public Player(State goalState) {
        this.goalState = goalState;
        this.colorSize = goalState.colorSizes.length;
        this.positionSize = goalState.elements.length;
        this.time = 0;
        this.preStateIdx = 0;
        this.stateList = new ArrayList<>();
        this.solutionColorSizes = MiscUtil.initArray(this.colorSize, Config.UNEXPLORED_VALUE);
        this.solvedColorSizes = MiscUtil.initArray(this.colorSize);
        this.solvedElements = MiscUtil.initArray(this.positionSize, UNSOLVED_VALUE);
        this.stateMatrix = MiscUtil.initStateSpaceMatrix(this.colorSize, this.positionSize);
    }

    protected boolean invalidateDomColor(int whichColor) {
        if (whichColor < 0 || whichColor >= this.stateMatrix.length) {
            return false;
        }

        for (int i = 0 ; i < this.stateMatrix[0].length ; i++) {
            if (this.stateMatrix[whichColor][i] != Config.FIXED_ANSWER_VALUE) {
                this.stateMatrix[whichColor][i] = Config.FIXED_NON_ANSWER_VALUE;
            }
        }
        return true;
    }

    protected boolean invalidateDomPosition(int whichPosition) {
        if (whichPosition < 0 || whichPosition >= this.stateMatrix[0].length) {
            return false;
        }

        for (int i = 0 ; i < this.stateMatrix.length ; i++) {
            if (this.stateMatrix[i][whichPosition] != Config.FIXED_ANSWER_VALUE) {
                this.stateMatrix[i][whichPosition] = Config.FIXED_NON_ANSWER_VALUE;
            }
        }
        return true;
    }

    protected boolean updateStateMatrix(int whichColor, int whichPos, int whichStatus) {
        if (whichColor < 0 || whichColor >= this.stateMatrix.length || whichPos < 0 || whichPos >= this.stateMatrix[0].length) {
            return false;
        } else if (this.stateMatrix[whichColor][whichPos] == Config.FIXED_ANSWER_VALUE) {
            return false;
        }

        this.stateMatrix[whichColor][whichPos] = whichStatus;
        if(whichStatus == Config.FIXED_ANSWER_VALUE) {
            invalidateDomPosition(whichPos);
            this.solvedColorSizes[whichColor]++;
            this.solvedElements[whichPos] = whichColor;
            if (this.solvedColorSizes[whichColor] == this.solutionColorSizes[whichColor]) {
                invalidateDomColor(whichColor);
            }
        }
        return true;
    }

    protected abstract State search();

    public void play() {
        long startTime = System.nanoTime();
        while (true) {
            State currentState = search();
            currentState.setPegs(GameMaster.evaluate(currentState, this.goalState));
            this.time++;
            this.stateList.add(currentState);
            this.preStateIdx = this.stateList.size() - 1;
            if (GameMaster.checkIfGoalState(currentState, this.positionSize)) {
                long exeTime = System.nanoTime() - startTime;
                System.out.println("GOAL! " + String.valueOf(this.time) + "\t" + currentState.toString().toUpperCase());
                System.out.println();
                System.out.println("Performance Measurement");
                System.out.println("Guess:\t" + String.valueOf(this.time) + "\tTime[ns]:\t" + String.valueOf(exeTime));
                break;
            } else {
                System.out.println("Round " + String.valueOf(this.time) + "\t" + currentState.toString(this.stateMatrix));
            }
        }
    }
}
