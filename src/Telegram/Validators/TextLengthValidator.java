package Telegram.Validators;

import Telegram.ValidationError;
import Telegram.ValidationErrors.TextLengthError;
import Telegram.Validator;

import java.util.List;

public class TextLengthValidator implements Validator {
    private final String text;
    private final Integer from;
    private final Integer to;

    public TextLengthValidator(String text, Integer from, Integer to) {
        this.text = text;
        this.from = from;
        this.to = to;
    }

    public TextLengthValidator(String text, Integer to) {
        this(text, 1, to);
    }

    @Override
    public List<ValidationError> errors() {
        if (text.length() > to || text.length() < from) {
            return List.of(new TextLengthError(from, to));
        }

        return List.of();
    }
}
