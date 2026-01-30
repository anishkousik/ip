package typecast.ui;

import java.util.ArrayList;
import java.util.Scanner;

import typecast.task.Task;
import typecast.task.TaskList;

/**
 * Handles all user interface interactions.
 * Displays messages and reads user input.
 */
public class Ui {

    private static final String LINE = "____________________________________________________________";
    private final Scanner scanner;

    /**
     * Constructs a Ui instance and initializes the scanner for user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message and logo.
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
     * Displays the goodbye message.
     */
    public void showGoodbye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    /**
     * Displays an error message when tasks fail to load.
     */
    public void showLoadingError() {
        System.out.println(LINE);
        System.out.println("Error loading tasks from file. Starting with an empty task list.");
        System.out.println(LINE);
    }

    /**
     * Displays the number of tasks loaded from storage.
     *
     * @param count The number of tasks loaded.
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
     * @param tasks The TaskList to display.
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
     * Displays the list of matching tasks from a search.
     *
     * @param matchingTasks The list of tasks that match the search criteria.
     */
    public void showMatchingTasks(ArrayList<Task> matchingTasks) {
        System.out.println(LINE);
        if (matchingTasks.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            System.out.println("Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println((i + 1) + "." + matchingTasks.get(i).toString());
            }
        }
        System.out.println(LINE);
    }

    /**
     * Displays a message when a task is marked as done.
     *
     * @param task The task that was marked.
     */
    public void showTaskMarked(Task task) {
        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task.toString());
        System.out.println(LINE);
    }

    /**
     * Displays a message when a task is unmarked.
     *
     * @param task The task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        System.out.println(LINE);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task.toString());
        System.out.println(LINE);
    }

    /**
     * Displays a message when a task is added.
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
     * Displays a message when a task is deleted.
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
     * @return The user's input as a trimmed string.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Closes the scanner.
     */
    public void close() {
        scanner.close();
    }
}