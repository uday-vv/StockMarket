package com.example.stockexchangeapplication.service.impl;

import com.example.stockexchangeapplication.exceptions.AlreadyExistsException;
import com.example.stockexchangeapplication.exceptions.NotFoundException;
import com.example.stockexchangeapplication.io.response.StockExchangeResponse;
import com.example.stockexchangeapplication.model.StockExchange;
import com.example.stockexchangeapplication.repository.StockExchangeRepository;
import com.example.stockexchangeapplication.service.IStockExchangeService;
import com.example.stockexchangeapplication.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StockExchangeService implements IStockExchangeService {

    @Autowired
    StockExchangeRepository stockExchangeRepository;

    @Autowired
    CompanyStockExchangeService mapService;

    @Autowired
    Mapper mapper;

    @Override
    public StockExchange addStockExchange(StockExchange stockExchange) throws AlreadyExistsException {
        StockExchange returnValue = null;
        try {
            returnValue = stockExchangeRepository.save(stockExchange);
        } catch (Exception ex) {
            throw new AlreadyExistsException("Stock Exchange with Name "+stockExchange.getName()+" already exists");
        }
        return returnValue;
    }

    @Override
    public StockExchange getStockExchangeByCode(String exchangeCode) throws NotFoundException {
        Optional<StockExchange> stockExchange = stockExchangeRepository.findByCode(exchangeCode);
        if (stockExchange.isEmpty()) {
            throw new NotFoundException("Exchange "+exchangeCode+" Not Found");
        }
        return stockExchange.get();
    }

    @Override
    public StockExchange getStockExchangeById(Long id) throws NotFoundException {
        Optional<StockExchange> stockExchange = stockExchangeRepository.findById(id);

        if (stockExchange.isEmpty()) {
            throw new NotFoundException("Exchange with id "+id+" Not Found");
        }

        return stockExchange.get();
    }

    @Override
    public List<StockExchangeResponse> getAllStockExchanges() {
        List<StockExchange> stockExchanges = stockExchangeRepository.findAll();
        return stockExchanges.stream().map(stockExchange -> {
            StockExchangeResponse response = mapper.getModelMapper().map(stockExchange, StockExchangeResponse.class);
            response.setNumberOfCompanies(mapService.getNumberOfCompaniesInExchange(response.getId()));
            return response;
        }).collect(Collectors.toList());
    }
}
