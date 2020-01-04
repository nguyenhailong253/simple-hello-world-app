package unit.services.greetings;

import model.User;
import dao.InMemoryUserDAO;
import dao.UserDAO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import services.greetings.GreetingsService;
import utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GreetingsServiceTest {

    private UserDAO mockDAO = mock(InMemoryUserDAO.class);
    private List<User> expectedUsers;
    private GreetingsService handler;
    private String currentTime = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());

    @Before
    public void initialise() {
        handler = new GreetingsService(mockDAO);
        expectedUsers = new ArrayList<>();
    }

    @Test
    public void shouldReturnGreetingMessage_ForMultipleUsers() {
        expectedUsers.add(new User("Bruce"));
        expectedUsers.add(new User("Peter"));
        when(mockDAO.getUsers()).thenReturn(expectedUsers);
        String expectedMsg = "Hello Bruce, Peter - the time on the server is " + currentTime;

        String actualMsg = handler.getGreetingMessage();

        Assert.assertEquals(expectedMsg, actualMsg);
    }

    @Test
    public void shouldReturnGreetingMessage_For1User() {
        expectedUsers.add(new User("Nick"));
        when(mockDAO.getUsers()).thenReturn(expectedUsers);
        String expectedMsg = "Hello Nick - the time on the server is " + currentTime;

        String actualMsg = handler.getGreetingMessage();

        Assert.assertEquals(expectedMsg, actualMsg);
    }
}