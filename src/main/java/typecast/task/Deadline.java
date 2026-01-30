package typecast.task;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {

    protected LocalDateTime by;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    public Deadline(String description, String by) {
        super(description);
        this.by = parseDateTime(by);
    }

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }


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

    public LocalDateTime getBy() {
        return by;
    }


    public String getByString() {
        return by.format(INPUT_FORMATTER);
    }

    @Override
    public String toString() {
        return "[D][" + getStatus() + "] " + description + " (by: " + by.format(OUTPUT_FORMATTER) + ")";
    }
}