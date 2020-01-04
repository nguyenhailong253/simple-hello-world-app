package unit.handlers.greetings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import dao.InMemoryUserDAO;
import handlers.Response;
import dao.UserDAO;
import org.junit.Before;
import org.junit.Test;
import handlers.greetings.GreetingsHandler;
import utils.Constants;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.*;

public class GreetingsHandlerTest {

    private String currentTime = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());
    private Gson jsonConverter = new GsonBuilder().create();
    private HttpExchange mockHttpExchange;
    private OutputStream mockOutputStream;
    private GreetingsHandler handler;

    private void initialiseHandlerAndExchangeClass() {
        UserDAO userDAO = new InMemoryUserDAO();
        userDAO.createUser("Peter");
        handler = new GreetingsHandler(userDAO);
        mockHttpExchange = mock(HttpExchange.class);
        mockOutputStream = mock(OutputStream.class);
    }

    private void verifyResponse(Response response) throws Exception {
        String responseBody = jsonConverter.toJson(response.getResponseBody());
        verify(mockHttpExchange).sendResponseHeaders(response.getStatusCode(), responseBody.length());
        verify(mockOutputStream).write(responseBody.getBytes());
    }

    @Before
    public void initialiseHandlerAndOutputStream() {
        initialiseHandlerAndExchangeClass();
        when(mockHttpExchange.getResponseBody()).thenReturn(mockOutputStream);
    }

    @Test
    public void shouldReturnGreetingMessage_WhenReceiveGetRequest() throws Exception {
        String expectedMessage = "Hello " + Constants.OWNER_NAME + ", Peter - the time on the server is " + currentTime;

        when(mockHttpExchange.getRequestMethod()).thenReturn("GET");

        handler.handle(mockHttpExchange);

        verifyResponse(new Response(200, expectedMessage));
    }

    @Test
    public void shouldSendResponseWith400Code_WhenReceiveUnsupportedRequestMethods() throws Exception {
        when(mockHttpExchange.getRequestMethod()).thenReturn("PUT");

        handler.handle(mockHttpExchange);

        verifyResponse(new Response(405, Constants.METHOD_NOT_ALLOWED));
    }
}