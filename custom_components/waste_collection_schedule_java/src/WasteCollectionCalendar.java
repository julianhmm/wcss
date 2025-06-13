package waste_collection_schedule_java;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Minimal example of a calendar entity converted from the Python version.
 * <p>
 * Note: This is only a skeleton and does not implement the full Home Assistant
 * integration logic.
 */
public class WasteCollectionCalendar {
    private final List<Collection> collections = new ArrayList<>();

    public WasteCollectionCalendar() {
    }

    /**
     * Add a collection date.
     */
    public void addCollection(LocalDateTime date, String type) {
        collections.add(new Collection(date, type));
    }

    /**
     * Return the next collection after "now".
     */
    public Collection getNextCollection() {
        LocalDateTime now = LocalDateTime.now();
        return collections.stream()
                .filter(c -> !c.date.isBefore(now))
                .sorted((a, b) -> a.date.compareTo(b.date))
                .findFirst()
                .orElse(null);
    }

    /** Simple record of a collection. */
    public static class Collection {
        public final LocalDateTime date;
        public final String type;

        public Collection(LocalDateTime date, String type) {
            this.date = date;
            this.type = type;
        }

        public long daysUntil() {
            return ChronoUnit.DAYS.between(LocalDateTime.now(), date);
        }
    }
}
