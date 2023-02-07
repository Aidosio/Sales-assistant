package com.example.sales.repository;

import com.example.sales.entity.Category.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {}