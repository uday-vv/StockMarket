package com.example.stockexchangeapplication.model.charts;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceData<T> {
    private T reference;
    private List<Map<String,Float>> pricesByDateTime;
}
