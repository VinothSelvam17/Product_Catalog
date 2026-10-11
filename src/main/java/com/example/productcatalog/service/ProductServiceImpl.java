package com.example.productcatalog.service;

import com.example.productcatalog.dto.ProductRequestDto;
import com.example.productcatalog.dto.ProductResponseDto;
import com.example.productcatalog.entity.Product;
import com.example.productcatalog.exception.ResourceNotFoundException;
import com.example.productcatalog.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapToResponseDto(product);
    }

    @Override
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        Product product = mapToEntity(dto);
        Product saved = productRepository.save(product);
        return mapToResponseDto(saved);
    }

    @Override
    public ProductResponseDto updateProduct(Long id, ProductRequestDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setInStock(dto.getInStock());

        Product updated = productRepository.save(product);
        return mapToResponseDto(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    @Override
    public Page<ProductResponseDto> searchProducts(String name, String category, int page, int size) {
        // Create a Pageable object from the page number and size
        Pageable pageable = PageRequest.of(page, size);

        // Choose the correct query based on which parameters are provided
        Page<Product> products;
        if (name != null && category != null) {
            products = productRepository.findByNameContainingIgnoreCaseAndCategory(name, category, pageable);
        } else if (name != null) {
            products = productRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (category != null) {
            products = productRepository.findByCategory(category, pageable);
        } else {
            products = productRepository.findAll(pageable);
        }

        // Map each Product entity to a ProductResponseDto
        return products.map(this::mapToResponseDto);
    }

    private ProductResponseDto mapToResponseDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getInStock()
        );
    }

    private Product mapToEntity(ProductRequestDto dto) {
        return new Product(
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getCategory(),
                dto.getInStock()
        );
    }
}