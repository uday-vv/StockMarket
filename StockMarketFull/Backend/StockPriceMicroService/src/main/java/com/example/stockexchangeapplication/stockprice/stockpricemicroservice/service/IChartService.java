package com.example.stockexchangeapplication.stockprice.stockpricemicroservice.service;

import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.charts.PerformanceData;

import java.time.LocalDateTime;

public interface IChartService {
    PerformanceData getPerformanceByCompany(String companyCode, LocalDateTime startDate, LocalDateTime endDate);
    PerformanceData getPerformanceBySector(Long sectorId, LocalDateTime startDate, LocalDateTime endDate, String token) throws Exception;
    PerformanceData getPerformanceByCompany(String companyCode) throws Exception;
}
