package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.model.*;


public interface CompilationService {
    CompilationDto saveCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto patchCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId);
}
