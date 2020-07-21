package org.example.smartpool;

import org.example.smartpool.container.Container;
import org.example.smartpool.container.ContainerElementUnavailableException;
import org.example.smartpool.container.ContainerException;

/**
 * Object pool.
 *
 * @author Ivan Batura
 */
public class ObjectPool {
    /**
     * {@link Container}.
     */
    private final Container container;

    public ObjectPool(int size) {
        this.container = new Container(size);
    }

    /**
     * Push element to object pool.
     *
     * @param o {@link PriceQuotation}
     *
     * @throws ObjectPoolException raised while container failed to add element
     */
    public void push(final PriceQuotation o) throws ObjectPoolException {
        if (o == null)
            return;
        try {
            container.add(o);
        } catch (final ContainerException e) {
            throw new ObjectPoolException("Failed to push object to the pool", e);
        }
    }

    /**
     * Obtain object from pool.
     *
     * @return {@link PriceQuotation}
     * @throws ObjectPoolException raised while container failed to obtain element
     */
    public PriceQuotation obtain() throws ObjectPoolException {
        try {
            return container.getAndLock();
        } catch (final ContainerElementUnavailableException e) {
            throw new ObjectPoolException("Failed to get object from pool", e);
        }
    }

    /**
     * Release one object to the pool.
     *
     * @param o {@link PriceQuotation}
     * @throws ObjectPoolException raised while container failed to release element
     */
    public void release(final PriceQuotation o) throws ObjectPoolException {
        try {
            container.releaseAndUnlock(o.hashCode());
        } catch (ContainerException e) {
            throw new ObjectPoolException("Failed to return object to pool", e);
        }
    }

    /**
     * Release all object and wait {@code timeout} they to be realesed by part that obtained them.
     * After timeout, release they, without wait.
     *
     * @param timeout timeout until all used element will be released
     */
    public void releaseAll(final long timeout) {
        new ReleaseWorker(container, timeout).start();
    }

    /**
     * Release all objects.
     */
    public void releaseAllNow() throws ObjectPoolException {
        container.releaseAllNow();
    }

    /**
     * Cleanup pool, clear memory.
     */
    public void tierDown() {
        container.cleanup();
    }

    /**
     * Worker in separate Thread to remove all object with provided timeout.
     * If timeout reached - release object with not wait they to be released.
     */
    private static class ReleaseWorker extends Thread {
        /**
         * {@link Container}.
         */
        private final Container container;
        /**
         * Timeout until all used element will be released.
         */
        private final long timeout;

        public ReleaseWorker(Container container, long timeout) {
            this.container = container;
            this.timeout = timeout;
        }

        @Override
        public void run() {
            synchronized (container) {
                if (container.getLockSize() == 0)
                    return;
                try {
                    Thread.sleep(timeout);
                } catch (final InterruptedException e) {
                    throw new RuntimeException("Release all failed", e);
                }
                if (container.getLockSize() == 0)
                    return;
                container.releaseAllNow();
            }
        }
    }
}
