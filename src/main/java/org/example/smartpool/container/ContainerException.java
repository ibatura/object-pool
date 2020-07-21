package org.example.smartpool.container;

/**
 * Main exception for {@link Container}.
 *
 * @author Ivan Batura
 */
public class ContainerException extends Exception {

    private static final long serialVersionUID = 8418057307228409874L;

    public ContainerException(final String message) {
        super(message);
    }
}
