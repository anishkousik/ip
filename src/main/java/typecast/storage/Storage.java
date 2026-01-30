package typecast.storage;

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

import typecast.exception.TypeCastException;
import typecast.task.Deadline;
import typecast.task.Event;
import typecast.task.Task;
import typecast.task.Todo;

/**
 * Handles loading and saving of tasks to persistent storage.
 * Tasks are stored in a text file with a specific format.
 */
public class Storage {

    private final String filePath;
    private static final DateTimeFormatter STORAGE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Constructs a Storage instance with the specified file path.
     *
     * @param filePath The path to the storage file.
     */
    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads tasks from the storage file.
     * Creates the file and parent directories if they don't exist.
     *
     * @return ArrayList of tasks loaded from the file.
     */
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

    /**
     * Parses a single line from the storage file into a Task object.
     *
     * @param line The line to parse.
     * @return The parsed Task object.
     * @throws TypeCastException If the line format is invalid.
     */
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
        default:
            throw new TypeCastException("Unknown task type: " + type);
        }

        if (status.equals("1")) {
            task.markDone();
        }

        return task;
    }

    /**
     * Saves the given list of tasks to the storage file.
     *
     * @param tasks The list of tasks to save.
     */
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

    /**
     * Formats a task into a string representation for storage.
     *
     * @param task The task to format.
     * @return The formatted string.
     */
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
            return "E | " + status + " | " + description + " | "
                    + event.getFromString() + " | " + event.getToString();
        }

        return "T | " + status + " | " + description;
    }
}