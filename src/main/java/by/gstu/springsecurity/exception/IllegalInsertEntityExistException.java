package by.gstu.springsecurity.exception;

public class IllegalInsertEntityExistException extends RuntimeException {
    public IllegalInsertEntityExistException() {
    }

    public IllegalInsertEntityExistException(String message) {
        super(message);
    }

    public IllegalInsertEntityExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalInsertEntityExistException(Throwable cause) {
        super(cause);
    }
}
