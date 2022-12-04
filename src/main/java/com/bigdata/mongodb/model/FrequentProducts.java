package com.bigdata.mongodb.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Data
@Component
public class FrequentProducts {
    Map<List<Integer>, Integer> result;
}
