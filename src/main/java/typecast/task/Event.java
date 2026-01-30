package typecast.task;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task with a start and end time.
 * An event task has a description and a specific time range during which it occurs.
 */
public class Event extends Task {

    protected LocalDateTime from;
    protected LocalDateTime to;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    /**
     * Creates a new Event task with the given description and time range as strings.
     *
     * @param description The description of the event.
     * @param from The start date/time as a string in format "yyyy-MM-dd HHmm" or "yyyy-MM-dd".
     * @param to The end date/time as a string in format "yyyy-MM-dd HHmm" or "yyyy-MM-dd".
     * @throws IllegalArgumentException If the date format is invalid.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = parseDateTime(from);
        this.to = parseDateTime(to);
    }

    /**
     * Creates a new Event task with the given description and time range as LocalDateTime objects.
     *
     * @param description The description of the event.
     * @param from The start date/time as a LocalDateTime object.
     * @param to The end date/time as a LocalDateTime object.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Parses a date/time string into a LocalDateTime object.
     * Accepts formats: "yyyy-MM-dd HHmm" or "yyyy-MM-dd" (defaults to 00:00).
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
                return LocalDateTime.parse(dateTimeStr + " 0000", 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            } catch (Exception ex) {
                throw new IllegalArgumentException(
                    "Invalid date format. Please use yyyy-MM-dd or yyyy-MM-dd HHmm (e.g., 2019-12-02 1800)");
            }
        }
    }

    /**
     * Returns the start date/time of the event.
     *
     * @return The LocalDateTime representing the start time.
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the end date/time of the event.
     *
     * @return The LocalDateTime representing the end time.
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Returns the start date/time as a formatted string for storage.
     *
     * @return The start time in "yyyy-MM-dd HHmm" format.
     */
    public String getFromString() {
        return from.format(INPUT_FORMATTER);
    }

    /**
     * Returns the end date/time as a formatted string for storage.
     *
     * @return The end time in "yyyy-MM-dd HHmm" format.
     */
    public String getToString() {
        return to.format(INPUT_FORMATTER);
    }

    /**
     * Returns a string representation of the event task.
     *
     * @return A formatted string in the format "[E][status] description (from: MMM dd yyyy, h:mma to: MMM dd yyyy, h:mma)".
     */
    @Override
    public String toString() {
        return "[E][" + getStatus() + "] " + description + 
               " (from: " + from.format(OUTPUT_FORMATTER) + 
               " to: " + to.format(OUTPUT_FORMATTER) + ")";
    }
}