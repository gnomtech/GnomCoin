package Telegram.Commands;

import Telegram.ParsedCommand;
import Telegram.TelegramCommand;
import Telegram.Validators.ArgsSizeValidator;
import Telegram.Validators.ComposedValidator;
import Telegram.Validators.TextLengthValidator;
import Users.Users;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetChatTitle;

import java.io.IOException;

public class ChangeGroupTitleCommand implements TelegramCommand {
    private final TelegramBot bot;
    private final Users users;
    private final Integer price;

    public ChangeGroupTitleCommand(Integer price, TelegramBot bot, Users users) {
        this.price = price;
        this.bot = bot;
        this.users = users;
    }

    @Override
    public boolean isMatch(String command) {
        return command.equals("/changeGroupTitle");
    }

    @Override
    public void execute(ParsedCommand command, User telegramUser, Chat chat) throws IOException {
        final var argsValidator = new ArgsSizeValidator(command, 1);
        if (argsValidator.errors().size() != 0) {
            final var sendErrorMessage = new SendMessage(chat.id(), argsValidator.errors().get(0).text()).parseMode(ParseMode.Markdown);
            bot.execute(sendErrorMessage);

            return;
        }

        final var textLengthValidator = new TextLengthValidator(command.args().get(0), 100);
        if (textLengthValidator.errors().size() != 0) {
            final var sendErrorMessage = new SendMessage(chat.id(), textLengthValidator.errors().get(0).text()).parseMode(ParseMode.Markdown);
            bot.execute(sendErrorMessage);

            return;
        }

        final var user = users.userById(telegramUser.id().toString());
        if (user.isEmpty()) {
            final var sendErrorMessage = new SendMessage(chat.id(), "Пользователь с ID %d не обнаружен.".formatted(telegramUser.id()));
            bot.execute(sendErrorMessage);

            return;
        }

        if (user.get().sum() < price) {
            final var sendErrorMessage = new SendMessage(chat.id(), "Недостаточно средств. Операция стоит %dGC.".formatted(price));
            bot.execute(sendErrorMessage);

            return;
        }

        final var changeTitle = new SetChatTitle(chat.id(), command.args().get(0));
        final var result = bot.execute(changeTitle);

        if (result.isOk()) {
            users.changeUserSum(user.get(), -1 * price);
        } else {
            System.out.println(result.description());
        }

        final var finalMessage = result.isOk()
                ? new SendMessage(chat.id(), "Название успешно заменено")
                : new SendMessage(chat.id(), "Не удалось заменить название. Койны не сняты.");

        bot.execute(finalMessage);
    }

    @Override
    public String help() {
        return "/changeGroupTitle `NET_TITLE` - изменить название группы на `NEW_TITLE`. Стоимость операции: %dGC.".formatted(price);
    }
}
