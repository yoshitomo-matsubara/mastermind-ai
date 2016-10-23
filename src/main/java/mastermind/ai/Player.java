package mastermind.ai;

import mastermind.game.GameMaster;
import mastermind.game.Pegs;
import mastermind.game.State;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final State goalState;
    private int colorSize, positionSize, time;

    public Player(State goalState) {
        this.goalState = goalState;
        this.colorSize = goalState.colorSizes.length;
        this.positionSize = goalState.elements.length;
        this.time = 0;
    }

    private State searchNaively(List<State> stateList) {
        if(stateList.size() == 0) {
            int[] elements = new int[this.positionSize];
            for (int i = 0;i < elements.length;i++) {
                elements[i] = 0;
            }
            return new State(elements, this.colorSize);
        }

        State preState = stateList.get(stateList.size() - 1);
        int[] elements = new int[preState.elements.length];
        boolean carryFlag = true;
        for (int i = elements.length - 1;i >= 0;i--) {
            int n = (carryFlag)? preState.elements[i] + 1 : preState.elements[i];
            carryFlag = n >= this.colorSize;
            if (carryFlag) {
                n = 0;
            }
            elements[i] = n;
        }
        return new State(elements, this.positionSize);
    }

    private State search(List<State> stateList, List<Pegs> pegsList) {
        return searchNaively(stateList);
    }

    public void play() {
        List<State> stateList = new ArrayList<>();
        List<Pegs> pegsList = new ArrayList<>();
        Pegs pegs = null;
        while (pegs == null || !GameMaster.checkIfGoalState(pegs, this.positionSize)) {
            State currentState = search(stateList, pegsList);
            pegs = GameMaster.evaluate(currentState, this.goalState);
            this.time++;
            stateList.add(currentState);
            pegsList.add(pegs);
            System.out.println("Round" + String.valueOf(this.time) + "\t" + currentState.toString() + "\t" + pegs.toString());
        }
    }
}
