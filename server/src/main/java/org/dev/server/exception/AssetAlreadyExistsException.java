package org.dev.server.exception;

public class AssetAlreadyExistsException extends RuntimeException {
    public AssetAlreadyExistsException(String message) {
        super(message);
    }
}
