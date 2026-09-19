import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads applications from a text file and saves them back.
 * Each application is one line, with fields separated by tabs.
 */

public class ApplicationStore {
    private final Path file;

    public ApplicationStore(Path file) {
        this.file = file;
    }

    public List<Application> load() {
        List<Application> loaded = new ArrayList<>();
        if (!Files.exists(file)) {
            return loaded; // first run: nothing saved yet
        }
        try {
            for (String line : Files.readAllLines(file)) {
                if (line.isBlank()) {
                    continue;
                }
                try {
                    loaded.add(Application.fromLine(line));
                } catch (IllegalArgumentException | DateTimeParseException e) {
                    System.out.println("Skipping an unreadable line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Couldn't read " + file + ": " + e.getMessage());
        }
        return loaded;
    }

    public void save(List<Application> applications) {
        List<String> lines = new ArrayList<>();
        for (Application application : applications) {
            lines.add(application.toLine());
        }
        try {
            Files.write(file, lines);
        } catch (IOException e) {
            System.out.println("Couldn't save to " + file + ": " + e.getMessage());
        }
    }
}