package mastermind.game;

import org.apache.commons.cli.*;

import java.util.Random;

public class MiscUtil {
    public static CommandLine setParams(Options options, String[] args) {
        CommandLineParser clp = new DefaultParser();
        CommandLine cl = null;
        try {
            cl = clp.parse(options, args);
        }
        catch (ParseException pe) {
            pe.printStackTrace();
        }
        return cl;
    }

    public static CommandLine setCommandLine(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder(Config.COLOR_SIZE_OPTION)
                .hasArg(true)
                .required(true)
                .desc("number of colors")
                .build());
        options.addOption(Option.builder(Config.POSITION_SIZE_OPTION)
                .hasArg(true)
                .required(true)
                .desc("number of positions")
                .build());
        return setParams(options, args);
    }

    public static State generateGoalState(int colorSize, int positionSize) {
        int[] elements = new int[positionSize];
        Random rand = new Random();
        for (int i = 0;i < positionSize; i++) {
            elements[i] = rand.nextInt(colorSize);
        }
        return new State(elements, colorSize);
    }
}
