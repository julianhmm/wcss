package waste_collection_schedule_java;

import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;

/** Demonstration entry point for the Java port. */
public class Main {
    public static void main(String[] args) {
        // Example ICS data with two events
        String icsData = """
BEGIN:VCALENDAR
BEGIN:VEVENT
DTSTART;VALUE=DATE:20240101
SUMMARY:Restmuell
END:VEVENT
BEGIN:VEVENT
DTSTART;VALUE=DATE:20240107
SUMMARY:Papier
END:VEVENT
END:VCALENDAR
""";

        // Create a source from ICS data
        ICSSource source = new ICSSource(new StringReader(icsData));
        SourceShell shell = new SourceShell(source);
        shell.fetch();

        // Aggregate and query
        CollectionAggregator agg = new CollectionAggregator();
        agg.addShell(shell);

        List<Collection> upcoming = agg.getUpcoming(5, null, null);
        for (Collection c : upcoming) {
            System.out.println(c.getDate() + " - " + c.getType());
        }
    }
}
