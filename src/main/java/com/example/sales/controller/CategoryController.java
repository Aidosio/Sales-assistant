package com.example.sales.controller;

import com.example.sales.entity.Category.Category;
import com.example.sales.entity.Category.Product;
import com.example.sales.repository.CategoryRepository;
import com.example.sales.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Category getByIdCategories(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new RuntimeException("Category not found");
        }
        return category;
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @GetMapping("/{id}/products")
    public List<Product> getProductsByCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new RuntimeException("Category not found");
        }
        return category.getProducts();
    }

    @GetMapping("/{idC}/products/{idP}")
    public Product getProductByCategoryAndId(@PathVariable Long idC, @PathVariable Long idP) {
        Category category = categoryRepository.findById(idC).orElse(null);
        if (category == null) {
            throw new RuntimeException("Category not found");
        }
        return category.getProducts().stream().filter(product -> product.getId().equals(idP)).findFirst().orElse(null);
    }

    @PostMapping("/{id}/products")
    public Product createProduct(@PathVariable Long id, @RequestBody Product product) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new RuntimeException("Category not created");
        }
        product.setCategory(category);
        return productRepository.save(product);
    }
}