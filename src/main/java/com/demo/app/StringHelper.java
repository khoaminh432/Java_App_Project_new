package com.demo.app;

/**
 * Utility class for string operations
 */
public class StringHelper {
    
    /**
     * Reverse a string
     */
    public String reverse(String input) {
        if (input == null) {
            return null;
        }
        return new StringBuilder(input).reverse().toString();
    }
    
    /**
     * Convert string to uppercase
     */
    public String toUpperCase(String input) {
        if (input == null) {
            return null;
        }
        return input.toUpperCase();
    }
    
    /**
     * Convert string to lowercase
     */
    public String toLowerCase(String input) {
        if (input == null) {
            return null;
        }
        return input.toLowerCase();
    }
    
    /**
     * Count words in a string
     */
    public int countWords(String input) {
        if (input == null || input.trim().isEmpty()) {
            return 0;
        }
        String[] words = input.trim().split("\\s+");
        return words.length;
    }
}
