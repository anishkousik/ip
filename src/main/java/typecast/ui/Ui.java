package typecast.ui;
import java.util.Scanner;
import typecast.task.Task;
import typecast.task.TaskList;


public class Ui {
    private static final String LINE = "____________________________________________________________";
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }


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


    public void showGoodbye() {
        System.out.println(LINE);
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }


    public void showLoadingError() {
        System.out.println(LINE);
        System.out.println("Error loading tasks from file. Starting with an empty task list.");
        System.out.println(LINE);
    }


    public void showTasksLoaded(int count) {
        System.out.println("Loaded " + count + " task(s) from previous session.");
        System.out.println(LINE);
    }


    public void showError(String message) {
        System.out.println(LINE);
        System.out.println(" " + message);
        System.out.println(LINE);
    }


    public void showMessage(String message) {
        System.out.println(LINE);
        System.out.println(message);
        System.out.println(LINE);
    }

 
    public void showTaskList(TaskList tasks) {
        System.out.println(LINE);
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i).toString());
        }
        System.out.println(LINE);
    }


    public void showTaskMarked(Task task) {
        System.out.println(LINE);
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task.toString());
        System.out.println(LINE);
    }


    public void showTaskUnmarked(Task task) {
        System.out.println(LINE);
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task.toString());
        System.out.println(LINE);
    }


    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println(LINE);
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task.toString());
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
        System.out.println(LINE);
    }


    public void showTaskDeleted(Task task, int totalTasks) {
        System.out.println(LINE);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task.toString());
        System.out.println("Now you have " + totalTasks + " tasks in the list.");
        System.out.println(LINE);
    }


    public String readCommand() {
        return scanner.nextLine();
    }


    public void close() {
        scanner.close();
    }
}