package org.example.smartpool;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Ivan Batura
 */
public class PriceQuotationTest {

    @Test
    public void serialiseTest() {
        final PriceQuotation priceQuotation = new PriceQuotation(1, 2, System.currentTimeMillis(), "test2123");
        byte[] b = PriceQuotation.serialise(priceQuotation);
        final PriceQuotation result = PriceQuotation.deSerialize(b);
        assertEquals("Serialistion/Deserialistion incorrect.", priceQuotation.getInstrumentName(), result.getInstrumentName());
        assertEquals("Serialistion/Deserialistion incorrect.", priceQuotation.getPricePurchase(), result.getPricePurchase(), 0.1);
        assertEquals("Serialistion/Deserialistion incorrect.", priceQuotation.getPriceSelling(), result.getPriceSelling(), 0.1);
        assertEquals("Serialistion/Deserialistion incorrect.", priceQuotation.getTimestamp(), result.getTimestamp());
    }
}