package Bank;

import Bank.TransactionErrorReasons.NoMoneyErrorReason;
import Bank.TransactionErrorReasons.NoUserErrorReason;
import Bank.TransactionErrorReasons.SelfSendErrorReason;
import Users.User;
import Users.Users;

import java.io.FileNotFoundException;
import java.io.IOException;

public class TelegramBank implements Bank {
    private final Users users;

    public TelegramBank(Users users) {
        this.users = users;
    }

    @Override
    public Integer total() throws FileNotFoundException {
        return users
                .all()
                .stream()
                .map(User::sum).reduce(0, Integer::sum);
    }

    @Override
    public Transaction transaction(String sourceUserId, String targetUserId, Integer sum) throws IOException {
        final var maybeSourceUser = users.userById(sourceUserId);
        final var maybeTargetUser = users.userById(targetUserId);

        if (maybeSourceUser.isEmpty()) {
            return new TelegramTransaction(new NoUserErrorReason(sourceUserId));
        }

        if (maybeTargetUser.isEmpty()) {
            return new TelegramTransaction(new NoUserErrorReason(targetUserId));
        }

        if (sourceUserId.equals(targetUserId)) {
            return new TelegramTransaction(new SelfSendErrorReason());
        }

        final var sourceUser = maybeSourceUser.get();
        final var targetUser = maybeTargetUser.get();

        if (sourceUser.sum() < sum) {
            return new TelegramTransaction(new NoMoneyErrorReason(sourceUser, sum));
        }

        users.changeUserSum(sourceUser, -1 * sum);
        users.changeUserSum(targetUser, sum);

        return new TelegramTransaction();
    }
}
