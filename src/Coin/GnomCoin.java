package Coin;

import Emission.Emission;
import Users.User;
import Users.Users;

import java.io.IOException;
import java.util.function.BiFunction;

public class GnomCoin implements Coin {
    private final Emission emission;
    private final Users users;
    private final BiFunction<User, Boolean, UserAction> actionGenerator;

    public GnomCoin(Emission emission, Users users) {
        this.emission = emission;
        this.users = users;

        actionGenerator = TelegramUserAction::new;
    }

    @Override
    public UserAction action(User user) throws IOException {
        final var emissionResult = emission.tryEmit();

        if (emissionResult) {
            users.changeUserSum(user, 1);
        }

        return actionGenerator.apply(user, emissionResult);
    }
}
