package typecast.task;

import typecast.exception.TypeCastException;

import java.util.ArrayList;

/**
 * Manages the list of tasks, providing operations to add, delete, and access tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty TaskList.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
        assert tasks != null : "Task list should be initialized";
    }

    /**
     * Creates a TaskList with existing tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        assert tasks != null : "Tasks cannot be null";
        this.tasks = tasks;
    }

    /**
     * Adds a task to the list.
     */
    public void add(Task task) {
        assert task != null : "Cannot add null task";
        tasks.add(task);
        assert tasks.contains(task) : "Task should be in list after adding";
    }

    /**
     * Deletes a task at the specified index.
     * @param index The index of the task to delete (0-based)
     * @return The deleted task
     * @throws TypeCastException if the index is out of range
     */
    public Task delete(int index) throws TypeCastException {
        assert index >= 0 : "Index cannot be negative";
        if (index < 0 || index >= tasks.size()) {
            throw new TypeCastException("Task index out of range.");
        }
        int sizeBefore = tasks.size();
        Task removedTask = tasks.remove(index);
        assert tasks.size() == sizeBefore - 1 : "Size should decrease by 1 after deletion";
        assert removedTask != null : "Removed task should not be null";
        return removedTask;
    }

    /**
     * Gets a task at the specified index.
     * @param index The index of the task (0-based)
     * @return The task at the specified index
     */
    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "Index out of bounds";
        Task task = tasks.get(index);
        assert task != null : "Retrieved task should not be null";
        return task;
    }

    /**
     * Marks a task as done at the specified index.
     * @param index The index of the task (0-based)
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void markTaskDone(int index) {
        assert index >= 0 && index < tasks.size() : "Index must be valid";
        tasks.get(index).markDone();
        assert tasks.get(index).getStatus().equals("X") : "Task should be marked as done";
    }

    /**
     * Marks a task as not done at the specified index.
     * @param index The index of the task (0-based)
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void markTaskNotDone(int index) {
        assert index >= 0 && index < tasks.size() : "Index must be valid";
        tasks.get(index).markNotDone();
        assert tasks.get(index).getStatus().equals(" ") : "Task should be marked as not done";
    }

    /**
     * Returns the number of tasks in the list.
     */
    public int size() {
        int size = tasks.size();
        assert size >= 0 : "Size cannot be negative";
        return size;
    }

    /**
     * Returns the ArrayList of tasks (for storage purposes).
     */
    public ArrayList<Task> getTasks() {
        assert tasks != null : "Tasks list should never be null";
        return tasks;
    }
}