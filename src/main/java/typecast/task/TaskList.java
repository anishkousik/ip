package typecast.task;
import java.util.ArrayList;
import typecast.exception.TypeCastException;

/**
 * Manages a list of tasks.
 * Provides methods to add, delete, retrieve, and modify tasks in the list.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates a new empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a TaskList with the given list of tasks.
     *
     * @param tasks The ArrayList of tasks to initialize the TaskList with.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes a task from the list at the specified index.
     *
     * @param index The zero-based index of the task to delete.
     * @return The deleted task.
     * @throws TypeCastException If the index is out of range.
     */
    public Task delete(int index) throws TypeCastException {
        if (index < 0 || index >= tasks.size()) {
            throw new TypeCastException("Task index out of range.");
        }
        return tasks.remove(index);
    }

    /**
     * Retrieves a task from the list at the specified index.
     *
     * @param index The zero-based index of the task to retrieve.
     * @return The task at the specified index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Marks a task as done at the specified index.
     *
     * @param index The zero-based index of the task to mark as done.
     */
    public void markTaskDone(int index) {
        tasks.get(index).markDone();
    }

    /**
     * Marks a task as not done at the specified index.
     *
     * @param index The zero-based index of the task to mark as not done.
     */
    public void markTaskNotDone(int index) {
        tasks.get(index).markNotDone();
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The size of the task list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying ArrayList of tasks.
     *
     * @return The ArrayList containing all tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }
}