package unit.dao;

import dao.InMemoryUserDAO;
import model.User;
import dao.UserDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryUserDAOTest {

    private static List<User> expectedUsers;

    private static UserDAO userDAO;

    private static void initialiseUserListAndDAO() {
        expectedUsers = new ArrayList<>();
        expectedUsers.add(new User(Constants.OWNER_NAME));
        expectedUsers.add(new User("Tony"));
        userDAO = new InMemoryUserDAO();
        userDAO.createUser("Tony");
    }

    public static class TestGetUserByName {

        @Before
        public void initialiseExpectedValues() {
            initialiseUserListAndDAO();
        }

        @Test
        public void shouldReturnUser_GivenUserName() {
            User owner = userDAO.getUserByName(Constants.OWNER_NAME);
            Optional<User> resultUser = Optional.of(owner);
            Assert.assertTrue(resultUser.isPresent());
        }

        @Test
        public void shouldIgnoreCaseSensitivity() {
            User existingUser = userDAO.getUserByName("tony");
            Optional<User> resultUser = Optional.of(existingUser);
            Assert.assertTrue(resultUser.isPresent());
        }

        @Test
        public void shouldReturnNull_WhenReceiveNonExistentUserName() {
            User nonExistentUser = userDAO.getUserByName("Vision");
            Optional<User> resultUser = Optional.ofNullable(nonExistentUser);
            Assert.assertFalse(resultUser.isPresent());
        }
    }

    public static class TestDeleteUser {

        @Before
        public void initialiseExpectedValues() {
            initialiseUserListAndDAO();
        }

        @Test
        public void shouldDeleteExistedUser() {
            expectedUsers.remove(1);

            userDAO.deleteUserByName("Tony");
            List<User> actualUsers = userDAO.getUsers();
            boolean isExpectedListEqualActualList = TestHelper.compareListOfUsers(expectedUsers, actualUsers);

            Assert.assertTrue(isExpectedListEqualActualList);
        }

        @Test
        public void shouldIgnoreCaseSensitivity_WhenDeleteUser() {
            expectedUsers.remove(1);

            userDAO.deleteUserByName("tOnY");
            List<User> actualUsers = userDAO.getUsers();
            boolean isExpectedListEqualActualList = TestHelper.compareListOfUsers(expectedUsers, actualUsers);

            Assert.assertTrue(isExpectedListEqualActualList);
        }

        @Test
        public void shouldDoNothing_WhenDeleteNonExistentUser() {
            userDAO.deleteUserByName("Thor");
            List<User> actualUsers = userDAO.getUsers();
            boolean isExpectedListEqualActualList = TestHelper.compareListOfUsers(expectedUsers, actualUsers);

            Assert.assertTrue(isExpectedListEqualActualList);
        }
    }

    public static class TestUpdateUserName {

        @Before
        public void initialiseExpectedValues() {
            initialiseUserListAndDAO();
        }

        @Test
        public void shouldUpdateUserWithNewName_WhenUserExisted() {
            expectedUsers.get(1).setName("Stephen");

            userDAO.updateUserName("Tony", "Stephen");
            List<User> actualUsers = userDAO.getUsers();
            boolean isExpectedListEqualActualList = TestHelper.compareListOfUsers(expectedUsers, actualUsers);

            Assert.assertTrue(isExpectedListEqualActualList);
        }

        @Test
        public void shouldIgnoreCaseSensitivity_WhenUpdateUser() {
            expectedUsers.get(1).setName("stEPHEn");

            userDAO.updateUserName("tony", "stEPHEn");
            List<User> actualUsers = userDAO.getUsers();
            boolean isExpectedListEqualActualList = TestHelper.compareListOfUsers(expectedUsers, actualUsers);

            Assert.assertTrue(isExpectedListEqualActualList);
        }

        @Test
        public void shouldDoNothing_WhenUpdateNonExistentUser() {
            userDAO.updateUserName("Natasha", "Clint");
            List<User> actualUsers = userDAO.getUsers();
            boolean isExpectedListEqualActualList = TestHelper.compareListOfUsers(expectedUsers, actualUsers);

            Assert.assertTrue(isExpectedListEqualActualList);
        }
    }
}