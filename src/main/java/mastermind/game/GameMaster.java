package mastermind.game;

import mastermind.ai.ColorFirstPlayer;
import mastermind.ai.NaivePlayer;
import mastermind.ai.Player;
import org.apache.commons.cli.CommandLine;

public class GameMaster {
    public static int[] evaluate(State state, State goalState) {
        int[] rwe = new int[]{0, 0, 0};
        for (int i = 0; i < state.elements.length; i++) {
            if (state.elements[i] == goalState.elements[i]) {
                // red
                rwe[0]++;
            }
        }

        for (int i = 0; i < state.colorSizes.length; i++) {
            rwe[1] += Math.min(state.colorSizes[i], goalState.colorSizes[i]);
        }
        // white
        rwe[1] -= rwe[0];
        // empty
        rwe[2] = goalState.elements.length - rwe[1] - rwe[0];
        return rwe;
    }

    public static boolean checkIfGoalState(State state, int positionSize) {
        return state.pegs[0] == positionSize && state.pegs[1] == 0 && state.pegs[2] == 0;
    }

    public static void start(int colorSize, int positionSize, String methodType, int[] answerElements) {
        State goalState = (answerElements == null) ? MiscUtil.generateGoalState(colorSize, positionSize)
                : new State(answerElements, colorSize);
        System.out.println("Answer: " + goalState.toShortString(null));
        Player player;
        if (methodType.equals(ColorFirstPlayer.TYPE)) {
            player = new ColorFirstPlayer(goalState);
        } else {
            player = new NaivePlayer(goalState);
        }
        player.play();
    }

    public static void main(String[] args) {
        CommandLine cl = MiscUtil.setCommandLine(args);
        int colorSize = Integer.parseInt(cl.getOptionValue(Config.COLOR_SIZE_OPTION));
        int positionSize = Integer.parseInt(cl.getOptionValue(Config.POSITION_SIZE_OPTION));
        String methodType = cl.hasOption(Config.METHOD_OPTION) ? cl.getOptionValue(Config.METHOD_OPTION) : null;
        int[] answerElements = cl.hasOption(Config.ANSWER_OPTION) ?
                MiscUtil.convertToArray(cl.getOptionValue(Config.ANSWER_OPTION)) : null;
        if (colorSize < 1 || positionSize < 1) {
            System.out.println("Both numbers of colors and positions must be 1 or greater.");
            return;
        }

        if (answerElements != null && !MiscUtil.checkIfValidAnswer(answerElements, colorSize, positionSize)) {
            System.out.println("Given answer does not meet the requirements of both numbers of colors and positions.");
            return;
        }

        System.out.println("##############################");
        System.out.println("  Mastermind Game Simulation");
        System.out.println("##############################\n");
        start(colorSize, positionSize, methodType, answerElements);
    }
}
