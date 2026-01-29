import java.util.ArrayList;
import java.util.Scanner;

public class TypeCast {
    private static final String FILE_PATH = "./ip/data/tasks.txt";
    
    public static void main(String[] args) {
        String logo =
        "████████╗██╗   ██╗██████╗ ███████╗ ██████╗ █████╗ ███████╗████████╗\n"
      + "╚══██╔══╝╚██╗ ██╔╝██╔══██╗██╔════╝██╔════╝██╔══██╗██╔════╝╚══██╔══╝\n"
      + "   ██║    ╚████╔╝ ██████╔╝█████╗  ██║     ███████║███████╗   ██║   \n"
      + "   ██║     ╚██╔╝  ██╔═══╝ ██╔══╝  ██║     ██╔══██║╚════██║   ██║   \n"
      + "   ██║      ██║   ██║     ███████╗╚██████╗██║  ██║███████║   ██║   \n"
      + "   ╚═╝      ╚═╝   ╚═╝     ╚══════╝ ╚═════╝╚═╝  ╚═╝╚══════╝   ╚═╝   \n";


        System.out.println(logo + "\n");
        System.out.println("""
____________________________________________________________
Hello! I'm TypeCast
What can I do for you?
____________________________________________________________
""");
        

        Storage storage = new Storage(FILE_PATH);
        ArrayList<Task> tasks = storage.loadTasks();
        

        if (!tasks.isEmpty()) {
            System.out.println("Loaded " + tasks.size() + " task(s) from previous session.");
            System.out.println("____________________________________________________________");
        }
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            
            String input = scanner.nextLine();
            
            try {
                if (input.equals("bye")) {
                    System.out.println("""
____________________________________________________________
Bye. Hope to see you again soon!
____________________________________________________________
""");
                    break;
                } else if (input.equals("list")) {
                    System.out.println("____________________________________________________________");
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + "." + tasks.get(i).toString());
                    }
                    System.out.println("____________________________________________________________");
                } else if (input.startsWith("mark ")) {
                    int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                    tasks.get(taskIndex).markDone();
                    System.out.println("____________________________________________________________");
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks.get(taskIndex).toString());
                    System.out.println("____________________________________________________________");
                    storage.saveTasks(tasks);
                } else if (input.startsWith("unmark ")) {
                    int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                    tasks.get(taskIndex).markNotDone();
                    System.out.println("____________________________________________________________");
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks.get(taskIndex).toString());
                    System.out.println("____________________________________________________________");
                    storage.saveTasks(tasks);
                } else if (input.startsWith("delete ")) {
                    int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                    if (taskIndex < 0 || taskIndex >= tasks.size()) {
                        throw new TypeCastException("Task index out of range.");
                    }
                    Task removedTask = tasks.remove(taskIndex);
                    System.out.println("____________________________________________________________");
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removedTask.toString());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                    storage.saveTasks(tasks);
                } else if (input.startsWith("todo ")) {
                    String description = input.substring(5).trim();
                    if (description.isEmpty()) {
                        throw new TypeCastException("The description of a todo cannot be empty.");
                    }
                    tasks.add(new Todo(description));
                    System.out.println("____________________________________________________________");
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks.get(tasks.size() - 1).toString());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                    storage.saveTasks(tasks);
                } else if (input.startsWith("deadline ")) {
                    String rest = input.substring(9);
                    int byIndex = rest.indexOf(" /by ");
                    if (byIndex == -1) {
                        throw new TypeCastException("The format of deadline should be: deadline <description> /by <date/time>");
                    }
                    String description = rest.substring(0, byIndex).trim();
                    String by = rest.substring(byIndex + 5).trim();
                    if (description.isEmpty()) {
                        throw new TypeCastException("The description of a deadline cannot be empty.");
                    }
                    if (by.isEmpty()) {
                        throw new TypeCastException("The deadline date/time cannot be empty.");
                    }
                    tasks.add(new Deadline(description, by));
                    System.out.println("____________________________________________________________");
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks.get(tasks.size() - 1).toString());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                    storage.saveTasks(tasks);
                } else if (input.startsWith("event ")) {
                    String rest = input.substring(6);
                    int fromIndex = rest.indexOf(" /from ");
                    int toIndex = rest.indexOf(" /to ");
                    if (fromIndex == -1 || toIndex == -1) {
                        throw new TypeCastException("The format of event should be: event <description> /from <start> /to <end>");
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
                    tasks.add(new Event(description, from, to));
                    System.out.println("____________________________________________________________");
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + tasks.get(tasks.size() - 1).toString());
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("____________________________________________________________");
                    storage.saveTasks(tasks);
                } else if (input.equals("todo") || input.equals("deadline") || input.equals("event")) {
                    if (input.equals("todo")) {
                        throw new TypeCastException("The description of a todo cannot be empty.");
                    } else if (input.equals("deadline")) {
                        throw new TypeCastException("The format of deadline should be: deadline <description> /by <date/time>");
                    } else {
                        throw new TypeCastException("The format of event should be: event <description> /from <start> /to <end>");
                    }
                } else {
                    throw new TypeCastException("Sorry, that is not a valid command!");
                }
            } catch (TypeCastException e) {
                System.out.println("____________________________________________________________");
                System.out.println(" " + e.getMessage());
                System.out.println("____________________________________________________________");
            }
            
        }
        
        scanner.close();
        
    }
}