package com.bigdata.mongodb.controller;

import com.bigdata.mongodb.entity.Order;
import com.bigdata.mongodb.entity.OrderDetails;
import com.bigdata.mongodb.entity.Product;
import com.bigdata.mongodb.entity.User;
import com.bigdata.mongodb.repository.OrderRepository;
import com.bigdata.mongodb.repository.ProductRepository;
import com.bigdata.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public class OrderController {
    @Autowired
    private OrderRepository repo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ProductRepository productRepo;

    @PostMapping("/add")
    public void addOrder(@RequestBody Order order) {
        User customer = order.getCreatedBy();
        customer = userRepo.findById(customer.getId()).get();
        order.setCreatedBy(customer);
        long totalPrice = 0L;
        List<OrderDetails> orderDetailsList = order.getOrderDetailsList();
        for(OrderDetails orderDetails : orderDetailsList) {
            Product product = productRepo.findById(orderDetails.getProduct().getId()).get();
            product.setTotalBalanceQTY(product.getTotalBalanceQTY() - 1);
            product.setTotalSoldQTY(product.getTotalSoldQTY() + 1);
            orderDetails.setProduct(product);
            totalPrice += orderDetails.getAmount() * product.getPrice();
        }
        order.setOrderDetailsList(orderDetailsList);
        order.setTotalPrice(totalPrice);
        repo.save(order);
    }

    @GetMapping("")
    public List<Order> findOrders() {
        return repo.findAll();
    }
}

