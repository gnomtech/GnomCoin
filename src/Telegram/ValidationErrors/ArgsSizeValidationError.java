package Telegram.ValidationErrors;

import Telegram.ParsedCommand;
import Telegram.ValidationError;

public class ArgsSizeValidationError implements ValidationError {
    private final ParsedCommand command;
    private final Integer size;

    public ArgsSizeValidationError(ParsedCommand command, Integer size) {
        this.command = command;
        this.size = size;
    }

    @Override
    public String text() {
        return "Некоректное число аргументов в команде %s. Должно быть %d, а не %d.".formatted(command.command(), size, command.args().size());
    }
}
