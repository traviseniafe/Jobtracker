import java.time.LocalDate;

/**
 * One job application: company, role, current status, date added and notes.
 */

public class Application {
    private final String company;
    private final String role;
    private Status status;
    private final LocalDate dateAdded;
    private final String notes;

    public Application(String company, String role, Status status, LocalDate dateAdded, String notes) {
        this.company = company;
        this.role = role;
        this.status = status;
        this.dateAdded = dateAdded;
        this.notes = notes;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    //Turn this application into one line of tab-separated text for saving. 
    public String toLine() {
        return String.join("\t", company, role, status.name(), dateAdded.toString(), notes);
    }

    // Rebuild an application from a saved line. Throws if the line is malformed. 
    public static Application fromLine(String line) {
        String[] parts = line.split("\t", -1); // -1 keeps empty trailing fields
        if (parts.length != 5) {
            throw new IllegalArgumentException("Expected 5 fields but found " + parts.length);
        }
        Status status = Status.valueOf(parts[2]);          // throws if not a real status
        LocalDate date = LocalDate.parse(parts[3]);        // throws if not a real date
        return new Application(parts[0], parts[1], status, date, parts[4]);
    }

    @Override
    public String toString() {
        String text = String.format("%-18s %-24s %-10s %s", company, role, status, dateAdded);
        if (!notes.isBlank()) {
            text += "  (" + notes + ")";
        }
        return text;
    }
}