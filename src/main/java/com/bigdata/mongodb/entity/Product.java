package com.bigdata.mongodb.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(collection = "Products")
public class Product extends UpdateAuditable{
    @Id private String id;
    private String name;
    private String slug;
    private Long price;
    private String description;
    private List<Image> images = new ArrayList<Image>();
    private Category category = null;
    private Brand brand = null;
    private List<Option> options;
    private Long totalSoldQTY = 0L;
    private Long totalBalanceQTY = 0L;

    @Override
    public boolean equals(Object object) {
        if(object instanceof Product) {
            Product product = (Product) object;
            if(this.id.equals(product.getId()))
                return true;
        }
        return false;
    }
    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}
