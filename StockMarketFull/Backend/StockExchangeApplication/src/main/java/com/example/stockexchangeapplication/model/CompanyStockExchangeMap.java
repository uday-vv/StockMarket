package com.example.stockexchangeapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "CompanyStockExchangeMap")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyStockExchangeMap {
    @Id
    @GeneratedValue
    private long id;

    private String companyCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Company company;

    @ManyToOne(fetch = FetchType.EAGER)
    private StockExchange stockExchange;

}
