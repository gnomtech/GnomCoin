package Bank.TransactionErrorReasons;

import Bank.TransactionErrorReason;
import Users.User;

public class NoMoneyErrorReason implements TransactionErrorReason {
    private final User user;
    private final Integer sum;

    public NoMoneyErrorReason(User user, Integer sum) {
        this.user = user;
        this.sum = sum;
    }

    @Override
    public String text() {
        return "Пользователь {} не обладает нужной суммой {}".formatted(user.id(), sum);
    }
}
