package exception;

public class ExistedUserNameException extends RuntimeException {
    public ExistedUserNameException(String msg) {
        super(msg);
    }
}
