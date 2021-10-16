package Giveaway;

import Users.User;
import Users.Users;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;

import java.io.IOException;
import java.util.*;

enum State {
    NEW,
    IN_PROGRESS,
    FINISHED
}

class GiveawayState {
    public final Integer time;
    public final State state;
    public final Integer messageId;
    public final Set<User> players;

    public GiveawayState() {
        this(100, State.NEW, 0, Set.of());
    }

    public GiveawayState(Integer time, State state, Integer messageId, Set<User> players) {
        this.time = time;
        this.state = state;
        this.messageId = messageId;
        this.players = players;
    }

    public GiveawayState withMessageId(Integer messageId) {
        return new GiveawayState(time, State.IN_PROGRESS, messageId, players);
    }

    public GiveawayState tick() {
        final var state = time - 1 > 0 ? State.IN_PROGRESS : State.FINISHED;

        return new GiveawayState(time - 1, state, messageId, players);
    }

    public GiveawayState withPlayer(User player) {
        final var players = new HashSet<>(this.players);
        players.add(player);

        return new GiveawayState(time, state, messageId, players);
    }
}

public class SteamKeyTelegramGiveaway implements Giveaway {
    private final TelegramBot bot;
    private final Long groupId;
    private final GiveawayState state;
    private final Users users;
    private final String gift;

    public SteamKeyTelegramGiveaway(String gift, TelegramBot bot, Long groupId, Users users) {
        this(gift, bot, groupId, users, new GiveawayState());
    }

    private SteamKeyTelegramGiveaway(String gift, TelegramBot bot, Long groupId, Users users, GiveawayState state) {
        this.gift = gift;
        this.bot = bot;
        this.groupId = groupId;
        this.state = state;
        this.users = users;
    }

    @Override
    public Giveaway start() {
        final var sendMessage = new SendMessage(groupId, "Розыгрыш начался! Стоимость участия 100GC. Для участия отправь /go! Время до окончания розыгрыша " + state.time + " секунд.");
        final var result = bot.execute(sendMessage);
        final var messageId = result.message().messageId();

        return new SteamKeyTelegramGiveaway(gift, bot, groupId, users, state.withMessageId(messageId));
    }

    @Override
    public Giveaway tick() {
        if (state.state == State.IN_PROGRESS) {
            final var editMessage = new EditMessageText(groupId, state.messageId, "Розыгрыш начался! Стоимость участия 100GC. Для участия отправь /go! Время до окончания розыгрыша " + state.time + " секунд.");
            bot.execute(editMessage);

            final var newState = state.tick();
            if (newState.state == State.FINISHED && state.players.size() > 0) {
                final var playersList = new ArrayList<>(newState.players);
                final var random = new Random();
                final var winnerIndex = random.nextInt(playersList.size());
                final var winnerPlayer = playersList.get(winnerIndex);
                final var sendMessage = new SendMessage(groupId, "Розыгрыш завершен! Победитель %s. Выигрыш отправлен в ЛС.".formatted(winnerPlayer.id()));
                final var sendGift = new SendMessage(winnerPlayer.id(), gift);

                bot.execute(sendMessage);
                bot.execute(sendGift);
            }

            return new SteamKeyTelegramGiveaway(gift, bot, groupId, users, newState);
        }

        return this;
    }

    @Override
    public Giveaway userMessage(User user, String message) throws IOException {
        if (message.equals("/go") && state.state == State.IN_PROGRESS && !state.players.contains(user)) {
            if (user.sum() >= 100) {
                users.changeUserSum(user, -100);
            } else {
                return this;
            }

            final var sendMessage = new SendMessage(groupId, "Пользователь %s добавлен для участия!".formatted(user.id()));
            bot.execute(sendMessage);

            return new SteamKeyTelegramGiveaway(gift, bot, groupId, users, state.withPlayer(user));
        }

        return this;
    }

    @Override
    public boolean isFinished() {
        return state.state == State.FINISHED;
    }
}
