package com.example.stockexchangeapplication.stockprice.stockpricemicroservice.controller;

import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.ImportSummary;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.StockPrice;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.service.impl.StockPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/stockprices")
@CrossOrigin(origins = "*")
public class StockPriceController {

    @Autowired
    StockPriceService stockPriceService;



    @PostMapping(
            path = "",
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<ImportSummary> addStockPricesToCompany(@RequestBody List<StockPrice> stockPrices, @RequestHeader (name="Authorization") String token) throws Exception {
        return new ResponseEntity<>(stockPriceService.saveStockPrices(stockPrices, token), HttpStatus.OK);
    }

    @GetMapping(
            path = "/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<StockPrice> getStockPriceId(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(stockPriceService.getStockPriceById(id),HttpStatus.OK);
    }

    @GetMapping(
            path = "/byCompanyId/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<List<StockPrice>> getStockPricesByCompanyId(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(stockPriceService.getStockPricesByCompanyId(id),HttpStatus.OK);
    }

    @GetMapping(
            path = "/byCompanyCode/{companyCode}",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<List<StockPrice>> getStockPricesByCompanyCode(@PathVariable String companyCode) throws Exception {
        List<StockPrice> stockPricesByCompanyCode = stockPriceService.getStockPricesByCompanyCode(companyCode);
        return new ResponseEntity<>(stockPricesByCompanyCode,HttpStatus.OK);
    }

    @GetMapping(
            path = "/byCompanyCode/{companyCode}/filterBy",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<List<StockPrice>> getStockPricesByCompanyCodeInDateRange(@PathVariable String companyCode, @PathParam("startDate") String startDate, @PathParam("endDate") String endDate) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new ResponseEntity<>(stockPriceService.getStocksByCompanyCodeInDateRange(companyCode, LocalDateTime.parse(startDate,formatter), LocalDateTime.parse(endDate, formatter)),HttpStatus.OK);
    }

    @GetMapping(
            path = "/bySector/{sectorId}",
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<List<List<StockPrice>>> getStockPricesBySector(@PathVariable Long sectorId, @RequestHeader (name="Authorization") String token) throws Exception {
        return new ResponseEntity<>(stockPriceService.getStockPricesBySector(sectorId, token),HttpStatus.OK);
    }

}
