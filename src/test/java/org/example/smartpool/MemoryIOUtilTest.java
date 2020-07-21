package org.example.smartpool;

import org.example.smartpool.utils.MemoryIOUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Ivan Batura
 */
public class MemoryIOUtilTest {

    @Test
    public void writeReadTest() {
        byte[] test = new byte[3];
        test[0] = 11;
        test[1] = 12;
        test[2] = 13;
        long start = MemoryIOUtil.write(test);

        byte[] result = MemoryIOUtil.read(start, test.length);

        Assert.assertArrayEquals("Write and read ate not correct.", test, result);
    }

}