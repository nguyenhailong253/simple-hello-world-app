package unit.dao;

import model.User;

import java.util.List;

public class TestHelper {

    public static boolean compareListOfUsers(List<User> base, List<User> target) {
        if (base.size() != target.size()) {
            return false;
        }

        base.forEach(baseUser ->
                target.removeIf(targetUser ->
                        targetUser.getName().equalsIgnoreCase(baseUser.getName())));
        return target.size() == 0;
    }
}
