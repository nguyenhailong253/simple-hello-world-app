package exception;

public class NonExistentUserNameException extends RuntimeException {
    public NonExistentUserNameException(String msg) {
        super(msg);
    }
}
