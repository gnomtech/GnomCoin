package Telegram;

import java.util.List;

public interface Validator {
    List<ValidationError> errors();
}
