package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.CategoryDto;
import ru.practicum.ewm.dto.NewCategoryDto;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.CategoryMapper;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.service.CategoryService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto saveCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.INSTANCE.newCategoryDtoToCategory(newCategoryDto);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.INSTANCE.categoryToCategoryDto(savedCategory);
    }

    @Override
    // с категорией не должно быть связано ни одного события
    // todo удалить все события перед удалением категории
    public void deleteCategory(long catId) {
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto patchCategory(NewCategoryDto newCategoryDto) {
        fixCategoryNameCase(newCategoryDto);
        String name = newCategoryDto.getName();
        if (categoryRepository.existsByName(name)) {
            Category category = CategoryMapper.INSTANCE.newCategoryDtoToCategory(newCategoryDto);
            Category savedCategory = categoryRepository.save(category);
            return CategoryMapper.INSTANCE.categoryToCategoryDto(savedCategory);
        } else {
            throw new NotFoundException("Category with name " + name + " was not found");
        }
    }

    private void fixCategoryNameCase(NewCategoryDto newCategoryDto) {
        String name = newCategoryDto.getName();
        String firstLetter = String.valueOf(name.charAt(0));
        String nameFixed = firstLetter + name.substring(1);
        newCategoryDto.setName(nameFixed);
    }
}
