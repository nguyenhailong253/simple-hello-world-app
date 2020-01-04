package dao;

import model.User;

import java.util.List;

public interface UserDAO {
    void createUser(String userName);
    List<User> getUsers();
    User getUserByName(String userName);
    void updateUserName(String targetName, String newName);
    void deleteUserByName(String targetName);
}
