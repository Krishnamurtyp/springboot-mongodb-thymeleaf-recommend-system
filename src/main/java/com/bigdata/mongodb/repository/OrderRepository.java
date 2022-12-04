package com.bigdata.mongodb.repository;

import com.bigdata.mongodb.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
