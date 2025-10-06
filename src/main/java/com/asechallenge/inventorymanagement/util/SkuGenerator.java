package com.asechallenge.inventorymanagement.util;

public class SkuGenerator {

    private SkuGenerator() {
    }

   public static String generateSKU(String name, String variant) {
        String base = name.replaceAll("\\s+", "").toLowerCase().trim();

        if (variant == null || variant.isBlank()) {
            return base;
        }

        // Remove spaces in variant
        String cleanedVariant = variant.replaceAll("\\s+", "").trim();

        // Uppercase first letter, lowercase rest
        String camelVariant = cleanedVariant.substring(0, 1).toUpperCase() +
                            cleanedVariant.substring(1).toLowerCase();

        return base + camelVariant;
    }
}