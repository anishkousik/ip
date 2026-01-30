package typecast.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that occurs during a specific time period.
 */
public class Event extends Task {

    protected LocalDateTime from;
    protected LocalDateTime to;
    private static final DateTimeFormatter INPUT_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    /**
     * Constructs an Event task with the specified description and time range strings.
     *
     * @param description The description of the event.
     * @param from The start time in string format (yyyy-MM-dd or yyyy-MM-dd HHmm).
     * @param to The end time in string format (yyyy-MM-dd or yyyy-MM-dd HHmm).
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = parseDateTime(from);
        this.to = parseDateTime(to);
    }

    /**
     * Constructs an Event task with the specified description and time range LocalDateTime objects.
     *
     * @param description The description of the event.
     * @param from The start time as a LocalDateTime object.
     * @param to The end time as a LocalDateTime object.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
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
                return LocalDateTime.parse(dateTimeStr + " 0000",
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
            } catch (Exception ex) {
                throw new IllegalArgumentException(
                        "Invalid date format. Please use yyyy-MM-dd or yyyy-MM-dd HHmm "
                                + "(e.g., 2019-12-02 1800)");
            }
        }
    }

    /**
     * Returns the start time of the event.
     *
     * @return The start time.
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the end time of the event.
     *
     * @return The end time.
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Returns the start time as a formatted string for storage.
     *
     * @return The formatted start time string.
     */
    public String getFromString() {
        return from.format(INPUT_FORMATTER);
    }

    /**
     * Returns the end time as a formatted string for storage.
     *
     * @return The formatted end time string.
     */
    public String getToString() {
        return to.format(INPUT_FORMATTER);
    }

    /**
     * Returns a string representation of the event task.
     *
     * @return The formatted event task string.
     */
    @Override
    public String toString() {
        return "[E][" + getStatus() + "] " + description
                + " (from: " + from.format(OUTPUT_FORMATTER)
                + " to: " + to.format(OUTPUT_FORMATTER) + ")";
    }
}