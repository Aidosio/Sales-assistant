package com.example.sales.entity.Category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
