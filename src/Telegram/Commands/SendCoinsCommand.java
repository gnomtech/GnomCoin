package Telegram.Commands;

import Bank.Bank;
import Telegram.ParsedCommand;
import Telegram.TelegramCommand;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.IOException;

public class SendCoinsCommand implements TelegramCommand {
    private final Bank bank;
    private final TelegramBot bot;

    public SendCoinsCommand(Bank bank, TelegramBot bot) {
        this.bank = bank;
        this.bot = bot;
    }

    @Override
    public boolean isMatch(String command) {
        return command.equals("/sendCoins");
    }

    @Override
    public void execute(ParsedCommand command, User telegramUser, Chat chat) throws IOException {
        if (command.args().size() != 2) {
            final var sendErrorMessage = new SendMessage(chat.id(), "Некорректное число аргументов! Должно быть `/sendCoins USER_ID SUM`, где USER_ID это ID пользователя, а SUM это сумма.").parseMode(ParseMode.Markdown);

            bot.execute(sendErrorMessage);
            return;
        }

        final var transaction = bank.transaction(telegramUser.id().toString(), command.args().get(0), Integer.valueOf(command.args().get(1)));
        final var sendMessage = transaction.isSuccess()
                ? new SendMessage(chat.id(), "Пользователь %d успешно отправил %sGC пользователю %s!".formatted(telegramUser.id(), command.args().get(1), command.args().get(0)))
                : new SendMessage(chat.id(), transaction.reason().get().text());

        bot.execute(sendMessage);
    }

    @Override
    public String help() {
        return "/sendCoins `ID` `SUM` - используется для передачи койнов другому участнику. `ID` - идентификатор участника в системе. `SUM` - сумма для перевода.";
    }
}
