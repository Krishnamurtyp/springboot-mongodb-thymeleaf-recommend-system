package com.bigdata.mongodb.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Lob;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    private String name;
    private String type;
    @Lob
    private byte[] data;
}
