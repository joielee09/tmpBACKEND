package com.carbonzero.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.carbonzero.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findTop5ByIsActiveAndCategoryIdAndIsEcoFriendlyOrderByCarbonEmissionsAsc(boolean active,Long categoryId,boolean isEcoFriendly);
}
