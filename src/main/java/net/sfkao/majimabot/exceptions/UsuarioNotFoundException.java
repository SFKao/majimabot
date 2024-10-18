package net.sfkao.majimabot.exceptions;

public class UsuarioNotFoundException extends Exception {

    public UsuarioNotFoundException(String message) {
        super(message);
    }

    public UsuarioNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
