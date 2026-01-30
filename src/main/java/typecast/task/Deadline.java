package typecast.task;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a deadline.
 * A deadline task has a description and a specific date/time by which it must be completed.
 */
public class Deadline extends Task {

    protected LocalDateTime by;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    /**
     * Creates a new Deadline task with the given description and deadline date/time string.
     *
     * @param description The description of the deadline task.
     * @param by The deadline date/time as a string in format "yyyy-MM-dd HHmm" or "yyyy-MM-dd".
     * @throws IllegalArgumentException If the date format is invalid.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = parseDateTime(by);
    }

    /**
     * Creates a new Deadline task with the given description and deadline LocalDateTime.
     *
     * @param description The description of the deadline task.
     * @param by The deadline date/time as a LocalDateTime object.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }


    /**
     * Parses a date/time string into a LocalDateTime object.
     * Accepts formats: "yyyy-MM-dd HHmm" or "yyyy-MM-dd" (defaults to 23:59).
     *
     * @param dateTimeStr The date/time string to parse.
     * @return The parsed LocalDateTime object.
     * @throws IllegalArgumentException If the date format is invalid.
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
                    "Invalid date format. Please use yyyy-MM-dd or yyyy-MM-dd HHmm (e.g., 2019-12-02 1800)");
            }
        }
    }

    /**
     * Returns the deadline date/time.
     *
     * @return The LocalDateTime representing the deadline.
     */
    public LocalDateTime getBy() {
        return by;
    }


    /**
     * Returns the deadline date/time.
     *
     * @return The LocalDateTime representing the deadline.
     */
    public String getByString() {
        return by.format(INPUT_FORMATTER);
    }

    /**
     * Returns a string representation of the deadline task.
     *
     * @return A formatted string in the format "[D][status] description (by: MMM dd yyyy, h:mma)".
     */
    @Override
    public String toString() {
        return "[D][" + getStatus() + "] " + description + " (by: " + by.format(OUTPUT_FORMATTER) + ")";
    }
}