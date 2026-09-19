# job-tracker-java

A command-line job application tracker written in Java. Add applications, update their status as things progress, filter by status, and see a summary. Everything is saved automatically between runs.

**Status:** in progress (v0.1, core features working)

## Requirements

Java 11 or newer (JDK, not just the runtime). Check with:

```
javac -version
```

## Build and run

From the project folder:

```
javac -d out *.java
java -cp out Main
```

The first command compiles every `.java` file into the `out` folder. The second runs the program.

## Features

- Add an application (company, role, status, optional notes; today's date is recorded)
- List all applications
- Update the status of an application
- Filter by status
- Summary count per status
- Delete an application

Statuses: `WISHLIST`, `APPLIED`, `INTERVIEW`, `OFFER`, `REJECTED`

## Where is my data?

Saved as tab-separated text in `job-tracker-applications.tsv` in your home folder, so it works from any directory and your personal data never ends up in this repo.

## How the code is organised

| File | Job |
| --- | --- |
| `Status.java` | The allowed statuses (an enum) |
| `Application.java` | One application, and how to turn it into a saved line and back |
| `ApplicationTracker.java` | The list of applications and the operations on it |
| `ApplicationStore.java` | Reading and writing the data file |
| `Main.java` | The menu, keyboard input and screen output |

## Roadmap

- [ ] Unit tests (JUnit) for `ApplicationTracker` and `Application`
- [ ] Follow-up dates and reminders
- [ ] Search by company name
- [ ] Sort by date or status
- [ ] Export to CSV
- [ ] Swap the text file for SQLite

## What I'm learning

Object-oriented design (classes, encapsulation, enums), collections (`List`, `Map`), file handling with `java.nio`, exceptions, and separating input/output from program logic.
