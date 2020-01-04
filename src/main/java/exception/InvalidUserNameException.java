package exception;

public class InvalidUserNameException extends RuntimeException {
    public InvalidUserNameException(String msg) {
        super(msg);
    }
}
