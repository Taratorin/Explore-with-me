package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
//    todo delete DONE
    private final CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody NewCategoryDto newCategoryDto,
                                                      HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — добавление категории");
        return ResponseEntity.status(201).body(categoryService.saveCategory(newCategoryDto));
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId, HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — удаление категории");
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> patchCategory(@PathVariable long catId,
                                                     @Valid @RequestBody NewCategoryDto newCategoryDto,
                                                     HttpServletRequest request) {
        log.info("Получен запрос " + request.getRequestURI() + " — изменение категории");
        return ResponseEntity.status(200).body(categoryService.patchCategory(newCategoryDto, catId));
    }


}
