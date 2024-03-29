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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        List<Event> events;
        if (eventIds != null && !eventIds.isEmpty()) {
            events = eventRepository.findAllById(eventIds);
        } else {
            events = new ArrayList<>();
        }
        Compilation compilation = Compilation.builder()
                .events(new HashSet<>(events))
                .pinned(newCompilationDto.isPinned())
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
        List<Event> eventsList;
        if (eventIds != null && !eventIds.isEmpty()) {
            eventsList = eventRepository.findAllById(eventIds);
        } else {
            eventsList = new ArrayList<>();
        }
        compilation.setEvents(new HashSet<>(eventsList));
        if (updateCompilationRequest.getPinned() != null &&
                !(updateCompilationRequest.getPinned() && compilation.getPinned())) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        String title = updateCompilationRequest.getTitle();
        if (title != null && !title.isBlank() && !title.equals(compilation.getTitle())) {
            compilation.setTitle(title);
        }
        compilationRepository.save(compilation);
        return CompilationMapper.INSTANCE.toCompilationDto(compilation);
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
}
