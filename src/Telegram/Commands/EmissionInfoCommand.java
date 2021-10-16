package Telegram.Commands;

import Emission.Emission;
import Strings.Percent;
import Telegram.ParsedCommand;
import Telegram.TelegramCommand;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class EmissionInfoCommand implements TelegramCommand {
    private final Emission emission;
    private final TelegramBot bot;

    public EmissionInfoCommand(Emission emission, TelegramBot bot) {
        this.emission = emission;
        this.bot = bot;
    }

    @Override
    public boolean isMatch(String command) {
        return command.equals("/emissionInfo");
    }

    @Override
    public void execute(ParsedCommand command, User telegramUser, Chat chat) throws IOException {
        final var sendMessage = new SendMessage(chat.id(), "Всего выпущено: %dGC. Шанс выпадения: %s".formatted(emission.total(), new Percent(emission.chance() * 100)));

        bot.execute(sendMessage);
    }

    @Override
    public String help() {
        return "/emissionInfo - Выводит информацию о текущем состоянии эмиссии: сколько койнов выпущено, каков шанс выпадения.";
    }
}
