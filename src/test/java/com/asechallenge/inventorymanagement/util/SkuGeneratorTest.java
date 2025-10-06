package com.asechallenge.inventorymanagement.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SkuGeneratorTest {

    @Test
    void generateSKU_withVariant_shouldReturnCombinedCamelCaseSKU() {
        String result = SkuGenerator.generateSKU("Dell XPS", "13 inch");
        assertEquals("dellxps13inch", result);
    }

    @Test
    void generateSKU_withoutVariant_shouldReturnBaseOnly() {
        String result = SkuGenerator.generateSKU("Mouse", null);
        assertEquals("mouse", result);
    }
}