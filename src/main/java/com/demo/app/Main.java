package com.demo.app;

/**
 * Main application class for the Java Demo App
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Java Demo Application ===");
        System.out.println("Welcome to the Java App Project!");
        System.out.println();
        
        // Demonstrate calculator functionality
        Calculator calculator = new Calculator();
        
        System.out.println("Calculator Demo:");
        int a = 10;
        int b = 5;
        
        System.out.println("Addition: " + a + " + " + b + " = " + calculator.add(a, b));
        System.out.println("Subtraction: " + a + " - " + b + " = " + calculator.subtract(a, b));
        System.out.println("Multiplication: " + a + " * " + b + " = " + calculator.multiply(a, b));
        System.out.println("Division: " + a + " / " + b + " = " + calculator.divide(a, b));
        System.out.println();
        
        // Demonstrate string utilities
        StringHelper helper = new StringHelper();
        String text = "Hello Java World";
        
        System.out.println("String Utilities Demo:");
        System.out.println("Original text: " + text);
        System.out.println("Reversed: " + helper.reverse(text));
        System.out.println("Uppercase: " + helper.toUpperCase(text));
        System.out.println("Lowercase: " + helper.toLowerCase(text));
        System.out.println("Word count: " + helper.countWords(text));
        
        System.out.println();
        System.out.println("Application completed successfully!");
    }
}
