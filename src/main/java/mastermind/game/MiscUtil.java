package mastermind.game;

import org.apache.commons.cli.*;

import java.util.Random;

public class MiscUtil {
    public static CommandLine setParams(Options options, String[] args) {
        CommandLineParser clp = new DefaultParser();
        CommandLine cl = null;
        try {
            cl = clp.parse(options, args);
        } catch (ParseException pe) {
            HelpFormatter help = new HelpFormatter();
            help.printHelp("Mastermind AI", options, true);
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
        options.addOption(Option.builder(Config.METHOD_OPTION)
                .hasArg(true)
                .required(false)
                .desc("[optional] method")
                .build());
        options.addOption(Option.builder(Config.ANSWER_OPTION)
                .hasArg(true)
                .required(false)
                .desc("[optional] answer")
                .build());
        return setParams(options, args);
    }

    public static int[] convertToArray(String answer) {
        String[] elements = answer.split(",");
        int[] answerElements = new int[elements.length];
        for (int i = 0 ; i < elements.length ; i++) {
            answerElements[i] = Integer.parseInt(elements[i]);
        }
        return answerElements;
    }

    public static boolean checkIfValidAnswer(int[] answerElements, int colorSize, int positionSize) {
        if (answerElements.length != positionSize) {
            return false;
        }

        for (int element : answerElements) {
            if (element < 0 || element >= colorSize) {
                return false;
            }
        }
        return true;
    }

    public static State generateGoalState(int colorSize, int positionSize) {
        int[] elements = new int[positionSize];
        Random rand = new Random();
        for (int i = 0; i < positionSize; i++) {
            elements[i] = rand.nextInt(colorSize);
        }
        return new State(elements, colorSize);
    }

    public static int[] initArray(int arraySize, int initValue) {
        int[] array = new int[arraySize];
        for (int i = 0 ; i < array.length ; i++) {
            array[i] = initValue;
        }
        return array;
    }

    public static int[] initArray(int arraySize) {
        return initArray(arraySize, 0);
    }

    public static int[][] initStateSpaceMatrix(int colorSize, int positionSize) {
        int[][] stateSpaceMat = new int[colorSize][positionSize];
        for (int i = 0 ; i < stateSpaceMat.length ; i++) {
            for (int j = 0 ; j < stateSpaceMat[0].length ; j++) {
                stateSpaceMat[i][j] = Config.UNEXPLORED_VALUE;
            }
        }
        return stateSpaceMat;
    }

    public static int[] deepCopyArray(int[] orgArray) {
        int[] copyArray = new int[orgArray.length];
        for (int i = 0 ; i < orgArray.length ; i++) {
            copyArray[i] = orgArray[i];
        }
        return copyArray;
    }
}
