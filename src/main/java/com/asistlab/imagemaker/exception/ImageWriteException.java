package com.asistlab.imagemaker.exception;

public class ImageWriteException extends FileWriteException {

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
