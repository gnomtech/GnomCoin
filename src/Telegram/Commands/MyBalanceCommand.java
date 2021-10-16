package Telegram.Commands;

import Telegram.ParsedCommand;
import Telegram.TelegramCommand;
import Users.Users;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.FileNotFoundException;
import java.util.List;

public class MyBalanceCommand implements TelegramCommand {
    private final Users users;
    private final TelegramBot bot;

    public MyBalanceCommand(Users users, TelegramBot bot) {
        this.users = users;
        this.bot = bot;
    }

    @Override
    public boolean isMatch(String command) {
        return command.equals("/balance");
    }

    @Override
    public void execute(ParsedCommand command, User telegramUser, Chat chat) throws FileNotFoundException {
        final var user = users.userById(telegramUser.id().toString());
        final var balance = user.isPresent() ? user.get().sum() : 0;
        final var sendMessage = new SendMessage(chat.id(), "Баланс @%s (%d): %dGC".formatted(telegramUser.username(), telegramUser.id(), balance));

        bot.execute(sendMessage);
    }

    @Override
    public String help() {
        return "/balance - Выводит информацию о вашем текущем балансе.";
    }
}
