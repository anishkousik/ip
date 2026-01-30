package typecast.task;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import typecast.exception.TypeCastException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the TaskList class.
 */
public class TaskListTest {
    
    private TaskList tasks;
    private Todo sampleTodo;
    private Deadline sampleDeadline;
    
    @BeforeEach
    public void setUp() {
        tasks = new TaskList();
        sampleTodo = new Todo("read book");
        sampleDeadline = new Deadline("return book", "2024-12-31 2359");
    }
    
    @Test
    public void add_singleTask_success() {
        tasks.add(sampleTodo);
        assertEquals(1, tasks.size());
        assertEquals(sampleTodo, tasks.get(0));
    }
    
    @Test
    public void add_multipleTasks_success() {
        tasks.add(sampleTodo);
        tasks.add(sampleDeadline);
        assertEquals(2, tasks.size());
        assertEquals(sampleTodo, tasks.get(0));
        assertEquals(sampleDeadline, tasks.get(1));
    }
    
    @Test
    public void delete_validIndex_success() throws TypeCastException {
        tasks.add(sampleTodo);
        tasks.add(sampleDeadline);
        
        Task deleted = tasks.delete(0);
        
        assertEquals(sampleTodo, deleted);
        assertEquals(1, tasks.size());
        assertEquals(sampleDeadline, tasks.get(0));
    }
    
    @Test
    public void delete_invalidIndex_throwsException() {
        tasks.add(sampleTodo);
        
        assertThrows(TypeCastException.class, () -> tasks.delete(1));
        assertThrows(TypeCastException.class, () -> tasks.delete(-1));
    }
    
    @Test
    public void delete_emptyList_throwsException() {
        assertThrows(TypeCastException.class, () -> tasks.delete(0));
    }
    
    @Test
    public void markTaskDone_validIndex_success() {
        tasks.add(sampleTodo);
        assertFalse(sampleTodo.getStatus().equals("X"));
        
        tasks.markTaskDone(0);
        
        assertEquals("X", sampleTodo.getStatus());
    }
    
    @Test
    public void markTaskDone_invalidIndex_throwsException() {
        tasks.add(sampleTodo);
        assertThrows(IndexOutOfBoundsException.class, () -> tasks.markTaskDone(1));
    }
    
    @Test
    public void markTaskNotDone_validIndex_success() {
        tasks.add(sampleTodo);
        tasks.markTaskDone(0);
        assertEquals("X", sampleTodo.getStatus());
        
        tasks.markTaskNotDone(0);
        
        assertEquals(" ", sampleTodo.getStatus());
    }
    
    @Test
    public void get_validIndex_returnsTask() {
        tasks.add(sampleTodo);
        assertEquals(sampleTodo, tasks.get(0));
    }
    
    @Test
    public void get_invalidIndex_throwsException() {
        tasks.add(sampleTodo);
        assertThrows(IndexOutOfBoundsException.class, () -> tasks.get(1));
    }
    
    @Test
    public void size_emptyList_returnsZero() {
        assertEquals(0, tasks.size());
    }
    
    @Test
    public void size_afterAddingTasks_returnsCorrectSize() {
        tasks.add(sampleTodo);
        assertEquals(1, tasks.size());
        
        tasks.add(sampleDeadline);
        assertEquals(2, tasks.size());
    }
    
    @Test
    public void size_afterDeletingTasks_returnsCorrectSize() throws TypeCastException {
        tasks.add(sampleTodo);
        tasks.add(sampleDeadline);
        assertEquals(2, tasks.size());
        
        tasks.delete(0);
        assertEquals(1, tasks.size());
    }
}