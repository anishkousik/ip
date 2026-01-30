package typecast;
import typecast.exception.TypeCastException;
import typecast.parser.Parser;
import typecast.storage.Storage;
import typecast.task.TaskList;
import typecast.ui.Ui;

/**
 * Represents the main TypeCast task management application.
 * TypeCast allows users to manage tasks including todos, deadlines, and events.
 */
public class TypeCast {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Creates a TypeCast application instance with the specified file path.
     * Initializes the UI, storage, and loads existing tasks from the file.
     * If loading fails, starts with an empty task list.
     *
     * @param filePath The path to the file where tasks are stored.
     */
    public TypeCast(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (Exception e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Runs the main application loop.
     * Displays welcome message, loads tasks, processes user commands,
     * and handles exceptions until the user exits.
     */
    public void run() {
        ui.showWelcome();
        

        if (tasks.size() > 0) {
            ui.showTasksLoaded(tasks.size());
        }
        
        boolean isRunning = true;
        while (isRunning) {
            try {
                String input = ui.readCommand();
                isRunning = Parser.parseCommand(input, tasks, ui, storage);
            } catch (TypeCastException e) {
                ui.showError(e.getMessage());
            }
        }
        
        ui.showGoodbye();
        ui.close();
    }

    /**
     * Entry point of the TypeCast application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new TypeCast("./data/tasks.txt").run();
    }
}