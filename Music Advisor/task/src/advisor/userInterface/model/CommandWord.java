package advisor.userInterface.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum CommandWord {
    AUTH ("auth"),
    FEATURED ("featured"),
    NEW ("new"),
    CATEGORIES ("categories"),
    PLAYLISTS ("playlists"),
    PREV ("prev"),
    NEXT ("next"),
    EXIT ("exit");

    private final String input;

    static CommandWord parseCommand (String userInput) {
        return Arrays.stream(CommandWord.values())
                .filter(v -> v.getInput().equalsIgnoreCase(userInput))
                .findAny()
                .orElseThrow();
    }

}
