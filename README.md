# Mastermind AI project
## Development
- Java 1.8 +
- Maven
- IntelliJ IDEA

## Usage
    $ java -classpath mastermind-ai.jar mastermind.game.GameMaster -c <# of colors> -p <# of positions> [-m <method type>] [-a <answer>]
- *# of colors* must be 1 or greater
- *# of positions* must be 1 or greater
- [optional] *method type*: np (NaivePlayer, default), cfp (ColorFirstPlayer)
- [optional] *answer* is delimited with comma (e.g. 0,1,2,3)
    
## How to add a new player (strategy)
1. Create a new class file on **mastermind.ai** (e.g. SmartPlayer)
2. Let the new class inherit **mastermind.ai.Player** (abstract class)  
(You don't need to override methods other than ***search*** method.)

For example, you need to add the following lines to the new class.
```
public class SmartPlayer extends Player {
    public static final String TYPE = "sp";
    
    public SmartPlayer(State goalState) {
        super(goalState);
    }
    
    @Override
    protected State search() {
        ...
        int[] elements = [your guess, each element is between 0 and (# of position - 1)]
        return new State(elements, this.colorSize);
    }
}
```
