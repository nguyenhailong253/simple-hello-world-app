package acceptance;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.InMemoryUserDAO;
import dao.UserDAO;
import org.junit.*;
import org.junit.runners.MethodSorters;
import services.application.HelloWorldService;
import utils.Constants;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HelloWorldServiceTest {

    private static URL greetingUrl;
    private static URL usersUrl;
    private static UserDAO userDAO;
    private static HelloWorldService service;
    private static Gson jsonConverter;
    private String currentTime = new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date());

    private String toJsonString(Object value) {
        Map<String, Object> body = new HashMap<>();
        body.put("content", value);
        return jsonConverter.toJson(body);
    }

    @BeforeClass
    public static void startServer() throws IOException {
        jsonConverter = new GsonBuilder().create();
        userDAO = new InMemoryUserDAO();
        greetingUrl = new URL(Constants.URL);
        usersUrl = new URL(Constants.URL + "users");
        service = new HelloWorldService(userDAO);
        service.start();
    }

    @Test
    public void storyA_sendGETRequestToGreetingsEndPoint() throws IOException {
        MockClient client = new MockClient(greetingUrl);
        client.sendRequest("GET");

        int statusCode = client.getResponseCode();
        String actualMsg = client.getResponseMessage();
        String expectedMsg = "Hello " + Constants.OWNER_NAME + " - the time on the server is " + currentTime;

        Assert.assertEquals(200, statusCode);
        Assert.assertEquals(toJsonString(expectedMsg), actualMsg);
    }

    @Test
    public void storyB_sendGETRequestToUsersEndPoint() throws IOException {
        MockClient client = new MockClient(usersUrl);
        client.sendRequest("GET");

        int statusCode = client.getResponseCode();
        String actualMsg = client.getResponseMessage();
        List<String> expectedUsers = new ArrayList<>();
        expectedUsers.add(Constants.OWNER_NAME);

        Assert.assertEquals(200, statusCode);
        Assert.assertEquals(toJsonString(expectedUsers), actualMsg);
    }

    @Test
    public void storyC_sendPOSTRequestToUsersEndPoint_ToCreateUser() throws IOException {
        MockClient client = new MockClient(usersUrl);
        String requestBody = "{'name': 'Steve'}";
        client.sendRequest("POST", requestBody);

        int statusCode = client.getResponseCode();
        String actualMsg = client.getResponseMessage();
        String expectedMsg = Constants.SUCCESS;

        Assert.assertEquals(201, statusCode);
        Assert.assertEquals(toJsonString(expectedMsg), actualMsg);
    }

    @Test
    public void storyD_sendPOSTRequestToUsersEndPoint_ToCreateAlreadyExistedUser() throws IOException {
        MockClient client = new MockClient(usersUrl);
        String requestBody = "{'name': 'Steve'}";
        client.sendRequest("POST", requestBody);

        int statusCode = client.getResponseCode();
        String actualMsg = client.getResponseMessage();
        String expectedMsg = Constants.EXISTED_USER;

        Assert.assertEquals(400, statusCode);
        Assert.assertEquals(toJsonString(expectedMsg), actualMsg);
    }

    @Test
    public void storyE_sendGETRequestToGreetingsEndPoint() throws IOException {
        MockClient client = new MockClient(greetingUrl);
        client.sendRequest("GET");

        int statusCode = client.getResponseCode();
        String actualMsg = client.getResponseMessage();
        String expectedMsg = "Hello " + Constants.OWNER_NAME +", Steve - the time on the server is " + currentTime;

        Assert.assertEquals(200, statusCode);
        Assert.assertEquals(toJsonString(expectedMsg), actualMsg);
    }

    @Test
    public void storyF_sendPUTRequestToUsersEndPoint_ToUpdateUserName() throws IOException {
        MockClient client = new MockClient(new URL(Constants.URL + "users/Steve"));
        String requestBody = "{'name': 'Thor'}";
        client.sendRequest("PUT", requestBody);

        int statusCode = client.getResponseCode();
        String actualMsg = client.getResponseMessage();
        String expectedMsg = Constants.SUCCESS;

        Assert.assertEquals(200, statusCode);
        Assert.assertEquals(toJsonString(expectedMsg), actualMsg);
    }

    @Test
    public void storyG_sendPUTRequestToUsersEndPoint_ToUpdateNonExistentUser() throws IOException {
        MockClient client = new MockClient(new URL(Constants.URL + "users/Clint"));
        String requestBody = "{'name': 'Strange'}";
        client.sendRequest("PUT", requestBody);

        int statusCode = client.getResponseCode();
        String actualMsg = client.getResponseMessage();
        String expectedMsg = Constants.NON_EXISTENT_USER;

        Assert.assertEquals(400, statusCode);
        Assert.assertEquals(toJsonString(expectedMsg), actualMsg);
    }

    @Test
    public void storyH_sendPUTRequestToUsersEndPoint_ToUpdateUser_WithExistedName() throws IOException {
        MockClient client = new MockClient(new URL(Constants.URL + "users/Thor"));
        String requestBody = "{'name': '" + Constants.OWNER_NAME + "'}";
        client.sendRequest("PUT", requestBody);

        int statusCode = client.getResponseCode();
        String actualMsg = client.getResponseMessage();
        String expectedMsg = Constants.EXISTED_USER;

        Assert.assertEquals(400, statusCode);
        Assert.assertEquals(toJsonString(expectedMsg), actualMsg);
    }

    @Test
    public void storyI_sendGETRequestToGreetingsEndPoint() throws IOException {
        MockClient client = new MockClient(greetingUrl);
        client.sendRequest("GET");

        int statusCode = client.getResponseCode();
        String actualMsg = client.getResponseMessage();
        String expectedMsg = "Hello " + Constants.OWNER_NAME + ", Thor - the time on the server is " + currentTime;

        Assert.assertEquals(200, statusCode);
        Assert.assertEquals(toJsonString(expectedMsg), actualMsg);
    }

    @Test
    public void storyJ_sendDELETERequestToUsersEndPoint_ToDeleteUser() throws IOException {
        MockClient client = new MockClient(usersUrl);
        String requestBody = "{'name': 'Thor'}";
        client.sendRequest("DELETE", requestBody);

        int statusCode = client.getResponseCode();
        String actualMsg = client.getResponseMessage();
        String expectedMsg = Constants.SUCCESS;

        Assert.assertEquals(200, statusCode);
        Assert.assertEquals(toJsonString(expectedMsg), actualMsg);
    }

    @Test
    public void storyK_sendDELETERequestToUsersEndPoint_ToDeleteOwner() throws IOException {
        MockClient client = new MockClient(usersUrl);
        String requestBody = "{'name': '" + Constants.OWNER_NAME + "'}";
        client.sendRequest("DELETE", requestBody);

        int statusCode = client.getResponseCode();
        String actualMsg = client.getResponseMessage();
        String expectedMsg = Constants.TARGET_USER_IS_OWNER;

        Assert.assertEquals(400, statusCode);
        Assert.assertEquals(toJsonString(expectedMsg), actualMsg);
    }

    @Test
    public void storyL_sendDELETERequestToUsersEndPoint_ToDeleteNonExistentUser() throws IOException {
        MockClient client = new MockClient(usersUrl);
        String requestBody = "{'name': 'Rocket'}";
        client.sendRequest("DELETE", requestBody);

        int statusCode = client.getResponseCode();
        String actualMsg = client.getResponseMessage();
        String expectedMsg = Constants.NON_EXISTENT_USER;

        Assert.assertEquals(400, statusCode);
        Assert.assertEquals(toJsonString(expectedMsg), actualMsg);
    }

    @Test
    public void storyM_sendGETRequestToGreetingsEndPoint() throws IOException {
        MockClient client = new MockClient(greetingUrl);
        client.sendRequest("GET");

        int statusCode = client.getResponseCode();
        String actualMsg = client.getResponseMessage();
        String expectedMsg = "Hello " + Constants.OWNER_NAME + " - the time on the server is " + currentTime;

        Assert.assertEquals(200, statusCode);
        Assert.assertEquals(toJsonString(expectedMsg), actualMsg);
    }

    @Test
    public void storyN_sendTRACERequestToGreetingsEndPoint() throws IOException {
        MockClient client = new MockClient(greetingUrl);
        client.sendRequest("TRACE");

        int statusCode = client.getResponseCode();
        String actualMsg = client.getResponseMessage();
        String expectedMsg = Constants.METHOD_NOT_ALLOWED;

        Assert.assertEquals(405, statusCode);
        Assert.assertEquals(toJsonString(expectedMsg), actualMsg);
    }
}