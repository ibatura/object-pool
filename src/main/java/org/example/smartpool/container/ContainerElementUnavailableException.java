package org.example.smartpool.container;

/**
 *  Element Unavailable in pool exception.
 *
 * @author Ivan Batura
 */
public class ContainerElementUnavailableException extends ContainerException {
    private static final long serialVersionUID = 238429478684064097L;

    public ContainerElementUnavailableException(final String message) {
        super(message);
    }
}
