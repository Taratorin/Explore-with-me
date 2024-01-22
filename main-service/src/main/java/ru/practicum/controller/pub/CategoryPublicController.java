package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.CategoryDto;
import ru.practicum.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryPublicController {
    private final CategoryService categoryService;

    @GetMapping()
    public List<CategoryDto> getCategoriesPublic(@RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — получение категорий");
        return categoryService.getCategoriesPublic(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryByIdPublic(@PathVariable long catId, HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — получение информации о категории " +
                "по её идентификатору");
        return categoryService.getCategoryByIdPublic(catId);
    }
}
