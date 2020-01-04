package services.application;

import com.sun.net.httpserver.HttpServer;
import dao.UserDAO;
import handlers.greetings.GreetingsHandler;
import handlers.users.UserHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HelloWorldService {

    private UserDAO userDAO;

    public HelloWorldService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void start() throws IOException {
        System.out.println("Server is running on port 8000");
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(8000), 2);
        server.createContext("/", new GreetingsHandler(userDAO));
        server.createContext("/users", new UserHandler(userDAO));
        server.start();
    }
}
