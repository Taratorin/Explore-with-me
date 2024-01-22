package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.model.UpdateCompilationRequest;
import ru.practicum.service.CompilationService;

import javax.servlet.http.HttpServletRequest;

import static ru.practicum.config.Constants.ADMIN_CONTROLLER_PREFIX;


@RestController
@RequestMapping(path = ADMIN_CONTROLLER_PREFIX + "/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping()
    public CompilationDto saveCompilation(@RequestBody NewCompilationDto newCompilationDto, HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — добавление новой подборки событий");
        return compilationService.saveCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@RequestParam Long compId, HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — удаление подборки");
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto patchCompilation(@RequestBody UpdateCompilationRequest updateCompilationRequest,
            @RequestParam Long compId, HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — удаление подборки");
        return compilationService.patchCompilation(updateCompilationRequest, compId);
    }

}
