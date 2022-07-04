package com.example.stockexchangeapplication.stockprice.stockpricemicroservice.service.impl;

import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.StockPrice;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.charts.PerformanceData;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.service.IChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChartService implements IChartService {

    @Autowired
    StockPriceService stockPriceService;

    @Override
    public PerformanceData getPerformanceByCompany(String companyCode, LocalDateTime startDate, LocalDateTime endDate) {
        List<StockPrice> stockPrices = stockPriceService.getStocksByCompanyCodeInDateRange(companyCode, startDate, endDate);
        List<Map<String,Float>> pricesByDateTime = stockPrices.stream().map(stockPrice -> {
            Map<String,Float> priceByDateTime = new HashMap<>();
            priceByDateTime.put(stockPrice.getTimeStamp().toString(), stockPrice.getSharePrice());
            return priceByDateTime;
        }).collect(Collectors.toList());

        return new PerformanceData(pricesByDateTime);
    }

    @Override
    public PerformanceData getPerformanceBySector(Long sectorId, LocalDateTime startDate, LocalDateTime endDate, String token) throws Exception {
        List<List<StockPrice>> stockPrices = stockPriceService.getStockPricesBySector(sectorId, token);
        LocalDateTime start = startDate;
        List<Map<String, Float>> pricesByDateTime = new ArrayList<>();

        while (!start.isAfter(endDate)) {
            int totalNumberOfCompanies = stockPrices.size();
            System.out.println(start.toString());
            float priceAggregate = 0;
            for (List<StockPrice> stockPricesOfCompany: stockPrices) {
                boolean dateFound = false;
                int timesFound = 0;
                float price = 0;
                for(StockPrice stockPrice: stockPricesOfCompany) {
                    if (stockPrice.getTimeStamp().toLocalDate().toString().equals(start.toLocalDate().toString())) {
                        dateFound = true;
                        timesFound += 1;
                        price += stockPrice.getSharePrice();
                    }
                }
                if (! dateFound) {
                    totalNumberOfCompanies -= 1;
                } else {
                    price = price/timesFound;
                    priceAggregate += price;
                }
            }
            if(totalNumberOfCompanies != 0) {
                Map<String,Float> priceByDateTime = new HashMap<>();
                priceByDateTime.put(start.toString(), priceAggregate/totalNumberOfCompanies);
                pricesByDateTime.add(priceByDateTime);
            }
            start = start.plusDays(1);
        }

        return new PerformanceData(pricesByDateTime);
    }

    @Override
    public PerformanceData getPerformanceByCompany(String companyCode) throws Exception {
        List<StockPrice> stockPrices = stockPriceService.getStockPricesByCompanyCode(companyCode);
        List<Map<String,Float>> pricesByDateTime = stockPrices.stream().map(stockPrice -> {
            Map<String,Float> priceByDateTime = new HashMap<>();
            priceByDateTime.put(stockPrice.getTimeStamp().toString(), stockPrice.getSharePrice());
            return priceByDateTime;
        }).collect(Collectors.toList());

        if (pricesByDateTime.size() > 10) {
            return new PerformanceData(pricesByDateTime.subList(0,11));
        }

        return new PerformanceData(pricesByDateTime);
    }
}
