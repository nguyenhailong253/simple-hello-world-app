package unit.services.users;

import dao.InMemoryUserDAO;
import model.User;
import dao.UserDAO;
import exception.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import services.users.UserService;
import utils.Constants;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class UsersServiceTest {

    private static UserDAO spyUserDAO;
    private static UserService service;

    private static void initialiseDAOAndUserService() {
        spyUserDAO = Mockito.spy(new InMemoryUserDAO());
        spyUserDAO.createUser("Tony");
        spyUserDAO.createUser("Bruce");
        spyUserDAO.createUser("Peter");
        service = new UserService(spyUserDAO);
    }

    public static class TestCreateUser {

        @Before
        public void initialisePrivateFields() {
            initialiseDAOAndUserService();
        }

        @Test
        public void shouldCreateUser_WithValidName() {
            service.createUser("Clint");
            verify(spyUserDAO).createUser("Clint");
        }

        @Test(expected = ExistedUserNameException.class)
        public void shouldThrowExistedUserException() {
            service.createUser(Constants.OWNER_NAME);
        }

        @Test(expected = InvalidUserNameException.class)
        public void shouldThrowInvalidUserNameException_WhenReceiveEmptyName() {
            service.createUser("");
        }

        @Test(expected = InvalidUserNameException.class)
        public void shouldThrowInvalidUserNameException_WhenReceiveBlankName() {
            service.createUser("    ");
        }

        @Test(expected = InvalidUserNameException.class)
        public void shouldThrowInvalidUserNameException_WhenReceiveNumbers() {
            service.createUser("129412");
        }

        @Test(expected = InvalidUserNameException.class)
        public void shouldThrowInvalidUserNameException_WhenReceiveNonAlphabeticalName() {
            service.createUser("!@$*!^!*");
        }
    }

    public static class TestUpdateUserName {

        @Before
        public void initialiseDefaultVariables() {
            initialiseDAOAndUserService();
        }

        @Test
        public void shouldUpdateUserName_WhenTargetNameExistsAndNewNameDoesNot() {
            String targetName = "tony";
            service.updateUserName(targetName, "Wanda");
            verify(spyUserDAO).updateUserName(targetName, "Wanda");
        }

        @Test(expected = NonExistentUserNameException.class)
        public void shouldThrowNonExistentUserException_WhenUpdateNonExistentUserName() {
            String nonExistentName = "Vision";
            service.updateUserName(nonExistentName, "Wanda");
        }

        @Test(expected = ExistedUserNameException.class)
        public void shouldThrowExistedUserException_WhenUpdateUserWithNameAlreadyExisted() {
            String existedName = "Peter";
            service.updateUserName("Bruce", existedName);
        }

        @Test(expected = UserIsOwnerException.class)
        public void shouldUserIsOwnerException_WhenUpdateOwnerName() {
            service.updateUserName(Constants.OWNER_NAME, "Nick");
        }

        @Test(expected = InvalidUserNameException.class)
        public void shouldThrowInvalidUserNameException_WhenNewNameDoesNotMatchValidPattern() {
            service.updateUserName("Bruce", "123!#$!$");
        }
    }

    public static class TestDeleteUser {

        @Before
        public void initialiseDefaultVariables() {
            initialiseDAOAndUserService();
        }

        @Test
        public void shouldDeleteExistingUser() {
            service.deleteUser("Tony");
            verify(spyUserDAO).deleteUserByName("Tony");
        }

        @Test(expected = NonExistentUserNameException.class)
        public void shouldThrowNonExistentUserException() {
            service.deleteUser("Stephen");
        }

        @Test(expected = UserIsOwnerException.class)
        public void shouldThrowUserIsOwnerException_WhenDeleteOwner() {
            service.deleteUser(Constants.OWNER_NAME);
        }
    }

    public static class TestGetUsers {

        private List<User> expectedUsers;
        private UserDAO mockUserDAO;

        @Before
        public void initialisePrivateFields() {
            mockUserDAO = mock(InMemoryUserDAO.class);
            service = new UserService(mockUserDAO);
            expectedUsers = new ArrayList<>();
        }

        @Test
        public void shouldReturnListOfUserNames() {
            expectedUsers.add(new User("Tony"));
            expectedUsers.add(new User("Steve"));
            when(mockUserDAO.getUsers()).thenReturn(expectedUsers);

            List<String> expectedNames = new ArrayList<>();
            expectedNames.add("Tony");
            expectedNames.add("Steve");

            Assert.assertEquals(expectedNames, service.getUserNames());
        }
    }
}