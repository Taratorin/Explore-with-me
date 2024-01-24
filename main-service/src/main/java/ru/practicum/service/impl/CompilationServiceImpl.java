package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.model.UpdateCompilationRequest;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CompilationService;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        Set<Long> eventIds = newCompilationDto.getEvents();
        List<Event> events = eventRepository.findAllById(eventIds);
        Compilation compilation = Compilation.builder()
                .events(events)
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
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
        Set<Long> eventIds = updateCompilationRequest.getEvents();
        if (!eventIds.isEmpty()) {
            List<Event> events = eventRepository.findAllById(eventIds);
            compilation.setEvents(events);
        }
        updateDestination(updateCompilationRequest, compilation);
        Compilation savedCompilation = compilationRepository.save(compilation);
        return CompilationMapper.INSTANCE.toCompilationDto(savedCompilation);
    }

    @Override
    public List<CompilationDto> getCompilationsPublic(boolean pinned, int from, int size) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Compilation> compilations = compilationRepository.findByPinned(pinned, pageable);
        return compilations.stream()
                .map(CompilationMapper.INSTANCE::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationByIdPublic(long compId) {
        Compilation compilation = getCompilationById(compId);
        return CompilationMapper.INSTANCE.toCompilationDto(compilation);
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
