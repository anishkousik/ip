package typecast.task;

/**
 * Represents a simple todo task without any date/time constraints.
 * This is the simplest task type, containing only a description and completion status.
 */
public class Todo extends Task {

    /**
     * Creates a new Todo task with the given description.
     *
     * @param description The description of the todo task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of the todo task.
     *
     * @return A formatted string in the format "[T][status] description".
     */
    @Override
    public String toString() {
        return "[T][" + getStatus() + "] " + description;
    }
}
