package com.example.sales.controller;

import com.example.sales.entity.Category.Category;
import com.example.sales.entity.Category.Product;
import com.example.sales.entity.Category.ProductImage;
import com.example.sales.repository.CategoryRepository;
import com.example.sales.repository.ProductImageRepository;
import com.example.sales.repository.ProductRepository;
import com.example.sales.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final ProductService productService;

    public CategoryController(CategoryRepository categoryRepository, ProductRepository productRepository, ProductService productService) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.productService = productService;
    }

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

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.delete(category);
    }

    @GetMapping("/{idC}/products/{idP}")
    public Product getProductByCategoryAndId(@PathVariable Long idC, @PathVariable Long idP) {
        Category category = categoryRepository.findById(idC).orElse(null);
        if (category == null) {
            throw new RuntimeException("Category not found");
        }
        return category.getProducts().stream().filter(product -> product.getId().equals(idP)).findFirst().orElse(null);
    }

    @PostMapping(value = "/{id}/products", consumes = {"multipart/form-data"})
    public Product createProduct(@PathVariable Long id, @RequestParam("product") String productJson, @RequestParam("image") MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = null;
        try {
            product = objectMapper.readValue(productJson, Product.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        ProductImage productImage = new ProductImage();
        productImage.setProduct(savedProduct);
        try {
            productService.uploadProductImage(productImage, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return savedProduct;
    }

    @DeleteMapping("/{id}/products/{idP}")
    public String deleteProduct(@PathVariable Long id, @PathVariable Long idP) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new RuntimeException("Category not found");
        }
        Product product = category.getProducts().stream().filter(p -> p.getId().equals(idP)).findFirst().orElse(null);
        if (product == null) {
            throw new RuntimeException("Product not found");
        }
        category.getProducts().remove(product);
        categoryRepository.save(category);
        productRepository.delete(product);
        return "Product with id " + idP + " was successfully deleted";
    }

    @GetMapping("/{idC}/products/barcode/{barcode}")
    public Product getProductByCategoryAndBarcode(@PathVariable Long idC, @PathVariable long barcode) {
        Category category = categoryRepository.findById(idC).orElse(null);
        if (category == null) {
            throw new RuntimeException("Category not found");
        }
        return category.getProducts().stream().filter(product -> {
            return product.getBarcode() == barcode;
        }).findFirst().orElse(null);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category existingCategory = categoryRepository.findById(id).orElse(null);
        if (existingCategory == null) {
            throw new RuntimeException("Category not found");
        }
        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }

}