package Telegram.Commands;

import Telegram.ParsedCommand;
import Telegram.TelegramCommand;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class HelpCommand implements TelegramCommand {
    final private Collection<TelegramCommand> commands;
    final private TelegramBot bot;

    public HelpCommand(Collection<TelegramCommand> commands, TelegramBot bot) {
        this.commands = commands;
        this.bot = bot;
    }

    @Override
    public boolean isMatch(String command) {
        return command.equals("/help");
    }

    @Override
    public void execute(ParsedCommand command, User telegramUser, Chat chat) throws IOException {
        final var fullHelp = commands.stream().map(TelegramCommand::help).collect(Collectors.joining("\n\n"));
        final var sendMessage = new SendMessage(chat.id(), fullHelp).parseMode(ParseMode.Markdown);

        bot.execute(sendMessage);
    }

    @Override
    public String help() {
        return "/help - Выводит справку о командах.";
    }
}
