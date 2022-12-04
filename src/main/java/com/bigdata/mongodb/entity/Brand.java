package com.bigdata.mongodb.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "Brands")
public class Brand extends UpdateAuditable{
    @Id
    private String id;
    private String name;
    private String description;
    private Image logo;
}
