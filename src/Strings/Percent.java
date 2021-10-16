package Strings;

public class Percent {
    private final Number number;

    public Percent(Number number) {
        this.number = number;
    }

    @Override
    public String toString() {
        final var numberString = number.toString();
        final var numberParts = numberString.split("\\.");

        if (numberParts.length == 1) {
            return numberParts[0] + "%";
        }

        if (numberParts.length == 2) {
            var zeroCounter = 0;

            for (var i = 0; i < numberParts[1].length(); i++) {
                if (numberParts[1].charAt(i) == '0') {
                    zeroCounter++;
                } else {
                    break;
                }
            }

            return zeroCounter == numberParts[1].length()
                    ? numberParts[0] + "%"
                    : numberParts[0] + "." + numberParts[1].substring(0, zeroCounter + 1) + "%";
        }

        return "";
    }
}
