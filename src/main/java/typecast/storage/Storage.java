package typecast.storage;

import typecast.exception.TypeCastException;
import typecast.task.Deadline;
import typecast.task.Event;
import typecast.task.Task;
import typecast.task.Todo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading and saving tasks to/from a file.
 */
public class Storage {
    private final String filePath;
    private static final DateTimeFormatter STORAGE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    
    public Storage(String filePath) {
        assert filePath != null && !filePath.isEmpty() : "File path cannot be null or empty";
        this.filePath = filePath;
    }
    
    /**
     * Loads tasks from the file. Creates the file and directory if they don't exist.
     * @return ArrayList of tasks loaded from file
     */
    public ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        
        try {
            // Create directory if it doesn't exist
            Path path = Paths.get(filePath);
            Path parentDir = path.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            
            // Create file if it doesn't exist
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
                assert file.exists() : "File should exist after creation";
                return tasks; // Return empty list for new file
            }
            
            // Read and parse the file
            List<String> lines = Files.readAllLines(path);
            assert lines != null : "Lines should not be null";
            
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }
                
                try {
                    Task task = parseTask(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                } catch (Exception e) {
                    // Handle corrupted line - print warning but continue
                    System.out.println("Warning: Skipping corrupted line " + (i + 1) + ": " + line);
                    System.out.println("  Error: " + e.getMessage());
                }
            }
            
        } catch (IOException e) {
            System.out.println("Error loading tasks from file: " + e.getMessage());
        }
        
        assert tasks != null : "Task list should never be null";
        return tasks;
    }
    
    /**
     * Parses a single line from the file and creates the appropriate Task object.
     * Format: TYPE | STATUS | DESCRIPTION | [ADDITIONAL_INFO]
     */
    private Task parseTask(String line) throws TypeCastException {
        assert line != null && !line.isEmpty() : "Line cannot be null or empty";
        
        String[] parts = line.split(" \\| ");
        
        if (parts.length < 3) {
            throw new TypeCastException("Invalid task format: " + line);
        }
        
        String type = parts[0].trim();
        String status = parts[1].trim();
        String description = parts[2].trim();
        
        assert type != null && !type.isEmpty() : "Type cannot be empty";
        assert status.equals("0") || status.equals("1") : "Status must be 0 or 1";
        assert description != null && !description.isEmpty() : "Description cannot be empty";
        
        Task task = null;
        
        switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if (parts.length < 4) {
                    throw new TypeCastException("Invalid deadline format: missing deadline date");
                }
                String byStr = parts[3].trim();
                try {
                    LocalDateTime by = LocalDateTime.parse(byStr, STORAGE_FORMATTER);
                    task = new Deadline(description, by);
                } catch (Exception e) {
                    throw new TypeCastException("Invalid deadline date format: " + byStr);
                }
                break;
            case "E":
                if (parts.length < 5) {
                    throw new TypeCastException("Invalid event format: missing time range");
                }
                String fromStr = parts[3].trim();
                String toStr = parts[4].trim();
                try {
                    LocalDateTime from = LocalDateTime.parse(fromStr, STORAGE_FORMATTER);
                    LocalDateTime to = LocalDateTime.parse(toStr, STORAGE_FORMATTER);
                    task = new Event(description, from, to);
                } catch (Exception e) {
                    throw new TypeCastException("Invalid event date format");
                }
                break;
            default:
                throw new TypeCastException("Unknown task type: " + type);
        }
        
        assert task != null : "Task should be created";
        
        // Set the status
        if (status.equals("1")) {
            task.markDone();
        }
        
        return task;
    }
    
    /**
     * Saves all tasks to the file.
     * @param tasks The list of tasks to save
     */
    public void saveTasks(ArrayList<Task> tasks) {
        assert tasks != null : "Tasks cannot be null";
        
        try {
            // Create directory if it doesn't exist
            Path path = Paths.get(filePath);
            Path parentDir = path.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            
            // Write tasks to file
            FileWriter writer = new FileWriter(filePath);
            assert writer != null : "FileWriter should not be null";
            
            for (Task task : tasks) {
                assert task != null : "Task in list should not be null";
                writer.write(formatTask(task) + "\n");
            }
            writer.close();
            
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }
    
    /**
     * Formats a task into a string for saving to file.
     * Format: TYPE | STATUS | DESCRIPTION | [ADDITIONAL_INFO]
     */
    private String formatTask(Task task) {
        assert task != null : "Task cannot be null";
        
        String status = task.getStatus().equals("X") ? "1" : "0";
        String description = task.getDescription();
        
        assert description != null && !description.isEmpty() : "Description should not be empty";
        
        String formatted;
        if (task instanceof Todo) {
            formatted = "T | " + status + " | " + description;
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            formatted = "D | " + status + " | " + description + " | " + deadline.getByString();
        } else if (task instanceof Event) {
            Event event = (Event) task;
            formatted = "E | " + status + " | " + description + " | " + event.getFromString() + " | " + event.getToString();
        } else {
            formatted = "T | " + status + " | " + description; // Default to Todo
        }
        
        assert formatted != null && !formatted.isEmpty() : "Formatted string should not be empty";
        return formatted;
    }
}