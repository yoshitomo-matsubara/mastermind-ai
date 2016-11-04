# Mastermind AI project
## Development
- Java 1.8 +
- Maven
- IntelliJ IDEA

## Usage
    $ java -classpath mastermind-ai.jar mastermind.game.GameMaster -c <# of colors> -p <# of positions> [-a <answer>]
- *# of colors* must be 1 or greater
- *# of positions* must be 1 or greater
- [optional] *answer* is delimited with comma (e.g. 0,1,2,3)
    
## How to add a new player (strategy)
1. create a new class file on **mastermind.ai** (e.g. PlayerA)
2. let the new class inherit **mastermind.ai.Player**  
(You don't need to override methods other than ***search*** method.)

For instance,
```
public class PlayerA extends Player {
    public PlayerA(State goalState) {
        super(goalState);
    }
    
    protected State searchA() {
        ...
        State currentState = new State(~~~);
        currentState.setPegs(GameMaster.evaluate(currentState, this.goalState));
        return currentState;
    }

    @Override
    protected State search() {
        return searchA();
    }
}
```
