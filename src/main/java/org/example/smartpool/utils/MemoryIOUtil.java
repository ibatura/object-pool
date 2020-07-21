package org.example.smartpool.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Utils to work with non heap memory.
 *
 * @author Ivan Batura
 */
public final class MemoryIOUtil {
    private static final Unsafe unsafe = getUnsafe();

    private MemoryIOUtil() {
    }

    /**
     * Store array of bytes in memory.
     *
     * @param toWrite array of bytes to write
     * @return memory address
     */
    public static long write(final byte[] toWrite) {
        final long start = unsafe.allocateMemory(toWrite.length);
        for (int i = 0; i < toWrite.length; i++)
            unsafe.putByte(start + ((long) i), toWrite[i]);
        return start;
    }

    /**
     * Retrieve array of bytes from memory.
     * 
     * @param start start address
     * @param size size of the whole block
     * @return array of read bytes
     */
    public static byte[] read(final long start, final int size) {
        final byte[] result = new byte[size];
        for (int i = 0; i < size; i++)
            result[i] = unsafe.getByte(start + ((long) i));
        return result;
    }

    public static void free(final long start) {
        unsafe.freeMemory(start);
    }

    /**
     * Get {@link Unsafe}.
     * @return {@link Unsafe}
     */
    private static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (final Exception e) {
            throw new RuntimeException("Unsafe get failed", e);
        }
    }
}
