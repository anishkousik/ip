
public class Parser {


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
        } else if (input.equals("todo") || input.equals("deadline") || input.equals("event")) {
            handleEmptyCommand(input);
        } else {
            throw new TypeCastException("Sorry, that is not a valid command!");
        }
        
        return true; // Continue running
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