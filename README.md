# Java_App_Project_new
A simple Java demonstration application showcasing basic functionality.

## Features

This Java application includes:
- **Calculator**: Basic arithmetic operations (add, subtract, multiply, divide)
- **String Utilities**: String manipulation functions (reverse, uppercase, lowercase, word count)

## Requirements

- Java 17 or higher
- Bash shell (for build and run scripts)

## Project Structure

```
Java_App_Project_new/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── demo/
│                   └── app/
│                       ├── Main.java          # Main application entry point
│                       ├── Calculator.java     # Calculator utility class
│                       └── StringHelper.java   # String utility class
├── build.sh                                    # Build script
├── run.sh                                      # Run script
└── README.md
```

## How to Build

To compile the application, run:

```bash
./build.sh
```

This will compile all Java source files and place the compiled classes in the `target/classes` directory.

## How to Run

To run the application, execute:

```bash
./run.sh
```

The script will automatically build the application if it hasn't been built yet.

## Expected Output

When you run the application, you should see:
- Calculator demonstrations showing basic arithmetic operations
- String utility demonstrations showing text manipulation

## License

See LICENSE file for details.
