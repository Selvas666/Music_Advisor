package advisor.userInterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
@AllArgsConstructor
public class Command {
    private final CommandWord commandWord;
    private final String parameter;

    public static Optional<Command> parseCommand(String userInput) {
        String[] splitUserInput = userInput.split("\\s");

        if (splitUserInput.length == 1) {
            return Optional.of(new Command(CommandWord.parseCommand(splitUserInput[0]), ""));
        }
        else {
            String commandWord = splitUserInput[0];
            StringBuilder param = new StringBuilder();
            for (int i = 1; i < splitUserInput.length; i++){
                param.append(splitUserInput[i]);
                param.append(" ");
            }


            return Optional.of(new Command(CommandWord.parseCommand(commandWord), param.substring(0, param.length()-1)));

        }

    }
}
