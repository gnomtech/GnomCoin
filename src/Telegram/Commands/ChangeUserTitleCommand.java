package Telegram.Commands;

import Telegram.ParsedCommand;
import Telegram.TelegramCommand;
import Telegram.Validators.ArgsSizeValidator;
import Telegram.Validators.TextLengthValidator;
import Users.Users;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.PromoteChatMember;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetChatAdministratorCustomTitle;

import java.io.IOException;

public class ChangeUserTitleCommand implements TelegramCommand {
    private final Integer price;
    private final TelegramBot bot;
    private final Users users;

    public ChangeUserTitleCommand(Integer price, TelegramBot bot, Users users) {
        this.price = price;
        this.bot = bot;
        this.users = users;
    }

    @Override
    public boolean isMatch(String command) {
        return command.equals("/changeUserTitle");
    }

    @Override
    public void execute(ParsedCommand command, User telegramUser, Chat chat) throws IOException {
        final var argsValidator = new ArgsSizeValidator(command, 2);
        if (argsValidator.errors().size() != 0) {
            final var sendErrorMessage = new SendMessage(chat.id(), argsValidator.errors().get(0).text()).parseMode(ParseMode.Markdown);
            bot.execute(sendErrorMessage);

            return;
        }

        final var currentUser = users.userById(telegramUser.id().toString());
        System.out.println(currentUser.get());
        if (currentUser.isEmpty() || currentUser.get().sum() < price) {
            final var sendErrorMessage = new SendMessage(chat.id(), "Недостаточно средств.");
            bot.execute(sendErrorMessage);

            return;
        }

        final var newTitle = command.args().get(1);
        final var textLengthValidator = new TextLengthValidator(newTitle, 16);
        if (textLengthValidator.errors().size() != 0) {
            final var sendErrorMessage = new SendMessage(chat.id(), textLengthValidator.errors().get(0).text()).parseMode(ParseMode.Markdown);
            bot.execute(sendErrorMessage);

            return;
        }

        final var userId = command.args().get(0);
        final var user = users.userById(userId);
        if (user.isEmpty()) {
            final var sendErrorMessage = new SendMessage(chat.id(), "Пользователя с ID `%s` не существует.".formatted(userId));
            bot.execute(sendErrorMessage);

            return;
        }

        final var promoteUser = new PromoteChatMember(chat.id(), Long.parseLong(userId))
                .canPromoteMembers(false)
                .canChangeInfo(false)
                .canDeleteMessages(false)
                .canEditMessages(false)
                .canInviteUsers(false)
                .canManageChat(false)
                .canManageVoiceChats(false)
                .canPinMessages(true)
                .canRestrictMembers(false);

        final var changeTitle = new SetChatAdministratorCustomTitle(chat.id(), Long.parseLong(userId), newTitle);

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
            users.changeUserSum(currentUser.get(), -1 * price);
        }

        final var finalMessage = changeTitleResult.isOk()
                ? new SendMessage(chat.id(), "Плашка успешно заменена!")
                : new SendMessage(chat.id(), "Не удалось заменить плашку. Гном койны не сняты.");

        bot.execute(finalMessage);
    }

    @Override
    public String help() {
        return "/changeUserTitle `USER_ID` `NEW_TITLE` - меняет плашку пользователя с `USER_ID` на `NEW_TITLE`. Стоимость операции: %dGC.".formatted(price);
    }
}
