package org.example.smartpool;

/**
 * Main exception for {@link ObjectPool}.
 *
 * @author Ivan Batura
 */
public class ObjectPoolException extends Exception {

    private static final long serialVersionUID = -1635139022453857467L;

    public ObjectPoolException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
