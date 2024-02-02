package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.ComplaintDto;
import ru.practicum.model.Complaint;

@Mapper
public interface ComplaintMapper {

    ComplaintMapper INSTANCE = Mappers.getMapper(ComplaintMapper.class);

    @Mapping(target = "state", ignore = true)
    @Mapping(target = "comment", ignore = true)
    @Mapping(target = "complainer", ignore = true)
    Complaint toComplaint(ComplaintDto complaintDto);

    ComplaintDto toComplaintDto(Complaint complaint);

}
