package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static ru.practicum.config.Constants.ADMIN_CONTROLLER_PREFIX;

@RestController
@RequestMapping(ADMIN_CONTROLLER_PREFIX + "/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto newCategoryDto,
                                      HttpServletRequest request) {
        log.info("Получен запрос {} — добавление категории", request.getRequestURI());
        return categoryService.saveCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId, HttpServletRequest request) {
        log.info("Получен запрос {} — удаление категории", request.getRequestURI());
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto patchCategory(@PathVariable long catId,
                                     @Valid @RequestBody NewCategoryDto newCategoryDto,
                                     HttpServletRequest request) {
        log.info("Получен запрос {} — изменение категории", request.getRequestURI());
        return categoryService.patchCategory(newCategoryDto, catId);
    }

}