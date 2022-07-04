package com.example.stockexchangeapplication.io.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequest {

    private String ccode;

    private String companyName;

    private String ceo;

    private Double turnover;

    private String cwebsite;

    private String csel;
}
