package typecast.task;

import typecast.exception.TypeCastException;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
    }

    /**
     * Creates a TaskList with existing tasks.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the list.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes a task at the specified index.
     * @param index The index of the task to delete (0-based)
     * @return The deleted task
     * @throws TypeCastException if the index is out of range
     */
    public Task delete(int index) throws TypeCastException {
        if (index < 0 || index >= tasks.size()) {
            throw new TypeCastException("Task index out of range.");
        }
        return tasks.remove(index);
    }

    /**
     * Gets a task at the specified index.
     * @param index The index of the task (0-based)
     * @return The task at the specified index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Marks a task as done at the specified index.
     * @param index The index of the task (0-based)
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void markTaskDone(int index) {
        tasks.get(index).markDone();
    }

    /**
     * Marks a task as not done at the specified index.
     * @param index The index of the task (0-based)
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void markTaskNotDone(int index) {
        tasks.get(index).markNotDone();
    }

    /**
     * Returns the number of tasks in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the ArrayList of tasks (for storage purposes).
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Finds tasks that contain the given keyword in their description.
     * Uses Java Streams for filtering.
     * @param keyword The keyword to search for
     * @return List of matching tasks
     */
    public ArrayList<Task> findTasks(String keyword) {
        return tasks.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets all tasks that are marked as done.
     * Uses Java Streams for filtering.
     * @return List of completed tasks
     */
    public ArrayList<Task> getCompletedTasks() {
        return tasks.stream()
                .filter(task -> task.getStatus().equals("X"))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Gets all tasks that are not done yet.
     * Uses Java Streams for filtering.
     * @return List of pending tasks
     */
    public ArrayList<Task> getPendingTasks() {
        return tasks.stream()
                .filter(task -> task.getStatus().equals(" "))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Checks if there are any tasks matching the keyword.
     * Uses Java Streams.
     * @param keyword The keyword to search for
     * @return true if at least one task matches
     */
    public boolean hasTaskWithKeyword(String keyword) {
        return tasks.stream()
                .anyMatch(task -> task.getDescription().toLowerCase().contains(keyword.toLowerCase()));
    }

    /**
     * Counts the number of completed tasks.
     * Uses Java Streams.
     * @return Number of completed tasks
     */
    public long countCompletedTasks() {
        return tasks.stream()
                .filter(task -> task.getStatus().equals("X"))
                .count();
    }
}