package typecast.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Task class and its subclasses.
 */
public class TaskTest {
    
    private Todo todo;
    private Deadline deadline;
    private Event event;
    
    @BeforeEach
    public void setUp() {
        todo = new Todo("read book");
        deadline = new Deadline("return book", "2024-12-31 2359");
        event = new Event("project meeting", "2024-08-06 1400", "2024-08-06 1600");
    }
    
    @Test
    public void constructor_newTask_notDoneByDefault() {
        assertEquals(" ", todo.getStatus());
        assertEquals(" ", deadline.getStatus());
        assertEquals(" ", event.getStatus());
    }
    
    @Test
    public void getDescription_returnsCorrectDescription() {
        assertEquals("read book", todo.getDescription());
        assertEquals("return book", deadline.getDescription());
        assertEquals("project meeting", event.getDescription());
    }
    
    @Test
    public void markDone_marksTaskAsDone() {
        todo.markDone();
        assertEquals("X", todo.getStatus());
        
        deadline.markDone();
        assertEquals("X", deadline.getStatus());
    }
    
    @Test
    public void markNotDone_unmarksTask() {
        todo.markDone();
        assertEquals("X", todo.getStatus());
        
        todo.markNotDone();
        assertEquals(" ", todo.getStatus());
    }
    
    @Test
    public void markDone_multipleTimes_remainsDone() {
        todo.markDone();
        todo.markDone();
        assertEquals("X", todo.getStatus());
    }
    
    @Test
    public void toString_todo_correctFormat() {
        String expected = "[T][ ] read book";
        assertEquals(expected, todo.toString());
        
        todo.markDone();
        expected = "[T][X] read book";
        assertEquals(expected, todo.toString());
    }
    
    @Test
    public void toString_deadline_correctFormat() {
        String result = deadline.toString();
        assertTrue(result.startsWith("[D][ ] return book"));
        assertTrue(result.contains("(by:"));
        assertTrue(result.contains("Dec 31 2024"));
        
        deadline.markDone();
        result = deadline.toString();
        assertTrue(result.startsWith("[D][X] return book"));
    }
    
    @Test
    public void toString_event_correctFormat() {
        String result = event.toString();
        assertTrue(result.startsWith("[E][ ] project meeting"));
        assertTrue(result.contains("(from:"));
        assertTrue(result.contains("to:"));
        assertTrue(result.contains("Aug 06 2024"));
        
        event.markDone();
        result = event.toString();
        assertTrue(result.startsWith("[E][X] project meeting"));
    }
    
    @Test
    public void deadline_invalidDateFormat_throwsException() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Deadline("test", "invalid date"));
        assertThrows(IllegalArgumentException.class,
            () -> new Deadline("test", "2024-13-01"));  // Invalid month
    }
    
    @Test
    public void deadline_dateOnly_setsDefaultTime() {
        Deadline d = new Deadline("test", "2024-12-31");
        String result = d.toString();
        assertTrue(result.contains("11:59PM"));  // Default end-of-day time
    }
    
    @Test
    public void event_invalidDateFormat_throwsException() {
        assertThrows(IllegalArgumentException.class,
            () -> new Event("test", "invalid", "2024-12-31 1400"));
        assertThrows(IllegalArgumentException.class,
            () -> new Event("test", "2024-12-31 1400", "invalid"));
    }
    
    @Test
    public void event_dateOnly_setsDefaultTime() {
        Event e = new Event("test", "2024-12-31", "2025-01-01");
        String result = e.toString();
        assertTrue(result.contains("12:00AM"));  // Default start-of-day time
    }
}