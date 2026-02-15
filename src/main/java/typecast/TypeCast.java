package typecast;

import typecast.exception.TypeCastException;
import typecast.parser.Parser;
import typecast.storage.Storage;
import typecast.task.TaskList;
import typecast.ui.Ui;

/**
 * TypeCast is a chatbot that helps users manage their tasks.
 * It supports three types of tasks: Todos, Deadlines, and Events.
 */
public class TypeCast {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a TypeCast chatbot with the specified file path for storage.
     * @param filePath The path to the data file
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
     * Gets the response for a user command (for GUI).
     * @param input The user's command
     * @return The response message
     */
    public String getResponse(String input) {
        try {
            return Parser.parseCommandForGui(input, tasks, storage);
        } catch (TypeCastException e) {
            return e.getMessage();
        }
    }

    /**
     * Gets the task list.
     * @return The task list
     */
    public TaskList getTaskList() {
        return tasks;
    }

    /**
     * Runs the main chatbot loop (CLI mode).
     */
    public void run() {
        ui.showWelcome();
        
        // Show how many tasks were loaded
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
     * Main entry point for the TypeCast chatbot (CLI mode).
     */
    public static void main(String[] args) {
        new TypeCast("./data/tasks.txt").run();
    }
}