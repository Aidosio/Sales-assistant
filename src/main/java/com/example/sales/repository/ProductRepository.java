package com.example.sales.repository;

import com.example.sales.entity.Category.Category;
import com.example.sales.entity.Category.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndCategory(Long id, Category category);
    Optional<Product> findByIdAndCategoryId(Long prodId, Long categoryId);
}