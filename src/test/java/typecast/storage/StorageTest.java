package typecast.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import typecast.task.Deadline;
import typecast.task.Event;
import typecast.task.Task;
import typecast.task.Todo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Storage class.
 */
public class StorageTest {
    
    @TempDir
    Path tempDir;
    
    private Storage storage;
    private String testFilePath;
    
    @BeforeEach
    public void setUp() {
        testFilePath = tempDir.resolve("test_tasks.txt").toString();
        storage = new Storage(testFilePath);
    }
    
    @Test
    public void loadTasks_fileDoesNotExist_returnsEmptyList() {
        ArrayList<Task> tasks = storage.loadTasks();
        assertTrue(tasks.isEmpty());
    }
    
    @Test
    public void loadTasks_fileDoesNotExist_createsFile() {
        storage.loadTasks();
        File file = new File(testFilePath);
        assertTrue(file.exists());
    }
    
    @Test
    public void saveTasks_emptyList_createsEmptyFile() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        storage.saveTasks(tasks);
        
        File file = new File(testFilePath);
        assertTrue(file.exists());
        assertEquals(0, file.length());
    }
    
    @Test
    public void saveTasks_singleTodo_savesCorrectly() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("read book"));
        
        storage.saveTasks(tasks);
        ArrayList<Task> loadedTasks = storage.loadTasks();
        
        assertEquals(1, loadedTasks.size());
        assertEquals("read book", loadedTasks.get(0).getDescription());
        assertTrue(loadedTasks.get(0) instanceof Todo);
    }
    
    @Test
    public void saveTasks_markedTodo_preservesStatus() {
        ArrayList<Task> tasks = new ArrayList<>();
        Todo todo = new Todo("read book");
        todo.markDone();
        tasks.add(todo);
        
        storage.saveTasks(tasks);
        ArrayList<Task> loadedTasks = storage.loadTasks();
        
        assertEquals("X", loadedTasks.get(0).getStatus());
    }
    
    @Test
    public void saveTasks_deadline_savesCorrectly() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Deadline("return book", "2024-12-31 2359"));
        
        storage.saveTasks(tasks);
        ArrayList<Task> loadedTasks = storage.loadTasks();
        
        assertEquals(1, loadedTasks.size());
        assertEquals("return book", loadedTasks.get(0).getDescription());
        assertTrue(loadedTasks.get(0) instanceof Deadline);
    }
    
    @Test
    public void saveTasks_event_savesCorrectly() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Event("project meeting", "2024-08-06 1400", "2024-08-06 1600"));
        
        storage.saveTasks(tasks);
        ArrayList<Task> loadedTasks = storage.loadTasks();
        
        assertEquals(1, loadedTasks.size());
        assertEquals("project meeting", loadedTasks.get(0).getDescription());
        assertTrue(loadedTasks.get(0) instanceof Event);
    }
    
    @Test
    public void saveTasks_multipleTasks_savesAll() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("read book"));
        tasks.add(new Deadline("return book", "2024-12-31 2359"));
        tasks.add(new Event("meeting", "2024-08-06 1400", "2024-08-06 1600"));
        
        storage.saveTasks(tasks);
        ArrayList<Task> loadedTasks = storage.loadTasks();
        
        assertEquals(3, loadedTasks.size());
        assertTrue(loadedTasks.get(0) instanceof Todo);
        assertTrue(loadedTasks.get(1) instanceof Deadline);
        assertTrue(loadedTasks.get(2) instanceof Event);
    }
    
    @Test
    public void loadTasks_corruptedLine_skipsLine() throws IOException {
        // Create file with corrupted data
        FileWriter writer = new FileWriter(testFilePath);
        writer.write("T | 0 | read book\n");
        writer.write("CORRUPTED LINE\n");
        writer.write("T | 0 | buy groceries\n");
        writer.close();
        
        ArrayList<Task> tasks = storage.loadTasks();
        
        // Should load 2 valid tasks, skip corrupted line
        assertEquals(2, tasks.size());
        assertEquals("read book", tasks.get(0).getDescription());
        assertEquals("buy groceries", tasks.get(1).getDescription());
    }
    
    @Test
    public void loadTasks_emptyLines_skipsEmptyLines() throws IOException {
        // Create file with empty lines
        FileWriter writer = new FileWriter(testFilePath);
        writer.write("T | 0 | read book\n");
        writer.write("\n");
        writer.write("T | 0 | buy groceries\n");
        writer.write("\n");
        writer.close();
        
        ArrayList<Task> tasks = storage.loadTasks();
        
        assertEquals(2, tasks.size());
    }
    
    @Test
    public void loadTasks_invalidDateInDeadline_skipsTask() throws IOException {
        FileWriter writer = new FileWriter(testFilePath);
        writer.write("D | 0 | return book | invalid-date\n");
        writer.close();
        
        ArrayList<Task> tasks = storage.loadTasks();
        
        // Should skip the invalid task
        assertEquals(0, tasks.size());
    }
    
    @Test
    public void roundTrip_complexTasks_preservesData() {
        // Create tasks with various states
        ArrayList<Task> originalTasks = new ArrayList<>();
        
        Todo todo = new Todo("read book");
        todo.markDone();
        originalTasks.add(todo);
        
        Deadline deadline = new Deadline("return book", "2024-12-31 2359");
        originalTasks.add(deadline);
        
        Event event = new Event("meeting", "2024-08-06 1400", "2024-08-06 1600");
        event.markDone();
        originalTasks.add(event);
        
        // Save and load
        storage.saveTasks(originalTasks);
        ArrayList<Task> loadedTasks = storage.loadTasks();
        
        // Verify all tasks and their states
        assertEquals(3, loadedTasks.size());
        assertEquals("X", loadedTasks.get(0).getStatus());  // Todo was done
        assertEquals(" ", loadedTasks.get(1).getStatus());  // Deadline was not done
        assertEquals("X", loadedTasks.get(2).getStatus());  // Event was done
    }
}