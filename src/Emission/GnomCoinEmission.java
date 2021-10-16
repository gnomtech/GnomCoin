package Emission;

import Bank.Bank;

import java.io.FileNotFoundException;
import java.util.Random;

public class GnomCoinEmission implements Emission {
    private final Bank bank;
    private final Integer emissionMax;
    private final Random random;

    public GnomCoinEmission(Bank bank, Integer emissionMax) {
        this.bank = bank;
        this.emissionMax = emissionMax;
        this.random = new Random();
    }

    @Override
    public Boolean tryEmit() throws FileNotFoundException {
        final var randomInt = random.nextInt(emissionMax + 1);
        final var total = bank.total();

        if (randomInt == 0) {
            return false;
        }

        return (emissionMax - total) >= randomInt;
    }

    @Override
    public Integer total() throws FileNotFoundException {
        return bank.total();
    }

    @Override
    public Double chance() throws FileNotFoundException {
        final var total = bank.total();

        return (emissionMax.doubleValue() - total.doubleValue()) / emissionMax.doubleValue();
    }
}
