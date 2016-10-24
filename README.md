# Mastermind AI project
## Development
- Java 1.8 +
- Maven
- IntelliJ IDEA

## Usage
    $ java -classpath mastermind-ai.jar mastermind.game.GameMaster -c <# of colors> -p <# of positions>` 
    
## How to add a new player (strategy)
1. create a new class file on **mastermind.ai** (e.g. PlayerA)
2. let the new class inherit **mastermind.ai.Player**  
(You don't need to override methods other than ***search*** method.)

For instance
```
public class PlayerA extends Player {
    public PlayerA(State goalState) {
        super(goalState);
    }
    
    private State searchA(List<State> stateList, List<Pegs> pegsList) {
        ...
        return new State(~~~);
    }

    @Override
    public State search(List<State> stateList, List<Pegs> pegsList) {
        return searchA(stateList, pegsList);
    }
}
```
