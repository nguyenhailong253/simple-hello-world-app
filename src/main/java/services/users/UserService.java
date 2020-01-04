package services.users;

import dao.UserDAO;
import exception.InvalidUserNameException;
import exception.ExistedUserNameException;
import exception.NonExistentUserNameException;
import exception.UserIsOwnerException;
import utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void createUser(String userName) {
        validateUserName(userName);
        validateUserNotExisted(userName);

        userDAO.createUser(userName);
    }

    public List<String> getUserNames() {
        List<String> userNames = new ArrayList<>();
        userDAO.getUsers().forEach(user -> userNames.add(user.getName()));
        return userNames;
    }

    public void updateUserName(String targetName, String newName) {
        validateUserIsNotOwner(targetName);
        validateUserExisted(targetName);
        validateUserName(newName);
        validateUserNotExisted(newName);

        userDAO.updateUserName(targetName, newName);
    }

    public void deleteUser(String targetName) {
        validateUserIsNotOwner(targetName);
        validateUserExisted(targetName);

        userDAO.deleteUserByName(targetName);
    }

    private void validateUserName(String userName) {
        if (userName.isEmpty() || userName.matches(Constants.INVALID_PATTERN)) {
            throw new InvalidUserNameException(Constants.INVALID_USER_NAME);
        }
    }

    private void validateUserExisted(String userName) {
        if (userDAO.getUserByName(userName) == null) {
            throw new NonExistentUserNameException(Constants.NON_EXISTENT_USER);
        }
    }

    private void validateUserNotExisted(String userName) {
        if (userDAO.getUserByName(userName) != null) {
            throw new ExistedUserNameException(Constants.EXISTED_USER);
        }
    }

    private void validateUserIsNotOwner(String userName) {
        if (userName.equals(Constants.OWNER_NAME)) {
            throw new UserIsOwnerException(Constants.TARGET_USER_IS_OWNER);
        }
    }
}
