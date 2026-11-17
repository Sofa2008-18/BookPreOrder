
package com.bookstore.bookpreorder.service;

import com.bookstore.bookpreorder.dto.CategoryRequest;
import com.bookstore.bookpreorder.dto.CategoryResponse;
import com.bookstore.bookpreorder.exception.ResourceNotFoundException;
import com.bookstore.bookpreorder.model.Category;
import com.bookstore.bookpreorder.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // Создание категории
    public CategoryResponse createCategory(CategoryRequest request) {

        if (categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Такая категория уже существует");
        }

        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        Category savedCategory = categoryRepository.save(category);

        return toResponse(savedCategory);
    }

    // Получение всех категорий
    public List<CategoryResponse> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Получение категории по ID
    public CategoryResponse getCategoryById(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Категория с ID " + id + " не найдена"
                ));

        return toResponse(category);
    }

    // Обновление категории
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Категория с ID " + id + " не найдена"
                ));

        category.setName(request.getName());
        category.setDescription(request.getDescription());

        Category updatedCategory = categoryRepository.save(category);

        return toResponse(updatedCategory);
    }

    // Удаление категории
    public void deleteCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Категория с ID " + id + " не найдена"
                ));

        categoryRepository.delete(category);
    }

    // Преобразование Entity в DTO
    private CategoryResponse toResponse(Category category) {

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }
}
