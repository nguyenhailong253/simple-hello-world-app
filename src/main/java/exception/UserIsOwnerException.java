package exception;

public class UserIsOwnerException extends RuntimeException {
    public UserIsOwnerException(String msg) {
        super(msg);
    }
}
