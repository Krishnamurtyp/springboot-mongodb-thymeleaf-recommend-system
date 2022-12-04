package com.bigdata.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Option {
    private String name;
    private Long size;
    private Long soldQTY = 0L;
    private Long balanceQTY;
    private String color;
}
