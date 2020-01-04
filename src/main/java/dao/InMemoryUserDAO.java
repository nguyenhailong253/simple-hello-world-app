package dao;

import model.User;
import utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class InMemoryUserDAO implements UserDAO {

    private List<User> users;

    public InMemoryUserDAO() {
        users = new ArrayList<>();
        createUser(Constants.OWNER_NAME);
    }

    @Override
    public void createUser(String userName) {
        users.add(new User(userName));
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public User getUserByName(String userName) {
        return users.stream()
                .filter(user -> user.getName().equalsIgnoreCase(userName))
                .findAny()
                .orElse(null);
    }

    @Override
    public void updateUserName(String targetName, String newName) {
        users.stream().filter(user -> user.getName().equalsIgnoreCase(targetName))
                .forEach(user -> user.setName(newName));
    }

    @Override
    public void deleteUserByName(String targetName) {
        users.removeIf(user -> user.getName().equalsIgnoreCase(targetName));
    }
}
