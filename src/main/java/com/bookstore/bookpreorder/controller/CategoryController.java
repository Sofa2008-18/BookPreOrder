
package com.bookstore.bookpreorder.controller;

import com.bookstore.bookpreorder.dto.CategoryRequest;
import com.bookstore.bookpreorder.dto.CategoryResponse;
import com.bookstore.bookpreorder.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Создание категории
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(
            @Valid @RequestBody CategoryRequest request) {

        return categoryService.createCategory(request);
    }

    // Получение всех категорий
    @GetMapping
    public List<CategoryResponse> getAllCategories() {

        return categoryService.getAllCategories();
    }

    // Получение категории по ID
    @GetMapping("/{id}")
    public CategoryResponse getCategoryById(@PathVariable Long id) {

        return categoryService.getCategoryById(id);
    }

    // Обновление категории
    @PutMapping("/{id}")
    public CategoryResponse updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {

        return categoryService.updateCategory(id, request);
    }

    // Удаление категории
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {

        categoryService.deleteCategory(id);
    }
}
