package com.bigdata.mongodb.repository;

import com.bigdata.mongodb.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
