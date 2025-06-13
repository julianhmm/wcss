package waste_collection_schedule_java;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        WasteCollectionCalendar cal = new WasteCollectionCalendar();
        cal.addCollection(LocalDateTime.now().plusDays(1), "Restmüll");
        cal.addCollection(LocalDateTime.now().plusDays(7), "Papier");

        WasteCollectionCalendar.Collection next = cal.getNextCollection();
        if (next != null) {
            System.out.println("Next collection: " + next.type + " in " + next.daysUntil() + " days");
        } else {
            System.out.println("No upcoming collection");
        }
    }
}
