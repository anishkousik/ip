package typecast.parser;

import java.util.ArrayList;

import typecast.exception.TypeCastException;
import typecast.storage.Storage;
import typecast.task.Deadline;
import typecast.task.Event;
import typecast.task.Task;
import typecast.task.TaskList;
import typecast.task.Todo;
import typecast.ui.Ui;

/**
 * Handles parsing and execution of user commands.
 * Supports commands: list, mark, unmark, delete, todo, deadline, event, find, and bye.
 */
public class Parser {

    /**
     * Parses and executes the given user command.
     *
     * @param input The user's command input.
     * @param tasks The task list to operate on.
     * @param ui The user interface for displaying messages.
     * @param storage The storage for persisting tasks.
     * @return true if the application should continue running, false if user wants to exit.
     * @throws TypeCastException If the command is invalid or execution fails.
     */
    public static boolean parseCommand(String input, TaskList tasks, Ui ui, Storage storage)
            throws TypeCastException {

        if (input.equals("bye")) {
            return false; // Signal to exit
        } else if (input.equals("list")) {
            ui.showTaskList(tasks);
        } else if (input.startsWith("mark ")) {
            handleMark(input, tasks, ui, storage);
        } else if (input.startsWith("unmark ")) {
            handleUnmark(input, tasks, ui, storage);
        } else if (input.startsWith("delete ")) {
            handleDelete(input, tasks, ui, storage);
        } else if (input.startsWith("todo ")) {
            handleTodo(input, tasks, ui, storage);
        } else if (input.startsWith("deadline ")) {
            handleDeadline(input, tasks, ui, storage);
        } else if (input.startsWith("event ")) {
            handleEvent(input, tasks, ui, storage);
        } else if (input.startsWith("find ")) {
            handleFind(input, tasks, ui);
        } else if (input.equals("todo") || input.equals("deadline") || input.equals("event")) {
            handleEmptyCommand(input);
        } else if (input.equals("find")) {
            throw new TypeCastException("Please provide a keyword to search for. Usage: find <keyword>");
        } else {
            throw new TypeCastException("Sorry, that is not a valid command!");
        }

        return true; // Continue running
    }

    /**
     * Handles the mark command to mark a task as done.
     *
     * @param input The user's input containing the task number.
     * @param tasks The task list to modify.
     * @param ui The user interface for displaying messages.
     * @param storage The storage for persisting changes.
     * @throws TypeCastException If the task number is invalid or out of range.
     */
    private static void handleMark(String input, TaskList tasks, Ui ui, Storage storage)
            throws TypeCastException {
        try {
            int taskIndex = Integer.parseInt(input.substring(5)) - 1;
            tasks.markTaskDone(taskIndex);
            ui.showTaskMarked(tasks.get(taskIndex));
            storage.saveTasks(tasks.getTasks());
        } catch (NumberFormatException e) {
            throw new TypeCastException("Invalid task number. Please enter a valid number.");
        } catch (IndexOutOfBoundsException e) {
            throw new TypeCastException("Task index out of range.");
        }
    }

    /**
     * Handles the unmark command to mark a task as not done.
     *
     * @param input The user's input containing the task number.
     * @param tasks The task list to modify.
     * @param ui The user interface for displaying messages.
     * @param storage The storage for persisting changes.
     * @throws TypeCastException If the task number is invalid or out of range.
     */
    private static void handleUnmark(String input, TaskList tasks, Ui ui, Storage storage)
            throws TypeCastException {
        try {
            int taskIndex = Integer.parseInt(input.substring(7)) - 1;
            tasks.markTaskNotDone(taskIndex);
            ui.showTaskUnmarked(tasks.get(taskIndex));
            storage.saveTasks(tasks.getTasks());
        } catch (NumberFormatException e) {
            throw new TypeCastException("Invalid task number. Please enter a valid number.");
        } catch (IndexOutOfBoundsException e) {
            throw new TypeCastException("Task index out of range.");
        }
    }

    /**
     * Handles the delete command to remove a task from the list.
     *
     * @param input The user's input containing the task number.
     * @param tasks The task list to modify.
     * @param ui The user interface for displaying messages.
     * @param storage The storage for persisting changes.
     * @throws TypeCastException If the task number is invalid.
     */
    private static void handleDelete(String input, TaskList tasks, Ui ui, Storage storage)
            throws TypeCastException {
        try {
            int taskIndex = Integer.parseInt(input.substring(7)) - 1;
            Task removedTask = tasks.delete(taskIndex);
            ui.showTaskDeleted(removedTask, tasks.size());
            storage.saveTasks(tasks.getTasks());
        } catch (NumberFormatException e) {
            throw new TypeCastException("Invalid task number. Please enter a valid number.");
        }
    }

    /**
     * Handles the todo command to create a new todo task.
     *
     * @param input The user's input containing the task description.
     * @param tasks The task list to add the todo to.
     * @param ui The user interface for displaying messages.
     * @param storage The storage for persisting changes.
     * @throws TypeCastException If the description is empty.
     */
    private static void handleTodo(String input, TaskList tasks, Ui ui, Storage storage)
            throws TypeCastException {
        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            throw new TypeCastException("The description of a todo cannot be empty.");
        }
        Task task = new Todo(description);
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
        storage.saveTasks(tasks.getTasks());
    }

    /**
     * Handles the deadline command to create a new deadline task.
     *
     * @param input The user's input containing the task description and deadline.
     * @param tasks The task list to add the deadline to.
     * @param ui The user interface for displaying messages.
     * @param storage The storage for persisting changes.
     * @throws TypeCastException If the format is invalid or fields are empty.
     */
    private static void handleDeadline(String input, TaskList tasks, Ui ui, Storage storage)
            throws TypeCastException {
        String rest = input.substring(9);
        int byIndex = rest.indexOf(" /by ");
        if (byIndex == -1) {
            throw new TypeCastException(
                    "The format of deadline should be: deadline <description> /by <date/time>");
        }
        String description = rest.substring(0, byIndex).trim();
        String by = rest.substring(byIndex + 5).trim();
        if (description.isEmpty()) {
            throw new TypeCastException("The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new TypeCastException("The deadline date/time cannot be empty.");
        }
        try {
            Task task = new Deadline(description, by);
            tasks.add(task);
            ui.showTaskAdded(task, tasks.size());
            storage.saveTasks(tasks.getTasks());
        } catch (IllegalArgumentException e) {
            throw new TypeCastException(e.getMessage());
        }
    }

    /**
     * Handles the event command to create a new event task.
     *
     * @param input The user's input containing the task description and time range.
     * @param tasks The task list to add the event to.
     * @param ui The user interface for displaying messages.
     * @param storage The storage for persisting changes.
     * @throws TypeCastException If the format is invalid or fields are empty.
     */
    private static void handleEvent(String input, TaskList tasks, Ui ui, Storage storage)
            throws TypeCastException {
        String rest = input.substring(6);
        int fromIndex = rest.indexOf(" /from ");
        int toIndex = rest.indexOf(" /to ");
        if (fromIndex == -1 || toIndex == -1) {
            throw new TypeCastException(
                    "The format of event should be: event <description> /from <start> /to <end>");
        }
        String description = rest.substring(0, fromIndex).trim();
        String from = rest.substring(fromIndex + 7, toIndex).trim();
        String to = rest.substring(toIndex + 5).trim();
        if (description.isEmpty()) {
            throw new TypeCastException("The description of an event cannot be empty.");
        }
        if (from.isEmpty()) {
            throw new TypeCastException("The event start date/time cannot be empty.");
        }
        if (to.isEmpty()) {
            throw new TypeCastException("The event end date/time cannot be empty.");
        }
        try {
            Task task = new Event(description, from, to);
            tasks.add(task);
            ui.showTaskAdded(task, tasks.size());
            storage.saveTasks(tasks.getTasks());
        } catch (IllegalArgumentException e) {
            throw new TypeCastException(e.getMessage());
        }
    }

    /**
     * Handles the find command to search for tasks by keyword.
     *
     * @param input The user's input containing the search keyword.
     * @param tasks The task list to search.
     * @param ui The user interface for displaying results.
     * @throws TypeCastException If the keyword is empty.
     */
    private static void handleFind(String input, TaskList tasks, Ui ui) throws TypeCastException {
        String keyword = input.substring(5).trim();
        if (keyword.isEmpty()) {
            throw new TypeCastException("Please provide a keyword to search for. Usage: find <keyword>");
        }
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        ui.showMatchingTasks(matchingTasks);
    }

    /**
     * Handles empty task commands (todo, deadline, event without arguments).
     *
     * @param input The command that was entered without arguments.
     * @throws TypeCastException Always throws an exception with appropriate error message.
     */
    private static void handleEmptyCommand(String input) throws TypeCastException {
        if (input.equals("todo")) {
            throw new TypeCastException("The description of a todo cannot be empty.");
        } else if (input.equals("deadline")) {
            throw new TypeCastException(
                    "The format of deadline should be: deadline <description> /by <date/time>");
        } else {
            throw new TypeCastException(
                    "The format of event should be: event <description> /from <start> /to <end>");
        }
    }
}