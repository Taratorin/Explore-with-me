package ru.practicum.model;

import lombok.*;

import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Location {
    private Float lat;
    private Float lon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Float.compare(lat, location.lat) == 0 && Float.compare(lon, location.lon) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }
}
