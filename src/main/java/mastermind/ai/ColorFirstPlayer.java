package mastermind.ai;

import mastermind.game.Config;
import mastermind.game.MiscUtil;
import mastermind.game.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ColorFirstPlayer extends Player{
    public static final String TYPE = "cfp";
    private static final int NORMAL_MODE = 0;
    private static final int REPLACED_MODE_A = 1;
    private static final int REPLACED_MODE_B = 2;
    private static final int REPLACED_MODE_C = 3;
    private static final int BACKTRACK_MODE = 4;
    private static final int RIGHT_AFTER_REPLACED_MODE = 5;
    private int colorIndex, shuffleFreq, mode, orgValue;
    private int[] candidates, shuffledIdxs;

    public ColorFirstPlayer(State goalState) {
        super(goalState);
        this.colorIndex = 0;
        this.shuffleFreq = 0;
        this.mode = NORMAL_MODE;
        this.orgValue = -1;
        this.candidates = new int[1];
        this.shuffledIdxs = new int[]{-1, -1};
    }

    private State replaceColor(int[] elements, int copyColor, int orgPos) {
        this.orgValue = elements[orgPos];
        elements[orgPos] = copyColor;
        return new State(elements, this.colorSize);
    }

    private State backtrack(int back) {
        this.orgValue = -1;
        this.mode = BACKTRACK_MODE;
        State currentState = this.stateList.get(this.preStateIdx - back);
        for (int i = 0 ; i < back ; i++) {
            this.stateList.add(currentState);
        }

        this.preStateIdx = this.stateList.size() - 1;
        return shuffle(currentState);
    }

    private State backwardShuffle(State preState, int whichPosX, int whichColorX, int whichPosY) {
        int[] elements = MiscUtil.deepCopyArray(preState.elements);
        int tmp = elements[whichPosY];
        elements[whichPosX] = whichColorX;
        elements[whichPosY] = this.orgValue;
        for (int i = 0 ; i < elements.length ; i++) {
            if (i != whichPosX && i != whichPosY && elements[i] == this.orgValue && this.solvedElements[i] == UNSOLVED_VALUE
                    && this.stateMatrix[tmp][i] != Config.FIXED_NON_ANSWER_VALUE) {
                elements[i] = tmp;
                break;
            }
        }

        this.mode = BACKTRACK_MODE;
        this.orgValue = -1;
        return new State(elements, this.colorSize);
    }

    private State forwardShuffle(State preState, int whichPosX, int whichPosY, int whichColorY) {
        int[] elements = MiscUtil.deepCopyArray(preState.elements);
        for (int i = 0 ; i < elements.length ; i++) {
            if (i != whichPosX && i != whichPosY && elements[i] == whichColorY && this.solvedElements[i] == UNSOLVED_VALUE
                    && this.stateMatrix[this.orgValue][i] != Config.FIXED_NON_ANSWER_VALUE) {
                elements[i] = this.orgValue;
                break;
            }
        }

        this.mode = BACKTRACK_MODE;
        this.orgValue = -1;
        return new State(elements, this.colorSize);
    }

    private int buildColorListMap(int[] elements, HashMap<Integer, List<Integer>> colorListMap) {
        List<Integer> positionList = new ArrayList<>();
        for (int i = 0 ; i < elements.length ; i++) {
            if (this.stateMatrix[elements[i]][i] != Config.FIXED_ANSWER_VALUE) {
                positionList.add(i);
            }
        }

        // most constrained position key
        int minOption = Integer.MAX_VALUE;
        int mcPositionKey = -1;
        for (int position : positionList) {
            List<Integer> colorList = new ArrayList<>();
            for (int i = 0 ; i < this.stateMatrix.length ; i++) {
                int value = this.stateMatrix[i][position];
                if (value != Config.FIXED_NON_ANSWER_VALUE && value != Config.FIXED_ANSWER_VALUE) {
                    colorList.add(i);
                }
            }

            colorListMap.put(position, colorList);
            if (colorList.size() < minOption && colorList.size() > 0) {
                minOption = colorList.size();
                mcPositionKey = position;
            }
        }
        return mcPositionKey;
    }

    private State swapBalls(int[] elements) {
        // Key: position, Value: candidate color list
        HashMap<Integer, List<Integer>> colorListMap = new HashMap<>();
        // most constrained position key
        int mcPositionKey = buildColorListMap(elements, colorListMap);
        int shuffleIdxX = mcPositionKey;
        if (shuffleIdxX == -1) {
            colorListMap.clear();
            shuffleIdxX = buildColorListMap(elements, colorListMap);
        }

        int colorX = elements[shuffleIdxX];
        int colorY = -1;
        List<Integer> colorList = colorListMap.get(shuffleIdxX);
        for (int color : colorList) {
            if (color != colorX) {
                colorY = color;
                break;
            }
        }

        int shuffleIdxY = -1;
        for (int positionKey : colorListMap.keySet()) {
            if (positionKey != mcPositionKey && elements[positionKey] == colorY) {
                shuffleIdxY = positionKey;
                break;
            }
        }

        elements[shuffleIdxX] = colorY;
        elements[shuffleIdxY] = colorX;
        this.orgValue = -1;
        if (this.mode == REPLACED_MODE_A || this.mode == REPLACED_MODE_B) {
            this.mode = RIGHT_AFTER_REPLACED_MODE;
        } else if (this.mode == REPLACED_MODE_C || this.mode == RIGHT_AFTER_REPLACED_MODE) {
            this.mode = NORMAL_MODE;
        }

        this.shuffledIdxs = new int[]{shuffleIdxX, shuffleIdxY};
        return new State(elements, this.colorSize);
    }

    private State shuffle(State preState) {
        this.shuffleFreq++;
        if (this.shuffleFreq == 1) {
            for (int i = 0 ; i < this.solutionColorSizes.length ; i++) {
                if (this.solutionColorSizes[i] == 0) {
                    invalidateDomColor(i);
                }
            }
            int[] elements = MiscUtil.deepCopyArray(preState.elements);
            return swapBalls(elements);
        }

        // understand pegs and reduce domains
        int preRedPegSize = preState.pegs[0];
        int whichPosX = (this.shuffledIdxs != null) ? this.shuffledIdxs[0] : -1;
        int whichPosY = (this.shuffledIdxs != null) ? this.shuffledIdxs[1] : -1;
        int whichColorX = (this.shuffledIdxs != null) ? preState.elements[whichPosX] : -1;
        int whichColorY = (this.shuffledIdxs != null) ? preState.elements[whichPosY] : -1;
        if (this.mode == REPLACED_MODE_A) {
            int prePreRedPegSize = this.stateList.get(this.preStateIdx - 1).pegs[0];
            int redPegDiff = preRedPegSize - prePreRedPegSize;
            if (redPegDiff == 0) {
                updateStateMatrix(whichColorY, whichPosY, Config.FIXED_ANSWER_VALUE);
                updateStateMatrix(whichColorY, whichPosX, Config.FIXED_NON_ANSWER_VALUE);
                updateStateMatrix(this.orgValue, whichPosX, Config.FIXED_NON_ANSWER_VALUE);
            } else {
                updateStateMatrix(this.orgValue, whichPosX, Config.FIXED_ANSWER_VALUE);
                updateStateMatrix(this.orgValue, whichPosY, Config.FIXED_NON_ANSWER_VALUE);
                updateStateMatrix(whichColorY, whichPosY, Config.FIXED_NON_ANSWER_VALUE);
            }
            return backtrack(1);
        } else if(this.mode == REPLACED_MODE_B) {
            int prePreRedPegSize = this.stateList.get(this.preStateIdx - 1).pegs[0];
            int redPegDiff = preRedPegSize - prePreRedPegSize;
            if (redPegDiff == 0) {
                updateStateMatrix(this.orgValue, whichPosY, Config.FIXED_ANSWER_VALUE);
                updateStateMatrix(this.orgValue, whichPosX, Config.FIXED_NON_ANSWER_VALUE);
                updateStateMatrix(whichColorY, whichPosX, Config.FIXED_NON_ANSWER_VALUE);
            } else {
                updateStateMatrix(whichColorY, whichPosX, Config.FIXED_ANSWER_VALUE);
                updateStateMatrix(whichColorY, whichPosY, Config.FIXED_NON_ANSWER_VALUE);
                updateStateMatrix(this.orgValue, whichPosY, Config.FIXED_NON_ANSWER_VALUE);
            }
            return backtrack(2);
        } else if(this.mode == REPLACED_MODE_C) {
            int prePreRedPegSize = this.stateList.get(this.preStateIdx - 1).pegs[0];
            int redPegDiff = preRedPegSize - prePreRedPegSize;
            if (redPegDiff == 0) {
                updateStateMatrix(this.orgValue, whichPosX, Config.FIXED_NON_ANSWER_VALUE);
                updateStateMatrix(whichColorY, whichPosX, Config.FIXED_NON_ANSWER_VALUE);
                return backtrack(2);
            } else if (redPegDiff == -1) {
                updateStateMatrix(this.orgValue, whichPosX, Config.FIXED_ANSWER_VALUE);
                updateStateMatrix(this.orgValue, whichPosY, Config.FIXED_ANSWER_VALUE);
                return backwardShuffle(preState, whichPosX, this.orgValue, whichPosY);
            } else if (redPegDiff == 1){
                updateStateMatrix(whichColorY, whichPosX, Config.FIXED_ANSWER_VALUE);
                updateStateMatrix(whichColorY, whichPosY, Config.FIXED_ANSWER_VALUE);
                return forwardShuffle(preState, whichPosX, whichPosY, whichColorY);
            }
            return backtrack(2);
        } else if (this.mode == BACKTRACK_MODE) {
            this.mode = NORMAL_MODE;
        } else {
            int prePreRedPegSize = this.stateList.get(this.preStateIdx - 1).pegs[0];
            int redPegDiff = preRedPegSize - prePreRedPegSize;
            if (redPegDiff == -2) {
                updateStateMatrix(whichColorX, whichPosY, Config.FIXED_ANSWER_VALUE);
                updateStateMatrix(whichColorY, whichPosX, Config.FIXED_ANSWER_VALUE);
                return backtrack(1);
            } else if (redPegDiff == 2) {
                updateStateMatrix(whichColorX, whichPosX, Config.FIXED_ANSWER_VALUE);
                updateStateMatrix(whichColorY, whichPosY, Config.FIXED_ANSWER_VALUE);
            } else if (redPegDiff == 0) {
                this.candidates = MiscUtil.deepCopyArray(preState.elements);
                this.orgValue = whichColorX;
                this.mode = REPLACED_MODE_C;
                int[] elements = MiscUtil.deepCopyArray(preState.elements);
                return replaceColor(elements, whichColorY, whichPosX);
            } else {
                int[] elements = MiscUtil.deepCopyArray(preState.elements);
                if (redPegDiff == -1) {
                    this.mode = REPLACED_MODE_B;
                } else if (redPegDiff == 1) {
                    this.mode = REPLACED_MODE_A;
                }
                return replaceColor(elements, whichColorY, whichPosX);
            }
        }

        int[] elements = MiscUtil.deepCopyArray(preState.elements);
        return swapBalls(elements);
    }

    @Override
    protected State search() {
        if (this.stateList.size() == 0) {
            int[] elements = MiscUtil.initArray(this.positionSize, this.colorIndex);
            this.colorIndex++;
            return new State(elements, this.colorSize);
        }

        State preState =  this.stateList.get(this.preStateIdx);
        if (this.colorIndex <= this.solutionColorSizes.length) {
            int pegSize = preState.pegs[0] + preState.pegs[1];
            if (this.preStateIdx > 0) {
                State prePreState = this.stateList.get(this.preStateIdx - 1);
                pegSize -= (prePreState.pegs[0] + prePreState.pegs[1]);
            }

            this.solutionColorSizes[this.colorIndex - 1] = pegSize;
            int totalPegSize = 0;
            for (int i = 0 ; i < this.colorIndex ; i++) {
                totalPegSize += this.solutionColorSizes[i];
            }

            // search color in the solution until all the colors are given OR it reaches the edge
            if (totalPegSize == this.positionSize) {
                this.colorIndex = Integer.MAX_VALUE;
                this.candidates = new int[this.positionSize];
                int index = 0;
                for (int i = 0 ; i < this.solutionColorSizes.length ; i++) {
                    for (int j = 0 ; j < this.solutionColorSizes[i] ; j++) {
                        this.candidates[index] = i;
                        index++;
                    }
                }
            }

            if (this.colorIndex < this.solutionColorSizes.length) {
                int[] elements = MiscUtil.initArray(this.positionSize, this.colorIndex);
                int index = 0;
                for (int i = 0 ; i < this.solutionColorSizes.length ; i++) {
                    for (int j = 0 ; j < this.solutionColorSizes[i] ; j++) {
                        elements[index] = i;
                        index++;
                    }
                }

                this.colorIndex++;
                return new State(elements, this.colorSize);
            }
        }
        // shuffle the candidates based on preState after finding all the colors in the solution and their sizes
        return shuffle(preState);
    }
}
