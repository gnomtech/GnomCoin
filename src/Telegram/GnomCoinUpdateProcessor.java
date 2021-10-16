package Telegram;

import Coin.Coin;
import Giveaway.SteamKeyTelegramGiveaway;
import Giveaway.Giveaway;
import Users.Users;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GnomCoinUpdateProcessor implements UpdateProcessor {
    private final List<TelegramCommand> commands;
    private final Long groupId;
    private final String adminId;
    private final Users users;
    private final Coin coin;
    private final TelegramBot bot;

    private List<Giveaway> giveaways = new ArrayList<>();
    public GnomCoinUpdateProcessor(
            List<TelegramCommand> commands,
            Long groupId,
            String adminId,
            Users users,
            Coin coin,
            TelegramBot bot) {
        this.commands = commands;
        this.groupId = groupId;
        this.adminId = adminId;
        this.users = users;
        this.coin = coin;
        this.bot = bot;
    }

    @Override
    public void process(Update update) throws IOException {
        final var message = update.message();

        if (message == null) {
            return;
        }

        final var chat = message.chat();
        final var telegramUser = message.from();
        final var parsedCommand = new TelegramParsedCommand(message.text() == null ? "" : message.text());

        var itWasCommand = false;
        for (var command : commands) {
            if (command.isMatch(parsedCommand.command())) {
                command.execute(parsedCommand, telegramUser, chat);
                itWasCommand = true;
            }
        }

        if (telegramUser.id().toString().equals(adminId) && parsedCommand.command().equals("/giveaway")) {
            final var giv = new SteamKeyTelegramGiveaway(parsedCommand.args().get(0), bot, groupId, users);
            giveaways.add(giv.start());
        }

        // System.out.println(chat.id());
        if (!chat.id().equals(groupId)) {
            return;
        }

        users.createUser(telegramUser.id().toString(), 0);

        final var jsonUser = users.userById(telegramUser.id().toString());

        if (jsonUser.isEmpty()) {
            return;
        }

        giveaways = giveaways.stream().map(g -> {
            try {
                return g.userMessage(jsonUser.get(), message.text());
            } catch (IOException e) {
                e.printStackTrace();
                return g;
            }
        }).collect(Collectors.toList());

        final var isAudioMessageWithoutReply = message.voice() != null;
        if (isAudioMessageWithoutReply && jsonUser.get().sum() < 2) {
            final var sendMessage = new SendMessage(chat.id(), "У вас недостаточно GC для голосового сообщения. Стоимость голосового сообщения 2GC.");
            final var deleteMessage = new DeleteMessage(chat.id(), message.messageId());

            bot.execute(deleteMessage);
            bot.execute(sendMessage);

        } else if (isAudioMessageWithoutReply) {
            final var sendMessage = new SendMessage(chat.id(), "Голосовое сообщение принято. С вас снято 2GC.");
            users.changeUserSum(jsonUser.get(), -1 * 2);

            bot.execute(sendMessage);
        } else if (!itWasCommand) {
            final var result = coin.action(jsonUser.get());
        }


        /*if (result.isEmitCoin()) {
            final var sendMessage = new SendMessage(chat.id(), "Пользователь @%s (%d) получает 1 гном коин!".formatted(telegramUser.username(), telegramUser.id()));
            bot.execute(sendMessage);
        }*/
    }

    @Override
    public void tick() {
        this.giveaways = giveaways.stream()
                .map(g -> g.tick())
                .filter(g -> !g.isFinished())
                .collect(Collectors.toList());
    }
}
