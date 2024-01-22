package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.model.Category;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category categoryDtoToCategory(CategoryDto categoryDto);
    Category newCategoryDtoToCategory(NewCategoryDto newCategoryDto);

    CategoryDto categoryToCategoryDto(Category category);

}
