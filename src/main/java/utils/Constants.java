package utils;

public class Constants {

    // exception
    public static final String METHOD_NOT_ALLOWED = "Invalid request - method not allowed";
    public static final String EXISTED_USER = "User already exists";
    public static final String NON_EXISTENT_USER = "User does not exist!";
    public static final String TARGET_USER_IS_OWNER = "Target user is owner. No modifications could be carried out on owner of this world";
    public static final String INVALID_USER_NAME = "Invalid user name! User name should have at least 1 letter";

    // utils
    public static final String OWNER_NAME = System.getenv("OWNER_NAME");
    public static final String SUCCESS = "Success";
    public static final String COMMA = ", ";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String URL = "http://localhost:8000/";
    public static final String INVALID_PATTERN = "[^a-zA-Z]+";
}
