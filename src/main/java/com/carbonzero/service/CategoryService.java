package com.carbonzero.service;

import org.springframework.stereotype.Service;

import com.carbonzero.domain.Category;
import com.carbonzero.error.CategoryNotFoundException;
import com.carbonzero.repository.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category getCategory(Long categoryId) {
        return categoryRepository.findByIdAndIsActive(categoryId, true)
            .orElseThrow(() -> new CategoryNotFoundException(categoryId));
    }
}
