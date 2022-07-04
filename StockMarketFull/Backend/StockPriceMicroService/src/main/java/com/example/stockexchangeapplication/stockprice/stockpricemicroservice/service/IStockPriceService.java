package com.example.stockexchangeapplication.stockprice.stockpricemicroservice.service;

import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.ImportSummary;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.StockPrice;

import java.time.LocalDateTime;
import java.util.List;

public interface IStockPriceService {
    ImportSummary saveStockPrices(List<StockPrice> stockPrices, String token) throws Exception;
    void saveStockPrice(StockPrice stockPrice);
    StockPrice getStockPriceById(Long id) throws Exception;
    List<StockPrice> getStockPricesByCompanyId(Long id) throws Exception;
    List<StockPrice> getStockPricesByCompanyCode(String companyCode) throws Exception;
    List<StockPrice> getStocksByCompanyCodeInDateRange(String companyCode, LocalDateTime startDate, LocalDateTime endDate);
    List<List<StockPrice>> getStockPricesBySector(Long sectorId, String token) throws Exception;
}
