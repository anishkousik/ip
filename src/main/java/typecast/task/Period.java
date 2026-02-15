package typecast.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that needs to be done within a period (from start to end date).
 */
public class Period extends Task {

    protected LocalDateTime startDate;
    protected LocalDateTime endDate;
    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");

    public Period(String description, String startDate, String endDate) {
        super(description);
        this.startDate = parseDateTime(startDate);
        this.endDate = parseDateTime(endDate);
    }

    public Period(String description, LocalDateTime startDate, LocalDateTime endDate) {
        super(description);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Parses a date-time string into LocalDateTime.
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
                    "Invalid date format. Please use yyyy-MM-dd or yyyy-MM-dd HHmm (e.g., 2024-01-15 1000)");
            }
        }
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    /**
     * Gets the start date in storage format.
     */
    public String getStartDateString() {
        return startDate.format(INPUT_FORMATTER);
    }

    /**
     * Gets the end date in storage format.
     */
    public String getEndDateString() {
        return endDate.format(INPUT_FORMATTER);
    }

    @Override
    public String toString() {
        return "[P][" + getStatus() + "] " + description + 
               " (period: " + startDate.format(OUTPUT_FORMATTER) + 
               " to " + endDate.format(OUTPUT_FORMATTER) + ")";
    }
}