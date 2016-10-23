package mastermind.game;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameMasterTest extends GameMaster {
    @Test
    public void testEvaluate1() {
        State goalState = new State(new int[]{0, 0, 0, 0}, 4);
        State currentState = new State(new int[]{0, 0, 0, 0}, 4);
        Pegs pegs = evaluate(currentState, goalState);
        Pegs expectedPegs = new Pegs(new int[]{4, 0, 0});
        assertEquals(pegs.toString(), expectedPegs.toString());
    }

    @Test
    public void testEvaluate2() {
        State goalState = new State(new int[]{0, 0, 0, 0}, 4);
        State currentState = new State(new int[]{1, 1, 1, 1}, 4);
        Pegs pegs = evaluate(currentState, goalState);
        Pegs expectedPegs = new Pegs(new int[]{0, 0, 4});
        assertEquals(pegs.toString(), expectedPegs.toString());
    }

    @Test
    public void testEvaluate3() {
        State goalState = new State(new int[]{0, 0, 0, 0}, 4);
        State currentState = new State(new int[]{0, 0, 0, 1}, 4);
        Pegs pegs = evaluate(currentState, goalState);
        Pegs expectedPegs = new Pegs(new int[]{3, 0, 1});
        assertEquals(pegs.toString(), expectedPegs.toString());
    }

    @Test
    public void testEvaluate4() {
        State goalState = new State(new int[]{0, 1, 0, 0}, 4);
        State currentState = new State(new int[]{0, 0, 1, 0}, 4);
        Pegs pegs = evaluate(currentState, goalState);
        Pegs expectedPegs = new Pegs(new int[]{2, 2, 0});
        assertEquals(pegs.toString(), expectedPegs.toString());
    }

    @Test
    public void testEvaluate5() {
        State goalState = new State(new int[]{0, 1, 2, 3}, 4);
        State currentState = new State(new int[]{3, 2, 1, 0}, 4);
        Pegs pegs = evaluate(currentState, goalState);
        Pegs expectedPegs = new Pegs(new int[]{0, 4, 0});
        assertEquals(pegs.toString(), expectedPegs.toString());
    }

    @Test
    public void testCheckIfGoalState1() {
        Pegs pegs = new Pegs(new int[]{4, 0, 0});
        boolean isGoalState = checkIfGoalState(pegs, 4);
        assertEquals(isGoalState, true);
    }

    @Test
    public void testCheckIfGoalState2() {
        Pegs pegs = new Pegs(new int[]{3, 1, 0});
        boolean isGoalState = checkIfGoalState(pegs, 4);
        assertEquals(isGoalState, false);
    }

    @Test
    public void testCheckIfGoalState3() {
        Pegs pegs = new Pegs(new int[]{0, 0, 4});
        boolean isGoalState = checkIfGoalState(pegs, 4);
        assertEquals(isGoalState, false);
    }
}