package com.example.productcatalog.service;

import com.example.productcatalog.dto.ProductRequestDto;
import com.example.productcatalog.dto.ProductResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;

public interface ProductService {

    List<ProductResponseDto> getAllProducts();

    ProductResponseDto getProductById(Long id);

    ProductResponseDto createProduct(ProductRequestDto dto);

    ProductResponseDto updateProduct(Long id, ProductRequestDto dto);

    void deleteProduct(Long id);

    // Search and filter with pagination
    Page<ProductResponseDto> searchProducts(String name, String category, int page, int size);
}