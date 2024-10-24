package net.sfkao.majimabot.domain.exception;

public class InvalidPalabraException extends Exception{

    public InvalidPalabraException() {
    }

    public InvalidPalabraException(String message) {
        super(message);
    }

    public InvalidPalabraException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPalabraException(Throwable cause) {
        super(cause);
    }

    public InvalidPalabraException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
