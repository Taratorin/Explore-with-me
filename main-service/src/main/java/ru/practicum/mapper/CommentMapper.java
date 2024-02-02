package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.FullEventCommentDto;
import ru.practicum.dto.NewEventCommentDto;
import ru.practicum.dto.UpdateEventCommentDto;
import ru.practicum.model.EventComment;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    FullEventCommentDto toFullEventCommentDto(EventComment eventComment);

    void updateComment(UpdateEventCommentDto updateEventCommentDto, @MappingTarget EventComment eventComment);
}
