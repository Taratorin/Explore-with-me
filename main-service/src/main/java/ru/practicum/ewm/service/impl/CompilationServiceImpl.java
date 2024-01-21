package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.model.*;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.service.CompilationService;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;

    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.INSTANCE.toCompilation(newCompilationDto);
        Compilation savedCompilation = compilationRepository.save(compilation);
        return CompilationMapper.INSTANCE.toCompilationDto(savedCompilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        getCompilationById(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto patchCompilation(UpdateCompilationRequest updateCompilationRequest, Long compId) {
        Compilation compilation = getCompilationById(compId);
        updateDestination(updateCompilationRequest, compilation);
        Compilation savedCompilation = compilationRepository.save(compilation);
        return CompilationMapper.INSTANCE.toCompilationDto(savedCompilation);
    }

    private Compilation getCompilationById(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Подборка с id=" + compId + " не найдена."));
    }

    private void updateDestination(Object source, Object destination) {
        Field[] fieldsSource = source.getClass().getDeclaredFields();
        Map<String, Object> mapFieldsValues = new HashMap<>(fieldsSource.length);
        Map<String, Object> mapFieldsTypes = new HashMap<>(fieldsSource.length);
        for (Field field : fieldsSource) {
            field.setAccessible(true);
            String name = field.getName();
            Class<?> type = field.getType();
            Object o;
            try {
                o = field.get(source);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            mapFieldsValues.put(name, o);
            mapFieldsTypes.put(name, type);
        }
        Field[] fieldsDestination = destination.getClass().getDeclaredFields();
        for (Field field : fieldsDestination) {
            field.setAccessible(true);
            String fieldDestinationName = field.getName();
            Object fieldValue = mapFieldsValues.get(fieldDestinationName);
            Object type = mapFieldsTypes.get(fieldDestinationName);
            if (fieldValue != null && field.getType() == type) {
                try {
                    field.set(destination, fieldValue);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}