package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping()
    public List<CategoryDto> getCategoriesPublic(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = "10") @Positive int size,
                                                 HttpServletRequest request) {
        log.info("Получен запрос {} — получение категорий", request.getRequestURI());
        return categoryService.getCategoriesPublic(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryByIdPublic(@PathVariable long catId, HttpServletRequest request) {
        log.info("Получен запрос {} — получение информации о категории " +
                "по её идентификатору", request.getRequestURI());
        return categoryService.getCategoryByIdPublic(catId);
    }
}