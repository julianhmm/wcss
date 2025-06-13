package waste_collection_schedule_java;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Java representation of a waste collection entry.
 */
public class Collection {
    private LocalDate date;
    private String type;
    private String icon;
    private String picture;

    public Collection(LocalDate date, String type) {
        this.date = date;
        this.type = type;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    /**
     * Returns number of days until collection from now.
     */
    public long daysUntil() {
        return ChronoUnit.DAYS.between(LocalDate.now(), date);
    }

    @Override
    public String toString() {
        return "Collection{" + "date=" + date + ", type='" + type + '\'' + '}';
    }
}
