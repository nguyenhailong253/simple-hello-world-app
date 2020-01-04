package unit.handlers.users;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import dao.InMemoryUserDAO;
import handlers.Response;
import dao.UserDAO;
import org.junit.Before;
import org.junit.Test;
import handlers.users.UserHandler;
import utils.Constants;

import java.io.*;
import java.net.URI;

import static org.mockito.Mockito.*;

public class UserHandlerTest {

    private String USERS_URI = Constants.URL + "users";
    private Gson jsonConverter = new GsonBuilder().create();
    private HttpExchange mockHttpExchange;
    private OutputStream mockOutputStream;
    private UserHandler handler;

    private void initialiseHandlerAndMockRequest() {
        UserDAO userDAO = new InMemoryUserDAO();
        userDAO.createUser("Tony");
        userDAO.createUser("Bruce");
        userDAO.createUser("Peter");
        handler = new UserHandler(userDAO);
        mockHttpExchange = mock(HttpExchange.class);
        mockOutputStream = mock(OutputStream.class);
    }

    private void verifyResponse(Response response) throws Exception {
        String responseBody = jsonConverter.toJson(response.getResponseBody());
        verify(mockHttpExchange).sendResponseHeaders(response.getStatusCode(), responseBody.length());
        verify(mockOutputStream).write(responseBody.getBytes());
    }

    private void mockRequest(String requestMethod, URI requestURI, InputStream stubInputStream) {
        when(mockHttpExchange.getRequestMethod()).thenReturn(requestMethod);
        when(mockHttpExchange.getRequestBody()).thenReturn(stubInputStream);
        when(mockHttpExchange.getRequestURI()).thenReturn(requestURI);
    }

    @Before
    public void initialiseHandlerAndSetMockOutputStream() {
        initialiseHandlerAndMockRequest();
        when(mockHttpExchange.getResponseBody()).thenReturn(mockOutputStream);
    }

    @Test
    public void shouldReturnListOfUsers_WhenReceiveGETRequest() throws Exception {
        mockRequest("GET", new URI(USERS_URI), null);

        handler.handle(mockHttpExchange);

        verify(mockHttpExchange).sendResponseHeaders(anyInt(), anyString().length());
        verify(mockOutputStream).write(anyString().getBytes());
    }

    @Test
    public void shouldCreateNewUser_IfUserDoesNotExist_WhenReceivePOSTRequest() throws Exception {
        InputStream stubInputStream = new ByteArrayInputStream("{'name': 'Steve'}".getBytes());
        mockRequest("POST", new URI(USERS_URI), stubInputStream);

        handler.handle(mockHttpExchange);

        verifyResponse(new Response(201, Constants.SUCCESS));
    }

    @Test
    public void shouldSendResponseWith400Code_WhenReceivePOSTRequestToCreateExistedUser() throws Exception {
        String stubRequestBody = "{'name': '" + Constants.OWNER_NAME + "'}";
        InputStream stubInputStream = new ByteArrayInputStream(stubRequestBody.getBytes());
        mockRequest("POST", new URI(USERS_URI), stubInputStream);

        handler.handle(mockHttpExchange);

        verifyResponse(new Response(400, Constants.EXISTED_USER));
    }

    @Test
    public void shouldUpdateExistingUserWithNewName_WhenReceivePUTRequest() throws Exception {
        InputStream stubInputStream = new ByteArrayInputStream("{'name': 'Nick'}".getBytes());
        mockRequest("PUT", new URI(USERS_URI + "/Tony"), stubInputStream);

        handler.handle(mockHttpExchange);

        verifyResponse(new Response(200, Constants.SUCCESS));
    }

    @Test
    public void shouldSendResponseWith400Code_WhenReceivePUTRequestToUpdateNonExistentUser() throws Exception {
        InputStream stubInputStream = new ByteArrayInputStream("{'name': 'Natasha'}".getBytes());
        mockRequest("PUT", new URI(USERS_URI + "/Clint"), stubInputStream);

        handler.handle(mockHttpExchange);

        verifyResponse(new Response(400, Constants.NON_EXISTENT_USER));
    }

    @Test
    public void shouldSendResponseWith400Code_WhenReceivePUTRequestToUpdateUserWithAlreadyExistedName() throws Exception {
        String stubRequestBody = "{'name': '" + Constants.OWNER_NAME + "'}";
        InputStream stubInputStream = new ByteArrayInputStream(stubRequestBody.getBytes());
        mockRequest("PUT", new URI(USERS_URI + "/Bruce"), stubInputStream);

        handler.handle(mockHttpExchange);

        verifyResponse(new Response(400, Constants.EXISTED_USER));
    }

    @Test
    public void shouldSendResponseWith400Code_WhenReceivePUTRequestToUpdateOwner() throws Exception{
        InputStream stubInputStream = new ByteArrayInputStream("{'name': 'Scott'}".getBytes());
        mockRequest("PUT", new URI(USERS_URI + "/" + Constants.OWNER_NAME), stubInputStream);

        handler.handle(mockHttpExchange);

        verifyResponse(new Response(400, Constants.TARGET_USER_IS_OWNER));
    }

    @Test
    public void shouldDeleteExistingUser_WhenReceiveDeleteRequest() throws Exception {
        InputStream stubInputStream = new ByteArrayInputStream("{'name': 'Peter'}".getBytes());
        mockRequest("DELETE", new URI(USERS_URI), stubInputStream);

        handler.handle(mockHttpExchange);

        verifyResponse(new Response(200, Constants.SUCCESS));
    }

    @Test
    public void shouldSendResponseWith400Code_WhenReceiveDeleteRequestForNonExistentUser() throws Exception {
        InputStream stubInputStream = new ByteArrayInputStream("{'name': 'Wanda'}".getBytes());
        mockRequest("DELETE", new URI(USERS_URI), stubInputStream);

        handler.handle(mockHttpExchange);

        verifyResponse(new Response(400, Constants.NON_EXISTENT_USER));
    }

    @Test
    public void shouldSendResponseWith400Code_WhenReceiveDeleteRequestForOwner() throws Exception {
        String stubRequestBody = "{'name': '" + Constants.OWNER_NAME + "'}";
        InputStream stubInputStream = new ByteArrayInputStream(stubRequestBody.getBytes());
        mockRequest("DELETE", new URI(USERS_URI), stubInputStream);

        handler.handle(mockHttpExchange);

        verifyResponse(new Response(400, Constants.TARGET_USER_IS_OWNER));
    }

    @Test
    public void shouldSendResponseWith405Code_WhenReceiveUnsupportedRequestMethods() throws Exception {
        InputStream stubInputStream = new ByteArrayInputStream("{'name': 'Steve'}".getBytes());
        mockRequest("PATCH", new URI(USERS_URI), stubInputStream);

        handler.handle(mockHttpExchange);

        verifyResponse(new Response(405, Constants.METHOD_NOT_ALLOWED));
    }
}