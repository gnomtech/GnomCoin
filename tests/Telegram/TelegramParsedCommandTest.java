package Telegram;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class TelegramParsedCommandTest {
    @Test
    public void withoutArgs() {
        final var result = new TelegramParsedCommand("/help");

        assertThat(result.command()).isEqualTo("/help");
        assertThat(result.args()).hasSize(0);
    }

    @Test
    public void withoutArgsButWithSpaces() {
        final var result = new TelegramParsedCommand("/help   ");

        assertThat(result.command()).isEqualTo("/help");
        assertThat(result.args()).hasSize(0);
    }

    @Test
    public void argsWithRandomSpaces() {
        final var result = new TelegramParsedCommand("/command 111    222       333");

        assertThat(result.command()).isEqualTo("/command");
        assertThat(result.args()).hasSize(3);
        assertThat(result.args().get(0)).isEqualTo("111");
        assertThat(result.args().get(1)).isEqualTo("222");
        assertThat(result.args().get(2)).isEqualTo("333");
    }

    @Test
    public void argsWithRandomSpacesAndQuotes() {
        final var result = new TelegramParsedCommand("/command \"111\"   \" 222\"     \"  333\"");

        assertThat(result.command()).isEqualTo("/command");
        assertThat(result.args()).hasSize(3);
        assertThat(result.args().get(0)).isEqualTo("111");
        assertThat(result.args().get(1)).isEqualTo(" 222");
        assertThat(result.args().get(2)).isEqualTo("  333");
    }

    @Test
    public void incorrectQuotes() {
        final var result = new TelegramParsedCommand("/command \"111\"   \" 222 333");

        assertThat(result.command()).isEqualTo("/command");
        assertThat(result.args()).hasSize(2);
        assertThat(result.args().get(0)).isEqualTo("111");
        assertThat(result.args().get(1)).isEqualTo("\" 222 333");
    }
}
