package com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.charts;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceData {
    private List<Map<String,Float>> pricesByDateTime;
}
