package com.bigdata.mongodb.controller;

import com.bigdata.mongodb.entity.Category;
import com.bigdata.mongodb.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository repo;

    @GetMapping("")
    public List<Category> findCategories(){
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Category findCategoryById(@PathVariable String id) {
        return repo.findById(id).get();
    }

    @PostMapping("/add")
    public void addCategory(@RequestBody Category category) {
        repo.save(category);
    }

    @PutMapping("/update/{id}")
    public void updateCategory(@PathVariable String id, @RequestBody Category category) {
        if (repo.findById(id).isPresent()) {
            category.setId(id);
            repo.save(category);
        }
    }

    @DeleteMapping("/delete/{id}")
    public void removeCategory(@PathVariable String id) {
        if (repo.findById(id).isPresent())
            repo.deleteById(id);
    }
}
