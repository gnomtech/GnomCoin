package Telegram;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;

import java.io.IOException;

public interface TelegramCommand {
    boolean isMatch(String command);
    void execute(ParsedCommand command, User telegramUser, Chat chat) throws IOException;
    String help();
}
