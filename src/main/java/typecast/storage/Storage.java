package typecast.storage;

import typecast.exception.TypeCastException;
import typecast.task.Deadline;
import typecast.task.Event;
import typecast.task.Period;
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
        this.filePath = filePath;
    }
    
    public ArrayList<Task> loadTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        
        try {
            Path path = Paths.get(filePath);
            Path parentDir = path.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
                return tasks;
            }
            
            List<String> lines = Files.readAllLines(path);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) {
                    continue;
                }
                
                try {
                    Task task = parseTask(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                } catch (Exception e) {
                    System.out.println("Warning: Skipping corrupted line " + (i + 1) + ": " + line);
                    System.out.println("  Error: " + e.getMessage());
                }
            }
            
        } catch (IOException e) {
            System.out.println("Error loading tasks from file: " + e.getMessage());
        }
        
        return tasks;
    }
    
    private Task parseTask(String line) throws TypeCastException {
        String[] parts = line.split(" \\| ");
        
        if (parts.length < 3) {
            throw new TypeCastException("Invalid task format: " + line);
        }
        
        String type = parts[0].trim();
        String status = parts[1].trim();
        String description = parts[2].trim();
        
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
            case "P":
                if (parts.length < 5) {
                    throw new TypeCastException("Invalid period format: missing date range");
                }
                String startStr = parts[3].trim();
                String endStr = parts[4].trim();
                try {
                    LocalDateTime start = LocalDateTime.parse(startStr, STORAGE_FORMATTER);
                    LocalDateTime end = LocalDateTime.parse(endStr, STORAGE_FORMATTER);
                    task = new Period(description, start, end);
                } catch (Exception e) {
                    throw new TypeCastException("Invalid period date format");
                }
                break;
            default:
                throw new TypeCastException("Unknown task type: " + type);
        }
        
        if (status.equals("1")) {
            task.markDone();
        }
        
        return task;
    }
    
    public void saveTasks(ArrayList<Task> tasks) {
        try {
            Path path = Paths.get(filePath);
            Path parentDir = path.getParent();
            if (parentDir != null && !Files.exists(parentDir)) {
                Files.createDirectories(parentDir);
            }
            
            FileWriter writer = new FileWriter(filePath);
            for (Task task : tasks) {
                writer.write(formatTask(task) + "\n");
            }
            writer.close();
            
        } catch (IOException e) {
            System.out.println("Error saving tasks to file: " + e.getMessage());
        }
    }
    
    private String formatTask(Task task) {
        String status = task.getStatus().equals("X") ? "1" : "0";
        String description = task.getDescription();
        
        if (task instanceof Todo) {
            return "T | " + status + " | " + description;
        } else if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return "D | " + status + " | " + description + " | " + deadline.getByString();
        } else if (task instanceof Event) {
            Event event = (Event) task;
            return "E | " + status + " | " + description + " | " + event.getFromString() + " | " + event.getToString();
        } else if (task instanceof Period) {
            Period period = (Period) task;
            return "P | " + status + " | " + description + " | " + period.getStartDateString() + " | " + period.getEndDateString();
        }
        
        return "T | " + status + " | " + description;
    }
}