package Bank.TransactionErrorReasons;

import Bank.TransactionErrorReason;

public class SelfSendErrorReason implements TransactionErrorReason {
    @Override
    public String text() {
        return "Нельзя отправить GC самому себе!";
    }
}
