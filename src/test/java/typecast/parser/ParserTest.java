package typecast.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import typecast.exception.TypeCastException;
import typecast.storage.Storage;
import typecast.task.TaskList;
import typecast.ui.Ui;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Parser class.
 */
public class ParserTest {
    
    private TaskList tasks;
    private Ui ui;
    private Storage storage;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    
    @BeforeEach
    public void setUp(@TempDir Path tempDir) {
        tasks = new TaskList();
        ui = new Ui();
        storage = new Storage(tempDir.resolve("test_tasks.txt").toString());
        System.setOut(new PrintStream(outputStreamCaptor));
    }
    
    @Test
    public void parseCommand_bye_returnsFalse() throws TypeCastException {
        boolean result = Parser.parseCommand("bye", tasks, ui, storage);
        assertFalse(result);
    }
    
    @Test
    public void parseCommand_list_returnsTrue() throws TypeCastException {
        boolean result = Parser.parseCommand("list", tasks, ui, storage);
        assertTrue(result);
    }
    
    @Test
    public void parseCommand_todo_addsTask() throws TypeCastException {
        assertEquals(0, tasks.size());
        
        Parser.parseCommand("todo read book", tasks, ui, storage);
        
        assertEquals(1, tasks.size());
        assertEquals("read book", tasks.get(0).getDescription());
    }
    
    @Test
    public void parseCommand_todoEmptyDescription_throwsException() {
        assertThrows(TypeCastException.class, 
            () -> Parser.parseCommand("todo", tasks, ui, storage));
        assertThrows(TypeCastException.class, 
            () -> Parser.parseCommand("todo ", tasks, ui, storage));
    }
    
    @Test
    public void parseCommand_deadline_addsTask() throws TypeCastException {
        assertEquals(0, tasks.size());
        
        Parser.parseCommand("deadline return book /by 2024-12-31 2359", tasks, ui, storage);
        
        assertEquals(1, tasks.size());
        assertEquals("return book", tasks.get(0).getDescription());
    }
    
    @Test
    public void parseCommand_deadlineNoBy_throwsException() {
        assertThrows(TypeCastException.class,
            () -> Parser.parseCommand("deadline return book", tasks, ui, storage));
    }
    
    @Test
    public void parseCommand_deadlineEmptyDescription_throwsException() {
        assertThrows(TypeCastException.class,
            () -> Parser.parseCommand("deadline /by 2024-12-31", tasks, ui, storage));
    }
    
    @Test
    public void parseCommand_deadlineInvalidDate_throwsException() {
        assertThrows(TypeCastException.class,
            () -> Parser.parseCommand("deadline return book /by tomorrow", tasks, ui, storage));
    }
    
    @Test
    public void parseCommand_event_addsTask() throws TypeCastException {
        assertEquals(0, tasks.size());
        
        Parser.parseCommand("event meeting /from 2024-12-31 1400 /to 2024-12-31 1600", 
            tasks, ui, storage);
        
        assertEquals(1, tasks.size());
        assertEquals("meeting", tasks.get(0).getDescription());
    }
    
    @Test
    public void parseCommand_eventNoFromTo_throwsException() {
        assertThrows(TypeCastException.class,
            () -> Parser.parseCommand("event meeting", tasks, ui, storage));
        assertThrows(TypeCastException.class,
            () -> Parser.parseCommand("event meeting /from 2pm", tasks, ui, storage));
    }
    
    @Test
    public void parseCommand_mark_marksTask() throws TypeCastException {
        tasks.add(new typecast.task.Todo("read book"));
        assertEquals(" ", tasks.get(0).getStatus());
        
        Parser.parseCommand("mark 1", tasks, ui, storage);
        
        assertEquals("X", tasks.get(0).getStatus());
    }
    
    @Test
    public void parseCommand_markInvalidIndex_throwsException() throws TypeCastException {
        tasks.add(new typecast.task.Todo("read book"));
        
        assertThrows(TypeCastException.class,
            () -> Parser.parseCommand("mark 2", tasks, ui, storage));
        assertThrows(TypeCastException.class,
            () -> Parser.parseCommand("mark 0", tasks, ui, storage));
    }
    
    @Test
    public void parseCommand_markInvalidNumber_throwsException() {
        assertThrows(TypeCastException.class,
            () -> Parser.parseCommand("mark abc", tasks, ui, storage));
    }
    
    @Test
    public void parseCommand_unmark_unmarksTask() throws TypeCastException {
        tasks.add(new typecast.task.Todo("read book"));
        tasks.markTaskDone(0);
        assertEquals("X", tasks.get(0).getStatus());
        
        Parser.parseCommand("unmark 1", tasks, ui, storage);
        
        assertEquals(" ", tasks.get(0).getStatus());
    }
    
    @Test
    public void parseCommand_delete_deletesTask() throws TypeCastException {
        tasks.add(new typecast.task.Todo("read book"));
        tasks.add(new typecast.task.Todo("buy groceries"));
        assertEquals(2, tasks.size());
        
        Parser.parseCommand("delete 1", tasks, ui, storage);
        
        assertEquals(1, tasks.size());
        assertEquals("buy groceries", tasks.get(0).getDescription());
    }
    
    @Test
    public void parseCommand_deleteInvalidIndex_throwsException() throws TypeCastException {
        tasks.add(new typecast.task.Todo("read book"));
        
        assertThrows(TypeCastException.class,
            () -> Parser.parseCommand("delete 5", tasks, ui, storage));
    }
    
    @Test
    public void parseCommand_invalidCommand_throwsException() {
        assertThrows(TypeCastException.class,
            () -> Parser.parseCommand("invalid command", tasks, ui, storage));
        assertThrows(TypeCastException.class,
            () -> Parser.parseCommand("xyz", tasks, ui, storage));
    }
}