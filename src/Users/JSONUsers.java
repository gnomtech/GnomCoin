package Users;

import com.google.gson.Gson;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JSONUsers implements Users {
    private final Gson gson = new Gson();
    private final String dbPath;

    public JSONUsers(String dbPath) {
        this.dbPath = dbPath;
    }

    public void createUser(String id, Integer sum) throws IOException {
        final var users = readUsers();
        final var hasUser = Arrays.stream(users).anyMatch(u -> u.id().equals(id));

        if (!hasUser) {
            final var newUser = new JSONUser(id, sum);
            final var newUsers = Arrays.stream(users).collect(Collectors.toList());
            newUsers.add(newUser);
            writeUsers(newUsers);
        }
    }

    @Override
    public Optional<User> userById(String id) throws FileNotFoundException {
        final var users = readUsers();

        return Arrays.stream(users).filter(u -> u.id().equals(id)).findFirst();
    }

    @Override
    public List<User> all() throws FileNotFoundException {
        return Arrays.stream(readUsers()).collect(Collectors.toList());
    }

    @Override
    public void changeUserSum(User user, Integer sumDiff) throws IOException {
        final var users = readUsers();

        for (var i = 0; i < users.length; i++) {
            if (users[i].id().equals(user.id())) {
                users[i] = new JSONUser(user.id(), users[i].sum() + sumDiff);
                writeUsers(Arrays.stream(users).collect(Collectors.toList()));
                break;
            }
        }
    }

    private void writeUsers(List<User> users) throws IOException {
        final var writer = new FileWriter(dbPath);
        writer.write(gson.toJson(users));
        writer.close();
    }

    private User[] readUsers() throws FileNotFoundException {
        final var reader = new FileReader(dbPath);
        return gson.fromJson(reader, JSONUser[].class);
    }
}
