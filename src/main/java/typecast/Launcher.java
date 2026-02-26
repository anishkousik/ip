package typecast;

import javafx.application.Application;

/**
 * A launcher class to workaround classpath issues with JavaFX.
 * Falls back to CLI mode if GUI is not available (headless environment).
 */
public class Launcher {
    public static void main(String[] args) {
        // Check if running in headless mode (no display available)
        if (isHeadless()) {
            // Run in CLI mode
            System.out.println("Running in CLI mode (no display detected)");
            new TypeCast("./data/tasks.txt").run();
        } else {
            // Try to run in GUI mode
            try {
                Application.launch(Main.class, args);
            } catch (Exception e) {
                // If GUI fails, fall back to CLI
                System.err.println("Failed to launch GUI: " + e.getMessage());
                System.out.println("Falling back to CLI mode...");
                new TypeCast("./data/tasks.txt").run();
            }
        }
    }
    
    /**
     * Checks if the application is running in a headless environment.
     * @return true if headless, false otherwise
     */
    private static boolean isHeadless() {
        // Check java.awt.headless property
        String headlessProperty = System.getProperty("java.awt.headless");
        if ("true".equals(headlessProperty)) {
            return true;
        }
        
        // Check for DISPLAY environment variable (Linux/Unix)
        String display = System.getenv("DISPLAY");
        if (display == null || display.isEmpty()) {
            // On Linux without DISPLAY, it's headless
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("linux") || os.contains("unix")) {
                return true;
            }
        }
        
        // Try to detect if GraphicsEnvironment is headless
        try {
            Class<?> geClass = Class.forName("java.awt.GraphicsEnvironment");
            java.lang.reflect.Method isHeadlessMethod = geClass.getMethod("isHeadless");
            Object result = isHeadlessMethod.invoke(null);
            return (Boolean) result;
        } catch (Exception e) {
            // If we can't determine, assume not headless
            return false;
        }
    }
}