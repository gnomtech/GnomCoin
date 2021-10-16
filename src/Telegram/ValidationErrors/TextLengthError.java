package Telegram.ValidationErrors;

import Telegram.ValidationError;

public class TextLengthError implements ValidationError {
    private final Integer from;
    private final Integer to;

    public TextLengthError(Integer from, Integer to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String text() {
        return "Длина текста должна быть от %d до %d символов.".formatted(from, to);
    }
}
