package handlers.users;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import handlers.Request;
import handlers.Response;
import dao.UserDAO;
import services.users.UserService;
import utils.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class UserHandler implements HttpHandler {

    private Gson jsonConverter = new Gson();
    private UserDAO userDAO;
    private UserService userService;

    public UserHandler(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.userService = new UserService(this.userDAO);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Request request = new Request(exchange);
        Response response;
        try {
            switch (request.getMethod()) {
                case "GET":
                    response = handleGetUsersRequest();
                    break;
                case "POST":
                    response = handleCreateUserRequest(request);
                    break;
                case "PUT":
                    response = handleUpdateUserRequest(request);
                    break;
                case "DELETE":
                    response = handleDeleteUserRequest(request);
                    break;
                default:
                    response = new Response(405, Constants.METHOD_NOT_ALLOWED);
            }
        } catch (Exception e) {
            response = new Response(400, e.getMessage());
        }
        response.send(exchange);
    }

    private Response handleGetUsersRequest() {
        List<String> users = userService.getUserNames();
        return new Response(200, users);
    }

    private Response handleCreateUserRequest(Request request) throws IOException {
        Map<String, String> deserializedBody = jsonConverter.fromJson(request.getBody(), HashMap.class);
        userService.createUser(deserializedBody.get("name"));
        return new Response(201, Constants.SUCCESS);
    }

    private Response handleUpdateUserRequest(Request request) throws IOException {
        String existingName = request.getURI().replace("/users/", "");

        Map<String, String> deserializedBody = jsonConverter.fromJson(request.getBody(), HashMap.class);

        userService.updateUserName(existingName, deserializedBody.get("name"));
        return new Response(200, Constants.SUCCESS);
    }

    private Response handleDeleteUserRequest(Request request) throws IOException {
        Map<String, String> deserializedBody = jsonConverter.fromJson(request.getBody(), HashMap.class);
        userService.deleteUser(deserializedBody.get("name"));
        return new Response(200, Constants.SUCCESS);
    }
}
