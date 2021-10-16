package Users;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface Users {
    Optional<User> userById(String id) throws FileNotFoundException;
    List<User> all() throws FileNotFoundException;
    void createUser(String id, Integer sum) throws IOException;
    void changeUserSum(User user, Integer sumDiff) throws IOException;
}
