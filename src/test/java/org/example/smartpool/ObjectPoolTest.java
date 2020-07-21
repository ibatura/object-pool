package org.example.smartpool;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Ivan Batura
 */
public class ObjectPoolTest {

    @Test
    public void test() {
        try {
            ObjectPool pool = new ObjectPool(3);
            pool.push(new PriceQuotation(11, 23, System.currentTimeMillis(), "asdsad"));
            pool.push(new PriceQuotation(111, 233, System.currentTimeMillis(), "bkj"));
            pool.push(new PriceQuotation(123, 253, System.currentTimeMillis(), "sd"));
            PriceQuotation p0 = pool.obtain();
            PriceQuotation p1 = pool.obtain();
            PriceQuotation p2 = pool.obtain();

            pool.release(p0);
            pool.release(p1);
            pool.release(p2);
            pool.tierDown();
        } catch (Exception e) {
            Assert.fail("Failed to obtain and release objects from pool");
        }
    }

    @Test
    public void releaseAll() throws ObjectPoolException, InterruptedException {
        ObjectPool pool = new ObjectPool(3);
        pool.push(new PriceQuotation(11, 23, System.currentTimeMillis(), "asdsad"));
        pool.push(new PriceQuotation(111, 233, System.currentTimeMillis(), "bkj"));
        pool.push(new PriceQuotation(123, 253, System.currentTimeMillis(), "sd"));
        PriceQuotation p0 = pool.obtain();
        PriceQuotation p1 = pool.obtain();
        PriceQuotation p2 = pool.obtain();

        pool.releaseAll(TimeUnit.SECONDS.toMillis(1));
        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        try {
            pool.release(p0);
            Assert.fail("Failed to obtain and release all objects from pool");
        } catch (ObjectPoolException e) {
        }
        pool.tierDown();

    }
}