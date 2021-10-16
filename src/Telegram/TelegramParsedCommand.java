package Telegram;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TelegramParsedCommand implements ParsedCommand {
    private final String commandText;

    public TelegramParsedCommand(String commandText) {
        this.commandText = commandText;
    }

    @Override
    public String command() {
        final var commandParts = commandText.split(" ");

        if (commandParts.length >= 1) {
            return commandParts[0];
        }

        return "";
    }

    @Override
    public List<String> args() {
        final var commandParts = commandText.split(" ");

        if (commandParts.length <= 1) {
            return List.of();
        }

        final var onlyArgsText = Arrays.stream(commandParts).skip(1).collect(Collectors.joining(" "));

        var currentCommand = new StringBuilder();
        var result = new ArrayList<String>();
        var inQuotes = false;

        for (var i = 0; i < onlyArgsText.length(); i++) {
            final var currentChar = onlyArgsText.charAt(i);

            if (currentChar == '"' && inQuotes) {
                currentCommand.append(currentChar);
                result.add(currentCommand.toString());
                currentCommand.delete(0, currentCommand.length());
                inQuotes = false;
            } else if (currentChar == '"') {
                currentCommand.append(currentChar);
                inQuotes = true;
            } else if (currentChar == ' ' && inQuotes) {
                currentCommand.append(currentChar);
            } else if (currentChar == ' ' && currentCommand.length() != 0) {
                result.add(currentCommand.toString());
                currentCommand.delete(0, currentCommand.length());
            } else if (currentChar != ' ') {
                currentCommand.append(currentChar);
            }
        }

        if (currentCommand.length() != 0) {
            result.add(currentCommand.toString());
        }

        return result.stream()
                .map(str -> str.startsWith("\"") && str.endsWith("\"") ? str.substring(1, str.length() - 1) : str)
                .collect(Collectors.toList());
    }
}
