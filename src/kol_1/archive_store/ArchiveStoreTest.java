package src.kol_1.archive_store;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class NonExistingItemException extends Exception {
    public NonExistingItemException(int id) {
        super(String.format("Item with id %d doesn't exist", id));
    }
}

class ArchiveNotOpenableException extends Exception {

    public ArchiveNotOpenableException(String message) {
        super(message);
    }
}

abstract class Archive {
    int id;
    LocalDate dateArchived;

    public void archive(LocalDate date) {
        this.dateArchived = date;
    }

    abstract void open(LocalDate date) throws ArchiveNotOpenableException;
}

class LockedArchive extends Archive {
    LocalDate dateToOpen;

    public LockedArchive(int id, LocalDate dateToOpen) {
        this.id = id;
        this.dateToOpen = dateToOpen;
    }

    @Override
    void open(LocalDate date) throws ArchiveNotOpenableException {
        if (dateToOpen.isAfter(date)) {
            throw new ArchiveNotOpenableException(String.format("Item %d cannot be opened before %s", id, dateToOpen));
        }
    }
}

class SpecialArchive extends Archive {
    int maxOpen;
    int countOpen;

    public SpecialArchive(int id, int maxOpen) {
        this.id = id;
        this.maxOpen = maxOpen;
        countOpen = 0;
    }

    @Override
    void open(LocalDate date) throws ArchiveNotOpenableException {
        if (countOpen == maxOpen) {
            throw new ArchiveNotOpenableException(String.format("Item %d cannot be opened more than %d times", id, maxOpen));
        }
        countOpen++;
    }
}

class ArchiveStore {

    List<Archive> archives;
    StringBuilder sb;

    public ArchiveStore() {
        archives = new ArrayList<>();
        sb = new StringBuilder();
    }

    public void archiveItem(Archive item, LocalDate date) {
        item.archive(date);
        archives.add(item);
        sb.append(String.format("Item %d archived at %s\n", item.id, item.dateArchived));
    }

    public void openItem(int open, LocalDate date) throws NonExistingItemException {
        Archive archive = archives.stream().filter(it -> it.id == open).findFirst().orElseThrow(() -> new NonExistingItemException(open));
        try {
            archive.open(date);
            sb.append(String.format("Item %d opened at %s\n", archive.id, archive.dateArchived));
        } catch (ArchiveNotOpenableException e) {
            sb.append(e.getMessage()).append("\n");
        }
    }

    public String getLog() {
        return sb.toString();
    }
}

public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while (scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch (NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}