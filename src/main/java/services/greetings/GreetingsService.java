package services.greetings;

import dao.UserDAO;
import utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

public class GreetingsService {

    private UserDAO userDAO;

    public GreetingsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public String getGreetingMessage() {
        List<String> userNames = new ArrayList<>();
        userDAO.getUsers().forEach(user -> userNames.add(user.getName()));
        return constructGreetingMessage(userNames);
    }

    private String constructGreetingMessage(List<String> userNames) {
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add("Hello")
                .add(String.join(Constants.COMMA, userNames))
                .add("- the time on the server is")
                .add(new SimpleDateFormat(Constants.DATE_FORMAT).format(new Date()));
        return joiner.toString();
    }
}
