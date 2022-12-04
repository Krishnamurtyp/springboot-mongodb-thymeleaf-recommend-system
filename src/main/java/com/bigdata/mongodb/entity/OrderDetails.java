package com.bigdata.mongodb.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OrderDetails {
    @DocumentReference
    private Product product;
    private Long amount;
}
