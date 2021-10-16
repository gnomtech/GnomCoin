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
import com.pengrad.telegrambot.request.SetChatAdministratorCustomTitle;
import com.pengrad.telegrambot.request.PromoteChatMember;

import java.io.IOException;
import java.util.List;

public class ChangeTitleCommand implements TelegramCommand {
    private final TelegramBot bot;
    private final Users users;

    public ChangeTitleCommand(TelegramBot bot, Users users) {
        this.bot = bot;
        this.users = users;
    }

    @Override
    public boolean isMatch(String command) {
        return command.equals("/changeTitle");
    }

    @Override
    public void execute(ParsedCommand command, User telegramUser, Chat chat) throws IOException {
        final var argsValidator = new ArgsSizeValidator(command, 1);
        if (argsValidator.errors().size() != 0) {
            final var sendErrorMessage = new SendMessage(chat.id(), argsValidator.errors().get(0).text()).parseMode(ParseMode.Markdown);
            bot.execute(sendErrorMessage);

            return;
        }

        final var textLengthValidator = new TextLengthValidator(command.args().get(0), 16);
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

        if (user.get().sum() < 50) {
            final var sendErrorMessage = new SendMessage(chat.id(), "Недостаточно средств. Операция стоит 50GC.");
            bot.execute(sendErrorMessage);

            return;
        }

        final var promoteUser = new PromoteChatMember(chat.id(), telegramUser.id())
                .canPromoteMembers(false)
                .canChangeInfo(false)
                .canDeleteMessages(false)
                .canEditMessages(false)
                .canInviteUsers(false)
                .canManageChat(false)
                .canManageVoiceChats(false)
                .canPinMessages(true)
                .canRestrictMembers(false);

        final var changeTitle = new SetChatAdministratorCustomTitle(chat.id(), telegramUser.id(), command.args().get(0));

        final var result = bot.execute(promoteUser);
        if (!result.isOk()) {
            System.out.println(result.description());
            System.out.println(result);
        }

        final var changeTitleResult = bot.execute(changeTitle);
        if (!changeTitleResult.isOk()) {
            System.out.println(changeTitleResult.description());
            System.out.println(changeTitleResult);
        } else {
            users.changeUserSum(user.get(), -50);
        }

        final var finalMessage = changeTitleResult.isOk()
                ? new SendMessage(chat.id(), "Плашка успешно заменена!")
                : new SendMessage(chat.id(), "Не удалось заменить плашку. Гном койны не сняты.");

        bot.execute(finalMessage);
    }

    @Override
    public String help() {
        return "/changeTitle `TITLE` - изменить плашку. Стоимость операции 50GC.";
    }
}
