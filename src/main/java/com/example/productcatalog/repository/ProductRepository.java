package com.example.productcatalog.repository;

import com.example.productcatalog.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Search products by name (case-insensitive, partial match)
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Filter products by exact category
    Page<Product> findByCategory(String category, Pageable pageable);

    // Search by name AND filter by category simultaneously
    Page<Product> findByNameContainingIgnoreCaseAndCategory(String name, String category, Pageable pageable);
}