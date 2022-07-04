package com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDateTime;

@Entity
@Table(name = "StockPrice")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockPrice {
    @Id
    @GeneratedValue
    private long id;

    private String exchangeCode;

    private String companyCode;

    private Long companyId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeStamp;

    private float sharePrice;
}
