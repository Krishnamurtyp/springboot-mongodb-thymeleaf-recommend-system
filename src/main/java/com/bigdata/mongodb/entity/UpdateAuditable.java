package com.bigdata.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Data
public class UpdateAuditable extends CreateAuditable{
    @LastModifiedDate
    private LocalDateTime lastUpdatedAt = LocalDateTime.now();
    @LastModifiedBy
    private User lastUpdatedBy = null;
}
