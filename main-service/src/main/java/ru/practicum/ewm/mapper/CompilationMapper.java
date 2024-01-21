package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.model.Compilation;

@Mapper
public interface CompilationMapper {

    CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);


    CompilationDto toCompilationDto(Compilation compilation);

    Compilation toCompilation(NewCompilationDto newCompilationDto);

}
