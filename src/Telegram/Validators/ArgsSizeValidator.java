package Telegram.Validators;

import Telegram.ParsedCommand;
import Telegram.ValidationError;
import Telegram.ValidationErrors.ArgsSizeValidationError;
import Telegram.Validator;

import java.util.List;

public class ArgsSizeValidator implements Validator {
    private final Integer size;
    private final ParsedCommand command;

    public ArgsSizeValidator(ParsedCommand command, Integer size) {
        this.size = size;
        this.command = command;
    }

    @Override
    public List<ValidationError> errors() {
        if (command.args().size() != size) {
            return List.of(new ArgsSizeValidationError(command, size));
        }

        return List.of();
    }
}
