import Bank.TelegramBank;
import Coin.GnomCoin;
import Emission.GnomCoinEmission;
import Telegram.Commands.*;
import Telegram.GnomCoinUpdateProcessor;
import Users.JSONUsers;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        if (args.length != 2) {
            return;
        }

        final var GROUP_ID = Long.valueOf(args[0]);
        final var ADMIN_ID = Integer.valueOf(args[2]);

        final var bot = new TelegramBot(args[1]);
        final var users = new JSONUsers("./db.json"); // Путь к БД
        final var bank = new TelegramBank(users);
        final var gnomCoinEmission = new GnomCoinEmission(bank, 10_000);
        final var gnomCoin = new GnomCoin(gnomCoinEmission, users);

        final var userCommands = List.of(
                new MyIdCommand(bot),
                new MyBalanceCommand(users, bot),
                new SendCoinsCommand(bank, bot),
                new EmissionInfoCommand(gnomCoinEmission, bot),
                new ChangeTitleCommand(bot, users),
                new ChangeUserTitleCommand(100, bot, users),
                new ChangeGroupTitleCommand(1000, bot, users));

        final var helpCommand = new HelpCommand(userCommands, bot);
        final var allCommands = new ArrayList<>(userCommands);
        allCommands.add(helpCommand);

        final var updateProcessor = new GnomCoinUpdateProcessor(allCommands, GROUP_ID, ADMIN_ID.toString(), users, gnomCoin, bot);

        var lastUpdateId = 0;
        while (true) {
            GetUpdates getUpdates = new GetUpdates().offset(lastUpdateId);
            GetUpdatesResponse updatesResponse = bot.execute(getUpdates);
            List<Update> updates = updatesResponse.updates();

            for (var update : updates) {
                // System.out.println(update.message().chat());
                updateProcessor.process(update);
            }

            if (!updates.isEmpty()) {
                lastUpdateId = updates.get(updates.size() - 1).updateId() + 1;
            }

            updateProcessor.tick();
            Thread.sleep(1000);
        }
    }
}
