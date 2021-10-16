package Bank.TransactionErrorReasons;

import Bank.TransactionErrorReason;

public class NoUserErrorReason implements TransactionErrorReason {
    private final String userId;

    public NoUserErrorReason(String userId) {
        this.userId = userId;
    }

    @Override
    public String text() {
        return "Пользователя с ID %s не существует!".formatted(userId);
    }
}
