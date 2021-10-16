package Bank;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Bank {
    Integer total() throws FileNotFoundException;
    Transaction transaction(String sourceUserId, String  targetUserId, Integer sum) throws IOException;
}
