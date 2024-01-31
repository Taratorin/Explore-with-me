package ru.practicum.service;


import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.model.UpdateCompilationRequest;

import java.util.List;


public interface CompilationService {
    CompilationDto saveCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto patchCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId);

    List<CompilationDto> getCompilationsPublic(boolean pinned, int from, int size);

    CompilationDto getCompilationByIdPublic(long compId);
}
