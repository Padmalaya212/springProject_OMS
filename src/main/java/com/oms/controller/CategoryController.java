package com.oms.controller;


import com.oms.entity.Category;
import com.oms.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
	@Autowired
    private  CategoryService categoryService;

    CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping public ResponseEntity<List<Category>> getAll() { return ResponseEntity.ok(categoryService.getAll()); }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Integer id) { return ResponseEntity.ok(categoryService.getById(id)); }

    @PostMapping
    public ResponseEntity<Category> create(@Valid @RequestBody Category category) { return ResponseEntity.ok(categoryService.create(category)); }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Integer id, @Valid @RequestBody Category category) {
        return ResponseEntity.ok(categoryService.update(id, category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        categoryService.delete(id);
        return ResponseEntity.ok("Category deleted successfully");
    }
}