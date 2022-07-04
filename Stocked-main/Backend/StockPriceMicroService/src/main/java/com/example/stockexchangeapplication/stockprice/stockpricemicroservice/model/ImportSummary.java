package com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportSummary {
    private int addedEntries;
    private List<StockPrice> failedEntries;
    private List<StockPrice> addedStocks;
    private LocalDate startDateTime;
    private LocalDate lastDateTime;
    private List<LocalDate> missingDates;
}
