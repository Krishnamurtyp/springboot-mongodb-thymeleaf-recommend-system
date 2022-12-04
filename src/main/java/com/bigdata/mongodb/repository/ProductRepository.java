package com.bigdata.mongodb.repository;


import com.bigdata.mongodb.entity.Category;
import com.bigdata.mongodb.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByCategory(Category category);
}
