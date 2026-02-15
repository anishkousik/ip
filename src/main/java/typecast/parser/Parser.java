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

    /**
     * Parses a user command and returns response string (for GUI).
     */
    public static String parseCommandForGui(String input, TaskList tasks, Storage storage) 
            throws TypeCastException {
        
        if (input.equals("bye")) {
            return "Bye. Hope to see you again soon!";
        } else if (input.equals("list")) {
            return formatTaskList(tasks);
        } else if (input.startsWith("find ")) {
            return handleFindForGui(input, tasks);
        } else if (input.startsWith("mark ")) {
            return handleMarkForGui(input, tasks, storage);
        } else if (input.startsWith("unmark ")) {
            return handleUnmarkForGui(input, tasks, storage);
        } else if (input.startsWith("delete ")) {
            return handleDeleteForGui(input, tasks, storage);
        } else if (input.startsWith("todo ")) {
            return handleTodoForGui(input, tasks, storage);
        } else if (input.startsWith("deadline ")) {
            return handleDeadlineForGui(input, tasks, storage);
        } else if (input.startsWith("event ")) {
            return handleEventForGui(input, tasks, storage);
        } else if (input.equals("todo") || input.equals("deadline") || input.equals("event")) {
            handleEmptyCommand(input);
            return "";
        } else {
            throw new TypeCastException("Sorry, that is not a valid command!");
        }
    }

    private static String handleFindForGui(String input, TaskList tasks) throws TypeCastException {
        String keyword = input.substring(5).trim();
        if (keyword.isEmpty()) {
            throw new TypeCastException("The search keyword cannot be empty.");
        }
        
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        
        if (matchingTasks.isEmpty()) {
            return "No matching tasks found.";
        }
        
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        for (int i = 0; i < matchingTasks.size(); i++) {
            sb.append((i + 1)).append(".").append(matchingTasks.get(i).toString()).append("\n");
        }
        return sb.toString().trim();
    }

    private static String formatTaskList(TaskList tasks) {
        if (tasks.size() == 0) {
            return "You have no tasks in your list.";
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1)).append(".").append(tasks.get(i).toString()).append("\n");
        }
        return sb.toString().trim();
    }

    private static String handleMarkForGui(String input, TaskList tasks, Storage storage) 
            throws TypeCastException {
        try {
            int taskIndex = Integer.parseInt(input.substring(5)) - 1;
            tasks.markTaskDone(taskIndex);
            storage.saveTasks(tasks.getTasks());
            return "Nice! I've marked this task as done:\n  " + tasks.get(taskIndex).toString();
        } catch (NumberFormatException e) {
            throw new TypeCastException("Invalid task number. Please enter a valid number.");
        } catch (IndexOutOfBoundsException e) {
            throw new TypeCastException("Task index out of range.");
        }
    }

    private static String handleUnmarkForGui(String input, TaskList tasks, Storage storage) 
            throws TypeCastException {
        try {
            int taskIndex = Integer.parseInt(input.substring(7)) - 1;
            tasks.markTaskNotDone(taskIndex);
            storage.saveTasks(tasks.getTasks());
            return "OK, I've marked this task as not done yet:\n  " + tasks.get(taskIndex).toString();
        } catch (NumberFormatException e) {
            throw new TypeCastException("Invalid task number. Please enter a valid number.");
        } catch (IndexOutOfBoundsException e) {
            throw new TypeCastException("Task index out of range.");
        }
    }

    private static String handleDeleteForGui(String input, TaskList tasks, Storage storage) 
            throws TypeCastException {
        try {
            int taskIndex = Integer.parseInt(input.substring(7)) - 1;
            Task removedTask = tasks.delete(taskIndex);
            storage.saveTasks(tasks.getTasks());
            return "Noted. I've removed this task:\n  " + removedTask.toString() + 
                   "\nNow you have " + tasks.size() + " tasks in the list.";
        } catch (NumberFormatException e) {
            throw new TypeCastException("Invalid task number. Please enter a valid number.");
        }
    }

    private static String handleTodoForGui(String input, TaskList tasks, Storage storage) 
            throws TypeCastException {
        String description = input.substring(5).trim();
        if (description.isEmpty()) {
            throw new TypeCastException("The description of a todo cannot be empty.");
        }
        Task task = new Todo(description);
        tasks.add(task);
        storage.saveTasks(tasks.getTasks());
        return "Got it. I've added this task:\n  " + task.toString() + 
               "\nNow you have " + tasks.size() + " tasks in the list.";
    }

    private static String handleDeadlineForGui(String input, TaskList tasks, Storage storage) 
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
            storage.saveTasks(tasks.getTasks());
            return "Got it. I've added this task:\n  " + task.toString() + 
                   "\nNow you have " + tasks.size() + " tasks in the list.";
        } catch (IllegalArgumentException e) {
            throw new TypeCastException(e.getMessage());
        }
    }

    private static String handleEventForGui(String input, TaskList tasks, Storage storage) 
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
            storage.saveTasks(tasks.getTasks());
            return "Got it. I've added this task:\n  " + task.toString() + 
                   "\nNow you have " + tasks.size() + " tasks in the list.";
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
        } else if (input.startsWith("find ")) {
            handleFind(input, tasks, ui);
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
        } else if (input.equals("todo") || input.equals("deadline") || input.equals("event")) {
            handleEmptyCommand(input);
        } else {
            throw new TypeCastException("Sorry, that is not a valid command!");
        }
        
        return true;
    }

    private static void handleFind(String input, TaskList tasks, Ui ui) throws TypeCastException {
        String keyword = input.substring(5).trim();
        if (keyword.isEmpty()) {
            throw new TypeCastException("The search keyword cannot be empty.");
        }
        
        ArrayList<Task> matchingTasks = tasks.findTasks(keyword);
        
        System.out.println("____________________________________________________________");
        if (matchingTasks.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println((i + 1) + "." + matchingTasks.get(i).toString());
            }
        }
        System.out.println("____________________________________________________________");
    }

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