package waste_collection_schedule_java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Minimal ICS parser. Supports DTSTART;VALUE=DATE and SUMMARY fields.
 * This is only a demonstration and not a full parser.
 */
public class ICSSource implements SourceShell.Source {
    private final Reader reader;

    public ICSSource(Reader reader) {
        this.reader = reader;
    }

    @Override
    public List<Collection> fetch() {
        List<Collection> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(reader)) {
            String line;
            LocalDate date = null;
            String summary = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("DTSTART")) {
                    int idx = line.indexOf(":");
                    if (idx > 0) {
                        String value = line.substring(idx + 1);
                        date = LocalDate.parse(value, DateTimeFormatter.BASIC_ISO_DATE);
                    }
                } else if (line.startsWith("SUMMARY:")) {
                    summary = line.substring("SUMMARY:".length());
                } else if (line.equals("END:VEVENT")) {
                    if (date != null && summary != null) {
                        result.add(new Collection(date, summary));
                    }
                    date = null;
                    summary = null;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading ICS: " + e.getMessage());
        }
        return result;
    }
}
