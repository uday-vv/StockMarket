package com.example.stockexchangeapplication.io.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockExchangeResponse {
    private Long id;
    private String code;
    private String name;
    private Integer numberOfCompanies;
}
