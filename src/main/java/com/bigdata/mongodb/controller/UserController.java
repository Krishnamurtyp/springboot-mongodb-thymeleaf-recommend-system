package com.bigdata.mongodb.controller;

import com.bigdata.mongodb.entity.User;
import com.bigdata.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Autowired
    private UserRepository repo;

    @PostMapping("/add")
    public void addUser(@RequestBody User user) {
        repo.save(user);
    }

    @GetMapping("")
    public List<User> findUsers() {
        return repo.findAll();
    }
}
