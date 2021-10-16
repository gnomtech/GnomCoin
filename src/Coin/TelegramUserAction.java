package Coin;

import Users.User;

public class TelegramUserAction implements UserAction {
    private final User user;
    private final Boolean isEmitCoin;

    public TelegramUserAction(User user, Boolean isEmitCoin) {
        this.user = user;
        this.isEmitCoin = isEmitCoin;
    }

    @Override
    public User user() {
        return user;
    }

    @Override
    public Boolean isEmitCoin() {
        return isEmitCoin;
    }
}
