package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.service.CompilationService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping()
    public List<CompilationDto> getCompilationsPublic(@RequestParam(defaultValue = "false") boolean pinned,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size,
                                                      HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — получение категорий");
        return compilationService.getCompilationsPublic(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationByIdPublic(@PathVariable long compId, HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — получение информации о категории " +
                "по её идентификатору");
        return compilationService.getCompilationByIdPublic(compId);
    }
}
