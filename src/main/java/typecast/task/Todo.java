package typecast.task;

/**
 * Represents a simple todo task without any date/time attached.
 */
public class Todo extends Task {

    /**
     * Constructs a Todo task with the specified description.
     *
     * @param description The description of the todo task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of the todo task.
     *
     * @return The formatted todo task string.
     */
    @Override
    public String toString() {
        return "[T][" + getStatus() + "] " + description;
    }
}