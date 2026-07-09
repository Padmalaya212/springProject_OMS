package com.oms.service;


import com.oms.entity.Category;
import com.oms.exception.ResourceNotFoundException;
import com.oms.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
	@Autowired
    private  CategoryRepository categoryRepository;

    CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAll() { return categoryRepository.findAll(); }

    public Category getById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
    }

    public Category create(Category category) { return categoryRepository.save(category); }

    public Category update(Integer id, Category updated) {
        Category category = getById(id);
        category.setCategoryName(updated.getCategoryName());
        category.setDescription(updated.getDescription());
        category.setIsActive(updated.getIsActive());
        return categoryRepository.save(category);
    }

    public void delete(Integer id) { categoryRepository.delete(getById(id)); }
}