package waste_collection_schedule_java;

import java.util.ArrayList;
import java.util.List;

/**
 * Very small wrapper around a source that provides collection entries.
 */
public class SourceShell {
    private final Source source;
    private final List<Collection> entries = new ArrayList<>();

    public interface Source {
        List<Collection> fetch();
    }

    public SourceShell(Source source) {
        this.source = source;
    }

    /** Fetch data from the underlying source. */
    public void fetch() {
        entries.clear();
        try {
            entries.addAll(source.fetch());
        } catch (Exception e) {
            System.err.println("Failed to fetch from source: " + e.getMessage());
        }
    }

    public List<Collection> getEntries() {
        return entries;
    }
}
