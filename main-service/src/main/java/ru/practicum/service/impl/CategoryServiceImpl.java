package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto saveCategory(NewCategoryDto newCategoryDto) {
        checkUniqueName(newCategoryDto);
        Category category = CategoryMapper.INSTANCE.newCategoryDtoToCategory(newCategoryDto);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.INSTANCE.categoryToCategoryDto(savedCategory);
    }

    @Override
    public void deleteCategory(long catId) {
        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException("The category is not empty");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto patchCategory(NewCategoryDto newCategoryDto, Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id=" + catId + " не найдена."));
        checkUniqueName(newCategoryDto, category);
        CategoryMapper.INSTANCE.updateCategoryFromDto(newCategoryDto, category);
        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.INSTANCE.categoryToCategoryDto(savedCategory);
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

    private void checkUniqueName(NewCategoryDto newCategoryDto, Category category) {
        if (!category.getName().equals(newCategoryDto.getName()) &&
                categoryRepository.existsByName(newCategoryDto.getName())) {
            throw new ConflictException("Integrity constraint has been violated.");
        }
    }

    private void checkUniqueName(NewCategoryDto newCategoryDto) {
        if (categoryRepository.existsByName(newCategoryDto.getName())) {
            throw new ConflictException("Integrity constraint has been violated.");
        }
    }
}
