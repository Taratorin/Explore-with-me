package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.service.CategoryService;

import javax.servlet.http.HttpServletRequest;

import static ru.practicum.config.Constants.ADMIN_CONTROLLER_PREFIX;

@RestController
@RequestMapping(path = ADMIN_CONTROLLER_PREFIX + "/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping()
    public CategoryDto createCategory(@RequestBody NewCategoryDto newCategoryDto, HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — добавление категории");
        return categoryService.saveCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable long catId, HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — удаление категории");
        categoryService.deleteCategory(catId);
    }

    @PatchMapping()
    public CategoryDto patchCategory(@RequestBody NewCategoryDto newCategoryDto, HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — изменение категории");
        return categoryService.patchCategory(newCategoryDto);
    }


}
