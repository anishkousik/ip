package typecast.task;

import java.util.ArrayList;

import typecast.exception.TypeCastException;

/**
 * Manages a list of tasks.
 * Provides methods to add, delete, and modify tasks.
 */
public class TaskList {

    private final ArrayList<Task> tasks;

    /**
     * Constructs an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a TaskList with the given list of tasks.
     *
     * @param tasks The initial list of tasks.
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
     * Deletes a task at the specified index.
     *
     * @param index The index of the task to delete.
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
     * Gets the task at the specified index.
     *
     * @param index The index of the task.
     * @return The task at the specified index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Marks the task at the specified index as done.
     *
     * @param index The index of the task to mark as done.
     */
    public void markTaskDone(int index) {
        tasks.get(index).markDone();
    }

    /**
     * Marks the task at the specified index as not done.
     *
     * @param index The index of the task to mark as not done.
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
     * @return The ArrayList of tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }
}