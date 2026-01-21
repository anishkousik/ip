import java.util.Scanner;

public class TypeCast {
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
                           ____________________________________________________________\r
                            Hello! I'm TypeCast\r
                            What can I do for you?\r
                           ____________________________________________________________\r
                           """ //
        //
        //
        );
        
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[101];
        int numTasks = 0;
        
        while (true) {
            
            String input = scanner.nextLine();
            
            if (input.equals("bye")) {
                System.out.println("""
                                   ____________________________________________________________\r
                                    Bye. Hope to see you again soon!\r
                                   ____________________________________________________________\r
                                   """ //
                //
                );
                break;
            } else if (input.equals("list")) {
                System.out.println("____________________________________________________________");
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < numTasks; i++) {
                    System.out.println((i + 1) + ".[" + tasks[i].getStatus() + "] " + tasks[i].getDescription());
                }
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(input.substring(5)) - 1;
                tasks[taskIndex].markDone();
                System.out.println("____________________________________________________________");
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  [" + tasks[taskIndex].getStatus() + "] " + tasks[taskIndex].getDescription());
                System.out.println("____________________________________________________________");
            } else if (input.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(input.substring(7)) - 1;
                tasks[taskIndex].markNotDone();
                System.out.println("____________________________________________________________");
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  [" + tasks[taskIndex].getStatus() + "] " + tasks[taskIndex].getDescription());
                System.out.println("____________________________________________________________");
            } else {
                tasks[numTasks] = new Task(input);
                numTasks++;
                System.out.println("____________________________________________________________\r\n" + 
                    "added: " + input + "\n" + //
                    "____________________________________________________________\r\n"
                );
            }
            
        }
        
        scanner.close();
        
    }
}
