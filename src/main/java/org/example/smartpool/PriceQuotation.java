package org.example.smartpool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * PriceQuotation use for pool object.
 *
 * @author Ivan Batura
 */
public final class PriceQuotation {
    /**
     * Purchase price;
     */
    private final double pricePurchase;
    /**
     * Selling price;
     */
    private final double priceSelling;
    /**
     * date and time.
     */
    private final long timestamp;
    /**
     * name of the trading instrument.
     */
    private final String instrumentName;

    public PriceQuotation(double pricePurchase, double priceSelling, long timestamp, String instrumentName) {
        this.pricePurchase = pricePurchase;
        this.priceSelling = priceSelling;
        this.timestamp = timestamp;
        this.instrumentName = instrumentName;
    }

    public double getPricePurchase() {
        return pricePurchase;
    }

    public double getPriceSelling() {
        return priceSelling;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append("[pricePurchase=").append(pricePurchase);
        sb.append(", priceSelling=").append(priceSelling);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", instrumentName=").append(instrumentName);
        sb.append(']');
        return sb.toString();
    }

    /**
     * Read {@link PriceQuotation} from incoming {@link byte[]}.
     *
     * @param bytes
     *        {@link byte[]} source
     * @return {@link PriceQuotation}
     */
    public static PriceQuotation deSerialize(final byte[] bytes) {
        try (DataInputStream dataInput = new DataInputStream(new ByteArrayInputStream(bytes))) {
            final double pricePurchase = dataInput.readDouble();
            final double priceSelling = dataInput.readDouble();
            final long timestamp = dataInput.readLong();
            final String instrumentName = dataInput.readUTF();
            return new PriceQuotation(pricePurchase, priceSelling, timestamp, instrumentName);
        } catch (final IOException e) {
            throw new RuntimeException("Exception while deserialization", e);
        }
    }

    /**
     * Perform {@link PriceQuotation}
     *
     * @param element
     *        {@link PriceQuotation}
     * @return {@link byte[]}
     */
    public static byte[] serialise(final PriceQuotation element) {
        final ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try (DataOutputStream dataOutput = new DataOutputStream(bout)) {
            dataOutput.writeDouble(element.getPricePurchase());
            dataOutput.writeDouble(element.getPriceSelling());
            dataOutput.writeLong(element.getTimestamp());
            dataOutput.writeUTF(element.getInstrumentName());
            return bout.toByteArray();
        } catch (final IOException e) {
            throw new RuntimeException("Exception while serialization", e);
        }
    }
}
