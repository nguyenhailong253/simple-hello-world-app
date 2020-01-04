package handlers.greetings;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dao.UserDAO;
import handlers.Request;
import handlers.Response;
import services.greetings.GreetingsService;
import utils.Constants;

import java.io.IOException;

public class GreetingsHandler implements HttpHandler {

    private UserDAO userDAO;
    private GreetingsService greetingsService;

    public GreetingsHandler(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.greetingsService = new GreetingsService(this.userDAO);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Request request = new Request(exchange);
        Response response = new Response(405, Constants.METHOD_NOT_ALLOWED);
        try {
            if (request.getMethod().equals("GET")) {
                response = new Response(200, greetingsService.getGreetingMessage());
            }
        } catch (Exception e) {
            response = new Response(400, e.getMessage());
        }
        response.send(exchange);
    }
}
