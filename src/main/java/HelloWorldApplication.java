import dao.InMemoryUserDAO;
import dao.UserDAO;
import services.application.HelloWorldService;

public class HelloWorldApplication {
    private static UserDAO userDAO = new InMemoryUserDAO();
    private static HelloWorldService service = new HelloWorldService(userDAO);

    public static void main(String[] args) throws Exception {
        service.start();
    }
}
