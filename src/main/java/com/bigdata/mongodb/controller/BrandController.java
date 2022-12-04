package com.bigdata.mongodb.controller;

import com.bigdata.mongodb.entity.Brand;
import com.bigdata.mongodb.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/brands")
public class BrandController {
    @Autowired
    private BrandRepository repo;

    @GetMapping("")
    public List<Brand> findBrands() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Brand findBrandById(@PathVariable String id) {
        return repo.findById(id).get();
    }

    @PostMapping("/add")
    public void addBrand(@RequestBody Brand brand) {
        repo.save(brand);
    }

    @PutMapping("/update/{id}")
    public void updateBrand(@PathVariable String id, @RequestBody Brand brand) {
        if (repo.findById(id).isPresent()) {
            brand.setId(id);
            repo.save(brand);
        }
    }

    @DeleteMapping("/delete/{id}")
    public void removeBrand(@PathVariable String id) {
        if (repo.findById(id).isPresent())
            repo.deleteById(id);
    }
}
