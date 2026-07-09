package com.oms.service;

import com.oms.dto.ProductDTO;
import com.oms.entity.Category;
import com.oms.entity.Product;
import com.oms.entity.Stock;
import com.oms.exception.ResourceNotFoundException;
import com.oms.repository.CategoryRepository;
import com.oms.repository.ProductRepository;
import com.oms.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
	@Autowired
    private  ProductRepository productRepository;
	@Autowired private  CategoryRepository categoryRepository;
	@Autowired private  StockRepository stockRepository;

    ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, StockRepository stockRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.stockRepository = stockRepository;
    }

    public List<ProductDTO> getAll() {
        return productRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ProductDTO getById(Integer id) {
        return mapToDTO(findEntity(id));
    }

    public Product findEntity(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
    }

    public ProductDTO create(ProductDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + dto.getCategoryId()));

        Product product = new Product();
        product.setProductName(dto.getProductName());
        product.setCategory(category);
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setGstPercentage(dto.getGstPercentage());
        product.setImageUrl(dto.getImageUrl());
        Product saved = productRepository.save(product);

        // auto-create stock row with 0 quantity
        Stock stock = new Stock();
        stock.setProduct(saved);
        stock.setQuantity(0);
        stockRepository.save(stock);

        return mapToDTO(saved);
    }

    public ProductDTO update(Integer id, ProductDTO dto) {
        Product product = findEntity(id);
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + dto.getCategoryId()));

        product.setProductName(dto.getProductName());
        product.setCategory(category);
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setGstPercentage(dto.getGstPercentage());
        product.setImageUrl(dto.getImageUrl());
        product.setIsActive(dto.getIsActive());
        return mapToDTO(productRepository.save(product));
    }

    public void delete(Integer id) { productRepository.delete(findEntity(id)); }

    public List<ProductDTO> search(String name) {
        return productRepository.findByProductNameContainingIgnoreCase(name)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private ProductDTO mapToDTO(Product p) {
        ProductDTO dto = new ProductDTO();
        dto.setProductId(p.getProductId());
        dto.setProductName(p.getProductName());
        dto.setCategoryId(p.getCategory().getCategoryId());
        dto.setCategoryName(p.getCategory().getCategoryName());
        dto.setDescription(p.getDescription());
        dto.setPrice(p.getPrice());
        dto.setGstPercentage(p.getGstPercentage());
        dto.setImageUrl(p.getImageUrl());
        dto.setIsActive(p.getIsActive());
        stockRepository.findByProduct_ProductId(p.getProductId())
                .ifPresent(s -> dto.setStockQuantity(s.getQuantity()));
        return dto;
    }
}