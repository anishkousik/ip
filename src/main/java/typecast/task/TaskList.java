package typecast.task;
import java.util.ArrayList;

import typecast.exception.TypeCastException;


public class TaskList {
    private final ArrayList<Task> tasks;


    public TaskList() {
        this.tasks = new ArrayList<>();
    }


    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void add(Task task) {
        tasks.add(task);
    }


    public Task delete(int index) throws TypeCastException {
        if (index < 0 || index >= tasks.size()) {
            throw new TypeCastException("Task index out of range.");
        }
        return tasks.remove(index);
    }


    public Task get(int index) {
        return tasks.get(index);
    }

    public void markTaskDone(int index) {
        tasks.get(index).markDone();
    }


    public void markTaskNotDone(int index) {
        tasks.get(index).markNotDone();
    }

    public int size() {
        return tasks.size();
    }


    public ArrayList<Task> getTasks() {
        return tasks;
    }
}