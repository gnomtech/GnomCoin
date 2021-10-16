package Telegram;

import java.util.List;

public interface ParsedCommand {
    String command();
    List<String> args();
}
