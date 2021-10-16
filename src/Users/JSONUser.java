package Users;

import java.util.Objects;

public class JSONUser implements User {
    private final String id;
    private final Integer sum;

    public JSONUser(String id, Integer sum) {
        this.id = id;
        this.sum = sum;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Integer sum() {
        return sum;
    }

    @Override
    public String toString() {
        return "JSONUser{" +
                "id='" + id + '\'' +
                ", sum=" + sum +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JSONUser jsonUser = (JSONUser) o;
        return Objects.equals(id, jsonUser.id) && Objects.equals(sum, jsonUser.sum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sum);
    }
}
