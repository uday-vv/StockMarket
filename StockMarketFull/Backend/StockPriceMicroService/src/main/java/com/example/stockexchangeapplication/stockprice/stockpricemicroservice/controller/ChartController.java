package com.example.stockexchangeapplication.stockprice.stockpricemicroservice.controller;

import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.charts.PerformanceData;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.service.impl.ChartService;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.service.impl.StockPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/chartData")
@CrossOrigin(origins = "*")
public class ChartController {

    @Autowired
    StockPriceService stockPriceService;

    @Autowired
    ChartService chartService;

    @GetMapping(
            value = "/performance/byCompany",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<PerformanceData> getPerformanceDataByCompany(@PathParam("companyCode") String companyCode,@PathParam("startDate") String startDate, @PathParam("endDate") String endDate) throws Exception {
        if(startDate.equals("start") && endDate.equals("end")){
            return new ResponseEntity<>(chartService.getPerformanceByCompany(companyCode),HttpStatus.OK);
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"), dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startDateString = LocalDate.parse(startDate, dateFormatter).atStartOfDay().toString().concat(":00").replace("T"," ");
        String endDateString = LocalDate.parse(endDate, dateFormatter).atTime(20,0,0).toString().concat(":00").replace("T"," ");

        return new ResponseEntity<>(chartService.getPerformanceByCompany(companyCode, LocalDateTime.parse(startDateString, dateTimeFormatter), LocalDateTime.parse(endDateString, dateTimeFormatter)),HttpStatus.OK);
    }

    @GetMapping(
            value = "/performance/bySector",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<PerformanceData> getPerformanceDataBySector(@PathParam("sectorId") Long sectorId,@PathParam("startDate") String startDate, @PathParam("endDate") String endDate, @RequestHeader(name="Authorization") String token) throws Exception {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"), dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startDateString = LocalDate.parse(startDate, dateFormatter).atStartOfDay().toString().concat(":00").replace("T"," ");
        String endDateString = LocalDate.parse(endDate, dateFormatter).atTime(20,0,0).toString().concat(":00").replace("T"," ");

        PerformanceData performanceData = chartService.getPerformanceBySector(sectorId, LocalDateTime.parse(startDateString, dateTimeFormatter), LocalDateTime.parse(endDateString, dateTimeFormatter), token);

        return new ResponseEntity<>(performanceData,HttpStatus.OK);
    }
}
