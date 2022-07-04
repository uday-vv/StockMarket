package com.example.stockexchangeapplication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Company")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String ccode;

    @Column(nullable = false)
    private String cName;

    @Column(nullable = false)
    private String ceo;

    @Column(nullable = false)
    private Double turnover;

    @Column(nullable = false)
    @Type(type = "text")
    private String cwebsite;

    @Column(nullable = false)
    @Type(type = "text")
    private String csel;


    @OneToMany(targetEntity = CompanyStockExchangeMap.class, mappedBy = "company", cascade = CascadeType.REMOVE)
    private List<CompanyStockExchangeMap> companyStockExchangeMap;


}
