package waste_collection_schedule_java;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Aggregates collections from multiple sources and allows simple queries.
 */
public class CollectionAggregator {
    private final List<SourceShell> shells = new ArrayList<>();

    public void addShell(SourceShell shell) {
        shells.add(shell);
    }

    public List<Collection> getUpcoming(int count, Set<String> includeTypes, Set<String> excludeTypes) {
        List<Collection> all = new ArrayList<>();
        for (SourceShell shell : shells) {
            all.addAll(shell.getEntries());
        }
        LocalDate today = LocalDate.now();
        return all.stream()
                .filter(c -> !c.getDate().isBefore(today))
                .filter(c -> includeTypes == null || includeTypes.contains(c.getType()))
                .filter(c -> excludeTypes == null || !excludeTypes.contains(c.getType()))
                .sorted(Comparator.comparing(Collection::getDate))
                .limit(count)
                .collect(Collectors.toList());
    }
}
