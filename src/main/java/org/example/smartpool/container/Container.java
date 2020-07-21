package org.example.smartpool.container;

import org.example.smartpool.PriceQuotation;
import org.example.smartpool.common.Pair;
import org.example.smartpool.utils.MemoryIOUtil;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Container to manipulate with object in non heap memory.
 *
 * @author Ivan Batura
 */
public final class Container {
    /**
     * Max capacity for pool container.
     */
    private final int maxCapacity;
    /**
     *  {@link AtomicInteger} for capacity for used elements.
     */
    private final AtomicInteger usedCapacity;
    /**
     * {@link AtomicInteger} for capacity for available elements.
     */
    private final AtomicInteger availableCapacity;
    /**
     * Storage for obtained object, key = object hashcode, value = pair of memory address and its size.
     */
    private final Map<Integer, Pair<Long, Integer>> obtained = new ConcurrentHashMap<>();

    /**
     * {@link Queue} with pair of memory address and its size for objects, that can be obtained.
     */
    private final Queue<Pair<Long, Integer>> available;

    public Container(final Integer maxsize) {
        available = new ArrayBlockingQueue<>(maxsize);
        maxCapacity = maxsize;
        availableCapacity = new AtomicInteger(0);
        usedCapacity = new AtomicInteger(0);
    }

    /**
     * Add element to container pool.
     *
     * @param quotation {@link PriceQuotation}
     *
     * @throws ContainerException raised while maxCapacity reached.
     */
    public synchronized void add(final PriceQuotation quotation) throws ContainerException {
        if (availableCapacity.get() == maxCapacity)
            throw new ContainerException("Max Capacity reached");
        availableCapacity.incrementAndGet();
        byte[] bytes = PriceQuotation.serialise(quotation);
        long start = MemoryIOUtil.write(bytes);
        available.add(new Pair<>(start, bytes.length));
    }

    /**
     * Get object and lock it.
     *
     * @return {@link PriceQuotation}
     *
     * @throws ContainerElementUnavailableException if
     */
    public synchronized PriceQuotation getAndLock() throws ContainerElementUnavailableException {
        if (availableCapacity.get() == 0)
            throw new ContainerElementUnavailableException("No available object in pool");

        final Pair<Long, Integer> p = available.poll();
        if (p == null)
            throw new ContainerElementUnavailableException("No available object in pool as address is null");
        final byte[] bytes = MemoryIOUtil.read(p.getKey(), p.getValue());
        final PriceQuotation result = PriceQuotation.deSerialize(bytes);
        availableCapacity.decrementAndGet();
        usedCapacity.incrementAndGet();
        obtained.put(result.hashCode(), p);
        return result;
    }

    /**
     * Verify object for provided hash locked.
     *
     * @param hash hashcode of object, that should be checked.
     *
     * @return true, if object lock, otherwise, false
     */
    public synchronized boolean isLocked(final Integer hash) {
        if (usedCapacity.get() == 0)
            return false;
        return obtained.get(hash) != null;
    }

    /**
     * Release and unlock object by its hashcode.
     *
     * @param hash hash hashcode of object, that should be released
     *
     * @throws ContainerException while object not found by provided hashcode
     */
    public synchronized void releaseAndUnlock(final Integer hash) throws ContainerException {
        if (usedCapacity.get() == 0)
            throw new ContainerException("No object to release");
        final Pair<Long, Integer> p = obtained.get(hash);
        if (p == null)
            throw new ContainerException("Provided object is incorrect");
        obtained.remove(hash);
        usedCapacity.decrementAndGet();
        availableCapacity.incrementAndGet();
        available.offer(p);
    }

    /**
     * Release all obtained objects, immediately.
     */
    public synchronized void releaseAllNow() {
        for (final Map.Entry<Integer, Pair<Long, Integer>> entry : obtained.entrySet()) {
            try {
                releaseAndUnlock(entry.getKey());
            } catch (final ContainerException e) {
                //skipp, as object may be already released.
            }
        }
    }

    /**
     * Get size of Available object for obtain.
     *
     * @return size
     */
    public int getAvailableSize() {
        return availableCapacity.get();
    }

    /**
     * Get size of obtained objects.
     *
     * @return size
     */
    public int getLockSize() {
        return usedCapacity.get();
    }

    /**
     * Total size of the objects in container.
     *
     * @return size
     */
    public synchronized int getSize() {
        return availableCapacity.get() + usedCapacity.get();
    }

    /**
     * Clean memory.
     */
    public synchronized void cleanup() {
        Pair<Long, Integer> p = available.poll();
        while (p != null) {
            MemoryIOUtil.free(p.getKey());
            p = available.poll();
        }
        for (final Pair<Long, Integer> pLock : obtained.values()) {
            MemoryIOUtil.free(pLock.getKey());
        }
    }

}
