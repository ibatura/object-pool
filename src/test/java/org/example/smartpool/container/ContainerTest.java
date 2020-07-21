package org.example.smartpool.container;

import org.example.smartpool.PriceQuotation;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * @author Ivan Batura
 */
public class ContainerTest {

    @Test
    public void add() throws ContainerException {
        final Container container = new Container(3);
        container.add(new PriceQuotation(11, 1, System.currentTimeMillis(), "1"));
        container.add(new PriceQuotation(12, 12, System.currentTimeMillis(), "12"));
        container.add(new PriceQuotation(12, 13, System.currentTimeMillis(), "13"));
        Assert.assertEquals("Container add failed", 3, container.getSize());
        Assert.assertEquals("Container incorrect Available size", 3, container.getAvailableSize());
        container.cleanup();
    }

    @Test
    public void addException() throws ContainerException {
        final Container container = new Container(3);
        container.add(new PriceQuotation(11, 1, System.currentTimeMillis(), "1"));
        container.add(new PriceQuotation(12, 12, System.currentTimeMillis(), "12"));
        container.add(new PriceQuotation(12, 13, System.currentTimeMillis(), "13"));
        try {
            container.add(new PriceQuotation(12, 13, System.currentTimeMillis(), "13"));
            Assert.fail("Exception for max capacity not throws");
        } catch (ContainerException e) {

        } finally {
            container.cleanup();
        }
    }

    @Test
    public void getAndLock() throws ContainerException {
        final Container container = new Container(3);
        container.add(new PriceQuotation(11, 1, System.currentTimeMillis(), "1"));
        container.add(new PriceQuotation(12, 12, System.currentTimeMillis(), "12"));
        container.add(new PriceQuotation(12, 13, System.currentTimeMillis(), "13"));
        container.getAndLock();
        Assert.assertEquals("Container incorrect Lock size", 1, container.getLockSize());
        Assert.assertEquals("Container incorrect Available size", 2, container.getAvailableSize());
        container.cleanup();
    }

    @Test
    public void isLocked() throws ContainerException {
        final Container container = new Container(3);
        container.add(new PriceQuotation(11, 1, System.currentTimeMillis(), "1"));
        container.add(new PriceQuotation(12, 12, System.currentTimeMillis(), "12"));
        container.add(new PriceQuotation(12, 13, System.currentTimeMillis(), "13"));
        final PriceQuotation o = container.getAndLock();
        Assert.assertTrue("Container incorrect Lock object", container.isLocked(o.hashCode()));
        container.cleanup();
    }

    @Test
    public void releaseAndUnlock() throws ContainerException {
        final Container container = new Container(3);
        container.add(new PriceQuotation(11, 1, System.currentTimeMillis(), "1"));
        container.add(new PriceQuotation(12, 12, System.currentTimeMillis(), "12"));
        container.add(new PriceQuotation(12, 13, System.currentTimeMillis(), "13"));
        final PriceQuotation o = container.getAndLock();
        container.releaseAndUnlock(o.hashCode());
        Assert.assertEquals("Container incorrect Lock size", 0, container.getLockSize());
        Assert.assertEquals("Container incorrect Available size", 3, container.getAvailableSize());
        assertFalse("Container incorrect Lock object", container.isLocked(o.hashCode()));
        container.cleanup();
    }

    @Test
    public void releaseAndUnlockException() throws ContainerException {
        final Container container = new Container(3);
        container.add(new PriceQuotation(11, 1, System.currentTimeMillis(), "1"));
        container.add(new PriceQuotation(12, 12, System.currentTimeMillis(), "12"));
        container.add(new PriceQuotation(12, 13, System.currentTimeMillis(), "13"));
        final PriceQuotation o = container.getAndLock();
        try {
            container.releaseAndUnlock(new PriceQuotation(12, 13, System.currentTimeMillis(), "13").hashCode());
            Assert.fail("Exception for Provided object is incorrect not throws");
        } catch (final ContainerException e) {
        } finally {
            container.cleanup();
        }

    }
}