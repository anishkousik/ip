package typecast.task;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {

    protected LocalDateTime from;
    protected LocalDateTime to;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    public Event(String description, String from, String to) {
        super(description);
        this.from = parseDateTime(from);
        this.to = parseDateTime(to);
    }

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }


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

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public String getFromString() {
        return from.format(INPUT_FORMATTER);
    }


    public String getToString() {
        return to.format(INPUT_FORMATTER);
    }

    @Override
    public String toString() {
        return "[E][" + getStatus() + "] " + description + 
               " (from: " + from.format(OUTPUT_FORMATTER) + 
               " to: " + to.format(OUTPUT_FORMATTER) + ")";
    }
}