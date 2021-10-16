package Bank;

import java.util.Optional;

public interface Transaction {
    Boolean isSuccess();
    Optional<TransactionErrorReason> reason();
}
