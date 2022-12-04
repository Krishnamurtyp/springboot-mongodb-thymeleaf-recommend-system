package com.bigdata.mongodb.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "Orders")
public class Order extends UpdateAuditable {
    @Id
    private String id;
    private OrderStatus orderStatus = OrderStatus.UNCONFIMRED;
    private LocalDateTime estimatedDeliveryAt = LocalDateTime.now().plusDays(10);
    private LocalDateTime deliveryAt;
    private String country;
    private String province;
    private String district;
    private String street;
    private Long totalPrice;
    private String description;
    private List<OrderDetails> orderDetailsList;
}

enum OrderStatus {
    UNCONFIMRED,
    CONFIRMED,
    DELEVERING,
    DELIVERED
}
