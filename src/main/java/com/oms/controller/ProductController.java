package com.oms.controller;


import com.oms.dto.ProductDTO;
import com.oms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
	@Autowired
    private 
    ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping public ResponseEntity<List<ProductDTO>> getAll() { return ResponseEntity.ok(productService.getAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getById(@PathVariable Integer id) { return ResponseEntity.ok(productService.getById(id)); }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> search(@RequestParam String name) { return ResponseEntity.ok(productService.search(name)); }

    @PostMapping
    public ResponseEntity<ProductDTO> create(@RequestBody ProductDTO dto) { return ResponseEntity.ok(productService.create(dto)); }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Integer id, @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(productService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        productService.delete(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}