package Telegram.Commands;

import Telegram.ParsedCommand;
import Telegram.TelegramCommand;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;

import java.util.List;

public class MyIdCommand implements TelegramCommand {
    private final TelegramBot bot;

    public MyIdCommand(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public boolean isMatch(String command) {
        return command.equals("/myId");
    }

    @Override
    public void execute(ParsedCommand command, User telegramUser, Chat chat) {
        final var sendMessage = new SendMessage(chat.id(), "ID @%s %d".formatted(telegramUser.username(), telegramUser.id()));
        bot.execute(sendMessage);
    }

    @Override
    public String help() {
        return "/myId - Выводит ваш идентификатор в системе. Этот идентификатор используется для проведения различных операций.";
    }
}
