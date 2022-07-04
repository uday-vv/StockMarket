package com.example.stockexchangeapplication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "StockExchange")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockExchange {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "code", unique = true)
    private String code;

    @OneToMany(targetEntity = CompanyStockExchangeMap.class, mappedBy = "stockExchange", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<CompanyStockExchangeMap> companyStockExchangeMap;

}
