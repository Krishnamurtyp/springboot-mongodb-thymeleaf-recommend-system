package com.bigdata.mongodb.model;

import com.bigdata.mongodb.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {
    private String id;
    private String name;
    private String slug;
    private Long price;
    private String description;
    private Category category = null;
    private Brand brand = null;
    private List<Option> options;
    private List<Image> images;
    private List<MultipartFile> multipartFiles;
    private Long totalSoldQTY = 0L;
    private Long totalBalanceQTY = 0L;
}
