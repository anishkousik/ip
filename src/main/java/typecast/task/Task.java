package typecast.task;

/**
 * Represents a generic task with a description and completion status.
 * This is the base class for specific task types like Todo, Deadline, and Event.
 */
public class Task {

    protected String description;
    protected boolean isDone;

    /**
     * Constructs a Task with the specified description.
     * The task is initially marked as not done.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon of the task.
     *
     * @return "X" if the task is done, " " otherwise.
     */
    public String getStatus() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns the description of the task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Marks the task as done.
     */
    public void markDone() {
        isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markNotDone() {
        isDone = false;
    }

    /**
     * Returns a string representation of the task.
     *
     * @return The formatted task string.
     */
    @Override
    public String toString() {
        return "[T][" + getStatus() + "] " + description;
    }
}