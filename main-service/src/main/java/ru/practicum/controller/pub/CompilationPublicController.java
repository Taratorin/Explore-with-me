package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.service.CompilationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping()
    public List<CompilationDto> getCompilationsPublic(@RequestParam(defaultValue = "false") boolean pinned,
                                                      @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                      @RequestParam(defaultValue = "10") @Positive int size,
                                                      HttpServletRequest request) {
        log.info("Получен запрос {} — получение категорий", request.getRequestURI());
        return compilationService.getCompilationsPublic(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationByIdPublic(@PathVariable long compId, HttpServletRequest request) {
        log.info("Получен запрос {} — получение информации о категории по её идентификатору", request.getRequestURI());
        return compilationService.getCompilationByIdPublic(compId);
    }
}
