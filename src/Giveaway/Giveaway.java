package Giveaway;

import Users.User;

import java.io.IOException;

public interface Giveaway {
    Giveaway start();
    Giveaway tick();
    Giveaway userMessage(User user, String message) throws IOException;
    boolean isFinished();
}
