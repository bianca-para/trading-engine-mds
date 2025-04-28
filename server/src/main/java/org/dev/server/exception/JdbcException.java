package org.dev.server.exception;

public class JdbcException extends RuntimeException {
    public JdbcException(String message) {
        super(message);
    }
}
