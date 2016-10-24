package mastermind.game;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class GameMasterTest extends GameMaster {
    @Test
    public void testEvaluate1() {
        State goalState = new State(new int[]{0, 0, 0, 0}, 4);
        State currentState = new State(new int[]{0, 0, 0, 0}, 4);
        goalState.setPegs(new int[]{4, 0, 0});
        currentState.setPegs(GameMaster.evaluate(currentState, goalState));
        assertEquals(goalState.toString(), currentState.toString());
    }

    @Test
    public void testEvaluate2() {
        State goalState = new State(new int[]{0, 0, 0, 0}, 4);
        State currentState = new State(new int[]{1, 1, 1, 1}, 4);
        goalState.setPegs(new int[]{0, 0, 4});
        currentState.setPegs(GameMaster.evaluate(currentState, goalState));
        assertEquals(Arrays.toString(goalState.pegs), Arrays.toString(currentState.pegs));
    }

    @Test
    public void testEvaluate3() {
        State goalState = new State(new int[]{0, 0, 0, 0}, 4);
        State currentState = new State(new int[]{0, 0, 0, 1}, 4);
        goalState.setPegs(new int[]{3, 0, 1});
        currentState.setPegs(GameMaster.evaluate(currentState, goalState));
        assertEquals(Arrays.toString(goalState.pegs), Arrays.toString(currentState.pegs));
    }

    @Test
    public void testEvaluate4() {
        State goalState = new State(new int[]{0, 1, 0, 0}, 4);
        State currentState = new State(new int[]{0, 0, 1, 0}, 4);
        goalState.setPegs(new int[]{2, 2, 0});
        currentState.setPegs(GameMaster.evaluate(currentState, goalState));
        assertEquals(Arrays.toString(goalState.pegs), Arrays.toString(currentState.pegs));
    }

    @Test
    public void testEvaluate5() {
        State goalState = new State(new int[]{0, 1, 2, 3}, 4);
        State currentState = new State(new int[]{3, 2, 1, 0}, 4);
        goalState.setPegs(new int[]{0, 4, 0});
        currentState.setPegs(GameMaster.evaluate(currentState, goalState));
        assertEquals(Arrays.toString(goalState.pegs), Arrays.toString(currentState.pegs));
    }

    @Test
    public void testCheckIfGoalState1() {
        State currentState = new State(new int[]{0, 0, 0, 0}, 4);
        currentState.setPegs(new int[]{4, 0, 0});
        boolean isGoalState = checkIfGoalState(currentState, 4);
        assertEquals(isGoalState, true);
    }

    @Test
    public void testCheckIfGoalState2() {
        State currentState = new State(new int[]{0, 0, 0, 0}, 4);
        currentState.setPegs(new int[]{3, 1, 0});
        boolean isGoalState = checkIfGoalState(currentState, 4);
        assertEquals(isGoalState, false);
    }

    @Test
    public void testCheckIfGoalState3() {
        State currentState = new State(new int[]{0, 0, 0, 0}, 4);
        currentState.setPegs(new int[]{0, 0, 4});
        boolean isGoalState = checkIfGoalState(currentState, 4);
        assertEquals(isGoalState, false);
    }
}