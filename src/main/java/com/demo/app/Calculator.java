package com.demo.app;

/**
 * Simple calculator class with basic arithmetic operations
 */
public class Calculator {
    
    /**
     * Add two integers
     */
    public int add(int a, int b) {
        return a + b;
    }
    
    /**
     * Subtract b from a
     */
    public int subtract(int a, int b) {
        return a - b;
    }
    
    /**
     * Multiply two integers
     */
    public int multiply(int a, int b) {
        return a * b;
    }
    
    /**
     * Divide a by b
     */
    public double divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return (double) a / b;
    }
}
