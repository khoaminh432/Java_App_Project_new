# Running JavaFX Application in Dev Container

## Problem
JavaFX applications require a graphical display, which is not available by default in dev containers. This causes the error:
```
Unable to open DISPLAY
```

## Solutions

### Option 1: Run with Virtual Display (Recommended for Development)

Use the provided script to run your JavaFX application with a virtual display:

```bash
./run-javafx.sh
```

This script will:
1. Install Xvfb if not present (virtual framebuffer X server)
2. Start a virtual display on :99
3. Set JAVA_HOME to Java 21
4. Run your JavaFX application

### Option 2: Manual Setup

If you prefer to run commands manually:

```bash
# Install Xvfb (one-time setup)
sudo apt-get update && sudo apt-get install -y xvfb

# Start virtual display
export DISPLAY=:99
Xvfb :99 -screen 0 1024x768x24 &

# Set Java 21
export JAVA_HOME=/usr/local/sdkman/candidates/java/21.0.9-ms

# Run application
mvn javafx:run
```

### Option 3: Use X11 Forwarding from Host (For Visual Testing)

If you want to see the actual GUI on your host machine:

1. **On Windows**: Install VcXsrv or Xming
2. **On Mac**: Install XQuartz
3. **On Linux**: X11 is usually available

Then in your dev container:
```bash
export DISPLAY=host.docker.internal:0
mvn javafx:run
```

## Troubleshooting

### Xvfb already running
If you see "Server is already active for display 99":
```bash
rm /tmp/.X99-lock
killall Xvfb
```

Then restart the script.

### Connection issues
Make sure your display variable is set:
```bash
echo $DISPLAY
```

Should output `:99` when using virtual display.

## Java 21 Upgrade Notes

The application has been successfully upgraded to Java 21 LTS with:
- ✅ Java runtime version 21
- ✅ JavaFX version 21
- ✅ All FXML files updated to JavaFX 21
- ✅ All tests passing
- ✅ No CVE vulnerabilities found
