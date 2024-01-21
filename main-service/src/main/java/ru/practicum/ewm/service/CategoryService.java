package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto saveCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(long catId);

    CategoryDto patchCategory(NewCategoryDto newCategoryDto);

    List<CategoryDto> getCategoriesPublic(int from, int size);

    CategoryDto getCategoryByIdPublic(long catId);
}
