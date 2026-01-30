package typecast;
import typecast.exception.TypeCastException;
import typecast.parser.Parser;
import typecast.storage.Storage;
import typecast.task.TaskList;
import typecast.ui.Ui;

public class TypeCast {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;


    public TypeCast(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.loadTasks());
        } catch (Exception e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }


    public void run() {
        ui.showWelcome();
        

        if (tasks.size() > 0) {
            ui.showTasksLoaded(tasks.size());
        }
        
        boolean isRunning = true;
        while (isRunning) {
            try {
                String input = ui.readCommand();
                isRunning = Parser.parseCommand(input, tasks, ui, storage);
            } catch (TypeCastException e) {
                ui.showError(e.getMessage());
            }
        }
        
        ui.showGoodbye();
        ui.close();
    }


    public static void main(String[] args) {
        new TypeCast("./data/tasks.txt").run();
    }
}