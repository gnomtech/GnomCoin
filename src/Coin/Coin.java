package Coin;

import Users.User;

import java.io.IOException;

public interface Coin {
    UserAction action(User user) throws IOException;
}
