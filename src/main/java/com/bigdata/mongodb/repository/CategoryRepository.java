package com.bigdata.mongodb.repository;

import com.bigdata.mongodb.entity.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByNameContainingIgnoreCase(String name);
}
