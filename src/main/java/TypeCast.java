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
        String[] taskList = new String[100];
        int numTasks = 0;
        
        while (true) {
            
            String task = scanner.nextLine();
            
            if (task.compareTo("bye") != 0 && task.compareTo("list") != 0) {
                taskList[numTasks] = task;
                numTasks++;
                System.out.println("____________________________________________________________\r\n" + 
                    "added: " + task + "\n" + //
                    "____________________________________________________________\r\n"
                );
            }   else if (task.compareTo("list") == 0) {
                System.out.println("____________________________________________________________");
                for (int i = 0; i < numTasks; i++) {
                    System.out.println((i + 1) + ". " + taskList[i]);
                }
                System.out.println("____________________________________________________________");
            } 
            
            else {
                System.out.println("""
                                   ____________________________________________________________\r
                                    Bye. Hope to see you again soon!\r
                                   ____________________________________________________________\r
                                   """ //
                //
                );
                break;
            }
            
        }
        
        scanner.close();
        
    }
}
