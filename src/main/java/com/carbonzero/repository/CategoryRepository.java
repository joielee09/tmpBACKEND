package com.carbonzero.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carbonzero.domain.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByIdAndIsActive(Long categoryId, int i);

    Optional<Category> findByIdAndIsActive(Long categoryId, boolean active);

    List<Category> findAllByIsActive(boolean active);
}
