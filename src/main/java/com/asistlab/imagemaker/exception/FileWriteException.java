package com.asistlab.imagemaker.exception;

public class FileWriteException extends IllegalStateException {
    public FileWriteException() {
    }

    public FileWriteException(String s) {
        super(s);
    }

    public FileWriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileWriteException(Throwable cause) {
        super(cause);
    }
}
