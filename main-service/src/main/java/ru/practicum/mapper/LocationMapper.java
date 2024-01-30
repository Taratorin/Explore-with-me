package ru.practicum.mapper;

import org.mapstruct.Mapper;
import ru.practicum.dto.LocationDto;
import ru.practicum.model.Location;

@Mapper
public interface LocationMapper {

    LocationDto toLocationDto(Location location);

    Location toLocation(LocationDto locationDto);
}
