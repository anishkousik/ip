package typecast.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a deadline.
 */
public class Deadline extends Task {

    protected LocalDateTime by;
    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    /**
     * Constructs a Deadline task with the specified description and deadline string.
     *
     * @param description The description of the task.
     * @param by The deadline in string format (yyyy-MM-dd or yyyy-MM-dd HHmm).
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = parseDateTime(by);
    }

    /**
     * Constructs a Deadline task with the specified description and deadline LocalDateTime.
     *
     * @param description The description of the task.
     * @param by The deadline as a LocalDateTime object.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Parses a date-time string into a LocalDateTime object.
     *
     * @param dateTimeStr The date-time string to parse.
     * @return The parsed LocalDateTime object.
     * @throws IllegalArgumentException If the format is invalid.
     */
    private LocalDateTime parseDateTime(String dateTimeStr) {
        try {
            return LocalDateTime.parse(dateTimeStr, INPUT_FORMATTER);
        } catch (Exception e) {
            try {
                DateTimeFormatter dateOnlyFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                return LocalDateTime.parse(dateTimeStr + " 2359",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            } catch (Exception ex) {
                throw new IllegalArgumentException(
                        "Invalid date format. Please use yyyy-MM-dd or yyyy-MM-dd HHmm "
                                + "(e.g., 2019-12-02 1800)");
            }
        }
    }

    /**
     * Returns the deadline as a LocalDateTime object.
     *
     * @return The deadline.
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Returns the deadline as a formatted string for storage.
     *
     * @return The formatted deadline string.
     */
    public String getByString() {
        return by.format(INPUT_FORMATTER);
    }

    /**
     * Returns a string representation of the deadline task.
     *
     * @return The formatted deadline task string.
     */
    @Override
    public String toString() {
        return "[D][" + getStatus() + "] " + description + " (by: " + by.format(OUTPUT_FORMATTER) + ")";
    }
}