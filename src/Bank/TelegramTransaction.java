package Bank;

import java.util.Optional;

public class TelegramTransaction implements Transaction {
    private final Optional<TransactionErrorReason> reason;

    public TelegramTransaction(TransactionErrorReason reason) {
        this.reason = Optional.of(reason);
    }
    public TelegramTransaction() {
        this.reason = Optional.empty();
    }

    @Override
    public Boolean isSuccess() {
        return reason.isEmpty();
    }

    @Override
    public Optional<TransactionErrorReason> reason() {
        return reason;
    }
}
