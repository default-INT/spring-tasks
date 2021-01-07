package by.gstu.springsecurity.exception;

public class ImageWriteException extends IllegalStateException {

    public ImageWriteException() {
    }

    public ImageWriteException(String s) {
        super(s);
    }

    public ImageWriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageWriteException(Throwable cause) {
        super(cause);
    }

}
