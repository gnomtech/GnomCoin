import Strings.Percent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class PercentTests {
    @Test
    public void intNumber() {
        final var percent = new Percent(1);

        assertThat(percent.toString()).isEqualTo("1%");
    }

    @Test
    public void doubleAsInt() {
        final var percent = new Percent(1.0);

        assertThat(percent.toString()).isEqualTo("1%");
    }

    @Test
    public void doubleAsDouble() {
        final var percent = new Percent(1.000333);

        assertThat(percent.toString()).isEqualTo("1.0003%");
    }
}
