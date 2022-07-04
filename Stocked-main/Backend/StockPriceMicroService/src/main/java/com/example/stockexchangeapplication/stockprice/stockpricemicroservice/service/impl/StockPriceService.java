package com.example.stockexchangeapplication.stockprice.stockpricemicroservice.service.impl;

import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.ImportSummary;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.model.StockPrice;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.repository.StockPriceRepository;
import com.example.stockexchangeapplication.stockprice.stockpricemicroservice.service.IStockPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockPriceService implements IStockPriceService {

    @Autowired
    StockPriceRepository stockPriceRepository;

    private RestTemplate restTemplate;

    @Autowired
    public StockPriceService(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    @Override
    public ImportSummary saveStockPrices(List<StockPrice> stockPrices, String token) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        List<StockPrice> errorStockPrices = new ArrayList<>();
        List<StockPrice> addedStockPrices = new ArrayList<>();

        stockPrices.stream().forEach(stockPrice -> {
            try {
                HttpEntity<String> entityB = new HttpEntity<>(headers);
                String url = "https://stockexchangeapp.herokuapp.com/api/v1/companies/checkMapping?companyCode="+stockPrice.getCompanyCode()+"&exchangeCode="+stockPrice.getExchangeCode();
                ResponseEntity<String> existsResponse = null;
                try {
                    existsResponse = restTemplate.exchange(url,HttpMethod.GET, entityB, String.class);
                } catch(Exception e) {
                    errorStockPrices.add(stockPrice);
                }
                if (! existsResponse.getBody().equals("Not Found")) {
                    stockPrice.setCompanyId(Long.parseLong(existsResponse.getBody()));
                    addedStockPrices.add(stockPriceRepository.save(stockPrice));
                } else {
                    errorStockPrices.add(stockPrice);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Map<String, Object> dateSummary = (addedStockPrices.size() == 0)?null:getDatesSummary(addedStockPrices);

        ImportSummary importSummary = new ImportSummary();
        importSummary.setAddedEntries(stockPrices.size()- errorStockPrices.size());
        importSummary.setFailedEntries(errorStockPrices);
        importSummary.setStartDateTime((dateSummary == null)?null:(LocalDate) dateSummary.get("startDate"));
        importSummary.setLastDateTime((dateSummary == null)?null:(LocalDate) dateSummary.get("endDate"));
        importSummary.setMissingDates((dateSummary == null)?null:(List<LocalDate>) dateSummary.get("missingDates"));
        importSummary.setAddedStocks(addedStockPrices);

        return importSummary;

    }

    private Map<String, Object> getDatesSummary(List<StockPrice> addedStockPrices) {
        List<LocalDateTime> sortedDates = addedStockPrices.stream().map(StockPrice::getTimeStamp).sorted().collect(Collectors.toList());
        LocalDate startDate = sortedDates.get(0).toLocalDate();
        LocalDate endDate = sortedDates.get(sortedDates.size()-1).toLocalDate();

        LocalDate start = startDate;
        LocalDate end = endDate;
        List<LocalDate> missingDates = new ArrayList<>();

        while (!start.isAfter(end)) {
            boolean dateFound = false;
            for (LocalDateTime date : sortedDates) {
                if (start.toString().equals(date.toLocalDate().toString())) {
                    dateFound = true;
                    break;
                }
            }
            if (!dateFound) missingDates.add(start);
            start = start.plusDays(1);
        }

        Map<String, Object> dateSummary = new HashMap<>();
        dateSummary.put("startDate", startDate);
        dateSummary.put("endDate", endDate);
        dateSummary.put("missingDates", missingDates);


        return dateSummary;
    }

    @Override
    public void saveStockPrice(StockPrice stockPrice) {
        stockPriceRepository.save(stockPrice);
    }

    @Override
    public StockPrice getStockPriceById(Long id) throws Exception {
        if (! stockPriceRepository.existsById(id)) {
            throw new Exception("Entry doesn't exist");
        }

        return stockPriceRepository.findById(id).get();
    }

    @Override
    public List<StockPrice> getStockPricesByCompanyId(Long id) throws Exception {
        return stockPriceRepository.findByCompanyId(id);
    }

    @Override
    public List<StockPrice> getStockPricesByCompanyCode(String companyCode) throws Exception {
        List<StockPrice> byCompanyCode = stockPriceRepository.findByCompanyCodeOrderByTimeStampDesc(companyCode);
        return byCompanyCode;
    }

    @Override
    public List<StockPrice> getStocksByCompanyCodeInDateRange(String companyCode, LocalDateTime startDate, LocalDateTime endDate) {

        List<StockPrice> byCompanyCodeInDateRange = stockPriceRepository.findByCompanyCodeInDateRange(companyCode, startDate, endDate);
        return byCompanyCodeInDateRange;
    }

    @Override
    public List<List<StockPrice>> getStockPricesBySector(Long sectorId, String token) throws Exception {
        ResponseEntity<Long[]> response = null;
        System.out.println("Sector Id"+sectorId);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", token);
            HttpEntity entity = new HttpEntity<>(headers);
            response = restTemplate.exchange("https://stockexchangeapp.herokuapp.com/api/v1/sectors/"+sectorId+"/companies/ids",HttpMethod.GET,entity,Long[].class);
        } catch (Exception ex) {
            throw new Exception("Sector not found");
        }

        if (response.getStatusCode().value() != 200) {
           throw new Exception("Error Occurred");
        }
        List<List<StockPrice>> stocksByCompany = Arrays.stream(response.getBody()).map(id -> {
            try {
                return getStockPricesByCompanyId(id);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toList());
        return stocksByCompany;
    }
}
