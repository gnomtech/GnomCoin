package Telegram.Validators;

import Telegram.ValidationError;
import Telegram.Validator;

import java.util.List;

public class ComposedValidator implements Validator {
    public final List<Validator> validators;

    public ComposedValidator(List<Validator> validators) {
        this.validators = validators;
    }

    @Override
    public List<ValidationError> errors() {
        for (var validator : validators) {
            final var errors = validator.errors();

            if (errors.size() != 0) {
                return errors;
            }
        }

        return List.of();
    }
}
