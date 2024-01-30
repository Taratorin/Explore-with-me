package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.model.UpdateCompilationRequest;
import ru.practicum.service.CompilationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static ru.practicum.config.Constants.ADMIN_CONTROLLER_PREFIX;


@RestController
@RequestMapping(path = ADMIN_CONTROLLER_PREFIX + "/compilations")
@RequiredArgsConstructor
@Slf4j
public class CompilationAdminController {
private final CompilationService compilationService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto saveCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto,
                                          HttpServletRequest request) {
        log.info("Получен запрос {} — добавление новой подборки событий", request.getRequestURI());
        return compilationService.saveCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable long compId, HttpServletRequest request) {
        log.info("Получен запрос {} — удаление подборки", request.getRequestURI());
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto patchCompilation(@RequestBody @Valid UpdateCompilationRequest updateCompilationRequest,
            @PathVariable Long compId, HttpServletRequest request) {
        log.info("Получен запрос {} — удаление подборки", request.getRequestURI());
        return compilationService.patchCompilation(updateCompilationRequest, compId);
    }

}
