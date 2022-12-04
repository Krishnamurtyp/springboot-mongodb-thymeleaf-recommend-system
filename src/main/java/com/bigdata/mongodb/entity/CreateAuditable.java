package com.bigdata.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Data
public class CreateAuditable {
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();
    @CreatedBy
    private User createdBy = null;
}
