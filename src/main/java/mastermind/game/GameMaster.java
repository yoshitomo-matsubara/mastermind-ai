package mastermind.game;

import mastermind.ai.Player;
import org.apache.commons.cli.CommandLine;

public class GameMaster {
    public static Pegs evaluate(State state, State goalState) {
        int[] rwe = new int[]{0, 0, 0};
        for (int i = 0;i < state.elements.length;i++) {
            if (state.elements[i] == goalState.elements[i]) {
                // red
                rwe[0]++;
            }
        }
        for (int i = 0;i < state.elements.length;i++) {
            rwe[1] += Math.min(state.colorSizes[i], goalState.colorSizes[i]);
        }
        // white
        rwe[1] -= rwe[0];
        // empty
        rwe[2] = goalState.elements.length - rwe[1] - rwe[0];
        return new Pegs(rwe);
    }

    public static boolean checkIfGoalState(Pegs pegs, int positionSize) {
        return pegs.rwe[0] == positionSize && pegs.rwe[1] == 0 && pegs.rwe[2] == 0;
    }

    public static void start(int colorSize, int positionSize) {
        State goalState = MiscUtil.generateGoalState(colorSize, positionSize);
        Player player = new Player(goalState);
        player.play();
    }

    public static void main(String[] args) {
        CommandLine cl = MiscUtil.setCommandLine(args);
        int colorSize = Integer.parseInt(cl.getOptionValue(Config.COLOR_SIZE_OPTION));
        int positionSize = Integer.parseInt(cl.getOptionValue(Config.POSITION_SIZE_OPTION));
        if (colorSize < 1 || positionSize < 1)
            return;

        System.out.println("##############################");
        System.out.println("  Mastermind Game Simulation");
        System.out.println("##############################\n");
        start(colorSize, positionSize);
    }
}
