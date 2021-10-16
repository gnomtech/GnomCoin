package Telegram;

import com.pengrad.telegrambot.model.Update;

import java.io.IOException;

public interface UpdateProcessor {
    void process(Update update) throws IOException;
    void tick();
}
