package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.model.SortType;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.config.Constants.APP_NAME;

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
