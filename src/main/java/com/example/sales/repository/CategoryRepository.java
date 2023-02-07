package com.example.sales.repository;

import com.example.sales.entity.Category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}