package typecast.ui;
import java.util.Scanner;
import typecast.task.Task;
import typecast.task.TaskList;

/**
 * Handles user interface interactions.
 * Responsible for displaying messages and reading user input.
 */
public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner scanner;

    /**
     * Creates a new Ui instance and initializes the scanner for reading input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message with the TypeCast logo.
     */
    public void showWelcome() {
        String logo =
          " _____                   ____          _   \n"
        + "|_   _|   _ _ __   ___  / ___|__ _ ___| |_ \n"
        + "  | || | | | '_ \\ / _ \\| |   / _` / __| __|\n"
        + "  | || |_| | |_) |  __/| |__| (_| \\__ \\ |_ \n"
        + "  |_| \\__, | .__/ \\___| \\____\\__,_|___/\\__|\n"
        + "      |___/|_|\n";

        System.out.println(logo + "\n");
        System.out.println(LINE);
        System.out.println("Hello! I'm TypeCast");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Displays the goodbye message when the user exits.
     */
    public void showGoodbye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Displays an error message when tasks fail to load from file.
     */
    public void showLoadingError() {
        System.out.println(LINE);
        System.out.println("Error loading tasks from file. Starting with an empty task list.");
        System.out.println(LINE);
    }

    /**
     * Displays a message indicating how many tasks were loaded.
     *
     * @param count The number of tasks loaded from storage.
     */
    public void showTasksLoaded(int count) {
        System.out.println("Loaded " + count + " task(s) from previous session.");
        System.out.println(LINE);
    }

    /**
     * Displays an error message.
     *
     * @param message The error message to display.
     */
    public void showError(String message) {
        System.out.println(LINE);
        System.out.println(" " + message);
        System.out.println(LINE);
    }

    /**
     * Displays a general message.
     *
     * @param message The message to display.
     */
    public void showMessage(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

    /**
     * Displays the list of all tasks.
     *
     * @param tasks The TaskList containing all tasks to display.
     */
    public void showTaskList(TaskList tasks) {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i).toString());
        }
        System.out.println(LINE);
    }

    /**
     * Displays a confirmation message when a task is marked as done.
     *
     * @param task The task that was marked as done.
     */
    public void showTaskMarked(Task task) {
        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task.toString());
        System.out.println(LINE);
    }

    /**
     * Displays a confirmation message when a task is marked as not done.
     *
     * @param task The task that was marked as not done.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println(LINE);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task.toString());
        System.out.println(LINE);
    }

    /**
     * Displays a confirmation message when a task is added.
     *
     * @param task The task that was added.
     * @param totalTasks The total number of tasks after adding.
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task.toString());
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Displays a confirmation message when a task is deleted.
     *
     * @param task The task that was deleted.
     * @param totalTasks The total number of tasks after deletion.
     */
    public void showTaskDeleted(Task task, int totalTasks) {
        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task.toString());
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
        System.out.println(LINE);
    }

    /**
     * Reads a command from the user.
     *
     * @return The user's input as a string.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Closes the scanner to release resources.
     */
    public void close() {
        scanner.close();
    }
}