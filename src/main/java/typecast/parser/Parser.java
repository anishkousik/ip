package typecast.parser;

import typecast.exception.TypeCastException;
import typecast.storage.Storage;
import typecast.task.Deadline;
import typecast.task.Event;
import typecast.task.Task;
import typecast.task.TaskList;
import typecast.task.Todo;
import typecast.ui.Ui;

import java.util.ArrayList;

/**
 * Parses user input and executes the appropriate commands.
 */
public class Parser {

    private static final int INDEX_OFFSET = 1; // Convert 1-based user input to 0-based array index
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String DELIMITER_BY = " /by ";
    private static final String DELIMITER_FROM = " /from ";
    private static final String DELIMITER_TO = " /to ";

    /**
     * Parses a user command and returns response string (for GUI).
     */
    public static String parseCommandForGui(String input, TaskList tasks, Storage storage) 
            throws TypeCastException {
        
        if (input.equals("bye")) {
            return "Bye. Hope to see you again soon!";
        } else if (input.equals("list")) {
            return formatTaskList(tasks);
        } else if (input.startsWith("mark ")) {
            return handleMarkForGui(input, tasks, storage);
        } else if (input.startsWith("unmark ")) {
            return handleUnmarkForGui(input, tasks, storage);
        } else if (input.startsWith("delete ")) {
            return handleDeleteForGui(input, tasks, storage);
        } else if (input.startsWith(COMMAND_TODO + " ")) {
            return handleTodoForGui(input, tasks, storage);
        } else if (input.startsWith(COMMAND_DEADLINE + " ")) {
            return handleDeadlineForGui(input, tasks, storage);
        } else if (input.startsWith(COMMAND_EVENT + " ")) {
            return handleEventForGui(input, tasks, storage);
        } else if (isEmptyCommand(input)) {
            handleEmptyCommand(input);
            return "";
        } else {
            throw new TypeCastException("Sorry, that is not a valid command!");
        }
    }

    /**
     * Checks if the command is an empty task command.
     */
    private static boolean isEmptyCommand(String input) {
        return input.equals(COMMAND_TODO) || input.equals(COMMAND_DEADLINE) || input.equals(COMMAND_EVENT);
    }

    /**
     * Formats the task list as a string.
     */
    private static String formatTaskList(TaskList tasks) {
        if (tasks.size() == 0) {
            return "You have no tasks in your list.";
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append(".").append(tasks.get(i).toString()).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Extracts the task index from user input.
     */
    private static int extractTaskIndex(String input, int startPosition) throws TypeCastException {
        try {
            return Integer.parseInt(input.substring(startPosition)) - INDEX_OFFSET;
        } catch (NumberFormatException e) {
            throw new TypeCastException("Invalid task number. Please enter a valid number.");
        }
    }

    private static String handleMarkForGui(String input, TaskList tasks, Storage storage) 
            throws TypeCastException {
        try {
            int taskIndex = extractTaskIndex(input, 5);
            tasks.markTaskDone(taskIndex);
            storage.saveTasks(tasks.getTasks());
            return "Nice! I've marked this task as done:\n  " + tasks.get(taskIndex).toString();
        } catch (IndexOutOfBoundsException e) {
            throw new TypeCastException("Task index out of range.");
        }
    }

    private static String handleUnmarkForGui(String input, TaskList tasks, Storage storage) 
            throws TypeCastException {
        try {
            int taskIndex = extractTaskIndex(input, 7);
            tasks.markTaskNotDone(taskIndex);
            storage.saveTasks(tasks.getTasks());
            return "OK, I've marked this task as not done yet:\n  " + tasks.get(taskIndex).toString();
        } catch (IndexOutOfBoundsException e) {
            throw new TypeCastException("Task index out of range.");
        }
    }

    private static String handleDeleteForGui(String input, TaskList tasks, Storage storage) 
            throws TypeCastException {
        int taskIndex = extractTaskIndex(input, 7);
        Task removedTask = tasks.delete(taskIndex);
        storage.saveTasks(tasks.getTasks());
        return buildTaskRemovedMessage(removedTask, tasks.size());
    }

    private static String buildTaskRemovedMessage(Task task, int remainingTasks) {
        return "Noted. I've removed this task:\n  " + task.toString() + 
               "\nNow you have " + remainingTasks + " tasks in the list.";
    }

    private static String handleTodoForGui(String input, TaskList tasks, Storage storage) 
            throws TypeCastException {
        String description = extractDescription(input, COMMAND_TODO.length());
        validateNonEmpty(description, "todo");
        
        Task task = new Todo(description);
        tasks.add(task);
        storage.saveTasks(tasks.getTasks());
        return buildTaskAddedMessage(task, tasks.size());
    }

    private static String buildTaskAddedMessage(Task task, int totalTasks) {
        return "Got it. I've added this task:\n  " + task.toString() + 
               "\nNow you have " + totalTasks + " tasks in the list.";
    }

    private static String extractDescription(String input, int commandLength) {
        return input.substring(commandLength + 1).trim();
    }

    private static void validateNonEmpty(String text, String fieldName) throws TypeCastException {
        if (text.isEmpty()) {
            throw new TypeCastException("The " + fieldName + " cannot be empty.");
        }
    }

    private static String handleDeadlineForGui(String input, TaskList tasks, Storage storage) 
            throws TypeCastException {
        String rest = input.substring(COMMAND_DEADLINE.length() + 1);
        int byIndex = rest.indexOf(DELIMITER_BY);
        
        if (byIndex == -1) {
            throw new TypeCastException(
                "The format of deadline should be: deadline <description> /by <date/time>");
        }
        
        String description = rest.substring(0, byIndex).trim();
        String by = rest.substring(byIndex + DELIMITER_BY.length()).trim();
        
        validateNonEmpty(description, "description of a deadline");
        validateNonEmpty(by, "deadline date/time");
        
        try {
            Task task = new Deadline(description, by);
            tasks.add(task);
            storage.saveTasks(tasks.getTasks());
            return buildTaskAddedMessage(task, tasks.size());
        } catch (IllegalArgumentException e) {
            throw new TypeCastException(e.getMessage());
        }
    }

    private static String handleEventForGui(String input, TaskList tasks, Storage storage) 
            throws TypeCastException {
        String rest = input.substring(COMMAND_EVENT.length() + 1);
        int fromIndex = rest.indexOf(DELIMITER_FROM);
        int toIndex = rest.indexOf(DELIMITER_TO);
        
        if (fromIndex == -1 || toIndex == -1) {
            throw new TypeCastException(
                "The format of event should be: event <description> /from <start> /to <end>");
        }
        
        String description = rest.substring(0, fromIndex).trim();
        String from = rest.substring(fromIndex + DELIMITER_FROM.length(), toIndex).trim();
        String to = rest.substring(toIndex + DELIMITER_TO.length()).trim();
        
        validateNonEmpty(description, "description of an event");
        validateNonEmpty(from, "event start date/time");
        validateNonEmpty(to, "event end date/time");
        
        try {
            Task task = new Event(description, from, to);
            tasks.add(task);
            storage.saveTasks(tasks.getTasks());
            return buildTaskAddedMessage(task, tasks.size());
        } catch (IllegalArgumentException e) {
            throw new TypeCastException(e.getMessage());
        }
    }

    /**
     * Parses a user command and executes it (for CLI).
     */
    public static boolean parseCommand(String input, TaskList tasks, Ui ui, Storage storage) 
            throws TypeCastException {
        
        if (input.equals("bye")) {
            return false;
        } else if (input.equals("list")) {
            ui.showTaskList(tasks);
        } else if (input.startsWith("mark ")) {
            handleMark(input, tasks, ui, storage);
        } else if (input.startsWith("unmark ")) {
            handleUnmark(input, tasks, ui, storage);
        } else if (input.startsWith("delete ")) {
            handleDelete(input, tasks, ui, storage);
        } else if (input.startsWith(COMMAND_TODO + " ")) {
            handleTodo(input, tasks, ui, storage);
        } else if (input.startsWith(COMMAND_DEADLINE + " ")) {
            handleDeadline(input, tasks, ui, storage);
        } else if (input.startsWith(COMMAND_EVENT + " ")) {
            handleEvent(input, tasks, ui, storage);
        } else if (isEmptyCommand(input)) {
            handleEmptyCommand(input);
        } else {
            throw new TypeCastException("Sorry, that is not a valid command!");
        }
        
        return true;
    }

    private static void handleMark(String input, TaskList tasks, Ui ui, Storage storage) 
            throws TypeCastException {
        try {
            int taskIndex = extractTaskIndex(input, 5);
            tasks.markTaskDone(taskIndex);
            ui.showTaskMarked(tasks.get(taskIndex));
            storage.saveTasks(tasks.getTasks());
        } catch (IndexOutOfBoundsException e) {
            throw new TypeCastException("Task index out of range.");
        }
    }

    private static void handleUnmark(String input, TaskList tasks, Ui ui, Storage storage) 
            throws TypeCastException {
        try {
            int taskIndex = extractTaskIndex(input, 7);
            tasks.markTaskNotDone(taskIndex);
            ui.showTaskUnmarked(tasks.get(taskIndex));
            storage.saveTasks(tasks.getTasks());
        } catch (IndexOutOfBoundsException e) {
            throw new TypeCastException("Task index out of range.");
        }
    }

    private static void handleDelete(String input, TaskList tasks, Ui ui, Storage storage) 
            throws TypeCastException {
        int taskIndex = extractTaskIndex(input, 7);
        Task removedTask = tasks.delete(taskIndex);
        ui.showTaskDeleted(removedTask, tasks.size());
        storage.saveTasks(tasks.getTasks());
    }

    private static void handleTodo(String input, TaskList tasks, Ui ui, Storage storage) 
            throws TypeCastException {
        String description = extractDescription(input, COMMAND_TODO.length());
        validateNonEmpty(description, "todo");
        
        Task task = new Todo(description);
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
        storage.saveTasks(tasks.getTasks());
    }

    private static void handleDeadline(String input, TaskList tasks, Ui ui, Storage storage) 
            throws TypeCastException {
        String rest = input.substring(COMMAND_DEADLINE.length() + 1);
        int byIndex = rest.indexOf(DELIMITER_BY);
        
        if (byIndex == -1) {
            throw new TypeCastException(
                "The format of deadline should be: deadline <description> /by <date/time>");
        }
        
        String description = rest.substring(0, byIndex).trim();
        String by = rest.substring(byIndex + DELIMITER_BY.length()).trim();
        
        validateNonEmpty(description, "description of a deadline");
        validateNonEmpty(by, "deadline date/time");
        
        try {
            Task task = new Deadline(description, by);
            tasks.add(task);
            ui.showTaskAdded(task, tasks.size());
            storage.saveTasks(tasks.getTasks());
        } catch (IllegalArgumentException e) {
            throw new TypeCastException(e.getMessage());
        }
    }

    private static void handleEvent(String input, TaskList tasks, Ui ui, Storage storage) 
            throws TypeCastException {
        String rest = input.substring(COMMAND_EVENT.length() + 1);
        int fromIndex = rest.indexOf(DELIMITER_FROM);
        int toIndex = rest.indexOf(DELIMITER_TO);
        
        if (fromIndex == -1 || toIndex == -1) {
            throw new TypeCastException(
                "The format of event should be: event <description> /from <start> /to <end>");
        }
        
        String description = rest.substring(0, fromIndex).trim();
        String from = rest.substring(fromIndex + DELIMITER_FROM.length(), toIndex).trim();
        String to = rest.substring(toIndex + DELIMITER_TO.length()).trim();
        
        validateNonEmpty(description, "description of an event");
        validateNonEmpty(from, "event start date/time");
        validateNonEmpty(to, "event end date/time");
        
        try {
            Task task = new Event(description, from, to);
            tasks.add(task);
            ui.showTaskAdded(task, tasks.size());
            storage.saveTasks(tasks.getTasks());
        } catch (IllegalArgumentException e) {
            throw new TypeCastException(e.getMessage());
        }
    }

    private static void handleEmptyCommand(String input) throws TypeCastException {
        if (input.equals(COMMAND_TODO)) {
            throw new TypeCastException("The description of a todo cannot be empty.");
        } else if (input.equals(COMMAND_DEADLINE)) {
            throw new TypeCastException(
                "The format of deadline should be: deadline <description> /by <date/time>");
        } else {
            throw new TypeCastException(
                "The format of event should be: event <description> /from <start> /to <end>");
        }
    }
}