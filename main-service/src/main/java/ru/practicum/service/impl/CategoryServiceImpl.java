package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<CategoryDto> getCategoriesPublic(int from, int size) {
        int pageNumber = from / size;
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.stream()
                .map(CategoryMapper.INSTANCE::categoryToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryByIdPublic(long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id=" + catId + " не найдена."));
        return CategoryMapper.INSTANCE.categoryToCategoryDto(category);
    }

    private void fixCategoryNameCase(NewCategoryDto newCategoryDto) {
        String name = newCategoryDto.getName();
        String firstLetter = String.valueOf(name.charAt(0));
        String nameFixed = firstLetter + name.substring(1);
        newCategoryDto.setName(nameFixed);
    }
}
