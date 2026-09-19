import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * The command-line menu. This class only deals with the keyboard and screen.
 */

public class Main {

    public static void main(String[] args) {
        // Saved in your home folder so it works wherever you run the program,
        // and your personal data stays out of the project folder.
        Path dataFile = Path.of(System.getProperty("user.home"), "job-tracker-applications.tsv");

        ApplicationStore store = new ApplicationStore(dataFile);
        ApplicationTracker tracker = new ApplicationTracker(store.load());
        Scanner scanner = new Scanner(System.in);

        System.out.println("Job Application Tracker");
        System.out.println("Data file: " + dataFile);

        boolean running = true;
        while (running) {
            printMenu();
            if (!scanner.hasNextLine()) {
                break; // input ended (for example Ctrl+D)
            }
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    listAll(tracker.getAll());
                    break;
                case "2":
                    addApplication(scanner, tracker);
                    break;
                case "3":
                    updateStatus(scanner, tracker);
                    break;
                case "4":
                    filterByStatus(scanner, tracker);
                    break;
                case "5":
                    showSummary(tracker);
                    break;
                case "6":
                    deleteApplication(scanner, tracker);
                    break;
                case "7":
                    running = false;
                    break;
                default:
                    System.out.println("Please choose 1-7.");
            }

            store.save(tracker.getAll()); // save after every action
        }
        System.out.println("Saved. Good luck!");
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1) List all   2) Add   3) Update status   4) Filter by status");
        System.out.println("5) Summary    6) Delete   7) Quit");
        System.out.print("> ");
    }

    private static void listAll(List<Application> applications) {
        if (applications.isEmpty()) {
            System.out.println("No applications yet.");
            return;
        }
        for (int i = 0; i < applications.size(); i++) {
            System.out.println((i + 1) + ". " + applications.get(i));
        }
    }

    private static void addApplication(Scanner scanner, ApplicationTracker tracker) {
        String company = ask(scanner, "Company: ");
        String role = ask(scanner, "Role: ");
        if (company.isEmpty() || role.isEmpty()) {
            System.out.println("Company and role can't be empty. Nothing added.");
            return;
        }
        Status status = askForStatus(scanner);
        if (status == null) {
            return;
        }
        String notes = ask(scanner, "Notes (optional): ");

        tracker.add(new Application(company, role, status, LocalDate.now(), notes));
        System.out.println("Added.");
    }

    private static void updateStatus(Scanner scanner, ApplicationTracker tracker) {
        if (tracker.getAll().isEmpty()) {
            System.out.println("No applications yet.");
            return;
        }
        listAll(tracker.getAll());
        int number = parseNumber(ask(scanner, "Number to update: "));
        Status status = askForStatus(scanner);
        if (status == null) {
            return;
        }
        if (tracker.updateStatus(number - 1, status)) { // list is shown from 1, Java counts from 0
            System.out.println("Updated.");
        } else {
            System.out.println("That's not a valid number.");
        }
    }

    private static void filterByStatus(Scanner scanner, ApplicationTracker tracker) {
        Status status = askForStatus(scanner);
        if (status == null) {
            return;
        }
        List<Application> matches = tracker.withStatus(status);
        if (matches.isEmpty()) {
            System.out.println("Nothing with status " + status + ".");
        } else {
            matches.forEach(System.out::println);
        }
    }

    private static void showSummary(ApplicationTracker tracker) {
        Map<Status, Integer> counts = tracker.countByStatus();
        for (Map.Entry<Status, Integer> entry : counts.entrySet()) {
            System.out.printf("%-10s %d%n", entry.getKey(), entry.getValue());
        }
        System.out.println("Total:     " + tracker.getAll().size());
    }

    private static void deleteApplication(Scanner scanner, ApplicationTracker tracker) {
        if (tracker.getAll().isEmpty()) {
            System.out.println("No applications yet.");
            return;
        }
        listAll(tracker.getAll());
        int number = parseNumber(ask(scanner, "Number to delete: "));
        if (tracker.remove(number - 1)) {
            System.out.println("Deleted.");
        } else {
            System.out.println("That's not a valid number.");
        }
    }

    // ----- small helpers -----

    //Show a prompt and return what the user typed. Tabs become spaces so saving stays safe. 
    private static String ask(Scanner scanner, String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return "";
        }
        return scanner.nextLine().replace('\t', ' ').trim();
    }

    // Show the statuses as a numbered list and return the one chosen, or null if invalid. 
    private static Status askForStatus(Scanner scanner) {
        Status[] all = Status.values();
        for (int i = 0; i < all.length; i++) {
            System.out.println("  " + (i + 1) + ") " + all[i]);
        }
        int number = parseNumber(ask(scanner, "Status number: "));
        if (number < 1 || number > all.length) {
            System.out.println("That's not a valid status.");
            return null;
        }
        return all[number - 1];
    }

    // Convert text to a number, or return -1 if it isn't one. 
    private static int parseNumber(String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}