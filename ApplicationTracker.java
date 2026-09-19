import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Holds the list of applications and the operations you can do on it.
 * It knows nothing about the keyboard or files, which keeps it easy to test.
 */

public class ApplicationTracker {
    private final List<Application> applications;

    public ApplicationTracker(List<Application> existing) {
        this.applications = new ArrayList<>(existing);
    }

    public void add(Application application) {
        applications.add(application);
    }

    //A read-only view, so other classes can't change the list directly. 
    public List<Application> getAll() {
        return Collections.unmodifiableList(applications);
    }

    //Update the status of the application at this index. Returns false if the index is invalid. 
    public boolean updateStatus(int index, Status newStatus) {
        if (!isValidIndex(index)) {
            return false;
        }
        applications.get(index).setStatus(newStatus);
        return true;
    }

    //Remove the application at this index. Returns false if the index is invalid.
    public boolean remove(int index) {
        if (!isValidIndex(index)) {
            return false;
        }
        applications.remove(index);
        return true;
    }

    public List<Application> withStatus(Status status) {
        List<Application> matches = new ArrayList<>();
        for (Application application : applications) {
            if (application.getStatus() == status) {
                matches.add(application);
            }
        }
        return matches;
    }

    //How many applications are in each status, including statuses with zero.
    public Map<Status, Integer> countByStatus() {
        Map<Status, Integer> counts = new EnumMap<>(Status.class);
        for (Status status : Status.values()) {
            counts.put(status, 0);
        }
        for (Application application : applications) {
            counts.merge(application.getStatus(), 1, Integer::sum);
        }
        return counts;
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < applications.size();
    }
}